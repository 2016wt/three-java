package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Message;
import com.sirius.mybatis.mapper.MessageMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.util.StringUtil;

@Service
public class MessageService {

	@Resource
	private MessageMapper messageMapper;
	
	/**
	 * 最新消息列表
	 * @param message
	 * @return
	 */
	public BTEntitiy messageList(Message message){
		if(StringUtil.isNullOrEmpty(message.getTitle())){
			message.setTitle(null);
		}
		if(StringUtil.isNullOrEmpty(message.getContent())){
			message.setContent(null);
		}
		List<Message> rows = messageMapper.messageList(message);
		int total = messageMapper.messageListContent(message);
		
		return new BTEntitiy(total, rows);
	}
	
	/***
	 * 消息详情
	 */
	public Message messageContent(long id){
		Message message = messageMapper.messageContent(id);
		return message;
	}
}
