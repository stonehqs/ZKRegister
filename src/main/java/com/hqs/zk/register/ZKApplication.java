package com.hqs.zk.register;

import com.hqs.zk.register.config.AppConfig;
import com.hqs.zk.register.thread.ZKRegister;
import com.hqs.zk.register.util.ZKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@SpringBootApplication
public class ZKApplication implements CommandLineRunner{
	@Autowired
	AppConfig appConfig;

	@Autowired
	ZKUtil zkUtil;

	public static void main(String[] args) {
		SpringApplication.run(ZKApplication.class, args);
		System.out.println("启动应用成功");
	}

	@Override
	public void run(String... strings) throws Exception {
		//获得本机IP
		String addr = InetAddress.getLocalHost().getHostAddress();
		Thread thread = new Thread(new ZKRegister(appConfig, zkUtil, addr));
		thread.setName("register-thread");
		thread.start();

		Thread scanner = new Thread(new Scanner());
		scanner.start();
	}
}

