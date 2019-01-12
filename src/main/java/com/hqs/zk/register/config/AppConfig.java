package com.hqs.zk.register.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by huangqingshi on 2019/1/9.
 */
@Component
public class AppConfig {

    @Value("${zookeeper.address}")
    private String zkAddress;

    @Value("${zookeeper.root.path}")
    private String zkPath;

    @Value("${server.port}")
    private String port;

    @Value("${server.netty.address}")
    private String nettyServer;

    @Value("${server.netty.port}")
    private int nettyPort;

    public int getNettyPort() {
        return nettyPort;
    }

    public String getNettyServer() {
        return nettyServer;
    }

    public void setNettyServer(String nettyServer) {
        this.nettyServer = nettyServer;
    }

    public void setNettyPort(int nettyPort) {
        this.nettyPort = nettyPort;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }
}
