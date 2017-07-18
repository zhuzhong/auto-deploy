/**
 * 
 */
package com.z.deploy.auto.service.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.z.deploy.auto.bean.MachineInfoVo;
import com.z.deploy.auto.bean.OnlineApplyVo;
import com.z.deploy.auto.bean.ProjDeployInfoVo;
import com.z.deploy.auto.service.FileDownloadService;
import com.z.deploy.auto.service.MachineInfoService;
import com.z.deploy.auto.service.OnlineApplyService;
import com.z.deploy.auto.service.ProjDeployInfoService;

/**
 * @author Administrator
 *
 */
public class OnlineApplyEventListener implements ApplicationListener<OnlineApplyEvent> {

    @Autowired
    private OnlineApplyService onlineApplyService;
    @Autowired
    private ProjDeployInfoService projDeployInfoService;

    @Autowired
    private MachineInfoService machineInfoService;

    public void onApplicationEvent(OnlineApplyEvent event) {
        Long applyId = (Long) event.getSource(); // 获取到申请的id
        // 查询申请信息
        OnlineApplyVo applyVo = onlineApplyService.queryApplyById(applyId);
        // 查询应用部署情况
        List<ProjDeployInfoVo> projDeployInfoVolist = projDeployInfoService.queryProjDeployInfoVoListByWarName(applyVo
                .getOnlineWarName());

        Map<Long/* machineId */, MachineInfoVo> machineMaps = new HashMap<>(); // 获取物理机或虚拟机的信息

        for (ProjDeployInfoVo info : projDeployInfoVolist) {
            if (machineMaps.get(info.getMachineId()) != null) {
                continue;
            }
            Long machineId = info.getMachineId();
            MachineInfoVo machineInfoVo = machineInfoService.queryMachineInfoVoById(machineId);
            machineMaps.put(machineId, machineInfoVo);
        }

        // 下载远程war包到本服务器的用户临时目录
        FileDownloadService fileDownloadService = new SvnFileDownloadServiceImpl("**", "**");
        File srcFile = fileDownloadService.downloadModel(applyVo.getOnlineWarPath(), applyVo.getOnlineWarName()); // war包下载
        for (ProjDeployInfoVo info : projDeployInfoVolist) {
            // 获取服务器地址
            MachineInfoVo machineInfo = machineMaps.get(info.getMachineId());
            // 登陆远程服务器
            Connection con = getConnection(machineInfo.getMachineIp(), machineInfo.getUserName(), machineInfo.getPwd());
            // 服务war备份
            StringBuilder sb = new StringBuilder("cp ");
            sb.append(info.getWarPath());
            sb.append("/");
            sb.append(info.getWarName());
            sb.append("  ");
            sb.append(info.getWarBakPath());
            sb.append("/");
            sb.append(info.getWarName());
            sb.append(".");
            sb.append(System.currentTimeMillis());
            execCommand(con, sb.toString());

            // 停服
            execCommand(con, info.getStopCommand());
            // 新war上传
            putFile2Remote(con, info.getWarPath(), srcFile);
            // 启动服务
            execCommand(con, info.getStartCommand());

            // 关闭远程连接
            con.close();
            LOG.info("部署ok");
        }

        // 删除本地war包
        fileDownloadService.deleteFile(srcFile);
    }

    private String putFile2Remote(Connection con, String remotePath, File file) {

        try {

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
            return "ook";
        } catch (IOException e) {

            e.printStackTrace();
            return "error";
        }

    }

    private Connection getConnection(String host, String user, String pass) {
        try {
            Connection con = new Connection(host);
            con.connect();
            boolean isAuthed = con.authenticateWithPassword(user, pass);
            System.out.println("isAuthed====" + isAuthed);
            return con;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(OnlineApplyEventListener.class);

    private String execCommand(Connection conn, String cmds) {
        try {
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
                if (stdOut != null)
                    stdOut.close();
                if (stdErr != null)
                    stdErr.close();
            }

            return outStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String processStream(InputStream in, String charset) throws IOException {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }
}
