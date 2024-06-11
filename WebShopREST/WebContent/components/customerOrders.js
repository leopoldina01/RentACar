Vue.component("customerOrders", {
	data: function () {
	    return {
			username: '',
			password: '',
			id: -1,
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			customer : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: '', allOrders: null, shoppingCart: null, score: '', customerType: ''},
			orderIds : [],
			customerOrders : [],
			rentedVehicles : [],
			selectedOrder: null,
			searchStoreName: '',
			storeName : '',
			selectedSort: 'unsorted',
			priceFrom: '',
			searchPriceFrom: '',
			priceTo: '',
			searchPriceTo: '',
			dateFrom: '',
			dateTo: '',
			showModal: false,
			storeComment: '',
			storeRating: '',
			comment: {id: -1, userId: -1, storeId: -1, content: '', rating: 0, status: 'PROCESSING'},
			showRateButton: false,
			comments: []
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
    	
    	<div class="modal-overlay"  v-if="showModal">
	    	<div class="modal">
	    		<h3>Rate order</h3>
	    		<textarea name="Comment" v-model="storeComment" placeholder="Enter comment"></textarea>
	    		<br>
	    		<label class="hidden" name="CommentLabel"></label>
	    		<br>
	    		<input type="number" name="Rating" placeholder="Enter Rating" v-model="storeRating"/>
	    		<br>
	    		<label class="hidden" name="RatingLabel"></label>
	    		<div class="modal-buttons">
	    			<button @click="rateStore" class="cancel-order-button">Submit</button>
	    			<button @click="cancelRating" class="cancel-order-button">Cancel</button>
	    		</div>
	    	</div>
	    </div>
    	
    	<div class="customerOrders">
    	
            <div class="sort-customer-orders">
                <select class="sort-orders-select" v-model="selectedSort" v-on:change="searchOrders">
                    <option value="unsorted">Sort by...</option>
                    <option value="priceAscending">Sort by price &#x2191;</option>
                    <option value="priceDescending">Sort by price &#x2193;</option>
                    <option value="dateAscending">Sort by date &#x2191;</option>
                    <option value="dateDescending">Sort by date &#x2193;</option>
                    <option value="storeNameAscending">Sort by store &#x2191;</option>
                    <option value="storeNameDescending">Sort by store &#x2193;</option>
                </select>
            </div>
            
            <div class="search-input-container-orders">
                <input type="text" placeholder="Enter store name" v-model="storeName"/>
                <input type="number" placeholder="Price from" v-model="priceFrom"/>
                <input type="number" placeholder="Price to" v-model="priceTo"/>
                <label class="hidden" name="priceLabel"></label>
                <label>From: </label>
                <input type="date" v-model="dateFrom">
                <label>To: </label>
                <input type="date" v-model="dateTo"/>
                <label class="hidden" name="dateLabel"></label>
                <button v-on:click="searchOrders"><i class="fas fa-search"></i> Search</button>
            </div>
            
            <h1 >My orders:</h1>
            <br>
            <div class="list-box-narudzbine">
                <div class="card " v-for="o in customerOrders">
                    
                    <div class="orderItem" @click="openDetails(o)">
                        <div>
                            <h2>Order: {{o.idString}}</h2>
                            <p>Order status: {{o.status}}</p>
                            <p>Total price: {{o.price}} din</p>
                            <p>Rented on: {{o.rentalDate}}</p>
                            <p v-if="o.status === 'CANCELED'">Canceled on: {{o.cancelDate}}</p>
                            <p>Pick up date: {{o.pickUpDate}} Return date: {{o.returnDate}}</p>
                        </div>
                        
                        <div class="ordered-in">
                            <label class="ordered-in-label">Ordered in: {{o.rentACarStore.name}}</label>
                            <br>
                            <img :src="o.rentACarStore.logoUrl">
                        </div>
                        <div v-if="o.status === 'PROCESSING'">
                        	<button class="cancel-order-button" v-on:click="cancelOrder(o)"><i class="fas fa-times-circle"></i> Cancel</button>
                        </div>
                        <div v-if="o.comment !== ''">
                        	<label>Why is rejected: {{o.comment}}</label>
                        </div>
                        <button v-on:click="openRatingWindow(o)" v-if="o.status === 'RETURNED'" class="rate-store-button">Rate</button>

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
						//this.orderIds = response.data.allOrders;
						
						axios
							.post('rest/orders/', this.customer.allOrders)
							.then(response => (this.customerOrders = response.data))
						})
			})
    },
    methods: {
		openDetails(order) {
      		this.selectedOrder = order;
      		
      		axios
      			.post('rest/vehicles/', order.rentedVehicles)
      			.then(response => (this.rentedVehicles = response.data))
    	},
    	
    	goToMyAccount : function(){
			router.push(`/customerProfile`)
		},
		
		searchOrders : function()
		{
			var valid = true;
			
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
			
			if (this.storeName === '')
			{
				this.searchStoreName = null;
			}
			else
			{
				this.searchStoreName = this.storeName;
			}
			
			var dateLab = document.getElementsByName('dateLabel')[0];
			var priceLab = document.getElementsByName('priceLabel')[0];
			dateLab.classList.add('hidden');
			priceLab.classList.add('hidden');
			
			if (this.dateFrom !== "0000-00-00" && this.dateTo !== "0000-00-00")
			{
				if (new Date(this.dateFrom) > new Date(this.dateTo))
				{
					dateLab.classList.remove('hidden');
					dateLab.textContent = "Date from can't be after date to!";
					dateLab.style.color = 'red';
					valid = false;
				}
			}
			
			if (this.priceFrom !== '' && this.priceTo !== '')
			{
				if (this.priceFrom > this.priceTo)
				{
					priceLab.classList.remove('hidden');
					priceLab.textContent = "Price from can't be less than price to!";
					priceLab.style.color = 'red';
					valid = false;
				}
			}
			
			if (valid)
			{
				axios
					.get('rest/orders/searchCustomerOrders/' + this.searchPriceFrom + '/' + this.searchPriceTo + '/' + this.dateFrom + '/' + this.dateTo + '/' + this.customer.id + '/' + this.searchStoreName + '/' + this.selectedSort)
					.then(response => {this.customerOrders = response.data;})
			}
			
		},
		
		cancelOrder(order)
		{
			axios
				.post('rest/orders/cancelOrder', order)
				.then(response => {this.customerOrders = response.data;})
		},
		openRatingWindow : function(order){
			this.comment.userId = this.user.id
			this.comment.storeId = order.rentACarStore.id
			
			this.showModal = true;
		},
		rateStore : function(){
			this.comment.content = this.storeComment
			this.comment.rating = this.storeRating
			
			var valid = true;
			var commentEl = document.getElementsByName('Comment')[0];
			var ratingEl = document.getElementsByName('Rating')[0];
	
			var commentLab = document.getElementsByName('CommentLabel')[0];
			var ratingLab = document.getElementsByName('RatingLabel')[0];

			commentLab.classList.add('hidden');
		    ratingLab.classList.add('hidden');
			
			commentEl.style.borderColor = '';
		    ratingEl.style.borderColor = '';
		    
		
			if (!commentEl.value)
			{
				commentEl.style.borderColor = 'red';
				commentEl.style.borderWidth = '1px';
				commentLab.classList.remove('hidden');
				commentLab.textContent = "Can't be empty";
				commentLab.style.color = 'red';
				valid = false;
			}
		
			if (!ratingEl.value)
			{
				ratingEl.style.borderColor = 'red';
				ratingEl.style.borderWidth = '1px';
				ratingLab.classList.remove('hidden');
				ratingLab.textContent = "Can't be empty";
				ratingLab.style.color = 'red';
				valid = false;
			}
			
			const pattern = /^[1-5]$/;
			
			if(!pattern.test(ratingEl.value) && ratingEl.value != "")
			{
				ratingEl.style.borderColor = 'red';
				ratingEl.style.borderWidth = '1px';
				ratingLab.classList.remove('hidden');
				ratingLab.textContent = "You can rate with 1-5.";
				ratingLab.style.color = 'red';
				valid = false;
			}
		
			if(valid){				
			axios
				.post('rest/comments/save', this.comment)
				.then( response =>{
					this.$router.go()
				})
			}
		},
		cancelRating : function(){
			this.comment.userId = -1;
			this.comment.storeId = -1;
			this.storeComment = ''
			this.storeRating = ''
			
			this.showModal = false;
		}
    }
});