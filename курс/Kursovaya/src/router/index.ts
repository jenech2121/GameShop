import { createWebHistory, createRouter, type RouteRecordRaw } from 'vue-router'
import home from '@/pages/home.vue'
import catalog from '@/pages/catalog.vue'
import cabinet from '@/pages/cabinet.vue'
import Corzina from '@/pages/Corzina.vue'
import About from '@/pages/about.vue'
import Contacts from '@/pages/Contacts.vue'
import AdminLogin from '@/pages/adminLogin.vue'

declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
    requiresAdmin?: boolean
  }
}

const routes: RouteRecordRaw[] = [
  { path: '/', component: home },
  { path: '/catalog', component: catalog },
  { path: '/cabinet', component: cabinet, meta: { requiresAuth: false } },
  { path: '/admin-login', component: AdminLogin },
  { path: '/Corzina', component: Corzina },
  { path: '/about', component: About },
  { path: '/contacts', component: Contacts }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const storedUser = localStorage.getItem('user')
  const user = storedUser ? JSON.parse(storedUser) : null

  if (to.meta.requiresAdmin) {
    if (user?.role === 'ADMIN') {
      next()
    } else {
      next({ path: '/admin-login', query: { redirect: to.fullPath } })
    }
    return
  }

  if (to.meta.requiresAuth && !user) {
    next({ path: '/admin-login', query: { redirect: to.fullPath } })
    return
  }

  next()
})

export default router
