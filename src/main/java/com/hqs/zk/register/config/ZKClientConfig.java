package com.hqs.zk.register.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by huangqingshi on 2019/1/9.
 */
@Component
public class ZKClientConfig {

    @Autowired
    AppConfig appConfig;

    @Bean
    public ZkClient buildZkClient() {
        return new ZkClient(appConfig.getZkAddress(), 5000);
    }

}
