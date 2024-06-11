Vue.component("selectedStore", {
	data: function () {
	    return {
			storeId: -1,
			tempWorkingStatus: '',
			selectedStore: {id: -1, name: '', offeredVehicles: [], workingTime: '', workingStatus: 'OPENED', location: null, locationId: -1, logoUrl: '', rating: 0},
			vehicles: [],
			address: '',
			longitude: -1,
			latitude: -1,
			kliknutaLongitude: -1,
			kliknutaLatitude: -1,
			showModal: false,
			comments: [],
			user : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: null, role: 'CUSTOMER', isBlocked: ''},
			showButtons: false,
			showCommentStatus: false,
			manager : {id: -1, username: '', password: '', firstName: '', lastName: '', gender: '', dateOfBirth: '', role: 'MANAGER', isBlocked: '', rentACarStore: ''}
	    }
	},
	    template: `
	<div class="selected-store-container">
        <header>
            <div class="logo">Rent A Car</div>
            <div class="nav-buttons">
                <button v-on:click="goToHomepage"><i class="fas fa-home"></i> Home page</button>
            </div>
        </header>
        <h1 class="selected-store-headline">{{selectedStore.name}}</h1>
        <hr class="separator">
        <h2 class="selected-store-slogan">RENT THE BEST CARS</h2>
        <img class="selected-store-logo":src="selectedStore.logoUrl">
        <div class="radno-vreme-i-status">
            <label>Working hours:</label>
            <label>{{selectedStore.workingTime}}</label>
            <br>
            <label>CURRENTLY </label>
            <label> {{tempWorkingStatus}}</label>
            <br>
            <label>RATING: {{selectedStore.rating}}/5</label>
        </div>
        <div class="selected-store-location-container">
        	<div id="map" class="map"></div>
            <label>{{address}}</label>
            <br>
            <label>{{longitude}},</label>
            <label>{{latitude}}</label>
        </div>
        <div class="selected-store-comment-container">
            <button v-on:click="openComments"><i class="fas fa-comment"></i> COMMENTS</button>
        </div>
        <h3 class="vehicles-h3">Vehicles:</h3>
        <div class="selected-store-vehicles-container">
	        <div v-for="v in vehicles" class="show-vehicle-list">
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
	        </div>
    	</div>
    	
    	<div class="modal-overlay2"  v-if="showModal">
	        <div class="modal2">
	        	<button v-on:click="closeCommentsWindow" class="close-modal-button"><i class="fas fa-times"></i></button>
	            <div class="single-comment-container" v-for="c in comments">
	            	<label v-if="showCommentStatus">Status: {{c.status}}</label>
	                <h3>Comment:</h3>
	                <textarea name="Comment" readonly>{{c.content}}</textarea>
	                <br>
	                <label>Rating: {{c.rating}} / 5</label>
	                <div class="comment-buttons-container" v-if="c.status === 'PROCESSING' && showButtons">
	                    <button v-on:click="approveComment(c)" class="approve-comment">Approve</button>
	                    <button v-on:click="rejectComment(c)" class="reject-comment">Reject</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    
    </div>
	    `,
    mounted () {
		this.storeId = this.$route.params.id;
		axios
			.put('rest/stores/setWorkingStatus')
			.then( response =>{
				axios
					.get('rest/stores/' + this.storeId)
					.then(response =>{
						this.selectedStore = response.data
						this.address = this.selectedStore.location.address
						this.longitude = this.selectedStore.location.longitude
						this.latitude = this.selectedStore.location.latitude
						this.tempWorkingStatus = this.selectedStore.workingStatus
						this.geocodeAddress(this.address);
				
						axios
							.get('rest/stores/vehicles/' + this.selectedStore.id)
							.then( response =>{
								this.vehicles = response.data
								axios
									.get('rest/user/currentLoggedUser')
									.then(response =>{
										this.user = response.data;
										axios
											.get('rest/user/managers/banana/' + this.user.id)
											.then(response=>{
												this.manager = response.data
												
											})	
									})
							})	
					})
			})
		 
    },
    methods: {
		goToHomepage : function(){
			router.push('/');
		},
    	
    	geocodeAddress(address) {
		    axios
		      .get('https://nominatim.openstreetmap.org/search', {
			        params: {
			          q: address,
			          format: 'json',
			        },
			      })
		      .then(response => {
		        if (response.data && response.data.length > 0) {
		          const result = response.data[0];
		          const longitude = parseFloat(result.lon);
		          const latitude = parseFloat(result.lat);
		          this.kliknutaLongitude = longitude;
		          this.kliknutaLatitude = latitude;
		          this.showLocationOnMap(longitude, latitude);
		        } else {
		          console.log('Address not found');
		        }
		      })
		      .catch(error => {
		        console.error('Geocoding error:', error);
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
			        zoom: 16, 
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
	    	/*map.on('click', (event) => {
		    	const clickedCoordinate = event.coordinate;
		    	const [clickedLongitude, clickedLatitude] = ol.proj.toLonLat(clickedCoordinate);
		    
		    	// Obrada kliknute lokacije
		    	console.log('Kliknuta lokacija:', clickedLongitude, clickedLatitude);
		    	
		    	this.kliknutaLongitude = clickedLongitude;
		    	this.kliknutaLatitude = clickedLatitude;
		    	
		    	marker.setGeometry(new ol.geom.Point(ol.proj.fromLonLat([clickedLongitude, clickedLatitude])));
		    	
		    	this.geocodeLocation(clickedLongitude, clickedLatitude);
		  	});*/
	  	},
	  	
	  	geocodeLocation(longitude, latitude) {
  			const url = `https://nominatim.openstreetmap.org/reverse?format=json&lon=${longitude}&lat=${latitude}`;
		  	axios
		    	.get(url)
		    	.then((response) => {
		      		if (response.data) {
		        	const address = response.data.display_name;
		        	console.log('Adresa:', address);
		        	// Dalja obrada adrese ili ažuriranje Vue data objekta
		      		} else {
		        		console.log('Adresa nije pronađena');
		      		}
		    	})
		    	.catch((error) => {
		      		console.error('Greška pri geokodiranju:', error);
		    	});
			
		},
		openComments : function(){
			axios
				.get('/manager')
			
			if(this.user.role === "CUSTOMER"){
				this.showButtons = false
			}else if(this.user.role === "MANAGER" ){
				if(this.manager.rentACarStore.id === this.selectedStore.id){
					this.showButtons = true
				}
				this.showCommentStatus = true;
			}else if(this.user.role === "ADMINISTRATOR"){
				this.showButtons = false
				this.showCommentStatus = true;
			}
			
			axios
				.get('rest/comments/' + this.user.role + '/' + this.selectedStore.id)
				.then( response =>{
					this.comments = response.data
					this.showModal = true;
				})
		},
		approveComment : function(comment){
			comment.status = "APPROVED"
			axios
				.put('rest/comments/', comment)
				.then(response =>{
					axios
						.put('rest/stores/updateRating/' + this.selectedStore.id + '/' +  comment.rating)
						.then(response =>{
							this.selectedStore.rating = response.data.rating							
							this.openComments();
						})
				})
		},
		rejectComment : function(comment){
			comment.status = "REJECTED"
			axios
				.put('rest/comments/', comment)
				.then(response =>{
					this.openComments();
				})
		},
		closeCommentsWindow : function(){
			this.showModal = false;
		}
	}
});