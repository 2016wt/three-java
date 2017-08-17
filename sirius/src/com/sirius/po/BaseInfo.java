package com.sirius.po;

import java.io.Serializable;

public class BaseInfo implements Serializable {
	private static final long serialVersionUID = -2094835355816769290L;

	public static final int pageSize = 20;

	private static final String homeImg = "https://oh2sweulg.qnssl.com/faba8802-085b-4cf2-b712-ad4a65afbe0b20170303151223.jpg";

	private static final String avatarImg = "https://oh2sweulg.qnssl.com/e933ee5d-91b7-439a-806e-c45d2102a6a320170311135322.jpg";

	public int getPageSize() {
		return pageSize;
	}

	public String getHomeImg() {
		return homeImg;
	}

	public String getAvatarImg() {
		return avatarImg;
	}

}
