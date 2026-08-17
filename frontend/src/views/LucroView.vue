<template>
  <div>
    <div class="card">
      <h2>📊 Consulta de Lucro por Produto</h2>
    </div>

    <div class="card">
      <div class="form-group">
        <label>Selecione o Produto</label>
        <select v-model="produtoSelecionado" @change="consultarLucro">
          <option value="">Selecione...</option>
          <option v-for="p in produtos" :key="p.id" :value="p.id">
            {{ p.codigo }} - {{ p.descricao }}
          </option>
        </select>
      </div>
    </div>

    <div v-if="lucro" class="card">
      <h3>Resultado</h3>
      <table>
        <tbody>
          <tr>
            <td><strong>Código</strong></td>
            <td>{{ lucro.codigo }}</td>
          </tr>
          <tr>
            <td><strong>Descrição</strong></td>
            <td>{{ lucro.descricao }}</td>
          </tr>
          <tr>
            <td><strong>Quantidade Total de Saída</strong></td>
            <td>{{ lucro.quantidadeTotalSaida }}</td>
          </tr>
          <tr>
            <td><strong>Lucro Total</strong></td>
            <td style="color: #48bb78; font-size: 1.2rem; font-weight: bold">
              R$ {{ formatarValor(lucro.lucroTotal) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="!lucro && produtoSelecionado === ''" class="card empty-state">
      <p>Selecione um produto para consultar o lucro.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '../api/api'
import type { Produto, LucroProduto } from '../types'

const produtos = ref<Produto[]>([])
const produtoSelecionado = ref<number | ''>('')
const lucro = ref<LucroProduto | null>(null)

const carregarProdutos = async () => {
  try {
    const response = await api.get<Produto[]>('/produtos')
    produtos.value = response.data
  } catch (error) {
    console.error('Erro ao carregar produtos:', error)
  }
}

const consultarLucro = async () => {
  if (!produtoSelecionado.value) {
    lucro.value = null
    return
  }
  try {
    const response = await api.get<LucroProduto>(`/produtos/${produtoSelecionado.value}/lucro`)
    lucro.value = response.data
  } catch (error) {
    console.error('Erro ao consultar lucro:', error)
  }
}

const formatarValor = (valor: number) => {
  return valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

onMounted(() => {
  carregarProdutos()
})
</script>
