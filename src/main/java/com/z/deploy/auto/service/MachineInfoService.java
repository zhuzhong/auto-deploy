package com.z.deploy.auto.service;

import com.z.deploy.auto.bean.MachineInfoVo;

public interface MachineInfoService {

    Long doSaveMachineInfoVo(MachineInfoVo info);

    MachineInfoVo queryMachineInfoVoById(Long machineId);
}
