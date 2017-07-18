/**
 * 
 */
package com.z.deploy.auto.bean;

import java.io.Serializable;

/**
 * 上线申请
 * 
 * @author Administrator
 *
 */
public class OnlineApplyVo implements Serializable {

	/**
	 * create t_online_apply( 
	 * appy_id, 
	 * online_war_name, 
	 * online_war_path,
	 * apply_state );
	 */
	private static final long serialVersionUID = 6465276457815671723L;
	private Long applyId;// 上线申请id
	private String onlineWarName;// 上线war包
	private String onlineWarPath;// 上线war路径

	private String applyState;// 上线申请状态

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getOnlineWarName() {
		return onlineWarName;
	}

	public void setOnlineWarName(String onlineWarName) {
		this.onlineWarName = onlineWarName;
	}

	public String getOnlineWarPath() {
		return onlineWarPath;
	}

	public void setOnlineWarPath(String onlineWarPath) {
		this.onlineWarPath = onlineWarPath;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

}
