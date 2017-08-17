package com.sirius.image;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.sirius.exception.XException;

public class ImgUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(ImgUtils.class);

	public static final String IMGPATH = "https://oh2sweulg.qnssl.com/";
	// 设置好账号的ACCESS_KEY和SECRET_KEY
	final static String ACCESS_KEY = "FeHivbsi8zFWlqGShEVW89nayvjGjBHfm6nm--uz";
	final static String SECRET_KEY = "GXAYiSup9BIXStnNduIqjd7ix01qrkxlh-MtG1T4";

	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	// 要上传的空间
	final static String bucketname = "dohko";
	// 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

	public static String uploadImg(CommonsMultipartFile file) throws XException {
		if (file.getSize() > 1024 * 1024) {
			throw new XException("图片不能大于1M");
		}

		// 获取上传文件的名字
		String filename = FilenameUtils.getName(file.getOriginalFilename());
		// .图片格式
		filename = filename.substring(filename.lastIndexOf("."));
		// 上传到七牛后保存的文件名
		String key = UUID.randomUUID().toString() + sdf.format(new Date())
				+ filename;
		byte[] bs = file.getBytes();
		// 创建上传对象
		UploadManager uploadManager = new UploadManager();

		try {
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);

			// 打印返回的信息
			return imgKey.getKey();
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时的异常的信息
			logger.error(r.toString());
			try {
				// 响应的文本信息
				return r.bodyString();
			} catch (QiniuException e1) {
				// ignore
			}
			throw new XException("上传图片失败");
		}
	}

	public static String uploadImgFull(byte[] bs, String filename)
			throws XException {
		try {
			// 获取上传文件的名字
			// filename = filename + ".png";
			// 上传到七牛后保存的文件名
			String key = filename;
			// 创建上传对象
			UploadManager uploadManager = new UploadManager();
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);
			// logger.debug(IMGPATH + imgKey.getKey());
			return IMGPATH + imgKey.getKey();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XException("上传图片失败");
		}
	}

	public static String uploadFull(byte[] bs, String filename)
			throws XException {
		try {
			logger.debug(bs.length + "");
			// 获取上传文件的名字
			// filename = filename + ".png";
			// 上传到七牛后保存的文件名
			String key = filename;
			// 创建上传对象
			UploadManager uploadManager = new UploadManager();
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);
			logger.debug(IMGPATH + imgKey.getKey());
			return IMGPATH + imgKey.getKey();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XException("上传图片失败");
		}
	}

	/**
	 * 全路径
	 * 
	 * @param file
	 * @return
	 * @throws XException
	 */
	public static String uploadImgFull(CommonsMultipartFile file)
			throws XException {
		if (file.getSize() > 1024 * 1024) {
			throw new XException("图片不能大于1M");
		}
		// 获取上传文件的名字
		String filename = FilenameUtils.getName(file.getOriginalFilename());
		// .图片格式
		filename = filename.substring(filename.lastIndexOf("."));
		// 上传到七牛后保存的文件名
		String key = UUID.randomUUID().toString() + sdf.format(new Date())
				+ filename;
		byte[] bs = file.getBytes();
		// 创建上传对象
		UploadManager uploadManager = new UploadManager();

		try {
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);

			// 打印返回的信息
			return IMGPATH + imgKey.getKey();
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时的异常的信息
			logger.error(r.toString());
			try {
				// 响应的文本信息
				return r.bodyString();
			} catch (QiniuException e1) {
				// ignore
			}

			throw new XException("上传图片失败");
		}
	}

	public static String uploadImg(String base64) throws XException {
		try {
			// 获取上传文件的名字
			String filename = ".png";
			// 上传到七牛后保存的文件名
			String key = UUID.randomUUID().toString() + sdf.format(new Date())
					+ filename;
			// 创建上传对象
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bs = decoder.decodeBuffer(base64);

			UploadManager uploadManager = new UploadManager();
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);
			return imgKey.getKey();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XException("上传图片失败");
		}
	}

	public static String upload(String base64, String filename)
			throws XException {
		try {
			// 获取上传文件的名字
			// 上传到七牛后保存的文件名
			String key = UUID.randomUUID().toString() + sdf.format(new Date())
					+ filename;
			// 创建上传对象
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bs = decoder.decodeBuffer(base64);

			UploadManager uploadManager = new UploadManager();
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);
			return imgKey.getKey();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XException("上传失败");
		}
	}

	public static String uploadFull(String base64, String filename)
			throws XException {
		return IMGPATH + upload(base64, filename);
	}

	public static String uploadImgFull(String base64) throws XException {
		try {
			// 获取上传文件的名字
			String filename = ".png";
			// 上传到七牛后保存的文件名
			String key = UUID.randomUUID().toString() + sdf.format(new Date())
					+ filename;
			// 创建上传对象
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bs = decoder.decodeBuffer(base64);
			UploadManager uploadManager = new UploadManager();
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());

			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);
			return IMGPATH + imgKey.getKey();
		} catch (Exception e) {
			e.printStackTrace();
			throw new XException("上传图片失败");
		}
	}

	public static List<String> uploadImgFull(List<String> base64)
			throws XException {
		List<String> list = new ArrayList<>();
		for (String string : base64) {
			list.add(uploadImgFull(string));
		}
		return list;

	}

	// 使用默认策略，只需要设置上传的空间名就可以了
	public static String getUpToken() {
		return auth.uploadToken(bucketname);
	}

	/**
	 * 上传apk
	 * 
	 * @param file
	 * @return 返回文件上传后的地址
	 * @throws XException
	 */
	public static String uploadApk(CommonsMultipartFile file) throws XException {
		// 获取上传文件的名字
		String filename = FilenameUtils.getName(file.getOriginalFilename());
		if (filename.contains(".")) {
			// .文件格式
			filename = filename.substring(filename.lastIndexOf("."));
		}
		// 上传到七牛后保存的文件名
		String key = UUID.randomUUID().toString() + sdf.format(new Date())
				+ filename;
		byte[] bs = file.getBytes();
		// 创建上传对象
		UploadManager uploadManager = new UploadManager();

		try {
			// 调用put方法上传
			Response res = uploadManager.put(bs, key, getUpToken());
			ImgKey imgKey = JSONObject.parseObject(res.bodyString(),
					ImgKey.class);

			// 打印返回的信息
			return IMGPATH + imgKey.getKey();
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时的异常的信息
			logger.error(r.toString());
			try {
				// 响应的文本信息
				return r.bodyString();
			} catch (QiniuException e1) {
				// ignore
			}
			throw new XException("上传apk失败");
		}
	}

	/**
	 * 删除七牛上的文件，根据url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean deleteByUrl(String url) {
		String key = url.substring(url.trim().lastIndexOf("/") + 1);
		logger.debug(key);
		// 实例化一个BucketManager对象
		BucketManager bucketManager = new BucketManager(auth);
		try {
			// 调用delete方法删除文件
			bucketManager.delete(bucketname, key);
			return true;
		} catch (QiniuException e) {
			// 捕获异常信息
			Response r = e.response;
			logger.error(r.toString());
		}
		return false;
	}

	/**
	 * 获取已上传的文件信息(返回信息不太准确)
	 * 
	 * @param url
	 * @return
	 */
	public static FileInfo getInfoByUrl(String url) {
		String key = url.substring(url.trim().lastIndexOf("/") + 1);
		// 实例化一个BucketManager对象
		BucketManager bucketManager = new BucketManager(auth);
		try {
			// 调用stat()方法获取文件的信息
			return bucketManager.stat(bucketname, key);
		} catch (QiniuException e) {
			// 捕获异常信息
			Response r = e.response;
			logger.error(r.toString());
		}
		return null;
	}
}
