package com.absurd.circle.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;



import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class BCSUtils {
	//BCS������
	private static final String ACCESS_KEY = "3de4772c4c4d00162c355b7f0d803f41";
	private static final String SECURE_KEY = "A96c98e72bbcfd46e2f94095039af09e";
	
	private static final String HOST = "bcs.duapp.com";
	private static final String BUCKET = "campus-media";
	private static BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(ACCESS_KEY, SECURE_KEY),HOST);

	
	/**
	 * �ϴ��ļ���BCS
	 * @param object  BCS���ļ����ϴ�·�� 
	 * @param content �ϴ��ļ����ֽ�����
	 * @throws java.io.IOException
	 */
	public static void upload(String object,byte[] content) throws IOException{
		ObjectMetadata metadata = new ObjectMetadata();
		
		metadata.setContentEncoding("utf-8");
		metadata.setContentLength(content.length);
		
		PutObjectRequest request = new PutObjectRequest(BUCKET, object,new ByteArrayInputStream(content), metadata);
		
		boolean isExist = baiduBCS.doesObjectExist(BUCKET, object);
		if(isExist){
			System.out.println("file exits");
		}else{
			baiduBCS.putObject(request);
		}
		
	}
	
	/**
	 * ���ļ����ϴ��ļ���BCS
	 * 
	 * @param srcFile �����ļ����
	 * @param object BCS���ļ��Ĵ洢·��
	 * @return boolean �����Ƿ��ϴ��ɹ�
	 */
	public static boolean PutObjectByFile(File srcFile,String object){
		PutObjectRequest request = new PutObjectRequest(BUCKET,object,srcFile);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/x-jpg");
		request.setMetadata(metadata);
		
		BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
		metadata = response.getResult();
		//�ٶ��ĵ�̫���ˣ�������
		return true;
	}
	
	/**
	 * ��BCS��ȡsrc
	 * @param src
	 * @return
	 */
	public static InputStream getObjectContent(String src){
		if(src != null){
			GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, src);
			BaiduBCSResponse<DownloadObject> response = baiduBCS.getObject(getObjectRequest);
			DownloadObject result = null;
			if(response != null)
				result = response.getResult();
			if (result != null)
				return result.getContent();
			else
				return null;
		}else
			return null;
	}
	
	/**
	 * �����������ϴ��ļ���BCS
	 * @param content
	 * @param length
	 * @param object
	 * @return boolean
	 */
	public static boolean PutObjectByInputStream(InputStream content,String object,long length){	
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/x-jpg");
		metadata.setContentLength(length);
		PutObjectRequest request =  new PutObjectRequest(BUCKET, object, content,metadata);
		
		ObjectMetadata result = baiduBCS.putObject(request).getResult();

		return true;
	}
	
	
	
	/**
	 * ��ȡ��BCS��GET������Դ��Url��ַ
	 * @param object
	 * @return
	 */
	public static String generateUrl(String object){
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET,BUCKET,object);
		///����
		return baiduBCS.generateUrl(generateUrlRequest);
	}
	
	
	
}
