/**
 * 
 */
package com.z.deploy.auto.service;

import java.io.File;

/**
 * @author Administrator
 *
 */
public interface FileDownloadService {
    public File downloadModel(String filepath, String fileName);

    public boolean deleteFile(File f);
}
