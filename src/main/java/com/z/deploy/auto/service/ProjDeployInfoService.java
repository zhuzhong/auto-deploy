package com.z.deploy.auto.service;

import java.util.List;

import com.z.deploy.auto.bean.ProjDeployInfoVo;

public interface ProjDeployInfoService {

    List<ProjDeployInfoVo> queryProjDeployInfoVoListByWarName(String warName);

    Long doSaveProjDeployInfoVo(ProjDeployInfoVo info);
}
