const Pocetna = { template: '<pocetna></pocetna>' }
const SignUpForm = {template: '<signUpForm></signUpForm>'}
const Profile = {template: '<profileView></profileView>'}
const LogInForm = {template: '<login></login>'}
const CustomerProfile = {template: '<customer></customer>'}
const ManagerProfile = {template: '<manager></manager>'}
const AdministratorProfile = {template: '<administrator></administrator>'}
const CreateStoreForm = { template: '<createStoreForm></createStoreForm>'}
const CreateManagerForm = {template: '<createManager></createManager>'}
const AllUsers = {template: '<allUsers></allUsers>'}
const ManagerStore = {template: '<managerStore></managerStore>'}
const ShowCustomerOrders = {template: '<customerOrders></customerOrders>'}
const SelectedStore = {template: '<selectedStore></selectedStore>'}
const CustomerCreateOrder = {template: '<customerCreateOrder></customerCreateOrder>'}
const ShoppingCartOverview = {template: '<shoppingCartOverview></shoppingCartOverview>'}
const ShowSuspiciousCustomers = {template: '<suspiciousCustomers></suspiciousCustomers>'}


const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{ path: '/', name: 'home', component: Pocetna},
		{ path: '/signUp', component: SignUpForm},
		{ path: '/profile/:id', component: Profile},
		{ path: '/login', component: LogInForm},
		{ path: '/customerProfile', component: CustomerProfile},
		{ path: '/managerProfile', component: ManagerProfile},
		{ path: '/administratorProfile', component: AdministratorProfile},
		{ path: '/createStore', component: CreateStoreForm},
		{ path: '/createManager', component: CreateManagerForm},
		{ path: '/administrator/showAllUsers', component: AllUsers},
		{ path: '/managerStore', component: ManagerStore},
		{ path: '/showCustomerOrders', component: ShowCustomerOrders},
		{ path: '/selectedStore/:id', component: SelectedStore},
		{ path: '/customerCreateOrder', component: CustomerCreateOrder},
		{ path: '/shoppingCartOverview', component: ShoppingCartOverview},
		{ path: '/showSuspiciousCustomers', component: ShowSuspiciousCustomers}
	  ]
});

var app = new Vue({
	router,
	el: '#element'
});