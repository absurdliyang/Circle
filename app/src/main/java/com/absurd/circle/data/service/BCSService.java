package com.absurd.circle.data.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

/**
 * Created by absurd on 14-4-6.
 */
public class BCSService {
    private static final String HOST = "bcs.duapp.com";
    private static final String BUCKET = "circle-android";

    private static final String IMAGE_PATH = "/image/";
    private static final String IMAGE_EXT = ".jpg";

    private static BaiduBCS baiduBCS = new BaiduBCS(new BCSCredentials(AppConstant.BCS_ACCESS_KEY, AppConstant.BCS_SECURE_KEY),HOST);

    /**
     * upload file to bcs from byte array
     * @param object  the uploading file bcs file path
     * @param content the uploading file content byte array
     * @throws IOException
     */
    public static boolean upload(String object,byte[] content) throws IOException{
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentEncoding("utf-8");
        metadata.setContentLength(content.length);

        PutObjectRequest request = new PutObjectRequest(BUCKET, object,new ByteArrayInputStream(content), metadata);
        boolean isExist = baiduBCS.doesObjectExist(BUCKET, object);
        if(isExist){
            AppContext.commonLog.i("BCS file existed already");
            return false;
        }
        baiduBCS.putObject(request);
        return true;
    }

    /**
     * upload file from input stream
     * @param content
     * @param length
     * @param object
     * @return boolean
     */
    public static boolean uploadFileByStream(InputStream content,String object,long length){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/x-jpg");
        metadata.setContentLength(length);
        String fileName = IMAGE_PATH + object;
        PutObjectRequest request =  new PutObjectRequest(BUCKET, fileName, content,metadata);

        ObjectMetadata result = baiduBCS.putObject(request).getResult();
        return true;
    }


    /**
     * upload file from local file
     *
     * @param srcFile local file name
     * @param object the uploading file bcs file path
     * @return boolean is success
     */
    public static String uploadImageByFile(File srcFile,String object){
        PutObjectRequest request = new PutObjectRequest(BUCKET,object,srcFile);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/x-jpg");
        request.setMetadata(metadata);

        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
        return generateUrl(object);
    }

    /**
     * upload file from local file
     *
     * @param srcFile local file name
     * @return boolean is success
     */
    public static String uploadImageByFile(File srcFile){
        String fileName = UUID.randomUUID().toString();
        fileName = IMAGE_PATH + fileName + IMAGE_EXT;
        return uploadImageByFile(srcFile,fileName);
    }

    /**
     * get src from bcs
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
     * get bcs object request url
     * @param object
     * @return
     */
    public static String generateUrl(String object){
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET,BUCKET,object);
        return baiduBCS.generateUrl(generateUrlRequest);
    }

}
