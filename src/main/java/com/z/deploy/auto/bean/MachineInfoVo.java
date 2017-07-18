/**
 * 
 */
package com.z.deploy.auto.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class MachineInfoVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -331875866617168372L;

    private Long machineId;
    private String machineName;
    private String machineIp;
    private String userName;
    private String pwd;
    private String remark;

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
