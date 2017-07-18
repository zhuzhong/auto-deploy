/**
 * 
 */
package com.z.deploy.auto.service;

import java.io.File;

/**
 * @author Administrator
 *
 */
public interface FileUploadService {
    // 将本地文件上传至服务器
    public String putFile2Remote(String remotePath, File file);
}
