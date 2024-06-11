package beans;


public class Manager extends User {
	private RentACarStore rentACarStore;

    public Manager() {
    }

    public Manager(int id, String username, String password, String firstName, String lastName, String gender, String dateOfBirth, Role role, RentACarStore rentACarStore) {
        super(id, username, password, firstName, lastName, gender, dateOfBirth);
        this.rentACarStore = rentACarStore;
        this.setRole(role);
    }

    public RentACarStore getRentACarStore() {
        return rentACarStore;
    }

    public void setRentACarStore(RentACarStore rentACarStore) {
        this.rentACarStore = rentACarStore;
    }
}
