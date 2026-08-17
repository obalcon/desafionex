export interface Produto {
  id: number;
  codigo: string;
  descricao: string;
  tipoProduto: 'ELETRONICO' | 'ELETRODOMESTICO' | 'MOVEL';
  valorFornecedor: number;
  quantidadeEstoque: number;
}

export interface ProdutoRequest {
  codigo: string;
  descricao: string;
  tipoProduto: 'ELETRONICO' | 'ELETRODOMESTICO' | 'MOVEL';
  valorFornecedor: number;
}

export interface Movimento {
  id: number;
  produtoId: number;
  produtoDescricao: string;
  tipoMovimentacao: 'ENTRADA' | 'SAIDA';
  valorVenda: number | null;
  dataVenda: string | null;
  quantidadeMovimentada: number;
}

export interface MovimentoRequest {
  produtoId: number;
  tipoMovimentacao: 'ENTRADA' | 'SAIDA';
  valorVenda: number | null;
  dataVenda: string | null;
  quantidadeMovimentada: number;
}

export interface LucroProduto {
  produtoId: number;
  codigo: string;
  descricao: string;
  quantidadeTotalSaida: number;
  lucroTotal: number;
}

export interface ProdutoTipo {
  id: number;
  codigo: string;
  descricao: string;
  tipoProduto: string;
  quantidadeDisponivel: number;
  quantidadeSaida: number;
}