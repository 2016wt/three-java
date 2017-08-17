package com.sirius.submail;


public abstract class SenderWapper {

	protected DataStore requestData = new DataStore();

	public abstract ISender getSender();
}
