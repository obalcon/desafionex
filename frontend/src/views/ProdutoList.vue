<template>
  <div>
    <div class="card" style="display: flex; justify-content: space-between; align-items: center">
      <h2>Lista de Produtos</h2>
      <router-link to="/produtos/novo" class="btn btn-primary">+ Novo Produto</router-link>
    </div>

    <div
      v-if="mensagem"
      :class="['alert', tipoMensagem === 'sucesso' ? 'alert-success' : 'alert-error']"
    >
      {{ mensagem }}
    </div>

    <div class="card">
      <table v-if="produtos.length > 0">
        <thead>
          <tr>
            <th>Código</th>
            <th>Descrição</th>
            <th>Tipo</th>
            <th>Valor Fornecedor</th>
            <th>Estoque</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.id">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.descricao }}</td>
            <td>
              <span :class="['badge', 'badge-' + produto.tipoProduto.toLowerCase()]">
                {{ formatarTipo(produto.tipoProduto) }}
              </span>
            </td>
            <td>R$ {{ formatarValor(produto.valorFornecedor) }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td class="actions">
              <router-link :to="`/produtos/${produto.id}/editar`" class="btn btn-warning"
                >Editar</router-link
              >
              <button @click="excluirProduto(produto.id)" class="btn btn-danger">Excluir</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">
        <p>Nenhum produto cadastrado.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '../api/api'
import type { Produto } from '../types'

const produtos = ref<Produto[]>([])
const mensagem = ref('')
const tipoMensagem = ref('')

const carregarProdutos = async () => {
  try {
    const response = await api.get<Produto[]>('/produtos')
    produtos.value = response.data
  } catch (error) {
    console.error('Erro ao carregar produtos:', error)
  }
}

const excluirProduto = async (id: number) => {
  if (!confirm('Tem certeza que deseja excluir este produto?')) return

  try {
    await api.delete(`/produtos/${id}`)
    mensagem.value = 'Produto excluído com sucesso!'
    tipoMensagem.value = 'sucesso'
    carregarProdutos()
  } catch (error: unknown) {
    tipoMensagem.value = 'erro'
    const err = error as { response?: { data?: { message?: string } } }
    mensagem.value = err.response?.data?.message || 'Erro ao excluir produto'
  }
}

const formatarTipo = (tipo: string) => {
  const tipos: Record<string, string> = {
    ELETRONICO: 'Eletrônico',
    ELETRODOMESTICO: 'Eletrodoméstico',
    MOVEL: 'Móvel',
  }
  return tipos[tipo] || tipo
}

const formatarValor = (valor: number) => {
  return valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

onMounted(() => {
  carregarProdutos()
})
</script>
