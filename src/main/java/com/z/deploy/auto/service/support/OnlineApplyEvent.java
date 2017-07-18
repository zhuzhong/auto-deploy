/**
 * 
 */
package com.z.deploy.auto.service.support;

import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 *
 */
public class OnlineApplyEvent extends ApplicationEvent {

    public OnlineApplyEvent(Long applyId) {
        super(applyId);

    }

    /**
     * 
     */
    private static final long serialVersionUID = -8130966584562394579L;

}
