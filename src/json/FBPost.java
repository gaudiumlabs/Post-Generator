package json;

import java.util.Date;

public class FBPost {

	private String message;
	private int likeCount;
	private int shareCount;
	private String source;
	private Date date;

	public FBPost() {
		this.message = "";
		this.likeCount = -1;
		this.setShareCount(-1);
		this.setSource("");
		this.setDate(null);
	}

	public FBPost(String message) {
		this.message = message;
		this.likeCount = -1;
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

	public boolean equals(FBPost post) {
		if (this.message == null && post.getMessage() == null) {
			return this.likeCount == post.getLikeCount();
		} else {
			return this.message.equals(post.getMessage())
					&& this.likeCount == post.getLikeCount();
		}
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
