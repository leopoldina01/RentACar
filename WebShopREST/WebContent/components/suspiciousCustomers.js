Vue.component("suspiciousCustomers", {
	data: function () {
	    return {
			username: '',
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			usernameEmpty: false,
			passwordEmpty: false,
			customers: []
	    }
	},
	    template: `
	 <div>		   
		<header>
				<div class="logo">Rent A Car</div>
				<div class="nav-buttons">
	                <button v-on:click="goToHomepage"><i class="fas fa-home"></i> Home page</button>
	                <button v-on:click="goToMyAccount"><i class="fas fa-user"></i> My Account</button>
	            </div>
	    </header>
	    
	    <h2 class="suspicious-h2">ALL SUSPICIOUS CUSTOMERS!</h2>
	    
	    <div class="table-container">
	        <table class="datagrid">
	            <thead>
	                <tr>
	                    <th>Username</th>
	                    <th>First name</th>
	                    <th>Last name</th>
	                    <th>Gender</th>
	                    <th>Date of birth</th>
	                    <th>Role</th>
	                    <th>Block</th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr v-for="u in customers">
	                    <td>{{u.username}}</td>
	                    <td>{{u.firstName}}</td>
	                    <td>{{u.lastName}}</td>
	                    <td>{{u.gender}}</td>
	                    <td>{{u.dateOfBirth}}</td>
	                    <td>{{u.role}}</td>
	                    <td v-if="u.role !== 'ADMINISTRATOR'">
	                    	<button class="block-button" v-if="!u.isBlocked" @click="blockThisUser(u)"><i class="fas fa-ban"></i> Block</button>
	                    	<label v-if="u.isBlocked" class="is-blocked-label">blocked</label>
	                    </td>
	                </tr>
	            </tbody>
	        </table>
	     </div>
	</div>
	    `,
    mounted () {
		axios
			.get('rest/users/customers/findSuspiciousCustomers')
			.then(response => {this.customers = response.data;})
    },
    methods: {
		goToHomepage : function(){
			router.push('/');
		},
		
		goToMyAccount : function(){
			router.push(`/administratorProfile`)
		},
		
		blockThisUser(user)
		{
			axios
				.post('rest/user/blockCustomer', user)
				.then(response => {this.customers = response.data;})
		}
    }
});