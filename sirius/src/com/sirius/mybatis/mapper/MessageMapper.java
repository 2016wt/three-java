package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sirius.entity.Message;

public interface MessageMapper {

	//最新消息列表
	List<Message> messageList(Message message);
	int messageListContent(Message message);
	
	//根据标题获得消息的内容
	@Select("select * from message where id=#{id}")
	Message messageContent(long id);
}
