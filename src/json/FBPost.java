package json;

public class FBPost {

	private String message;
	private int likeCount;
	
	public FBPost() {
		this.message = "";
		this.likeCount = -1;
	}

	public FBPost(String message) {
		this.message = message;
	}
	
	public FBPost(String message, int likes) {
		this.message = message;
		this.likeCount = likes;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
}
