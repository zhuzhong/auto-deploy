/**
 * 
 */
package com.z.deploy.auto.service.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;

import com.z.deploy.auto.service.FileUploadService;

/**
 * @author Administrator
 *
 */
public class ScpFileUploadServiceImpl implements FileUploadService {

    private String host, user, pass;

    public ScpFileUploadServiceImpl(String host, String user, String pass) {
        super();
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public String putFile2Remote(String remotePath, File file) {

        try {

            Connection con = new Connection(host);
            con.connect();
            boolean isAuthed = con.authenticateWithPassword(user, pass);
            System.out.println("isAuthed====" + isAuthed);

            SCPClient scpClient;
            scpClient = con.createSCPClient();

            SCPOutputStream outputStream = scpClient.put(file.getName(), file.length(), remotePath, null);
            FileInputStream fis = new FileInputStream(file);
            // 自己定义一个缓冲区
            byte[] buffer = new byte[1024];
            int flag = 0;
            while ((flag = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, flag);
            }
            fis.close();
            outputStream.flush();
            outputStream.close();
            con.close();
            return "ook";
        } catch (IOException e) {

            e.printStackTrace();
            return "error";
        }

    }

}
