Vue.component("createManager", {
	data: function () {
	    return {
			id: -1,
			confirmPassword: '',
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'MANAGER', isBlocked: ''},
			loggedUser : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'MANAGER', isBlocked: ''}
	    }
	},
	    template: `
	<div>
		<header>
			<div class="logo">Rent A Car</div>
			<div class="nav-buttons">
                <button v-on:click="goToMyAccount"><i class="fas fa-user"></i> My Account</button>
            </div>
    	</header>
    	
		  <div class="signup-form">
			<h2>Create manager</h2>
			<div>
			  <input v-model = "user.username" type="text" placeholder="Username" name = "Username">
			  <label class="hidden" name = "UsernameLabel"></label>
			  <input v-model = "user.password" type="password" placeholder="Password" name = "Password">
			  <label class="hidden" name = "PasswordLabel"></label>
			  <input v-model = "confirmPassword" type="password" placeholder="Confirm password" name = "ConfirmPassword">
			  <label class="hidden" name = "ConfirmPasswordLabel"></label>
			  <input v-model = "user.firstName" type="text" placeholder="FirstName" name = "FirstName">
			  <label class="hidden" name = "FirstNameLabel"></label>
			  <input v-model = "user.lastName" type="text" placeholder="LastName" name = "LastName">
			  <label class="hidden" name = "LastNameLabel"></label>
			  <select v-model = "user.gender" name = "Gender">
				<option value="" disabled selected>Select gender</option>
				<option value="male">Male</option>
				<option value="female">Female</option>
			  </select>
			  <label class="hidden" name = "GenderLabel"></label>
			  <input v-model = "user.dateOfBirth" type="date" name="BirthDate">
			  <label class="hidden" name = "BirthDateLabel"></label>
			  <button v-on:click = "registrate">Create manager</button>
			</div>
		  </div>
	 </div>
	    `,
	mounted () {
		//this.id = this.$route.params.id;
		axios
			.get('rest/user/currentLoggedUser')
			.then(response =>{
				this.loggedUser = response.data;	
			})
	},
	
    methods: {
		registrate : function()
		{
			var valid = true;
			var usernameEl = document.getElementsByName('Username')[0];
			var passwordEl = document.getElementsByName('Password')[0];
			var confirmPasswordEl = document.getElementsByName('ConfirmPassword')[0];
			var firstNameEl = document.getElementsByName('FirstName')[0];
			var lastNameEl = document.getElementsByName('LastName')[0];
			var genderEl = document.getElementsByName('Gender')[0];
			var birthDateEl = document.getElementsByName('BirthDate')[0];
		
			var usernameLab = document.getElementsByName('UsernameLabel')[0];
			var passwordLab = document.getElementsByName('PasswordLabel')[0];
			var confirmPasswordLab = document.getElementsByName('ConfirmPasswordLabel')[0];
			var firstNameLabel = document.getElementsByName('FirstNameLabel')[0];
			var lastNameLabel = document.getElementsByName('LastNameLabel')[0];
			var genderLabel = document.getElementsByName('GenderLabel')[0];
			var birthDateLabel = document.getElementsByName('BirthDateLabel')[0];
			
			usernameLab.classList.add('hidden');
		    passwordLab.classList.add('hidden');
		    confirmPasswordLab.classList.add('hidden');
		    firstNameLabel.classList.add('hidden');
		    lastNameLabel.classList.add('hidden');
		    genderLabel.classList.add('hidden');
		    birthDateLabel.classList.add('hidden');
			
			usernameEl.style.borderColor = '';
		    passwordEl.style.borderColor = '';
		    confirmPasswordEl.style.borderColor = '';
		    firstNameEl.style.borderColor = '';
		    lastNameEl.style.borderColor = '';
		    genderEl.style.borderColor = '';
		    birthDateEl.style.borderColor = '';
		    
		    
		
			if (!usernameEl.value)
			{
				usernameEl.style.borderColor = 'red';
				usernameEl.style.borderWidth = '1px';
				usernameLab.classList.remove('hidden');
				usernameLab.textContent = "Can't be empty";
				usernameLab.style.color = 'red';
				valid = false;
			}
		
			if (!passwordEl.value)
			{
				passwordEl.style.borderColor = 'red';
				passwordEl.style.borderWidth = '1px';
				passwordLab.classList.remove('hidden');
				passwordLab.textContent = "Can't be empty";
				passwordLab.style.color = 'red';
				valid = false;
			}
		
			if (!confirmPasswordEl.value)
			{
				confirmPasswordEl.style.borderColor = 'red';
				confirmPasswordEl.style.borderWidth = '1px';
				confirmPasswordLab.classList.remove('hidden');
				confirmPasswordLab.textContent = "Can't be empty";
				confirmPasswordLab.style.color = 'red';
				valid = false;
			}
		
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
		
			if (!firstNameEl.value)
			{
				firstNameEl.style.borderColor = 'red';
				firstNameEl.style.borderWidth = '1px';
				firstNameLabel.classList.remove('hidden');
				firstNameLabel.textContent = "Can't be empty";
				firstNameLabel.style.color = 'red';
				valid = false;
			}
		
			if (!lastNameEl.value)
			{
				lastNameEl.style.borderColor = 'red';
				lastNameEl.style.borderWidth = '1px';
				lastNameLabel.classList.remove('hidden');
				lastNameLabel.textContent = "Can't be empty";
				lastNameLabel.style.color = 'red';
				valid = false;
			}
		
			if (!genderEl.value)
			{
				genderEl.style.borderColor = 'red';
				genderEl.style.borderWidth = '1px';
				genderLabel.classList.remove('hidden');
				genderLabel.textContent = "Can't be empty";
				genderLabel.style.color = 'red';
				valid = false;
			}
		
			if (!birthDateEl.value)
			{
				birthDateEl.style.borderColor = 'red';
				birthDateEl.style.borderWidth = '1px';
				birthDateLabel.classList.remove('hidden');
				birthDateLabel.textContent = "Can't be empty";
				birthDateLabel.style.color = 'red';
				valid = false;
			}
			else
			{
				var currentDate = new Date();
			    var selectedDate = new Date(birthDateEl.value);
			    var eighteenYearsAgo = new Date();
			    eighteenYearsAgo.setFullYear(currentDate.getFullYear() - 18);
			    var soManyYears = new Date();
			    soManyYears.setFullYear(currentDate.getFullYear() - 100);
			
			    if (selectedDate > currentDate || selectedDate > eighteenYearsAgo) {
			      birthDateEl.style.borderColor = "red";
			      birthDateEl.style.borderWidth = "1px";
			      birthDateLabel.classList.remove("hidden");
			      birthDateLabel.textContent = "Date must be in the past and at least 18 years ago";
			      birthDateLabel.style.color = "red";
			      valid = false;
			    } else if (selectedDate < soManyYears) {
			      birthDateEl.style.borderColor = "red";
			      birthDateEl.style.borderWidth = "1px";
			      birthDateLabel.classList.remove("hidden");
			      birthDateLabel.textContent = "Invalid date of birth";
			      birthDateLabel.style.color = "red";
			      valid = false;
			    }
			}
			
			
			if (valid)
			{
				axios
					.post('rest/user/', this.user)
					.then(response => {
						if (!response.data)
						{
							alert("User already exists");
						}
						else
						{
							/*const userId = response.data.id;
							router.push(`/profile/${userId}`)*/
							//var adminId = this.id;
							//this.user.id = response.data.id
							router.push(`/administratorProfile`)
							this.user = response.data;
							axios
								.post('rest/user/managers/', this.user)
								.then(response => {router.push(`/administratorProfile`)})
						}
						
					})
				 //here goes another adress maybe
					//.then(response => (router.push(`/profile/` + response.data.id)));
			}
		},
		goToMyAccount : function(){
			router.push(`/administratorProfile`)
		}
    }
});

/*function validation() {
	var valid = true;
	var usernameEl = document.getElementsByName('Username')[0];
	var passwordEl = document.getElementsByName('Password')[0];
	var confirmPasswordEl = document.getElementsByName('ConfirmPassword')[0];
	var firstNameEl = document.getElementsByName('FirstName')[0];
	var lastNameEl = document.getElementsByName('LastName')[0];
	var genderEl = document.getElementsByName('Gender')[0];
	var birthDateEl = document.getElementsByName('BirthDate')[0];

	var usernameLab = document.getElementsByName('UsernameLabel')[0];
	var passwordLab = document.getElementsByName('PasswordLabel')[0];
	var confirmPasswordLab = document.getElementsByName('ConfirmPasswordLabel')[0];
	var firstNameLabel = document.getElementsByName('FirstNameLabel')[0];
	var lastNameLabel = document.getElementsByName('LastNameLabel')[0];
	var genderLabel = document.getElementsByName('GenderLabel')[0];
	var birthDateLabel = document.getElementsByName('BirthDateLabel')[0];
	
	usernameLab.classList.add('hidden');
    passwordLab.classList.add('hidden');
    confirmPasswordLab.classList.add('hidden');
    firstNameLabel.classList.add('hidden');
    lastNameLabel.classList.add('hidden');
    genderLabel.classList.add('hidden');
    birthDateLabel.classList.add('hidden');
	
	usernameEl.style.borderColor = '';
    passwordEl.style.borderColor = '';
    confirmPasswordEl.style.borderColor = '';
    firstNameEl.style.borderColor = '';
    lastNameEl.style.borderColor = '';
    genderEl.style.borderColor = '';
    birthDateEl.style.borderColor = '';
    
    

	if (!usernameEl.value)
	{
		usernameEl.style.borderColor = 'red';
		usernameEl.style.borderWidth = '1px';
		usernameLab.classList.remove('hidden');
		usernameLab.textContent = "Can't be empty";
		usernameLab.style.color = 'red';
		valid = false;
	}

	if (!passwordEl.value)
	{
		passwordEl.style.borderColor = 'red';
		passwordEl.style.borderWidth = '1px';
		passwordLab.classList.remove('hidden');
		passwordLab.textContent = "Can't be empty";
		passwordLab.style.color = 'red';
		valid = false;
	}

	if (!confirmPasswordEl.value)
	{
		confirmPasswordEl.style.borderColor = 'red';
		confirmPasswordEl.style.borderWidth = '1px';
		confirmPasswordLab.classList.remove('hidden');
		confirmPasswordLab.textContent = "Can't be empty";
		confirmPasswordLab.style.color = 'red';
		valid = false;
	}

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

	if (!firstNameEl.value)
	{
		firstNameEl.style.borderColor = 'red';
		firstNameEl.style.borderWidth = '1px';
		firstNameLabel.classList.remove('hidden');
		firstNameLabel.textContent = "Can't be empty";
		firstNameLabel.style.color = 'red';
		valid = false;
	}

	if (!lastNameEl.value)
	{
		lastNameEl.style.borderColor = 'red';
		lastNameEl.style.borderWidth = '1px';
		lastNameLabel.classList.remove('hidden');
		lastNameLabel.textContent = "Can't be empty";
		lastNameLabel.style.color = 'red';
		valid = false;
	}

	if (!genderEl.value)
	{
		genderEl.style.borderColor = 'red';
		genderEl.style.borderWidth = '1px';
		genderLabel.classList.remove('hidden');
		genderLabel.textContent = "Can't be empty";
		genderLabel.style.color = 'red';
		valid = false;
	}

	if (!birthDateEl.value)
	{
		birthDateEl.style.borderColor = 'red';
		birthDateEl.style.borderWidth = '1px';
		birthDateLabel.classList.remove('hidden');
		birthDateLabel.textContent = "Can't be empty";
		birthDateLabel.style.color = 'red';
		valid = false;
	}
	else
	{
		var currentDate = new Date();
	    var selectedDate = new Date(birthDateEl.value);
	    var eighteenYearsAgo = new Date();
	    eighteenYearsAgo.setFullYear(currentDate.getFullYear() - 18);
	    var soManyYears = new Date();
	    soManyYears.setFullYear(currentDate.getFullYear() - 100);
	
	    if (selectedDate > currentDate || selectedDate > eighteenYearsAgo) {
	      birthDateEl.style.borderColor = "red";
	      birthDateEl.style.borderWidth = "1px";
	      birthDateLabel.classList.remove("hidden");
	      birthDateLabel.textContent = "Date must be in the past and at least 18 years ago";
	      birthDateLabel.style.color = "red";
	      valid = false;
	    } else if (selectedDate < soManyYears) {
	      birthDateEl.style.borderColor = "red";
	      birthDateEl.style.borderWidth = "1px";
	      birthDateLabel.classList.remove("hidden");
	      birthDateLabel.textContent = "Invalid date of birth";
	      birthDateLabel.style.color = "red";
	      valid = false;
	    }
	}
	
	return valid;
}*/