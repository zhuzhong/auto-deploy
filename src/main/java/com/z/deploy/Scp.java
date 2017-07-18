package com.z.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPInputStream;
import ch.ethz.ssh2.SCPOutputStream;

public class Scp {
    public static void main(String[] args) throws IOException {
        Scp scp = new Scp();

        RmtConnection shellConnection = new RmtConnection();
        Connection srcConnection = shellConnection.getConnection("hostip", "usrename", "pwd");
        File srcFile = scp.getRemoteFile(srcConnection, "/opt/", "nginx.conf");
        Connection destConnection = shellConnection.getConnection("hostip", "usrname", "pwd");
        scp.putFile2Remote(destConnection, "/opt/test", srcFile);

    }

    public void putFile2Remote(Connection remoteConn, String remotePath, File file) throws IOException {
        SCPClient scpClient = remoteConn.createSCPClient();
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
        file.delete();
    }

    public File getRemoteFile(Connection remoteConn, String remotePath, String remoteFileName) throws IOException {

        /*
         * 从远程服务器中获取 file至本地，然后上传至另一服务器
         */

        if (remotePath == null) {
            throw new RuntimeException("remotePath is null");
        }

        if (!remotePath.endsWith("/")) {
            remotePath += "/";
        }
        if (remoteFileName == null) {
            throw new RuntimeException("remoteFileName is null");

        }

        File f = new File(System.getProperty("user.home") + "/" + remoteFileName);
        if (f.exists()) {
            f.delete();
        }
        OutputStream os = new FileOutputStream(f);

        SCPClient scpClient = remoteConn.createSCPClient();
        SCPInputStream sis = scpClient.get(remotePath + remoteFileName);
        // 自己定义一个缓冲区
        byte[] buffer = new byte[1024];
        int flag = 0;
        while ((flag = sis.read(buffer)) != -1) {
            os.write(buffer, 0, flag);
        }
        sis.close();
        os.flush();
        os.close();

        return f;
    }

}