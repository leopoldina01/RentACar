Vue.component("pocetna", {
	data: function () {
	    return {
			stores: [],
			address: '',
			rating: 0,
			storeName: '',
			vehicleType: 'SELECTCAR',
			selectedSort: 'unsorted',
			selectedGearshiftType: 'SELECTEDGEARSHIFT',
			selectedFuelType: 'SELECTEDFUEL',
			checked: 'false',
			loggedUser: {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			ulogovan: false,
			izlogovan: true,
			ulogovanCustomer: false
	    }
	},
	    template: `
	<div>
        <header>
            <div class="logo">Rent A Car</div>
            <div class="nav-buttons">
            	<button v-if="ulogovanCustomer" v-on:click = "CreateOrders"><i class="fas fa-plus-circle"></i> Create order</button>
                <button v-if="izlogovan" name="signUp" v-on:click="goToSignUp"><i class="fas fa-user-plus"></i> Sign up</button>
                <button v-if="izlogovan" name="logIn" v-on:click="goToLogIn"><i class="fas fa-sign-in-alt"></i> Log In</button>
                <button v-if="ulogovan" v-on:click="goToMyProfile"><i class="fas fa-user"></i> My Account</button>
                <button v-if="ulogovan" v-on:click="logOut"><i class="fas fa-sign-out-alt"></i> Log out</button>
            </div>
        </header>
        <div class="search-container">
            <input v-model="address" type="text" placeholder="Enter Location:">
            <input v-model="rating" type="number" placeholder="Enter Rating:">
            <input v-model="storeName" type="text" placeholder="Enter Store Name:">
            <select v-model="vehicleType">
                <option value="SELECTCAR" selected>Select Car Type</option>
                <option value="CAR">Car</option>
                <option value="VAN">Van</option>
                <option value="TRUCK">Truck</option>
                <option value="MOBILEHOME">Mobilehome</option>
            </select>
            <button v-on:click="search" ><i class="fas fa-search"></i> Search</button>
        </div>
        <div class="table-container">
            <table class="datagrid">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Location</th>
                        <th>Logo</th>
                        <th>Rating</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="s in stores" :key="s.id" v-on:click="openThisStore(s.id)">
                        <td>{{s.name}}</td>
                        <td>{{s.location.address}}</td>
                        <td><img :src="s.logoUrl" width="150px" height="100px"></td>
                        <td>{{s.rating}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="filter-container">
            <select v-model="selectedSort">
                <option value="unsorted" selected>Sort by...</option>
                <option value="nameAsc">Sort by name &#x2191;</option>
                <option value="nameDesc">Sort by name &#x2193;</option>
                <option value="locationAsc">Sort by location &#x2191;</option>
                <option value="locationDesc">Sort by location &#x2193;</option>
                <option value="ratingAsc">Sort by rating &#x2191;</option>
                <option value="ratingDesc">Sort by rating &#x2193;</option>
            </select>
            <br>
            <br>
            <select v-model="selectedGearshiftType">
                <option value="SELECTEDGEARSHIFT">Select Gearshift Type...</option>
                <option value="MANUAL">Manual</option>
                <option value="AUTOMATIC">Automatic</option>
            </select>
            <br>
            <br>
            <select v-model="selectedFuelType">
                <option value="SELECTEDFUEL">Select Fuel Type...</option>
                <option value="DIESEL">Diesel</option>
                <option value="PETROL">Petrol</option>
                <option value="HYBRID">Hybrid</option>
                <option value="ELECTRIC">Electric</option>
            </select>
            <br>
            <br>
            <div class="opened-containter">
                <label>Show Opened Stores:</label>
                <input id="statusCheckbox" type="checkbox">
            </div>
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
			.get('rest/stores/dobaviSveStorove')
			.then(response => {
				this.stores = response.data
				axios
					.get('rest/user/currentLoggedUser')
					.then(response =>{
						this.loggedUser = response.data
						if(this.loggedUser.username === undefined){
							this.ulogovan = false;
							this.izlogovan = true;
							this.ulogovanCustomer = false;
						}else{
							this.ulogovan = true;
							this.izlogovan = false;
							if (this.loggedUser.role === "CUSTOMER")
							{
								this.ulogovanCustomer = true;
							} else {
								this.ulogovanCustomer = false;
							}
						}
					})
					
				});
    },
    methods: {
		goToSignUp : function()
		{
			router.push('/signUp');
		},
		
		goToLogIn : function() 
		{
			router.push('/login')
		},
		
		search : function(){
			/*ZA SADA NEK SAMO SKINE FILTER*/
			this.selectedSort = 'unsorted'
			this.selectedGearshiftType =  'SELECTEDGEARSHIFT'
			this.selectedFuelType =  'SELECTEDFUEL'
			this.checked = 'false'
			uncheck()
			 
			if(this.address === ''){
				this.address = null;
			}
			
			if(this.rating === ''){
				this.rating = 0;
			}
			
			if(this.storeName === ''){
				this.storeName = null;
			}
			
			if(this.vehicleType === 'SELECTCAR'){
				this.vehicleType = 'SELECTCAR';
			}
			axios
				.get('rest/stores/search/' + this.address + '/' + this.rating + '/' + this.storeName + '/' + this.vehicleType)
				.then(response =>{
					this.stores = response.data
				})
		},
		
		filter : function(){
			this.address =  ''
			this.rating = 0
			this.storeName = ''
			this.vehicleType = 'SELECTCAR'
			
			if(this.stores != null){				
				if(isChecked()){
					this.checked = 'true';
				}else{
					this.checked = 'false';
				}
				axios
					.get('rest/stores/filter/' + this.selectedSort + '/' + this.selectedGearshiftType + '/' + this.selectedFuelType + '/' + this.checked) 
					.then(response =>{
						this.stores = response.data
					})
			}
			
		},
		
		removeFilters : function(){
			this.selectedSort = 'unsorted'
			this.selectedGearshiftType =  'SELECTEDGEARSHIFT'
			this.selectedFuelType =  'SELECTEDFUEL'
			this.checked = 'false'
			uncheck()
			axios
				.get('rest/stores/filter/' + this.selectedSort + '/' + this.selectedGearshiftType + '/' + this.selectedFuelType + '/' + this.checked) 
				.then(response =>{
					this.stores = response.data
				})
		},
		
		goToMyProfile : function(){
			if (this.loggedUser.role === "CUSTOMER")
			{
				router.push(`/customerProfile`)
			}
			else if (this.loggedUser.role === "ADMINISTRATOR")
			{
				router.push(`/administratorProfile`)
			}
			else if (this.loggedUser.role === "MANAGER")
			{
				router.push(`/managerProfile`)
			}
		},
		
		logOut : function(){
			axios
				.post('rest/user/logout')
				.then(response =>{
					this.$router.go()
				})
		},
		
		openThisStore : function(selectedStoreId){
			router.push(`/selectedStore/${selectedStoreId}`)
		},
		
		CreateOrders : function()
		{
			router.push('customerCreateOrder');
		}
    }
});

function isChecked(){
	const cb = document.getElementById("statusCheckbox");
	return cb.checked;
}

function uncheck(){
	const cb = document.getElementById("statusCheckbox");
	cb.checked = false;
}