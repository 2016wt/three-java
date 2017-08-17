package com.sirius.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.PayRecord;
import com.sirius.mybatis.mapper.PayRecordMapper;

@Service
public class PayRecordService {

	@Resource
	private PayRecordMapper payRecordMapper;

	public void ctrateRecord(PayRecord payRecord) {
		payRecordMapper.insert(payRecord);
	}

}
