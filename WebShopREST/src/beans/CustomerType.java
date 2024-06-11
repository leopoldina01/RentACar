package beans;

public class CustomerType {
	private int id;
    private UserType type;
    private int discount;
    private int requiredScore;

    public CustomerType() {
    }

    public CustomerType(int id, UserType type, int discount, int requiredScore) {
        this.id = id;
        this.type = type;
        this.discount = discount;
        this.requiredScore = requiredScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getRequiredScore() {
        return requiredScore;
    }

    public void setRequiredScore(int requiredScore) {
        this.requiredScore = requiredScore;
    }
}
