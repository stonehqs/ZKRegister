package com.hqs.zk.register.util;

import com.hqs.zk.register.config.AppConfig;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by huangqingshi on 2019/1/9.
 */
@Component
public class ZKUtil {

    @Autowired
    AppConfig appConfig;

    @Autowired
    ZkClient zkClient;

    /**
     * 创建主路径
     */
    public void createRootPath() {
        boolean exists = zkClient.exists(appConfig.getZkPath());
        if(!exists) {
            zkClient.createPersistent(appConfig.getZkPath());
        }
    }

    /**
     * 创建临时目录
     */
    public void createEphemeralNode(String path, String value) {
        zkClient.createEphemeral(path, value);
    }

    /**
     * 监听事件
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("parentPath:" + parentPath + ":list:" + currentChilds.toString());
            }
        });
    }

    /**
     * 获取所有注册节点
     * @return
     */
    public List<String> getAllNode(){
        List<String> children = zkClient.getChildren(appConfig.getZkPath());
        return children;
    }

    /**
     * 关闭 ZK
     */
    public void closeZK() {
        zkClient.close();

    }

}
