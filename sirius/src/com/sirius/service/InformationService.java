package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Information;
import com.sirius.entity.InformationImg;
import com.sirius.entity.query.InformationQuery;
import com.sirius.image.ImgUtils;
import com.sirius.mybatis.mapper.InformationImgMapper;
import com.sirius.mybatis.mapper.InformationMapper;
import com.sirius.po.BaseInfo;

@Service
public class InformationService {

	@Resource
	private InformationMapper informationMapper;

	@Resource
	private InformationImgMapper informationImgMapper;

	public void add(InformationQuery information) {
		if (information.getId() == null) {
			informationMapper.insert(information);
		} else {
			Information i = informationMapper.getById(information.getId());
			ImgUtils.deleteByUrl(i.getContent());
			informationMapper.update(information);
			informationImgMapper.clearByInformation(information.getId());
		}
		for (String img : information.getImgs()) {
			InformationImg informationImg = new InformationImg();
			informationImg.setUrl(img);
			informationImg.setInformationId(information.getId());
			informationImgMapper.insert(informationImg);
		}

	}

	public List<Information> informationList(long markId) {
		InformationQuery information = new InformationQuery();
		information.setSize(BaseInfo.pageSize);
		if (markId == 0) {
		} else {
			information.setMarkId(markId);
		}
		return informationMapper.informationList(information);
	}

	public List<Information> informationList(long markId, int type) {
		InformationQuery information = new InformationQuery();
		information.setSize(BaseInfo.pageSize);
		information.setType(type);
		if (markId == 0) {
		} else {
			information.setMarkId(markId);
		}
		return informationMapper.informationList(information);
	}

	public void read(long informationId) {
		informationMapper.read(informationId);

	}

	public Information getById(long informationId) {
		return informationMapper.getById(informationId);
	}

}
