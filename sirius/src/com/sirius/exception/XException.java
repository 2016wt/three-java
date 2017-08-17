package com.sirius.exception;


/**
 * 值得投自定义异常
 * 
 * @author dohko
 * 
 */

public class XException extends RuntimeException {
	private static final long serialVersionUID = -8046531585845860308L;

	private String message;
	private int code = -1;

	public XException() {

	}

	public XException(String message) {
		this.setMessage(message);
	}

	public XException(int code, String message) {
		setCode(code);
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
