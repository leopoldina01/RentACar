Vue.component("managerStore", {
	data: function () {
	    return {
			id: -1,
			manager : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: '', role: 'MANAGER', isBlocked: '', rentACarStore: ''},
			user: {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: '', role: 'MANAGER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			vehicles : [],
			vehicle : {id: -1, brand: '', model: '' },
			newVehicle: {id: -1, brand: '', model: '', price: '', vehicleType: '', store: null, gearshiftType: '', fuelType: '', consumption: '', doorsNumber: '', passengersNumber: '', description: '', imageUrl: '', status: 'AVAILABLE'},
			updatedVehicle: {id: -1, brand: '', model: '', price: '', vehicleType: '', store: null, gearshiftType: '', fuelType: '', consumption: '', doorsNumber: '', passengersNumber: '', description: '', imageUrl: '', status: 'AVAILABLE'},
			prikaziVozila: false,
			offeredVehicles : [],
			orders : [],
			prikaziNarudzbine : false,
			selectedOrder: null,
			rentedVehicles : [],
			customers : [],
			prikaziCustomere: false,
			priceFrom: '',
			searchPriceFrom: '',
			priceTo: '',
			searchPriceTo: '',
			dateFrom: '',
			dateTo: '',
			selectedSort: 'unsorted',
			createVehicleOpened: false,
			
			updateVehicleOpened: false,
			updatedBrand: '',
			updatedModel: '',
			updatedPrice: '',
			updatedVehicleType: '',
			updatedGearshiftType: '',
			updatedFuelType: '',
			updatedConsumption: '',
			updatedDoorsNumber: '',
			updatedPassengersNumber: '',
			updatedDescription: '',
			updatedImageUrl: '',
			showModal: false,
			comment: '',
			selectedOrderIndex: -1,
			takenOrderIndex: -1,
			rejectedOrder: ''
	    }
	},
	    template: `
	    <div>
	    	<header>
	            <div class="logo">Rent A Car</div>
	            <div class="nav-buttons">
	            	<button v-on:click="openCreateVehicleForm"><i class="fas fa-plus-circle"></i> Add Vehicle</button>
                	<button v-on:click="goToMyAccount"><i class="fas fa-user"></i> My Account</button>
            	</div>
	        </header>
	        
	        <div v-if="createVehicleOpened" class="create-vehicle-container">
			    <h2 class="create-vehicle-h2">CREATE VEHICLE</h2>
			  <input name="Brand" v-model="newVehicle.brand" class="small-input" type="text" placeholder="Enter Brand:">
			  <br>
			  <label class="hidden" name = "BrandLabel"></label>
			  <br>
			  <input name="Model" v-model="newVehicle.model" class="small-input" type="text" placeholder="Enter Model:">
			  <br>
			  <label class="hidden" name = "ModelLabel"></label>
			  <br>
			  <input name="ImageUrl" v-model="newVehicle.imageUrl" class="small-input" type="text" placeholder="Enter Image Url:">
			  <br>
			  <label class="hidden" name = "ImageUrlLabel"></label>
			  <br>
			  <input name="Price" v-model="newVehicle.price" class="small-input" type="number" placeholder="Enter Price:">
			  <br>
			  <label class="hidden" name = "PriceLabel"></label>
			  <br>
			  <input name="Consumption" v-model="newVehicle.consumption" class="small-input" type="number" placeholder="Enter Consumption:">
			  <br>
			  <label class="hidden" name = "ConsumptionLabel"></label>
			  <br>
			  <input name="DoorsNumber" v-model="newVehicle.doorsNumber" class="small-input" type="number" placeholder="Enter Number of Doors:">
			  <br>
			  <label class="hidden" name = "DoorsNumberLabel"></label>
			  <br>
			  <input name="PassengerNumber" v-model="newVehicle.passengersNumber" class="small-input" type="number" placeholder="Enter Passenger number:">
			  <br>
			  <label class="hidden" name = "PassengerNumberLabel"></label>
			  <br>
			  <select name="VehicleType" v-model="newVehicle.vehicleType">
			    <option selected value="">Select Vehicle Type</option>
			    <option value="CAR">CAR</option>
			    <option value="VAN">VAN</option>
			    <option value="TRUCK">TRUCK</option>
			    <option value="MOBILEHOME">MOBILEHOME</option>
			  </select>
			  <br>
			  <label class="hidden" name = "VehicleTypeLabel"></label>
			  <br>
			  <select name="GearshiftType" v-model="newVehicle.gearshiftType">
			    <option selected value="">Select Gearshift Type</option>
			    <option value="MANUAL">MANUAL</option>
			    <option value="AUTOMATIC">AUTOMATIC</option>
			  </select>
			  <br>
			  <label class="hidden" name = "GearshiftTypeLabel"></label>
			  <br>
			  <select name="FuelType" v-model="newVehicle.fuelType">
			    <option selected value="">Select Fuel Type</option>
			    <option value="DIESEL">DIESEL</option>
			    <option value="PETROL">PETROL</option>
			    <option value="HYBRID">HYBRID</option>
			    <option value="ELECTRIC">ELECTRIC</option>
			  </select>
			  <br>
			  <label class="hidden" name = "FuelTypeLabel"></label>
			  <br>
			  <textarea rows="5" cols="32" name="Description" v-model="newVehicle.description" class="large-input" placeholder="Description..."></textarea>
			  <br>
			  <label class="hidden" name = "DescriptionLabel"></label>
			  <br>
			  <button v-on:click="createVehicle">Create Vehicle</button>
			  <button v-on:click="cancelAddingVehicle">Cancel</button>
			</div>
			
			<div v-if="updateVehicleOpened" class="create-vehicle-container">
			    <h2 class="create-vehicle-h2">UPDATE VEHICLE</h2>
			  <input name="Brand" v-model="updatedBrand" class="small-input" type="text" placeholder="Enter Brand:">
			  <br>
			  <label class="hidden" name = "BrandLabel"></label>
			  <br>
			  <input name="Model" v-model="updatedModel" class="small-input" type="text" placeholder="Enter Model:">
			  <br>
			  <label class="hidden" name = "ModelLabel"></label>
			  <br>
			  <input name="ImageUrl" v-model="updatedImageUrl" class="small-input" type="text" placeholder="Enter Image Url:">
			  <br>
			  <label class="hidden" name = "ImageUrlLabel"></label>
			  <br>
			  <input name="Price" v-model="updatedPrice" class="small-input" type="number" placeholder="Enter Price:">
			  <br>
			  <label class="hidden" name = "PriceLabel"></label>
			  <br>
			  <input name="Consumption" v-model="updatedConsumption" class="small-input" type="number" placeholder="Enter Consumption:">
			  <br>
			  <label class="hidden" name = "ConsumptionLabel"></label>
			  <br>
			  <input name="DoorsNumber" v-model="updatedDoorsNumber" class="small-input" type="number" placeholder="Enter Number of Doors:">
			  <br>
			  <label class="hidden" name = "DoorsNumberLabel"></label>
			  <br>
			  <input name="PassengerNumber" v-model="updatedPassengersNumber" class="small-input" type="number" placeholder="Enter Passenger number:">
			  <br>
			  <label class="hidden" name = "PassengerNumberLabel"></label>
			  <br>
			  <select name="VehicleType" v-model="updatedVehicleType">
			    <option selected value="">Select Vehicle Type</option>
			    <option value="CAR">CAR</option>
			    <option value="VAN">VAN</option>
			    <option value="TRUCK">TRUCK</option>
			    <option value="MOBILEHOME">MOBILEHOME</option>
			  </select>
			  <br>
			  <label class="hidden" name = "VehicleTypeLabel"></label>
			  <br>
			  <select name="GearshiftType" v-model="updatedGearshiftType">
			    <option selected value="">Select Gearshift Type</option>
			    <option value="MANUAL">MANUAL</option>
			    <option value="AUTOMATIC">AUTOMATIC</option>
			  </select>
			  <br>
			  <label class="hidden" name = "GearshiftTypeLabel"></label>
			  <br>
			  <select name="FuelType" v-model="updatedFuelType">
			    <option selected value="">Select Fuel Type</option>
			    <option value="DIESEL">DIESEL</option>
			    <option value="PETROL">PETROL</option>
			    <option value="HYBRID">HYBRID</option>
			    <option value="ELECTRIC">ELECTRIC</option>
			  </select>
			  <br>
			  <label class="hidden" name = "FuelTypeLabel"></label>
			  <br>
			  <textarea rows="5" cols="32" name="Description" v-model="updatedDescription" class="large-input" placeholder="Description..."></textarea>
			  <br>
			  <label class="hidden" name = "DescriptionLabel"></label>
			  <br>
			  <button v-on:click="updateVehicle">Update Vehicle</button>
			  <button v-on:click="cancelUpdatingVehicle">Cancel</button>
			</div>
	        
	        <div class="managerStoreContainer">
	        	<div class="image">
	        		<img :src="manager.rentACarStore.logoUrl">
	        	</div>
	        	<div class="RentACarStoreText">
	        	    <p>
	        			<h2>{{manager.rentACarStore.name}}</h2>
	        			<label>{{manager.rentACarStore.location?.address}}</label>
	        			<br>
	        			<br>
	        			<br>
	        			<label>{{manager.rentACarStore.workingTime}}</label>
	        			<br>
	        			<label>{{manager.rentACarStore.workingStatus}}</label>
	        			<br>
	        			<br>
	        			<br>
	        			<label>Rating: {{manager.rentACarStore.rating}}</label>
	        			<br>
	        			<br>
	        			
	        		</p>
	        	</div>
	        </div>
	        
	        <div class="managerStoreVehiclesHeader" @click="openDetailsVehicles">
	        	<h2 class="manager-vehicles-h3">VEHICLES</h2>
	        </div>
	        
	        <div v-if="prikaziVozila" class="list-box">
				    <div class="vehicle-list" v-for="v in offeredVehicles">
				      <img class="vehicle-image" :src="v.imageUrl" alt="Vehicle Image">
		    			<div class="single-vehicle-details">
		    				<p class="vehicle-brand">{{v.brand}}</p>
		    				<p class="vehicle-model">{{v.model}}</p>
						    <p class="vehicle-price">Price: {{v.price}}din</p>
						    <button class="izmeni-vehicle-button" @click="updateVehicleButtonPress(v)"><i class="fas fa-pencil-alt"></i> Update</button>
                        	<button class="obrisi-vehicle-button" @click="deleteVehicle(v)"><i class="fas fa-trash-alt"></i> Delete</button>
						    <div class="vehicle-extra-details">
						      <p>Type: {{v.vehicleType}} ||</p>
						      <p>Consumption: {{v.consumption}} ||</p>
						      <p>Doors: {{v.doorsNumber}} ||</p>
						      <p>Passangers: {{v.passengersNumber}}</p>
						    </div>
		    			</div>
				    </div>
				    <!-- Add more cards as needed -->
			</div>
			
			<div class="managerStoreVehiclesHeader" @click="showCustomers">
	        	<h2>CUSTOMERS</h2>
	        </div>
	        
	        <div v-if="prikaziCustomere" class="list-box">
	        	<div class = "cardCustomers" v-for="c in customers">
	        	<label><span class="label-text">Username:	{{c.username}}</span></label>
	            <label><span class="label-text">First Name:		{{c.firstName}}</span></label>
	            <label><span class="label-text">Last Name:		{{c.lastName}}</span></label>
	            <label><span class="label-text">Gender:		{{c.gender}}</span></label>
	            <label><span class="label-text">Date of Birth: 		{{c.dateOfBirth}}</span></label>
	        	</div>
	        </div>
			
			<div class="managerStoreVehiclesHeader" @click="openOrders">
	        	<h2>ORDERS</h2>
	        </div>
	        
	        <div v-if="prikaziNarudzbine" class="list-box-narudzbine">
	        	
	        	<div >
		        	<select class="sort-orders-select" v-model="selectedSort" v-on:change="searchOrders">
		                <option value="unsorted">Sort by...</option>
		                <option value="priceAscending">Sort by price &#x2191;</option>
		                <option value="priceDescending">Sort by price &#x2193;</option>
		                <option value="dateAscending">Sort by date &#x2191;</option>
		                <option value="dateDescending">Sort by date &#x2193;</option>
		            </select>
		        </div>
	        	
	        	<div class="search-input-container-orders">
	        		<input type="number" placeholder="Price from" v-model="priceFrom"/>
	        		<input type="number" placeholder="Price to" v-model="priceTo"/>
	        		<label>From: </label>
	        		<input type="date" v-model="dateFrom">
	        		<label>To: </label>
	        		<input type="date" v-model="dateTo"/>
	        		<button v-on:click="searchOrders"><i class="fas fa-search"></i> Search</button>
	        	</div>
	        
			    <div class="card " v-for="(o, index) in orders">
			    	<div class="orderItem" @click="openDetails(o)">
			    		<div>
						    <h2>Order: {{o.idString}}</h2>
						    <p>Order status: {{o.status}}</p>
						    <p>Total price: {{o.price}} din</p>
						    <p>Rented on: {{o.rentalDate}}</p>
						    <p>Pick up date: {{o.pickUpDate}} Return date: {{o.returnDate}}</p>
						    <p>Customer: {{o.customerFirstAndLastName}}</p>
						</div>
					    <div class="ordered-in">
					        <label class="ordered-in-label">Ordered in: {{o.rentACarStore.name}}</label>
					        <br>
					        <img :src="o.rentACarStore.logoUrl">
					    </div>
					    <div v-if="o.status === 'PROCESSING'">
					    	<button class="cancel-order-button" v-on:click="approveOrder(o)"><i class="fas fa-check-circle"></i> Approve</button>
					    	<button class="cancel-order-button" @click="openCommentModal(o)"><i class="fas fa-times-circle"></i> Reject</button>
					    </div>
					    <div v-if="o.status === 'APPROVED'">
					    	<button class="cancel-order-button" @click="orderTaken(o, index)">Taken</button>
					    	<label v-bind:id="'takenLabel-' + index" class="hidden" name="takenLabel"></label>
					    </div>
					    <div v-if="o.status === 'TAKEN'">
					    	<button class="cancel-order-button" @click="returnVehicles(o)">Return</button>
					    </div>
			    	</div>
			    	<br>
			    	<div v-if="o === selectedOrder" class="vehicle-details">
			    		<h3>Ordered vehicles:</h3>
			    		<div v-for="v in rentedVehicles" class="vehicle-list">
			    			<img class="vehicle-image" :src="v.imageUrl" alt="Vehicle Image">
			    			<div class="single-vehicle-details">
			    				<p class="vehicle-brand">{{v.brand}}</p>
			    				<p class="vehicle-model">{{v.model}}</p>
							    <p class="vehicle-price">Price: {{v.price}}din</p>
							    <div class="vehicle-extra-details">
							      <p>Type: {{v.vehicleType}} ||</p>
							      <p>Consumption: {{v.consumption}} ||</p>
							      <p>Doors: {{v.doorsNumber}} ||</p>
							      <p>Passangers: {{v.passengersNumber}}</p>
							    </div>
			    			</div>
			    		</div>
				    </div>
			    </div>
			    
			    <div class="modal-overlay" v-if="showModal">
			    	<div class="modal">
			    	<h3>Reject order: {{this.rejectedOrder}}</h3>
			    	<textarea v-model="comment" placeholder="Enter comment"></textarea>
			    		<div class="modal-buttons">
			    			<button @click="submitComment" class="cancel-order-button">Submit</button>
			    			<button @click="cancelModal" class="cancel-order-button">Cancel</button>
			    		</div>
			    		<label class="hidden" name="submitRejection"></label>
			    	</div>
			    </div>
			    
	  		</div>
	  	</div>	
	</div>  		
	
</div>
	    `,
    mounted () {
		//this.id = this.$route.params.id;
		axios
			.get('rest/user/currentLoggedUser')
			.then(response =>{
				this.user = response.data;
				axios
					.get('rest/user/managers/banana/' + this.user.id)
					.then(response => (this.manager = response.data))	
			})
			
		
			
		//var provera = this.manager;
    },
    methods: {
		returnVehicles(order)
		{
			axios
				.get('rest/orders/returnOrder/' + order.id)
				.then(response => {this.orders = response.data;})
		},
		
		orderTaken(order, index)
		{
			this.selectedOrderIndex = index;
			this.takenOrderIndex = index;
			
			var takenLab = document.getElementById('takenLabel-' + index);
			takenLab.classList.add("hidden");
			
			const currentDate = new Date();
			const pickupdate = new Date(order.pickUpDate);
			if (currentDate < pickupdate)
			{
				takenLab.classList.remove("hidden");
				takenLab.textContent = "It's before pick up date!";
				takenLab.style.color = 'red';
			}
			else {
				axios
					.get('rest/orders/orderIsTaken/' + order.id)
					.then(response => {this.orders = response.data;})
			}
		},
		
		openCommentModal(order) {
		    this.selectedOrder = order;
		    this.rejectedOrder = order.idString;
		    this.showModal = true;
		},
		
		submitComment()
		{
			var submitRejectionLab = document.getElementsByName('submitRejection')[0];
			submitRejectionLab.classList.add('hidden');
			
			if (this.comment === '')
			{
				submitRejectionLab.classList.remove('hidden');
				submitRejectionLab.textContent = "Write a comment!";
				submitRejectionLab.style.color = 'red';
			}
			else {
				axios
					.get('rest/orders/rejectOrder/' + this.comment + '/' + this.selectedOrder.id)
					.then(response => {this.orders = response.data;})
				this.closeModal();
			}
		},
		
		cancelModal() {
	    	this.closeModal();
	    },
	    
	    closeModal()
	    {
			this.selectedOrder = null;
			this.showModal = false;
			this.comment = '';
		},
		
		approveOrder(order) 
		{
			axios
				.post('rest/orders/approveOrder', order)
				.then(response => {this.orders = response.data;})
		},
		
		openDetailsVehicles()
		{
			this.prikaziVozila = !this.prikaziVozila;
			
			axios
      			.post('rest/vehicles/', this.manager.rentACarStore.offeredVehicles)
      			.then(response => {
					  this.offeredVehicles = response.data
					  this.cancelAddingVehicle()
					  this.cancelUpdatingVehicle()
					  })
		},
		
		openOrders()
		{
			this.prikaziNarudzbine = !this.prikaziNarudzbine;
			
			axios
				.get('rest/orders/' + this.manager.rentACarStore.id)
				.then(response => {
					this.orders = response.data
					this.cancelAddingVehicle()
					this.cancelUpdatingVehicle()
				})
		},
		
		openDetails(order) {
      		this.selectedOrder = order;
      		
      		axios
      			.post('rest/vehicles/', order.rentedVehicles)
      			.then(response => (this.rentedVehicles = response.data))
    	},
    	
    	showCustomers()
    	{
			this.prikaziCustomere = !this.prikaziCustomere;
			
			axios
				.get('rest/orders/customers/' + this.manager.rentACarStore.id)
				.then(response => {
					this.customers = response.data
					this.cancelAddingVehicle()
					this.cancelUpdatingVehicle()
				})
		},
		
		goToMyAccount : function(){
			router.push(`/managerProfile`)
		},
		
		searchOrders : function() 
		{
			if (this.priceFrom === '')
			{
				this.searchPriceFrom = -1;
			}
			else 
			{
				this.searchPriceFrom = this.priceFrom;
			}
			
			if (this.priceTo === '')
			{
				this.searchPriceTo = -1;
			}
			else
			{
				this.searchPriceTo = this.priceTo;
			}
			
			if (this.dateFrom === '')
			{
				this.dateFrom = "0000-00-00";
			}
			
			if (this.dateTo === '')
			{
				this.dateTo = "0000-00-00";
			}
			
			axios
				.get('rest/orders/search/' + this.searchPriceFrom + '/' + this.searchPriceTo + '/' + this.dateFrom + '/' + this.dateTo + '/' + this.manager.rentACarStore.id + '/' + this.selectedSort)
				.then(response => {this.orders = response.data;})
		},
		
		handleSortChange() {
			axios
				.get('rest/orders/search/' + this.searchPriceFrom + '/' + this.searchPriceTo + '/' + this.dateFrom + '/' + this.dateTo + '/' + this.manager.rentACarStore.id + '/' + this.selectedSort)
				.then(response => {this.orders = response.data;})
		},
		openCreateVehicleForm : function(){
			this.prikaziCustomere = false;
			this.prikaziVozila = false;
			this.prikaziNarudzbine = false;
			this.createVehicleOpened = true;
			
			//reset fields
			this.newVehicle.id = -1, 
			this.newVehicle.brand = ''
			this.newVehicle.model = '' 
			this.newVehicle.price = '' 
			this.newVehicle.vehicleType = '' 
			this.newVehicle.store = null, 
			this.newVehicle.gearshiftType = '' 
			this.newVehicle.fuelType = ''
			this.newVehicle.consumption = '' 
			this.newVehicle.doorsNumber = '' 
			this.newVehicle.passengersNumber = ''
			this.newVehicle.description = '' 
			this.newVehicle.imageUrl = '' 
			this.newVehicle.status = 'AVAILABLE'
			
			
		},
		cancelAddingVehicle : function(){
			this.newVehicle.id = -1, 
			this.newVehicle.brand = ''
			this.newVehicle.model = '' 
			this.newVehicle.price = '' 
			this.newVehicle.vehicleType = '' 
			this.newVehicle.store = null, 
			this.newVehicle.gearshiftType = '' 
			this.newVehicle.fuelType = ''
			this.newVehicle.consumption = '' 
			this.newVehicle.doorsNumber = '' 
			this.newVehicle.passengersNumber = ''
			this.newVehicle.description = '' 
			this.newVehicle.imageUrl = '' 
			this.newVehicle.status = 'AVAILABLE'
			
			this.createVehicleOpened = false;
		},
		createVehicle : function(){
			
			var valid = true;
			var brandEl = document.getElementsByName('Brand')[0];
			var modelEl = document.getElementsByName('Model')[0];
			var priceEl = document.getElementsByName('Price')[0];
			var imageUrlEl = document.getElementsByName('ImageUrl')[0];
			var consumptionEl = document.getElementsByName('Consumption')[0];
			var doorsNumberEl = document.getElementsByName('DoorsNumber')[0];
			var passengerNumberEl = document.getElementsByName('PassengerNumber')[0];
			var vehicleTypeEl = document.getElementsByName('VehicleType')[0];
			var gearshiftTypeEl = document.getElementsByName('GearshiftType')[0];
			var fuelTypeEl = document.getElementsByName('FuelType')[0];
			var descriptionEl = document.getElementsByName('Description')[0];
		
			var brandLab = document.getElementsByName('BrandLabel')[0];
			var modelLab = document.getElementsByName('ModelLabel')[0];
			var priceLab = document.getElementsByName('PriceLabel')[0];
			var imageUrlLab = document.getElementsByName('ImageUrlLabel')[0];
			var consumptionLab = document.getElementsByName('ConsumptionLabel')[0];
			var doorsNumberLab = document.getElementsByName('DoorsNumberLabel')[0];
			var passengerNumberLab = document.getElementsByName('PassengerNumberLabel')[0];
			var vehicleTypeLab = document.getElementsByName('VehicleTypeLabel')[0];
			var gearshiftTypeLab = document.getElementsByName('GearshiftTypeLabel')[0];
			var fuelTypeLab = document.getElementsByName('FuelTypeLabel')[0];
			var descriptionLab = document.getElementsByName('DescriptionLabel')[0];
			
			brandLab.classList.add('hidden');
		    modelLab.classList.add('hidden');
		    priceLab.classList.add('hidden');
		    imageUrlLab.classList.add('hidden');
		    consumptionLab.classList.add('hidden');
		    doorsNumberLab.classList.add('hidden');
		    passengerNumberLab.classList.add('hidden');
		    vehicleTypeLab.classList.add('hidden');
		    gearshiftTypeLab.classList.add('hidden');
		    fuelTypeLab.classList.add('hidden');
		    descriptionLab.classList.add('hidden');
			
			brandEl.style.borderColor = '';
		    modelEl.style.borderColor = '';
		    priceEl.style.borderColor = '';
		    imageUrlEl.style.borderColor = '';
		    consumptionEl.style.borderColor = '';
		    doorsNumberEl.style.borderColor = '';
		    passengerNumberEl.style.borderColor = '';
		    vehicleTypeEl.style.borderColor = '';
		    gearshiftTypeEl.style.borderColor = '';
		    fuelTypeEl.style.borderColor = '';
		    descriptionEl.style.borderColor = '';
		    
		    
		
			if (!brandEl.value)
			{
				brandEl.style.borderColor = 'red';
				brandEl.style.borderWidth = '1px';
				brandLab.classList.remove('hidden');
				brandLab.textContent = "Can't be empty";
				brandLab.style.color = 'red';
				valid = false;
			}
		
			if (!modelEl.value)
			{
				modelEl.style.borderColor = 'red';
				modelEl.style.borderWidth = '1px';
				modelLab.classList.remove('hidden');
				modelLab.textContent = "Can't be empty";
				modelLab.style.color = 'red';
				valid = false;
			}
		
			if (!priceEl.value)
			{
				priceEl.style.borderColor = 'red';
				priceEl.style.borderWidth = '1px';
				priceLab.classList.remove('hidden');
				priceLab.textContent = "Can't be empty";
				priceLab.style.color = 'red';
				valid = false;
			}
		
			if (!imageUrlEl.value)
			{
				imageUrlEl.style.borderColor = 'red';
				imageUrlEl.style.borderWidth = '1px';
				imageUrlLab.classList.remove('hidden');
				imageUrlLab.textContent = "Can't be empty";
				imageUrlLab.style.color = 'red';
				valid = false;
			}
		
			if (!consumptionEl.value)
			{
				consumptionEl.style.borderColor = 'red';
				consumptionEl.style.borderWidth = '1px';
				consumptionLab.classList.remove('hidden');
				consumptionLab.textContent = "Can't be empty";
				consumptionLab.style.color = 'red';
				valid = false;
			}
		
			if (!doorsNumberEl.value)
			{
				doorsNumberEl.style.borderColor = 'red';
				doorsNumberEl.style.borderWidth = '1px';
				doorsNumberLab.classList.remove('hidden');
				doorsNumberLab.textContent = "Can't be empty";
				doorsNumberLab.style.color = 'red';
				valid = false;
			}
		
			if (!passengerNumberEl.value)
			{
				passengerNumberEl.style.borderColor = 'red';
				passengerNumberEl.style.borderWidth = '1px';
				passengerNumberLab.classList.remove('hidden');
				passengerNumberLab.textContent = "Can't be empty";
				passengerNumberLab.style.color = 'red';
				valid = false;
			}
			
			if (!vehicleTypeEl.value)
			{
				vehicleTypeEl.style.borderColor = 'red';
				vehicleTypeEl.style.borderWidth = '1px';
				vehicleTypeLab.classList.remove('hidden');
				vehicleTypeLab.textContent = "Can't be empty";
				vehicleTypeLab.style.color = 'red';
				valid = false;
			}
			
			if (!gearshiftTypeEl.value)
			{
				gearshiftTypeEl.style.borderColor = 'red';
				gearshiftTypeEl.style.borderWidth = '1px';
				gearshiftTypeLab.classList.remove('hidden');
				gearshiftTypeLab.textContent = "Can't be empty";
				gearshiftTypeLab.style.color = 'red';
				valid = false;
			}
			
			if (!fuelTypeEl.value)
			{
				fuelTypeEl.style.borderColor = 'red';
				fuelTypeEl.style.borderWidth = '1px';
				fuelTypeLab.classList.remove('hidden');
				fuelTypeLab.textContent = "Can't be empty";
				fuelTypeLab.style.color = 'red';
				valid = false;
			}
			
			
			if(valid){
				
				this.newVehicle.store = this.manager.rentACarStore
				axios
					.post('rest/vehicles/save', this.newVehicle)
					.then(response =>{
						this.createVehicleOpened = false;
						axios
							.put('rest/vehicles/' + this.newVehicle.store.id)
							.then(response => {
								this.$router.go();
							})
					})
			}
		},
		deleteVehicle : function(vehicle){
			axios
				.delete('rest/vehicles/' + vehicle.id)
				.then( response =>{
					axios
		      			.post('rest/vehicles/', this.manager.rentACarStore.offeredVehicles)
		      			.then(response => {
							  this.offeredVehicles = response.data
							  this.cancelAddingVehicle()
					  	})
				})
			
		},
		updateVehicleButtonPress : function(vehicle){
			this.prikaziCustomere = false;
			this.prikaziVozila = false;
			this.prikaziNarudzbine = false;
			this.updateVehicleOpened = true;
			
			this.updatedBrand = vehicle.brand
			this.updatedModel = vehicle.model
			this.updatedPrice = vehicle.price
			this.updatedVehicleType = vehicle.vehicleType
			this.updatedGearshiftType = vehicle.gearshiftType 
			this.updatedFuelType = vehicle.fuelType
			this.updatedConsumption = vehicle.consumption
			this.updatedDoorsNumber = vehicle.doorsNumber
			this.updatedPassengersNumber = vehicle.passengersNumber
			this.updatedDescription = vehicle.description
			this.updatedImageUrl = vehicle.imageUrl
			
			this.updatedVehicle.id = vehicle.id
			this.updatedVehicle.store = vehicle.store
			//this.updateVehicle(vehicle)
		},
		updateVehicle : function(){
			this.updatedVehicle.brand = this.updatedBrand
			this.updatedVehicle.model = this.updatedModel
			this.updatedVehicle.price = this.updatedPrice
			this.updatedVehicle.vehicleType = this.updatedVehicleType
			this.updatedVehicle.gearshiftType = this.updatedGearshiftType
			this.updatedVehicle.fuelType = this.updatedFuelType
			this.updatedVehicle.consumption = this.updatedConsumption
			this.updatedVehicle.doorsNumber = this.updatedDoorsNumber
			this.updatedVehicle.passengersNumber = this.updatedPassengersNumber
			this.updatedVehicle.description = this.updatedDescription
			this.updatedVehicle.imageUrl = this.updatedImageUrl
			
			
			var valid = true;
			var brandEl = document.getElementsByName('Brand')[0];
			var modelEl = document.getElementsByName('Model')[0];
			var priceEl = document.getElementsByName('Price')[0];
			var imageUrlEl = document.getElementsByName('ImageUrl')[0];
			var consumptionEl = document.getElementsByName('Consumption')[0];
			var doorsNumberEl = document.getElementsByName('DoorsNumber')[0];
			var passengerNumberEl = document.getElementsByName('PassengerNumber')[0];
			var vehicleTypeEl = document.getElementsByName('VehicleType')[0];
			var gearshiftTypeEl = document.getElementsByName('GearshiftType')[0];
			var fuelTypeEl = document.getElementsByName('FuelType')[0];
			var descriptionEl = document.getElementsByName('Description')[0];
		
			var brandLab = document.getElementsByName('BrandLabel')[0];
			var modelLab = document.getElementsByName('ModelLabel')[0];
			var priceLab = document.getElementsByName('PriceLabel')[0];
			var imageUrlLab = document.getElementsByName('ImageUrlLabel')[0];
			var consumptionLab = document.getElementsByName('ConsumptionLabel')[0];
			var doorsNumberLab = document.getElementsByName('DoorsNumberLabel')[0];
			var passengerNumberLab = document.getElementsByName('PassengerNumberLabel')[0];
			var vehicleTypeLab = document.getElementsByName('VehicleTypeLabel')[0];
			var gearshiftTypeLab = document.getElementsByName('GearshiftTypeLabel')[0];
			var fuelTypeLab = document.getElementsByName('FuelTypeLabel')[0];
			var descriptionLab = document.getElementsByName('DescriptionLabel')[0];
			
			brandLab.classList.add('hidden');
		    modelLab.classList.add('hidden');
		    priceLab.classList.add('hidden');
		    imageUrlLab.classList.add('hidden');
		    consumptionLab.classList.add('hidden');
		    doorsNumberLab.classList.add('hidden');
		    passengerNumberLab.classList.add('hidden');
		    vehicleTypeLab.classList.add('hidden');
		    gearshiftTypeLab.classList.add('hidden');
		    fuelTypeLab.classList.add('hidden');
		    descriptionLab.classList.add('hidden');
			
			brandEl.style.borderColor = '';
		    modelEl.style.borderColor = '';
		    priceEl.style.borderColor = '';
		    imageUrlEl.style.borderColor = '';
		    consumptionEl.style.borderColor = '';
		    doorsNumberEl.style.borderColor = '';
		    passengerNumberEl.style.borderColor = '';
		    vehicleTypeEl.style.borderColor = '';
		    gearshiftTypeEl.style.borderColor = '';
		    fuelTypeEl.style.borderColor = '';
		    descriptionEl.style.borderColor = '';
		    
		    
		
			if (!brandEl.value)
			{
				brandEl.style.borderColor = 'red';
				brandEl.style.borderWidth = '1px';
				brandLab.classList.remove('hidden');
				brandLab.textContent = "Can't be empty";
				brandLab.style.color = 'red';
				valid = false;
			}
		
			if (!modelEl.value)
			{
				modelEl.style.borderColor = 'red';
				modelEl.style.borderWidth = '1px';
				modelLab.classList.remove('hidden');
				modelLab.textContent = "Can't be empty";
				modelLab.style.color = 'red';
				valid = false;
			}
		
			if (!priceEl.value)
			{
				priceEl.style.borderColor = 'red';
				priceEl.style.borderWidth = '1px';
				priceLab.classList.remove('hidden');
				priceLab.textContent = "Can't be empty";
				priceLab.style.color = 'red';
				valid = false;
			}
		
			if (!imageUrlEl.value)
			{
				imageUrlEl.style.borderColor = 'red';
				imageUrlEl.style.borderWidth = '1px';
				imageUrlLab.classList.remove('hidden');
				imageUrlLab.textContent = "Can't be empty";
				imageUrlLab.style.color = 'red';
				valid = false;
			}
		
			if (!consumptionEl.value)
			{
				consumptionEl.style.borderColor = 'red';
				consumptionEl.style.borderWidth = '1px';
				consumptionLab.classList.remove('hidden');
				consumptionLab.textContent = "Can't be empty";
				consumptionLab.style.color = 'red';
				valid = false;
			}
		
			if (!doorsNumberEl.value)
			{
				doorsNumberEl.style.borderColor = 'red';
				doorsNumberEl.style.borderWidth = '1px';
				doorsNumberLab.classList.remove('hidden');
				doorsNumberLab.textContent = "Can't be empty";
				doorsNumberLab.style.color = 'red';
				valid = false;
			}
		
			if (!passengerNumberEl.value)
			{
				passengerNumberEl.style.borderColor = 'red';
				passengerNumberEl.style.borderWidth = '1px';
				passengerNumberLab.classList.remove('hidden');
				passengerNumberLab.textContent = "Can't be empty";
				passengerNumberLab.style.color = 'red';
				valid = false;
			}
			
			if (!vehicleTypeEl.value)
			{
				vehicleTypeEl.style.borderColor = 'red';
				vehicleTypeEl.style.borderWidth = '1px';
				vehicleTypeLab.classList.remove('hidden');
				vehicleTypeLab.textContent = "Can't be empty";
				vehicleTypeLab.style.color = 'red';
				valid = false;
			}
			
			if (!gearshiftTypeEl.value)
			{
				gearshiftTypeEl.style.borderColor = 'red';
				gearshiftTypeEl.style.borderWidth = '1px';
				gearshiftTypeLab.classList.remove('hidden');
				gearshiftTypeLab.textContent = "Can't be empty";
				gearshiftTypeLab.style.color = 'red';
				valid = false;
			}
			
			if (!fuelTypeEl.value)
			{
				fuelTypeEl.style.borderColor = 'red';
				fuelTypeEl.style.borderWidth = '1px';
				fuelTypeLab.classList.remove('hidden');
				fuelTypeLab.textContent = "Can't be empty";
				fuelTypeLab.style.color = 'red';
				valid = false;
			}
			
			if(valid){				
				axios
					.put('rest/vehicles/izmeni/', this.updatedVehicle)
					.then( response=>{
						this.cancelUpdatingVehicle()
					})
			}

		},
		cancelUpdatingVehicle : function(){
			this.updateVehicleOpened = false;
		}
    }
});