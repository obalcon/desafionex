import { createRouter, createWebHistory } from 'vue-router'
import ProdutoList from '../views/ProdutoList.vue'
import ProdutoForm from '../views/ProdutoForm.vue'
import MovimentoForm from '../views/MovimentoForm.vue'
import LucroView from '../views/LucroView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/produtos' },
    { path: '/produtos', name: 'produtos', component: ProdutoList },
    { path: '/produtos/novo', name: 'novo-produto', component: ProdutoForm },
    { path: '/produtos/:id/editar', name: 'editar-produto', component: ProdutoForm, props: true },
    { path: '/movimentos', name: 'movimentos', component: MovimentoForm },
    { path: '/lucro', name: 'lucro', component: LucroView },
  ],
})

export default router
