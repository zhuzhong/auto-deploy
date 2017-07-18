package com.z.deploy.auto.service;

import com.z.deploy.auto.bean.OnlineApplyVo;

public interface OnlineApplyService {

    /**
     * 保存一个上线申请
     * 
     * @param apply
     * @return
     */
    Long onSaveApply(OnlineApplyVo apply);

    /**
     * 通过id,查询信息
     */
    OnlineApplyVo queryApplyById(Long id);
}
