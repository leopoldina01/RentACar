Vue.component("allUsers", {
	data: function () {
	    return {
			users: null,
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: '', isBlocked: ''},
			searchUser : {id: -1, username: '', password: 'nekiPassword', firstName: '', lastName: '', gender: 'male', dateOfBirth: '2001-01-01', role: 'CUSTOMER', isBlocked: ''},
			searchUsername: '',
			searchFirstName: '',
			searchLastName: '',
			selectedSort: 'unsorted',
			selectedCustomerType: 'SELECTEDTYPE',
			selectedRole: 'SELECTEDROLE'
	    }
	},
	    template: `
	<div>
		<header>
            <div class="logo">Rent A Car</div>
            <div class="nav-buttons">
            	<button v-on:click="showSuspiciousCustomers"><i class="fas fa-exclamation-triangle"></i> Suspicious customers</button>
                <button v-on:click="goToMyAccount"><i class="fas fa-user"></i> My Account</button>
            </div>
        </header>
        
        <div class="search-input-container">
		    <input type="text" placeholder="Username" v-model="searchUser.username"/>
		    <input type="text" placeholder="First name" v-model="searchUser.firstName"/>
		    <input type="text" placeholder="Last name" v-model="searchUser.lastName"/>
		    <button v-on:click="searchUsers"><i class="fas fa-search"></i> Search</button>
		</div>
        
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
	                <tr v-for="u in users">
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
         <div class="filter-container">
            <select v-model="selectedSort">
                <option value="unsorted" selected>Sort by...</option>
                <option value="firstnameAsc">Sort by first name &#x2191;</option>
                <option value="firstnameDesc">Sort by first name &#x2193;</option>
                <option value="lastnameAsc">Sort by last name &#x2191;</option>
                <option value="lastnameDesc">Sort by last name &#x2193;</option>
                <option value="usernameAsc">Sort by username &#x2191;</option>
                <option value="usernameDesc">Sort by username &#x2193;</option>
                <option value="scoreAsc">Sort by score &#x2191;</option>
                <option value="scoreDesc">Sort by score &#x2193;</option>
            </select>
            <br>
            <br>
            <select v-model="selectedCustomerType">
                <option value="SELECTEDTYPE">Select Customer Type...</option>
                <option value="BRONZE">Bronze</option>
                <option value="SILVER">Silver</option>
                <option value="GOLD">Gold</option>
            </select>
            <br>
            <br>
            <select v-model="selectedRole">
                <option value="SELECTEDROLE">Select User Role...</option>
                <option value="CUSTOMER">Customer</option>
                <option value="MANAGER">Manager</option>
                <option value="ADMINISTRATOR">Administrator</option>
            </select>
            <br>
            <br>
            <div class="filter-container-buttons">
                <button v-on:click="filter" >Apply Filters</button>
                <button v-on:click="removeFilters">Remove Filters</button>
            </div>
        </div>
	</div>
	    `,
    mounted () {
		axios
			.get('rest/user/showAllUsers')
			.then(response => (this.users = response.data));
    },
    methods: {
		searchUsers : function() {
			axios
				.post('rest/user/searchUsers', this.searchUser)
				.then(response => {
					this.users = response.data;
				})
		},
		
		goToMyAccount : function(){
			router.push(`/administratorProfile`)
		},
		
		filter : function(){
			axios
				.get('rest/user/filter/' + this.selectedSort + '/' + this.selectedCustomerType + '/' + this.selectedRole) 
				.then(response =>{
					this.users = response.data
				})
		},

		removeFilters : function(){
			this.selectedSort = 'unsorted'
			this.selectedCustomerType =  'SELECTEDTYPE'
			this.selectedRole = 'SELECTEDROLE';
			axios
				.get('rest/user/filter/' + this.selectedSort + '/' + this.selectedCustomerType + '/' + this.selectedRole) 
				.then(response =>{
					this.users = response.data
				})
		},
		
		blockThisUser(user)
		{
			axios
				.post('rest/user/blockUser', user)
				.then(response => {this.users = response.data;})
		},
		
		showSuspiciousCustomers : function()
		{
			router.push('/showSuspiciousCustomers');
		}
    }
});