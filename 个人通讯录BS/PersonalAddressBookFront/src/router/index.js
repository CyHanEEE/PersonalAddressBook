import Vue from 'vue'
import Router from 'vue-router'
import Contact from '@/components/contact/ContactVue'

Vue.use(Router)

export default new Router({
  routes: [
	{
	  path: '/',
	  name: 'Contact',
	  component: Contact
	}
  ]
  
  
})
