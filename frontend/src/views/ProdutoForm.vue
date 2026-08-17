<template>
  <div>
    <div class="card">
      <h2>{{ isEdicao ? 'Editar Produto' : 'Novo Produto' }}</h2>
    </div>

    <div
      v-if="mensagem"
      :class="['alert', tipoMensagem === 'sucesso' ? 'alert-success' : 'alert-error']"
    >
      {{ mensagem }}
    </div>

    <div class="card">
      <form @submit.prevent="salvar">
        <div class="form-group">
          <label>Código</label>
          <input v-model="produto.codigo" type="text" required :disabled="isEdicao" />
        </div>

        <div class="form-group">
          <label>Descrição</label>
          <input v-model="produto.descricao" type="text" required />
        </div>

        <div class="form-group">
          <label>Tipo de Produto</label>
          <select v-model="produto.tipoProduto" required>
            <option value="ELETRONICO">Eletrônico</option>
            <option value="ELETRODOMESTICO">Eletrodoméstico</option>
            <option value="MOVEL">Móvel</option>
          </select>
        </div>

        <div class="form-group">
          <label>Valor no Fornecedor (R$)</label>
          <input
            v-model.number="produto.valorFornecedor"
            type="number"
            step="0.01"
            min="0.01"
            required
          />
        </div>

        <div style="display: flex; gap: 1rem; margin-top: 1.5rem">
          <button type="submit" class="btn btn-success">
            {{ isEdicao ? 'Atualizar' : 'Salvar' }}
          </button>
          <router-link to="/produtos" class="btn btn-primary">Voltar</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../api/api'
import type { Produto, ProdutoRequest } from '../types'

const route = useRoute()
const router = useRouter()

const produto = ref<ProdutoRequest>({
  codigo: '',
  descricao: '',
  tipoProduto: 'ELETRONICO',
  valorFornecedor: 0,
})

const mensagem = ref('')
const tipoMensagem = ref('')
const isEdicao = ref(false)
const produtoId = ref<number | null>(null)

onMounted(async () => {
  const id = route.params.id as string
  if (id) {
    isEdicao.value = true
    produtoId.value = parseInt(id)
    try {
      const response = await api.get<Produto>(`/produtos/${id}`)
      const p = response.data
      produto.value = {
        codigo: p.codigo,
        descricao: p.descricao,
        tipoProduto: p.tipoProduto,
        valorFornecedor: p.valorFornecedor,
      }
    } catch (error: unknown) {
      tipoMensagem.value = 'erro'
      const err = error as { response?: { data?: { message?: string } } }
      mensagem.value = err.response?.data?.message || 'Erro ao carregar produto.'
    }
  }
})

const salvar = async () => {
  try {
    if (isEdicao.value && produtoId.value) {
      await api.put(`/produtos/${produtoId.value}`, produto.value)
      mensagem.value = 'Produto atualizado com sucesso!'
    } else {
      await api.post('/produtos', produto.value)
      mensagem.value = 'Produto criado com sucesso!'
      produto.value = { codigo: '', descricao: '', tipoProduto: 'ELETRONICO', valorFornecedor: 0 }
    }
    tipoMensagem.value = 'sucesso'
    setTimeout(() => router.push('/produtos'), 1500)
  } catch (error: unknown) {
    tipoMensagem.value = 'erro'
    const err = error as { response?: { data?: { message?: string } } }
    mensagem.value = err.response?.data?.message || 'Erro ao salvar produto.'
  }
}
</script>
