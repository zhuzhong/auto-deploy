package com.z.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RmtShellExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RmtShellExecutor.class);

  //  private String charset = Charset.defaultCharset().toString();

   // private static final int TIME_OUT = 1000 * 5 * 60;

    public RmtShellExecutor() {

    }

    public String execCommand(Connection conn, String cmds) throws IOException {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        String charset = Charset.defaultCharset().toString();
        int TIME_OUT = 1000 * 5 * 60;
        try {

            Session session = conn.openSession();
            session.execCommand(cmds);
            stdOut = new StreamGobbler(session.getStdout());
            outStr = processStream(stdOut, charset);
            LOG.info(" stdoutStr={}", outStr);
            stdErr = new StreamGobbler(session.getStderr());
            outErr = processStream(stdErr, charset);
            LOG.info("stdErr={}", outErr);
            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
            ret = session.getExitStatus();

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stdOut != null)
                stdOut.close();
            if (stdErr != null)
                stdErr.close();
        }

        return outStr;
    }

    private String processStream(InputStream in, String charset) throws IOException {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        String usr = "username";
        String password = "pwd";
        String serverIP = "hostip";
        // String shPath = "/root/ab.sh";

        RmtShellExecutor exe = new RmtShellExecutor();
        RmtConnection shellConnection = new RmtConnection();
        String outInf;

        try {
            // outInf = exe.exec("sh " + shPath + " xn");
            outInf = exe.execCommand(shellConnection.getConnection(serverIP, usr, password), "ls /opt/test");
            System.out.println("outInf= " + outInf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
