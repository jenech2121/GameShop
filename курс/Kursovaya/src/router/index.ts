import { createWebHistory, createRouter } from 'vue-router'
//import Catalog from './components/Catalog.vue'
import home from '@/pages/home.vue'
import catalog from '@/pages/catalog.vue'
import cabinet from '@/pages/cabinet.vue'
import Corzina from '@/pages/Corzina.vue'
import About from '@/pages/about.vue'
import Contacts from '@/pages/Contacts.vue'

const routes = [
  { path: '/', component: home },
  { path: '/catalog', component: catalog },
  { path: '/cabinet', component: cabinet },
  { path: '/Corzina', component: Corzina },
  { path: '/about', component: About },
{path:  '/contacts', component: Contacts}
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
