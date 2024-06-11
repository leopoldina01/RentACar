Vue.component("createStoreForm", {
	data: function () {
	    return {
			adminId : -1,
			newStore : {id: -1, name: '', offeredVehicles: [], workingTime: '', workingStatus: 'OPENED', location: null, locationId: -1, logoUrl: '', rating: 0 },
			manager : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'MANAGER', isBlocked: '', rentACarStore: null},
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'MANAGER', isBlocked: ''},
			startTime : '',
			endTime: '',
			managers: null,
			address: '',
			longitude: 19.85,
			latitude: 45.24,
			managerUsername: '',
			isDisabled: false,
			formIsEnabled: false,
			confirmPassword: '',
			kliknutaLongitude: 19.85,
			kliknutaLatitude: 45.24,
			ulica: '',
			broj: '',
			grad: '',
			postanskiBroj: '',
			valid: true
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
    	<div id="map" class="mapCreateStore"></div>
    	<div class="profile-container">
    
	        <div>
	        	
	        
				<div class="newstore-container">
					<h2 class="newstore-headline">CREATE STORE</h2>
					<div>

						<input placeholder="Enter Store Name" name="StoreName" v-model="newStore.name" type="text">
						<br>
						<label class="hidden" name = "StoreNameLabel"></label>
						<br>
						<input  placeholder="Choose address on map" name="Address" v-model="address" type="text" readonly>
						<br>
						<label class="hidden" name="addressLabel"></label>
						<br>
						<label class="hidden" name = "AddressLabel"></label>
						<br>
						<label>{{longitude}}</label>
						<br>
						<label class="hidden" name = "LongitudeLabel"></label>
						<br>
						<label>{{latitude}}</label>
						<br>
						<label class="hidden" name = "LatitudeLabel"></label>
						<br>
						<br>
						<label>Working hours:</label>
						<br>
						<input placeholder="From" name="From" v-model="startTime" type="time">
						<br>
						<label class="hidden" name = "FromLabel"></label>
						<input placeholder="To" name="To" v-model="endTime" type="time">
						<br>
						<label class="hidden" name = "ToLabel"></label>
						<br>
						<input placeholder="Enter Logo URL" name="LogoUrl" v-model="newStore.logoUrl" type="text">
						<br>
						<label class="hidden" name = "LogoUrlLabel"></label>
						<br>
						<select v-model="managerUsername" name="managers">
							<option value="" disabled selected>Select manager</option>
							<option v-for="m in managers">{{m.username}}</option>
						</select>
						<br>
						<label class="hidden" name="ManagerLabel">Manager:</label>
						<br>
						<label v-if="formIsEnabled">There is 0 available managers, register new one.</label>
						<br>
						<button :disabled="isDisabled" v-on:click="createStore">Create Store</button>
					</div>
				</div>
                
				<div v-if="formIsEnabled" class="signup-form2">
                    <h2 class="heder-za-radnju">CREATE MANAGER</h2>
                    <div>
                      <input v-model = "manager.username" type="text" placeholder="Username" name = "Username">
                      <br>
                      <label class="hidden" name = "UsernameLabel"></label>
                      <br>
                      <input v-model = "manager.password" type="password" placeholder="Password" name = "Password">
                      <br>
                      <label class="hidden" name = "PasswordLabel"></label>
                      <br>
                      <input v-model = "confirmPassword" type="password" placeholder="Confirm password" name = "ConfirmPassword">
                      <br>
			  		  <label class="hidden" name = "ConfirmPasswordLabel"></label>
			  		  <br>
                      <input v-model = "manager.firstName" type="text" placeholder="FirstName" name = "FirstName">
                      <br>
                      <label class="hidden" name = "FirstNameLabel"></label>
                      <br>
                      <input v-model = "manager.lastName" type="text" placeholder="LastName" name = "LastName">
                      <br>
                      <label class="hidden" name = "LastNameLabel"></label>
                      <br>
                      <select v-model = "manager.gender" name = "Gender">
                        <option value="" disabled selected>Select gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                      </select>
                      <br>
                      <label class="hidden" name = "GenderLabel"></label>
                      <br>
                      <input v-model = "manager.dateOfBirth" type="date" name="BirthDate">
                      <br>
                      <label class="hidden" name = "BirthDateLabel"></label>
                      <br>
                      <button v-on:click = "registerNewManagerAndCreateStore">Register Manager and Create Store</button>
                    </div>
                  </div>
	        </div>	       
    	</div>
	</div>
	    `,
    mounted () {
		
		//this.adminId = this.$route.params.id;
		axios
			.get('rest/user/managers/')
			.then(response=>{
				this.managers = response.data
				let count = 0
				for(m in this.managers){
					count++;					
				}
				
				if(count == 0){
					this.isDisabled = true;
					this.formIsEnabled = true;
				}			
				this.showLocationOnMap(55.46, 19.20);
			}) 
			
    },
    methods: {
		createStore : function(){
			
			var valid1 = true;
			var storenameEl = document.getElementsByName('StoreName')[0];
			var addressEl = document.getElementsByName('Address')[0];
			var fromEl = document.getElementsByName('From')[0];
			var toEl = document.getElementsByName('To')[0];
			var logourlEl = document.getElementsByName('LogoUrl')[0];
			var managerEl = document.getElementsByName('managers')[0];
		
			var storenameLab = document.getElementsByName('StoreNameLabel')[0];
			var addressLab = document.getElementsByName('AddressLabel')[0];
			var fromLab = document.getElementsByName('FromLabel')[0];
			var toLab = document.getElementsByName('ToLabel')[0];
			var logourlLab = document.getElementsByName('LogoUrlLabel')[0];
			var managerLab = document.getElementsByName('ManagerLabel')[0];
			
			storenameLab.classList.add('hidden');
		    addressLab.classList.add('hidden');
		    fromLab.classList.add('hidden');
		    toLab.classList.add('hidden');
		    logourlLab.classList.add('hidden');
		    managerLab.classList.add('hidden');
			
			storenameEl.style.borderColor = '';
		    addressEl.style.borderColor = '';
		    fromEl.style.borderColor = '';
		    toEl.style.borderColor = '';
		    logourlEl.style.borderColor = '';
		    managerEl.style.borderColor = '';
		    
		    
		
			if (!storenameEl.value)
			{
				storenameEl.style.borderColor = 'red';
				storenameEl.style.borderWidth = '1px';
				storenameLab.classList.remove('hidden');
				storenameLab.textContent = "Can't be empty";
				storenameLab.style.color = 'red';
				valid1 = false;
			}
		
			if (!addressEl.value)
			{
				addressEl.style.borderColor = 'red';
				addressEl.style.borderWidth = '1px';
				addressLab.classList.remove('hidden');
				addressLab.textContent = "Can't be empty";
				addressLab.style.color = 'red';
				valid1 = false;
			}
			
			const pattern = /^[A-Za-z0-9\s]+,[A-Za-z0-9\s]+,[0-9\s]+$/;
			
			if(!pattern.test(addressEl.value) && addressEl.value != "")
			{
				addressEl.style.borderColor = 'red';
				addressEl.style.borderWidth = '1px';
				addressLab.classList.remove('hidden');
				addressLab.textContent = "Address pattern has to be [A-Za-z0-9\s]+,[A-Za-z0-9\s]+,[0-9\s]+";
				addressLab.style.color = 'red';
				valid1 = false;
			}
		
			if (!fromEl.value)
			{
				fromEl.style.borderColor = 'red';
				fromEl.style.borderWidth = '1px';
				fromLab.classList.remove('hidden');
				fromLab.textContent = "Can't be empty";
				fromLab.style.color = 'red';
				valid1 = false;
			}
			
			if (!toEl.value)
			{
				toEl.style.borderColor = 'red';
				toEl.style.borderWidth = '1px';
				toLab.classList.remove('hidden');
				toLab.textContent = "Can't be empty";
				toLab.style.color = 'red';
				valid1 = false;
			}
			
			if (!logourlEl.value)
			{
				logourlEl.style.borderColor = 'red';
				logourlEl.style.borderWidth = '1px';
				logourlLab.classList.remove('hidden');
				logourlLab.textContent = "Can't be empty";
				logourlLab.style.color = 'red';
				valid1 = false;
			}
			
			if(this.isDisabled == false){
				if (!managerEl.value)
				{
					managerEl.style.borderColor = 'red';
					managerEl.style.borderWidth = '1px';
					managerLab.classList.remove('hidden');
					managerLab.textContent = "Can't be empty";
					managerLab.style.color = 'red';
					valid1 = false;
				}
			}
			
			if(valid1){	
				this.fillLocation()
			}		
		},
		fillLocation : function(){
			axios
				.get('rest/locations/' + this.longitude + '/' + this.latitude + '/' + this.address)
				.then(response =>{
					this.newStore.location = response.data
					this.newStore.locationId = this.newStore.location.id
					this.newStore.workingTime = this.startTime + ' - ' + this.endTime
					if(this.isDisabled == true){
						this.user.username = this.manager.username
						this.user.password = this.manager.password
						this.user.firstName = this.manager.firstName
						this.user.lastName = this.manager.lastName
						this.user.gender = this.manager.gender
						this.user.dateOfBirth = this.manager.dateOfBirth
						this.user.role = this.manager.role
						
						this.checkUsername()						
					}else{						
						this.getManagerByUsername(this.managerUsername)
					}
					
				})
		},
		getManagerByUsername : function(managerUsername){
			axios
				.get('rest/user/managers/' + managerUsername)
				.then(response =>{
					this.manager = response.data
					
					this.saveManagerWithNewStore(this.manager)
				})
		},
		saveManagerWithNewStore : function(managerToSave){
			axios
				.post('rest/stores/', this.newStore)
				.then(response =>{
					this.newStore = response.data
					this.manager.rentACarStore = this.newStore							
					axios
						.put('rest/user/managers/', managerToSave)
						.then(response =>{
							if(this.isDisabled == true){
								this.saveUser(this.user)
							}else{								
								router.push(`/`)
							}
						})
				})
		},
		registerNewManagerAndCreateStore : function(){
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
			if(valid && this.valid){				
				this.createStore()
			}
		},
		saveUser : function(userToSave){						
				axios
					.post('rest/user/', this.user)
					.then(response =>{					
							router.push(`/`)
					})
		},
		checkUsername : function(){
			axios
				.get('rest/user/findby/' + this.user.username)
				.then(response =>{
					if(response.data){
						alert("User already exists")
					}else{
						this.saveManagerWithNewStore(this.manager)
					}
				})
		},
		goToMyAccount : function(){
			router.push(`/administratorProfile`)
		},
		
		initMap() {
        var map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM() // OtvoreniStreetMap (OSM) sloj
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([this.longitude, this.latitude]), // Postavite centar mape na određene koordinate (longitude, latitude)
                zoom: 12 // Postavite nivo zumiranja mape
            })
        });
    },
		
		showLocationOnMap(longitude, latitude) {
	    	const map = new ol.Map({
		      	target: 'map',
		      	layers: [
			        new ol.layer.Tile({
			          	source: new ol.source.OSM(),
		        		}),
		       	],
		       	
		      	view: new ol.View({
			        center: ol.proj.fromLonLat([this.kliknutaLongitude, this.kliknutaLatitude]),
			        zoom: 12, 
		      	}),
		    });
	
	    	const marker = new ol.Feature({
	      		geometry: new ol.geom.Point(ol.proj.fromLonLat([this.kliknutaLongitude, this.kliknutaLatitude])),
	    	});
	
	    	const markerStyle = new ol.style.Style({
		      	image: new ol.style.Icon({
				    src: 'https://openlayers.org/en/latest/examples/data/icon.png',
				    anchor: [0.5, 1],
				    anchorXUnits: 'fraction',
				    anchorYUnits: 'fraction',
				    opacity: 1,
				    scale: 0.5
				  })
	    	});
	
	    	marker.setStyle(markerStyle);
	
	    	const vectorSource = new ol.source.Vector({
	      		features: [marker],
	    	});
	
	    	const vectorLayer = new ol.layer.Vector({
	      		source: vectorSource,
	    	});
	
	    	map.addLayer(vectorLayer);
	    	
	    	//koristice se za izbor lokacije
	    	map.on('click', (event) => {
		    	const clickedCoordinate = event.coordinate;
		    	const [clickedLongitude, clickedLatitude] = ol.proj.toLonLat(clickedCoordinate);
		    
		    	// Obrada kliknute lokacije
		    	console.log('Kliknuta lokacija:', clickedLongitude, clickedLatitude);
		    	
		    	this.kliknutaLongitude = clickedLongitude;
		    	this.kliknutaLatitude = clickedLatitude;
		    	
		    	this.longitude = this.kliknutaLongitude;
		    	this.latitude = this.kliknutaLatitude;
		    	
		    	marker.setGeometry(new ol.geom.Point(ol.proj.fromLonLat([clickedLongitude, clickedLatitude])));
		    	
		    	
		    	
		    	this.geocodeLocation(clickedLongitude, clickedLatitude);
		  	});
	  	},
	  	
	  	geocodeLocation(longitude, latitude) {
  			const url = `https://nominatim.openstreetmap.org/reverse?format=json&lon=${longitude}&lat=${latitude}`;
		  	axios
		    	.get(url)
		    	.then((response) => {
		      		if (response.data) {
		        	
		        	// Dalja obrada adrese ili ažuriranje Vue data objekta
		        		var addressLab = document.getElementsByName('addressLabel')[0];
		        		addressLab.classList.add('hidden');
		        		
		        		
		        		
		        		const address = response.data.address;
        				const street = address.road || '';
				        const number = address.house_number || '';
				        const city = address.city || address.town || '';
				        const postalCode = address.postcode || '';
				        
				        this.address = street + ' ' + number + ', ' + city + ', ' + postalCode;
				        
				        if (number === '')
				        {
							this.valid = false;
							addressLab.classList.remove('hidden');
							addressLab.textContent = "You must select object!";
							addressLab.style.color = 'red';
						}
				        
						console.log('Adresa:', address);
				        console.log('Ulica i broj:', street, number);
				        console.log('Grad:', city);
				        console.log('Poštanski broj:', postalCode);
		      		} else {
		        		console.log('Adresa nije pronađena');
		      		}
		    	})
		    	.catch((error) => {
		      		console.error('Greška pri geokodiranju:', error);
		    	});
			
		}
    }
});