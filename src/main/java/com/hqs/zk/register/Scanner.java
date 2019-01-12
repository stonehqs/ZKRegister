package com.hqs.zk.register;

/**
 * Created by huangqingshi on 2019/1/12.
 */
public class Scanner implements Runnable {
    @Override
    public void run() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();
            System.out.println(msg);
        }
    }
}
