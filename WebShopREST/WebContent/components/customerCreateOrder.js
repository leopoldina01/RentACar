Vue.component("customerCreateOrder", {
	data: function () {
	    return {
			username: '',
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			currentLoggedUser : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			usernameEmpty: false,
			passwordEmpty: false,
			pickUpDate : '',
			searchPickUpDate : '',
			dateOfReturn : '',
			searchDateOfReturn : '',
			vehicles: [],
			vehiclesIds: [],
			valid: true,
			itemsInCart: 0,
			korpaJePrazna: false,
			korpaNijePrazna: false
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
    	
    	<div>
    		<div class="shopping-cart-container">
    			<label>Items in cart: {{itemsInCart}} </label>
    			<br>
    			<button class="shopping-cart-button" v-on:click="showShoppingCart"> <i class="fas fa-shopping-cart"></i> Shopping cart</button>
    		</div>
    		
	    	<div v-if="korpaJePrazna" class="search-input-container-orders-create">
				<label>Pick up date: </label>
				
				<div>
					<input type="date" v-model="pickUpDate" name="PickUpDate">
					<br>
					<label class="hidden" name = "pickUpDateLabel"></label>
					<label class="hidden" name = "pickUpAndReturnDateLabel"></label>
				</div>
				
	            
	            <label>Date of return: </label>
	            
	            <div>
	            	<input type="date" v-model="dateOfReturn" name="ReturnDate"/>
	            	<br>
	            	<label class="hidden" name = "returnDateLabel"></label>
	            </div>
	            
	            <button v-on:click="searchVehicles"><i class="fas fa-search"></i> Search</button>
			</div>
			
			<div v-if="korpaNijePrazna" class="requiredDatesForOrder">
				<label>Pick up date: {{pickUpDate}}</label>
				<label>Return date: {{dateOfReturn}}</label>
			</div>
		</div>
		
		<div class="all-vehicles-for-renting">
			<div v-for="v in vehicles" class ="show-vehicle-list-order">
				<div class="vehicle-image-wrapper">
					<img class="show-vehicle-image" :src="v.imageUrl" alt="Vehicle Image">
				</div>
				<div class="vehicle-details">
					<div class="vehicle-info-top">
						<label class="show-vehicle-brand">{{v.brand}}</label>
		                <label class="show-vehicle-model">{{v.model}}</label>
		                <label class="show-vehicle-price">{{v.price}}din/24h</label>
					</div>
					<label class="show-vehicle-status">{{v.status}}</label>
					<label class="show-vehicle-fuel">Fuel: {{v.fuelType}}</label>
					<div class="vehicle-bottom-details">
						<label class="show-vehicle-type">Type: {{v.vehicleType}}</label>
		                <label class="show-vehicle-consumption">Consumption: {{v.consumption}}</label>
		                <label class="show-vehicle-doors">Doors: {{v.doorsNumber}}</label>
		                <label class="show-vehicle-passengers">Passengers: {{v.passengersNumber}}</label>
		                <label class="show-vehicle-transmission">Transmission: {{v.gearshiftType}}</label>
		                <br>
		                <label class="show-vehicle-description">Description: {{v.description}}</label>
					</div>
				</div>
				<button class="add-button" @click="AddVehicleToCart(v.id, v.price)"><i class="fas fa-plus"></i></button>
			</div>
		</div>
		
	 </div>
	    `,
    mounted () {
		axios
			.get('rest/user/currentLoggedUser')
			.then(response =>{
				this.user = response.data;
				
				axios
					.get('rest/users/customers/' + this.user.id)
					.then(response => {
						this.customer = response.data;
						this.itemsInCart = this.customer.shoppingCart.vehicles.length;
						
						if (this.itemsInCart === 0)
						{
							this.korpaJePrazna = true;
							this.korpaNijePrazna = false;
							axios
								.get('rest/vehicles/allVehicles')
								.then(response => {this.vehicles = response.data;})
						}
						else {
							this.korpaJePrazna = false;
							this.korpaNijePrazna = true;
							this.pickUpDate = this.customer.shoppingCart.pickUpDate;
							this.dateOfReturn = this.customer.shoppingCart.returnDate;
							
							axios
								.get('rest/vehicles/addVehicleToCart/' + this.pickUpDate + '/' + this.dateOfReturn + '/' + this.customer.id)
								.then(response => {
									this.vehicles = response.data;
									})
						}
						
						
					})	
			})
    },
    methods: {
		goToMyAccount : function()
		{
			router.push(`/customerProfile`)
		},
		
		checkValidation : function()
		{
			this.valid = true;
			var pickUpDateEl = document.getElementsByName('PickUpDate')[0];
			var returnDateEl = document.getElementsByName('ReturnDate')[0];
			
			var pickUpDateLab = document.getElementsByName('pickUpDateLabel')[0];
			var returnDateLab = document.getElementsByName('returnDateLabel')[0];
			
			var pickUpDateAndReturnLab = document.getElementsByName('pickUpAndReturnDateLabel')[0];
			
			pickUpDateLab.classList.add('hidden');
			returnDateLab.classList.add('hidden');
			pickUpDateAndReturnLab.classList.add('hidden');
			
			pickUpDateEl.style.borderColor = '';
			returnDateEl.style.borderColor = '';
			
			if (this.pickUpDate === '')
			{
				this.searchPickUpDate = null;
				pickUpDateEl.style.borderColor = 'red';
				pickUpDateEl.style.borderWidth = '1px';
				pickUpDateLab.classList.remove('hidden');
				pickUpDateLab.textContent = "Select date";
				pickUpDateLab.style.color = 'red';
				this.valid = false;
			}
			else 
			{
				this.searchPickUpDate = this.pickUpDate;
			}
			
			if (this.dateOfReturn === '')
			{
				this.searchDateOfReturn = null;
				returnDateEl.style.borderColor = 'red';
				returnDateEl.style.borderWidth = '1px';
				returnDateLab.classList.remove('hidden');
				returnDateLab.textContent = "Select date";
				returnDateLab.style.color = 'red';
				this.valid = false;
			}
			else
			{
				this.searchDateOfReturn = this.dateOfReturn;
			}
			
			if (this.valid)
			{
				const pickUpDateObj = new Date(this.pickUpDate);
				const returnDateObj = new Date(this.dateOfReturn);
				
				if (pickUpDateObj >= returnDateObj) 
				{
					pickUpDateEl.style.borderColor = 'red';
					pickUpDateEl.style.borderWidth = '1px';
					returnDateEl.style.borderColor = 'red';
					returnDateEl.style.borderWidth = '1px';
					pickUpDateAndReturnLab.classList.remove('hidden');
					pickUpDateAndReturnLab.textContent = "Pick up date should be before return date";
					pickUpDateAndReturnLab.style.color = 'red';
			        this.valid = false;
			    }
			    
			    if (pickUpDateObj < new Date())
			    {
					pickUpDateEl.style.borderColor = 'red';
					pickUpDateEl.style.borderWidth = '1px';
					returnDateEl.style.borderColor = 'red';
					returnDateEl.style.borderWidth = '1px';
					pickUpDateAndReturnLab.classList.remove('hidden');
					pickUpDateAndReturnLab.textContent = "Pick up date shouldn't be before today";
					pickUpDateAndReturnLab.style.color = 'red';
			        this.valid = false;
				}
			}
		},
		
		searchVehicles : function() 
		{
			this.checkValidation();
			
			if (this.valid)
			{
				axios
					.get('rest/vehicles/searchAvailableVehicles/' + this.searchPickUpDate + '/' + this.searchDateOfReturn)
					.then(response => {
						this.vehicles = response.data;
						
						axios
							.get('rest/users/customers/updateDates/' + this.searchPickUpDate + '/' + this.searchDateOfReturn + '/' + this.customer.id)
							.then(response => {this.customer = response.data;})
						})
			}
		},
		
		AddVehicleToCart(vehicleId, vPrice) {
			if (this.korpaJePrazna)
			{
				this.checkValidation();
			}
			
			if (this.valid)
			{
				this.vehiclesIds.push(vehicleId);
				
				
				this.korpaJePrazna = false;
				this.korpaNijePrazna = true;
				
				axios
					.get('rest/users/customers/addCarToCart/' + vehicleId + '/' + this.customer.id + '/' + vPrice)
					.then(response => {
						this.customer = response.data;
						this.itemsInCart = this.customer.shoppingCart.vehicles.length;
						
						axios
							.get('rest/vehicles/addVehicleToCart/' + this.pickUpDate + '/' + this.dateOfReturn + '/' + this.customer.id)
							.then(response => {this.vehicles = response.data;})
						})
			}
		},
		
		showShoppingCart : function()
		{
			router.push('/shoppingCartOverview')
		}
    }
});