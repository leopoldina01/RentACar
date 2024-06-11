Vue.component("login", {
	data: function () {
	    return {
			username: '',
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			usernameEmpty: false,
			passwordEmpty: false
	    }
	},
	    template: `
	 <div>		   
		<header>
				<div class="logo">Rent A Car</div>
				<div class="nav-buttons">
	                <button v-on:click="goToHomepage"><i class="fas fa-home"></i> Home page</button>
	            </div>
	    </header>
		<div class="login-container">
	    <h2>Login Form</h2>
	    <div>
	      <div class="form-group">
	        <input type="text" name="LoginUsername" v-model = "user.username" placeholder="Enter your username">
	        <label class="login-label" v-if="usernameEmpty" name = "UsernameLabel">Can't be empty</label>
	      </div>
	      <div class="form-group">
	        <input type="password" name="LoginPassword" v-model = "user.password" placeholder="Enter your password">
	        <label class="login-label" v-if="passwordEmpty" name = "PasswordLabel">Can't be empty</label>
	      </div>
	      <div class="form-group">
	        <button v-on:click = "login" type="submit">Log In</button>
	      </div>
	    </div>
	  </div>
	</div>
	    `,
    mounted () {
		
    },
    methods: {
		login : function(){
			var valid = true;
			var usernameEl = document.getElementsByName('LoginUsername')[0];
			var passwordEl = document.getElementsByName('LoginPassword')[0];
			this.usernameEmpty = false;
			this.passwordEmpty = false;
			//var usernameLab = document.getElementsByName('UsernameLabel')[0];
			//var passwordLab = document.getElementsByName('PasswordLabel')[0];
			
			/*usernameLab.classList.add('hidden');
		    passwordLab.classList.add('hidden');*/
			
			usernameEl.style.borderColor = '';
		    passwordEl.style.borderColor = '';

			if (!usernameEl.value)
			{
				this.usernameEmpty = true
				usernameEl.style.borderColor = 'red';
				usernameEl.style.borderWidth = '1px';
				//usernameLab.classList.remove('hidden');
				//usernameLab.textContent = "Can't be empty";
				//usernameLab.style.color = 'red';
				valid = false;
			}
		
			if (!passwordEl.value)
			{
				this.passwordEmpty = true;
				passwordEl.style.borderColor = 'red';
				passwordEl.style.borderWidth = '1px';
				//passwordLab.classList.remove('hidden');
				//passwordLab.textContent = "Can't be empty";
				//passwordLab.style.color = 'red';
				valid = false;
			}
			if(valid){
				axios
					.get('rest/user/findby/' + this.user.username)
					.then(response =>{
						if(!response.data){
							alert("User doesn't exist.")
						}
						else{
							axios 
								.get('rest/user/' + this.user.username + '/' + this.user.password)
								.then( response =>{
									if(!response.data){
										alert("Username/Password are incorrect.")
									}else{
										if (response.data.isBlocked)
										{
											alert("This user is blocked!")
										}
										else
										{
											axios
											.post('rest/user/login', this.user)
											.then(response =>{
												if (response.status === 200){
									   				router.push(`/`);
												}
												
												axios
													.get('rest/user/currentLoggedUser')
													.then(response =>{
														this.user = response.data;	
														if(this.user.role === 'CUSTOMER'){												
															axios
																.get('rest/users/customers/loggedIn/' + this.user.username)
																.then(response => {this.customer = response.data;})
														}	
													})
												
											})
										}
									}
								})
						}	
				})
			}		
		},
		goToHomepage : function(){
			router.push('/');
		}
    }
});