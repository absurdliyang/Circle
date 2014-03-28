package com.absurd.circle.data.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by absurd on 14-3-28.
 */
public class BlobStorgeService{

    public static final String PICS_CONTAINER_NAME = "pics";
    public static final String PICS_FILE_EX = ".jpg";
    public static final String VOICES_CONTAINER_NAME = "voices";
    public static final String VOICES_FILE_EX = ".wav";

    public static final String API_ADDR = "https://circle.blob.core.windows.net/";

    public BlobStorgeService(){
    }

    public static boolean uploadImage(String absoluteFilePath,String strUrl){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(absoluteFilePath);
            int bytesRead = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            while((bytesRead = fis.read(b)) != -1){
                bos.write(b,0,bytesRead);
            }
            byte[] bytes = bos.toByteArray();
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.addRequestProperty("Content-Type","image/jpeg");
            urlConnection.setRequestProperty("Content-Length","" + bytes.length);
            // Write image data to server
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(bytes);
            wr.flush();
            wr.close();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 201 && urlConnection.getResponseMessage().equals("Created")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean uploadImage(String absoluteFilePath){
        String fileName = UUID.randomUUID().toString() + PICS_FILE_EX;
        String strUrl = API_ADDR + PICS_CONTAINER_NAME + "/" + fileName;
        System.out.println(strUrl);
        return uploadImage(absoluteFilePath,strUrl);
    }

    public static void main(String[] args){
        boolean res = BlobStorgeService.uploadImage("C:\\Users\\absurd\\Desktop\\Circle\\azure-mobile-services\\azure-mobile-service\\azure-mobile-services-master\\quickstart\\android\\ZUMOAPPNAME\\ic_launcher-web.png");
        if(res){
            System.out.println("ok");
        }else{
            System.out.println("not ok");
        }

    }

}
