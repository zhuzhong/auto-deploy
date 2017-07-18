package com.z.deploy.auto.service.support;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.z.deploy.auto.service.FileDownloadService;

public class SvnFileDownloadServiceImpl implements FileDownloadService {

    private SVNClientManager ourClientManager;
    private SVNURL repositoryOptUrl;
    private String userName;
    private String passwd;

    public SvnFileDownloadServiceImpl(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
    }

    private void setUpSVNClient(String userName, String passwd) {
        SVNRepositoryFactoryImpl.setup();
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, userName, passwd);
    }

    /**
     * 上传模型
     * 
     * @param dirPath
     */
    public void uploadMoel(String dirPath, String modelName) {
        setUpSVNClient(userName, passwd);
        File impDir = new File(dirPath);
        SVNCommitClient commitClient = ourClientManager.getCommitClient();
        commitClient.setIgnoreExternals(false);
        try {
            repositoryOptUrl = SVNURL.parseURIEncoded(RepositoryInfo.buffUrl + modelName);
            commitClient.doImport(impDir, repositoryOptUrl, "import operation!", null, true, true, SVNDepth.INFINITY);
        } catch (SVNException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 下载模型
     */
    @Override
    public File downloadModel(String filepath, String fileName) {
        setUpSVNClient(userName, passwd);
        fileName = fileName.trim();
        File outDir = new File(System.getProperty("user.home") + "/" + fileName);
        if (outDir.exists()) {
            outDir.delete();
        }

        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);

        try {
            filepath = filepath.trim();
            if (filepath.endsWith("/")) {
                filepath += fileName;
            } else {
                filepath += "/" + fileName;
            }
            SVNURL repositoryOptUrl = SVNURL.parseURIEncoded(filepath);
            updateClient.doExport(repositoryOptUrl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, "downloadModel", true,
                    SVNDepth.FILES);
        } catch (SVNException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("oook");
        return outDir;
    }

    /**
     * 删除模型
     */
    public void deleteModel(String deleteModelName) {
        setUpSVNClient(userName, passwd);
        SVNCommitClient commitClient = ourClientManager.getCommitClient();
        commitClient.setIgnoreExternals(false);

        try {
            repositoryOptUrl = SVNURL.parseURIEncoded(RepositoryInfo.storeUrl + deleteModelName);
            SVNURL deleteUrls[] = new SVNURL[1];
            deleteUrls[0] = repositoryOptUrl;
            commitClient.doDelete(deleteUrls, "delete model");
        } catch (SVNException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 移动模型
     */
    public void moveModel(String modelName) {
        setUpSVNClient(userName, passwd);
        SVNCopyClient copyClient = ourClientManager.getCopyClient();
        copyClient.setIgnoreExternals(false);

        try {
            repositoryOptUrl = SVNURL.parseURIEncoded(RepositoryInfo.buffUrl + modelName);
            SVNURL destUrl = SVNURL.parseURIEncoded(RepositoryInfo.storeUrl + modelName);
            SVNCopySource[] copySources = new SVNCopySource[1];
            copySources[0] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, repositoryOptUrl);

            copyClient.doCopy(copySources, destUrl, true, false, false, "move", null);
        } catch (SVNException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public boolean deleteFile(File f) {
       if(f.exists()){
          return f.delete();
       }
        return false;
    }
}