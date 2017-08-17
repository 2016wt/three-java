package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.AndroidApk;
import com.sirius.mybatis.mapper.AndroidApkMapper;

@Service
public class AppService {

	@Resource
	private AndroidApkMapper androidApkMapper;
	
	
	public AndroidApk newApk(int version,int type) {
		return androidApkMapper.newApk(version, type);
	}

	public List<AndroidApk> apkList() {
		return androidApkMapper.list();
	}



	public void insert(AndroidApk androidApk) {
		androidApkMapper.insert(androidApk);		
	}

}
