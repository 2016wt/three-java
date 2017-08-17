package com.sirius.interceptor;

import java.io.IOException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;

import com.alibaba.fastjson.JSONObject;

public class MappingJackson2HttpMessageConverter extends org.springframework.http.converter.json.MappingJackson2HttpMessageConverter {

	private final static Logger log = LoggerFactory.getLogger(MappingJackson2HttpMessageConverter.class);

	@Override
	protected void writeInternal(Object object, Type type,HttpOutputMessage outputMessage) throws IOException {
		super.writeInternal(object, type, outputMessage);
		log.debug(JSONObject.toJSONString(object));
	}
}