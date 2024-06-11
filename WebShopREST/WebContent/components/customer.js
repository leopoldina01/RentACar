Vue.component("customer", {
	data: function () {
	    return {
			id : -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: '', role: 'CUSTOMER', isBlocked: ''},
			updatedUser : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: '', role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			updatedUsername: '',
			updatedPassword: '',
			updatedFirstname: '',
			updatedLastname: '',
			updatedGender: '',
			updatedDateOfBirth: '',
			showUpdateForm: false
	    }
	},
	    template: `
	<div>
		<header>
			<div class="logo">Rent A Car</div>
			<div class = nav-buttons>
				<button v-on:click = "ShowMyOrders"><i class="fas fa-eye"></i> My orders</button>
				<button v-on:click= "showForm"><i class="fas fa-edit"></i> Edit</button>
				<button v-on:click="goToHomepage"><i class="fas fa-home"></i> Home page</button>
	        	<button v-on:click = "LogOut"><i class="fas fa-sign-out-alt"></i> LogOut</button>
			</div>
	        
    	</header>
    	<div class="profile-container">
    	
	        <div class="profile-image">
	            <img src="https://static.vecteezy.com/system/resources/thumbnails/009/734/564/small/default-avatar-profile-icon-of-social-media-user-vector.jpg" alt="Profile Image">
	        </div>
	        
	        <div class="profile-info">
	            <label><span class="label-text">Username:	{{user.username}}</span></label>
	            <label><span class="label-text">First Name:		{{user.firstName}}</span></label>
	            <label><span class="label-text">Last Name:		{{user.lastName}}</span></label>
	            <label><span class="label-text">Gender:		{{user.gender}}</span></label>
	            <label><span class="label-text">Date of Birth: 		{{user.dateOfBirth}}</span></label>
	            <label><span class="label-text">Role:	{{user.role}}</span></label>
	            <label><span class="label-text">Score: {{customer.score}}</span></label>
	            <label><span class="label-text">Type: {{customer.customerType.type}}</span></label>
	            <label><span class="label-text">Discount: {{customer.customerType.discount}}</span></label>
	        </div>
	        
	        <div v-if="showUpdateForm" id="formDiv">
            <div>
              <label for="username">Username:</label>
              <input type="text" id="username" name="username" v-model="updatedUsername" >
              <br>
              <label for="firstName">First Name:</label>
              <input type="text" id="firstName" name="firstName" v-model="updatedFirstname" >
              <br>
              <label for="lastName">Last Name:</label>
              <input type="text" id="lastName" name="lastName" v-model="updatedLastname" >
              <br>
              <label for="password">Password:</label>
              <input type="password" id="password" name="Password" v-model="updatedPassword" >
              <label class="hidden" name = "PasswordLabel"></label>
              <br>
              <label for="confirm_password">Confirm Password:</label>
              <input type="password" id="confirmPassword" name="ConfirmPassword">
              <label class="hidden" name = "ConfirmPasswordLabel"></label> 
              <br>
              <label for="gender">Gender:</label>
              <select v-model="updatedGender" id="gender" name="gender">
                <option value="male" selected>Male</option>
                <option value="female">Female</option>
              </select>
              <br>
              <label for="dateOfBirth">Date of Birth:</label>
              <input type="date" id="dateOfBirth" name="dateOfBirth" v-model="updatedDateOfBirth" >
              <br>
              <div class="edit-button-container">
                <button v-on:click = "update">Update</button>
                <button v-on:click = "hideForm">Cancel</button>
              </div>
            </div>
          </div>
    	</div>
	</div>
	    `,
    mounted () {
		//this.id = this.$route.params.id;
		/*axios
			.get('rest/user/' + this.id)
			.then(response => (this.user = response.data))*/
		axios
			.get('rest/user/currentLoggedUser')
			.then(response =>{
				this.user = response.data;
				axios
					.get('rest/users/customers/' + this.user.id)
					.then(response => {
						this.customer = response.data;
					})
			})
    },
    methods: {
		update : function(){
				this.updatedUser.id = this.user.id;
				this.updatedUser.username = this.user.username;
				this.updatedUser.password = this.user.password;
				this.updatedUser.firstName = this.user.firstName;
				this.updatedUser.lastName = this.user.lastName;
				this.updatedUser.gender = this.user.gender;
				this.updatedUser.dateOfBirth = this.user.dateOfBirth;
				
			if(validation()){
				if(this.updatedUsername){
					this.updatedUser.username = this.updatedUsername;
				}
				
				if(this.updatedPassword){
					this.updatedUser.password = this.updatedPassword
				}
				
				if(this.updatedFirstname){
					this.updatedUser.firstName = this.updatedFirstname
				}
				
				if(this.updatedLastname){
					this.updatedUser.lastName = this.updatedLastname
				}
				
				if(this.updatedGender){
					this.updatedUser.gender = this.updatedGender
				}
				
				if(this.updatedDateOfBirth){
					this.updatedUser.dateOfBirth = this.updatedDateOfBirth
				}
	
				axios
					.put('rest/user/' + this.user.id, this.updatedUser)
					.then(response => {
						if(!response.data){
							alert("Username already exist or is invalid date.");
						}else{
							//router.push(`/customerProfile`)
							this.showUpdateForm = false;
							this.$router.go()
						}
					})
			}
		},
		
		LogOut : function()
		{
			axios
				.post('rest/user/logout')
				.then(response =>{					
					router.push('/');
				})
		},
		
		ShowMyOrders : function()
		{
			router.push(`/showCustomerOrders`);
		},
		
		showForm : function(){
			this.showUpdateForm = true;
			
			this.updatedUsername = this.user.username
			this.updatedPassword = this.user.password
			this.updatedFirstname = this.user.firstName
			this.updatedLastname = this.user.lastName
			this.updatedGender = this.user.gender
			this.updatedDateOfBirth = this.dateOdBirth
		},
		
		hideForm : function(){
			this.showUpdateForm = false;
		},
		
		goToHomepage : function(){
			router.push('/');
		}
    }
});

/*function showForm(){
	var myFormDiv = document.getElementById("formDiv");
	//myFormDiv.style.display = "block";
	myFormDiv.classList.remove('hidden');
	
}

function hideForm(){
	var myFormDiv = document.getElementById("formDiv");
	//myFormDiv.style.display = "none";
	myFormDiv.classList.add('hidden');
}*/

function validation(){
	
	var valid = true;
	var passwordEl = document.getElementsByName('Password')[0];
	var confirmPasswordEl = document.getElementsByName('ConfirmPassword')[0];

	var passwordLab = document.getElementsByName('PasswordLabel')[0];
	var confirmPasswordLab = document.getElementsByName('ConfirmPasswordLabel')[0];

	passwordLab.classList.add('hidden');
	confirmPasswordLab.classList.add('hidden');

	passwordEl.style.borderColor = '';
	confirmPasswordEl.style.borderColor = '';
	
	if (passwordEl.value != confirmPasswordEl.value)
	{
		passwordEl.style.borderColor = 'red';
		passwordEl.style.borderWidth = '1px';

		confirmPasswordEl.style.borderColor = 'red';
		confirmPasswordEl.style.borderWidth = '1px';

		passwordLab.classList.remove('hidden');
		passwordLab.textContent = "Passwords don't match";
		passwordLab.style.color = 'red';

		confirmPasswordLab.classList.remove('hidden');
		confirmPasswordLab.textContent = "Passwords don't match";
		confirmPasswordLab.style.color = 'red';
		
		valid = false;
	}
	return valid;
}

