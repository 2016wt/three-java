package com.sirius.image;

import java.io.Serializable;

public class ImgKey implements Serializable{
	private static final long serialVersionUID = 1L;
	private String hash;
	private String key;
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "ImgKey [hash=" + hash + ", key=" + key + "]";
	}
	
}
