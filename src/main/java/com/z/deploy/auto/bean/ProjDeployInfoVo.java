/**
 * 
 */
package com.z.deploy.auto.bean;

import java.io.Serializable;

/**
 * 应用部署情况
 * 
 * @author Administrator
 *
 */
public class ProjDeployInfoVo implements Serializable {

    /**
     create table t_proj_deploy(
deploy_id,
machine_id,
proj_name,
war_name,
war_path,
start_command,
stop_command,
restart_command
);
     */
    private static final long serialVersionUID = -5242624438246866471L;

    private Long deployId;
    private Long machineId;
    private String projName;
    private String warName;
    private String warPath;
    private String warBakPath;//war包备份目录,备份的war名称＝原war包名+"."+System.currentTimeMillis();
    private String startCommand, stopCommand, restartCommand;
    
    public String getWarBakPath() {
        return warBakPath;
    }

    public void setWarBakPath(String warBakPath) {
        this.warBakPath = warBakPath;
    }



    public Long getDeployId() {
        return deployId;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getWarName() {
        return warName;
    }

    public void setWarName(String warName) {
        this.warName = warName;
    }

    public String getWarPath() {
        return warPath;
    }

    public void setWarPath(String warPath) {
        this.warPath = warPath;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public String getStopCommand() {
        return stopCommand;
    }

    public void setStopCommand(String stopCommand) {
        this.stopCommand = stopCommand;
    }

    public String getRestartCommand() {
        return restartCommand;
    }

    public void setRestartCommand(String restartCommand) {
        this.restartCommand = restartCommand;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
