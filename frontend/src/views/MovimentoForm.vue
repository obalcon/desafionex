<template>
  <div>
    <div class="card">
      <h2>Registrar Movimentação</h2>
    </div>

    <div
      v-if="mensagem"
      :class="['alert', tipoMensagem === 'sucesso' ? 'alert-success' : 'alert-error']"
    >
      {{ mensagem }}
    </div>

    <div class="card">
      <form @submit.prevent="registrar">
        <div class="form-group">
          <label>Produto</label>
          <select v-model="movimento.produtoId" required>
            <option value="">Selecione...</option>
            <option v-for="p in produtos" :key="p.id" :value="p.id">
              {{ p.codigo }} - {{ p.descricao }} (Estoque: {{ p.quantidadeEstoque }})
            </option>
          </select>
        </div>

        <div class="form-group">
          <label>Tipo de Movimentação</label>
          <select v-model="movimento.tipoMovimentacao" required>
            <option value="ENTRADA">Entrada</option>
            <option value="SAIDA">Saída</option>
          </select>
        </div>

        <div class="form-group">
          <label>Quantidade</label>
          <input v-model.number="movimento.quantidadeMovimentada" type="number" min="1" required />
        </div>

        <div class="form-group" v-if="movimento.tipoMovimentacao === 'SAIDA'">
          <label>Valor Total da Venda (R$)</label>
          <input
            v-model.number="movimento.valorVenda"
            type="number"
            step="0.01"
            min="0.01"
            required
          />
        </div>

        <div class="form-group" v-if="movimento.tipoMovimentacao === 'SAIDA'">
          <label>Data da Venda</label>
          <input v-model="movimento.dataVenda" type="date" required />
        </div>

        <div style="display: flex; gap: 1rem; margin-top: 1.5rem">
          <button type="submit" class="btn btn-success">Registrar</button>
          <router-link to="/produtos" class="btn btn-primary">Voltar</router-link>
        </div>
      </form>
    </div>

    <div class="card" v-if="movimentos.length > 0">
      <h3>Últimas Movimentações</h3>
      <table>
        <thead>
          <tr>
            <th>Produto</th>
            <th>Tipo</th>
            <th>Qtd</th>
            <th>Valor Venda</th>
            <th>Data</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in movimentos" :key="m.id">
            <td>{{ m.produtoDescricao }}</td>
            <td>
              <span
                :class="[
                  'badge',
                  m.tipoMovimentacao === 'ENTRADA' ? 'badge-entrada' : 'badge-saida',
                ]"
              >
                {{ m.tipoMovimentacao }}
              </span>
            </td>
            <td>{{ m.quantidadeMovimentada }}</td>
            <td>{{ m.valorVenda ? 'R$ ' + formatarValor(m.valorVenda) : '-' }}</td>
            <td>{{ m.dataVenda ? formatarData(m.dataVenda) : '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import api from '../api/api'
import type { Produto, Movimento, MovimentoRequest } from '../types'

const produtos = ref<Produto[]>([])
const movimentos = ref<Movimento[]>([])
const mensagem = ref('')
const tipoMensagem = ref('')

const movimento = ref<MovimentoRequest>({
  produtoId: 0,
  tipoMovimentacao: 'ENTRADA',
  valorVenda: null,
  dataVenda: null,
  quantidadeMovimentada: 1,
})

const carregarProdutos = async () => {
  try {
    const response = await api.get<Produto[]>('/produtos')
    produtos.value = response.data
  } catch {
    console.error('Erro ao carregar produtos')
  }
}

const carregarMovimentos = async (produtoId: number) => {
  if (!produtoId) return
  try {
    const response = await api.get<Movimento[]>(`/movimentos/produto/${produtoId}`)
    movimentos.value = response.data
  } catch {
    movimentos.value = []
  }
}

watch(
  () => movimento.value.produtoId,
  (novoId: number) => {
    if (novoId) carregarMovimentos(novoId)
  },
)

watch(
  () => movimento.value.tipoMovimentacao,
  (tipo: string) => {
    if (tipo === 'ENTRADA') {
      movimento.value.valorVenda = null as unknown as number
      movimento.value.dataVenda = null
    } else {
      movimento.value.valorVenda = 0
      movimento.value.dataVenda = new Date().toISOString().split('T')[0] as string
    }
  },
)

const registrar = async () => {
  try {
    await api.post('/movimentos', movimento.value)
    mensagem.value = 'Movimentação registrada com sucesso!'
    tipoMensagem.value = 'sucesso'

    const produtoIdAtual = movimento.value.produtoId
    movimento.value = {
      produtoId: 0,
      tipoMovimentacao: 'ENTRADA',
      valorVenda: null as unknown as number,
      dataVenda: null,
      quantidadeMovimentada: 1,
    }

    carregarProdutos()
    if (produtoIdAtual) carregarMovimentos(produtoIdAtual)
  } catch {
    tipoMensagem.value = 'erro'
    mensagem.value = 'Erro ao registrar movimentação.'
  }
}

const formatarValor = (valor: number) => {
  return valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatarData = (data: string) => {
  return new Date(data).toLocaleDateString('pt-BR')
}

onMounted(() => {
  carregarProdutos()
})
</script>
