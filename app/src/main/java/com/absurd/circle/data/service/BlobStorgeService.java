package com.absurd.circle.data.service;


import com.absurd.circle.app.AppConstant;
import com.microsoft.windowsazure.services.blob.client.BlobContainerPermissions;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by absurd on 14-3-28.
 */
public class BlobStorgeService{

    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;"
                    + "AccountName=" + AppConstant.ACCOUNT_NAME + ";"
                    + "AccountKey=" + AppConstant.ACCOUNT_KEY;


    public static void main(String[] args) {
        try {
            CloudStorageAccount account;
            CloudBlobClient serviceClient;
            CloudBlobContainer container;
            CloudBlockBlob blob;

            account = CloudStorageAccount.parse(storageConnectionString);
            serviceClient = account.createCloudBlobClient();
            // Container name must be lower case.
            container = serviceClient.getContainerReference("pics");
            container.createIfNotExist();

            // Set anonymous access on the container.
            BlobContainerPermissions containerPermissions;
            containerPermissions = new BlobContainerPermissions();
            container.uploadPermissions(containerPermissions);

            // Upload an image file.
            blob = container.getBlockBlobReference("123.jpg");
            File fileReference = new File("D:\\photo\\123.jpg");
            blob.upload(new FileInputStream(fileReference), fileReference.length());
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.print("FileNotFoundException encountered: ");
            System.out.println(fileNotFoundException.getMessage());
            System.exit(-1);
        } catch (StorageException storageException) {
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }

}
