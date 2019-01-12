package com.hqs.zk.register.thread;

import com.hqs.zk.register.config.AppConfig;
import com.hqs.zk.register.util.ZKUtil;

/**
 * Created by huangqingshi on 2019/1/9.
 */
public class ZKRegister implements Runnable {

    private AppConfig appConfig;
    private ZKUtil zkUtil;
    private String ip;

    public ZKRegister(AppConfig appConfig, ZKUtil zkUtil, String ip) {
        this.appConfig = appConfig;
        this.zkUtil = zkUtil;
        this.ip = ip;
    }

    @Override
    public void run() {
        zkUtil.createRootPath();
        String path = appConfig.getZkPath() + "/ip-"+ ip + ":" + appConfig.getPort();
        zkUtil.createEphemeralNode(path, path);
        zkUtil.subscribeEvent(appConfig.getZkPath());
    }
}
