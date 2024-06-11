Vue.component("shoppingCartOverview", {
	data: function () {
	    return {
			username: '',
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: null, customerType: ''},
			vehicles : [],
			vehiclesIds: [],
			itemsInCart : '',
			totalPrice: ''
	    }
	},
	    template: `
	    <div>
	    	<header>
				<div class="logo">Rent A Car</div>
				<div class="nav-buttons">
					<button v-on:click="goToCreateOrder"><i class="fas fa-plus-circle"></i> Add cars</button>
	                <button v-on:click="goToHomepage"><i class="fas fa-home"></i> Home page</button>
	            </div>
	    	</header>
	    	
	    	<div>
	    		<div class="shopping-cart-details">
	    			<label>Items in cart: {{itemsInCart}}</label>
	    			<label>Total price: {{totalPrice}}</label>
	    			<label>Discount: {{customer.customerType.discount}}%</label>
	    			<button v-on:click="makeOrder"><i class="fas fa-check-circle"></i> Order</button>
	    		</div>
	    		<div class="order-error-label-container">
	    			<label class="hidden" name="orderLabel"></label>
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
				<button class="add-button" @click="RemoveVehicleFromCart(v.id, v.price)"><i class="fas fa-minus"></i></button>
				
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
						this.vehiclesIds = this.customer.shoppingCart.vehicles;
						//this.orderIds = response.data.allOrders;
						
						axios
							.post('rest/vehicles/', this.customer.shoppingCart.vehicles)
							.then(response => {
								this.vehicles = response.data; 
								this.itemsInCart = this.vehicles.length;
								this.totalPrice = this.customer.shoppingCart.price;})
						})
			})
    },
    methods: {
		goToHomepage : function()
		{
			router.push('/');
		},
		
		goToCreateOrder : function()
		{
			router.push('/customerCreateOrder');
		},
		
		RemoveVehicleFromCart(vehicleId, price)
		{
			const index = this.vehiclesIds.findIndex(v => v === vehicleId);
			
			if (index !== -1)
			{
				if (this.vehicles.length === 1)
				{
					this.vehicles.pop();
					this.itemsInCart = this.vehicles.length;
				}
				else {
					this.vehicles.splice(index, 1);
					this.itemsInCart = this.vehicles.length;
				}
				
				axios
					.get('rest/users/customers/removeFromCart/' + vehicleId + '/' + this.customer.id + '/' + price)
					.then(response => {
						this.customer = response.data;
						this.totalPrice = this.customer.shoppingCart.price;
						this.vehiclesIds = this.customer.shoppingCart.vehicles;
					})
			}
		},
		
		makeOrder : function()
		{
			var orderLab = document.getElementsByName('orderLabel')[0];
			orderLab.classList.add('hidden');
			
			if(this.vehicles.length === 0)
			{
				
				orderLab.classList.remove('hidden');
				orderLab.textContent = "No vehicles in the cart!";
				orderLab.style.color = 'red';
			}
			else {
				axios
					.post('rest/orders/createFinalOrder', this.customer)
					.then(response => {
						this.customer = response.data;
						router.push('/customerCreateOrder')
					})
			}
		}
    }
});