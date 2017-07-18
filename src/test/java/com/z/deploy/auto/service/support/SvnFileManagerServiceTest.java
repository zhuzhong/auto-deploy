/**
 * 
 */
package com.z.deploy.auto.service.support;

import org.junit.Test;

import com.z.deploy.auto.service.FileDownloadService;

/**
 * @author Administrator
 *
 */
public class SvnFileManagerServiceTest {

    @Test
    public void downloadModel() {
        FileDownloadService manager=new SvnFileDownloadServiceImpl("zhuzhong", "zhuzhong");
        
        manager.downloadModel("https://10.148.16.40/svn/aldb/release/aldb-universe/magicmall/20170608/02", "cas.war");
    }
}
