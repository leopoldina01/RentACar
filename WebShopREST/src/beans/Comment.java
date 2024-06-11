package beans;

public class Comment {
    private int id;
    private int userId;
    private int storeId;
    private String content;
    private int rating;
    CommentStatus status;

    public Comment() {
    }

	public Comment(int id, int userId, int storeId, String content, int rating, CommentStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.storeId = storeId;
		this.content = content;
		this.rating = rating;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	
}
