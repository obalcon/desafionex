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

    <div class="card" style="display: flex; gap: 0.5rem; flex-wrap: wrap">
      <button
        v-for="opcao in filtros"
        :key="opcao.valor || 'todos'"
        @click="filtrar(opcao.valor)"
        class="btn"
        :style="
          filtroAtual === opcao.valor
            ? 'background: #667eea; color: white'
            : 'background: #e2e8f0; color: #4a5568'
        "
      >
        {{ opcao.label }}
      </button>
    </div>

    <div class="card">
      <table v-if="mostrarLista.length > 0">
        <thead>
          <tr>
            <th>Código</th>
            <th>Descrição</th>
            <th>Tipo</th>
            <th>Valor Fornecedor</th>
            <th>Estoque</th>
            <th v-if="filtroAtual">Qtd Saída</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in mostrarLista" :key="produto.id">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.descricao }}</td>
            <td>
              <span :class="['badge', 'badge-' + produto.tipoProduto.toLowerCase()]">
                {{ formatarTipo(produto.tipoProduto) }}
              </span>
            </td>
            <td>R$ {{ formatarValor(produto.valorFornecedor) }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td v-if="filtroAtual">{{ produto.quantidadeSaida }}</td>
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
        <p>Nenhum produto encontrado.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import api from '../api/api'
import type { Produto, ProdutoTipo } from '../types'

interface ProdutoExibicao {
  id: number
  codigo: string
  descricao: string
  tipoProduto: string
  valorFornecedor: number
  quantidadeEstoque: number
  quantidadeSaida?: number
}

const produtos = ref<ProdutoExibicao[]>([])
const produtosTipo = ref<ProdutoExibicao[]>([])
const mensagem = ref('')
const tipoMensagem = ref('')
const filtroAtual = ref<string | null>(null)

const filtros = [
  { valor: null, label: 'Todos' },
  { valor: 'ELETRONICO', label: 'Eletrônico' },
  { valor: 'ELETRODOMESTICO', label: 'Eletrodoméstico' },
  { valor: 'MOVEL', label: 'Móvel' },
]

const mostrarLista = computed<ProdutoExibicao[]>(() => {
  if (filtroAtual.value) {
    return produtosTipo.value
  }
  return produtos.value
})

const carregarProdutos = async () => {
  try {
    const response = await api.get<Produto[]>('/produtos')
    produtos.value = response.data.map((p) => ({
      id: p.id,
      codigo: p.codigo,
      descricao: p.descricao,
      tipoProduto: p.tipoProduto,
      valorFornecedor: p.valorFornecedor,
      quantidadeEstoque: p.quantidadeEstoque,
    }))
    produtosTipo.value = []
    filtroAtual.value = null
  } catch {
    console.error('Erro ao carregar produtos')
  }
}

const filtrarPorTipo = async (tipo: string) => {
  try {
    const response = await api.get<ProdutoTipo[]>(`/produtos/tipo/${tipo}`)
    produtosTipo.value = response.data.map((p) => ({
      id: p.id,
      codigo: p.codigo,
      descricao: p.descricao,
      tipoProduto: p.tipoProduto,
      valorFornecedor: p.valorFornecedor,
      quantidadeEstoque: p.quantidadeDisponivel,
      quantidadeSaida: p.quantidadeSaida,
    }))
    filtroAtual.value = tipo
  } catch {
    console.error('Erro ao filtrar')
  }
}

const filtrar = (tipo: string | null) => {
  if (tipo === null) {
    carregarProdutos()
  } else {
    filtrarPorTipo(tipo)
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
    mensagem.value = err.response?.data?.message || 'Erro ao excluir produto.'
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
