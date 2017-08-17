package com.sirius.po;

import java.io.Serializable;

public class JsonEntity implements Serializable {
	private static final long serialVersionUID = -7982820491494624333L;
	// code：
	// 0 成功
	// -1通用失败
	// -2登录验证失败

	private int code;
	private String msg;
	private Object data;

	private JsonEntity(Object data) {
		setData(data);
	}

	private JsonEntity(int code) {
		setCode(code);
	}

	private JsonEntity(String msg) {
		setMsg(msg);
	}
	
	private JsonEntity(String msg, Object data) {
		setMsg(msg);
		setData(data);
	}

	private JsonEntity(int code, String msg) {
		setCode(code);
		setMsg(msg);
	}

	public JsonEntity() {
	}

	public static JsonEntity getInstance(Object data) {
		return new JsonEntity(data);
	}

	public static JsonEntity getInstance(int code) {
		return new JsonEntity(code);
	}

	public static JsonEntity getInstance() {
		return new JsonEntity();
	}

	public static JsonEntity getError() {
		return JsonEntity.getInstance(-1);
	}

	public static JsonEntity getError(String message) {
		return JsonEntity.getInstance(-1, message);
	}

	public static JsonEntity getInstance(String message) {
		return new JsonEntity(message);
	}

	public static JsonEntity getInstance(int code, String message) {
		return new JsonEntity(code, message);
	}

	public static JsonEntity getInstance(String message, Object data) {
		return new JsonEntity(message, data);
	}

	public static JsonEntity getError(int code, String message) {
		return JsonEntity.getInstance(code > -1 ? -1 : code, message);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
