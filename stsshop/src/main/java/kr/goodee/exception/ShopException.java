package kr.goodee.exception;

public class ShopException extends RuntimeException {
	private String url;
	public ShopException(String msg,String url) {
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
