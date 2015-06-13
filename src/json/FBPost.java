package json;

import java.util.Date;

public class FBPost {

	private Long id;
	private String message;
	private int likeCount;
	private int shareCount;
	private String source;

	public FBPost() {
		this.message = "";
		this.likeCount = -1;
		this.setShareCount(-1);
		this.setSource("");
	}

	public FBPost(String message) {
		this.message = message;
		this.likeCount = -1;
		this.setShareCount(-1);
		this.setSource("");
	}

	public FBPost(String message, int likes) {
		this.message = message;
		this.likeCount = likes;
		this.setShareCount(-1);
		this.setSource("");
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
