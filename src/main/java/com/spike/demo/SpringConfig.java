package com.spike.demo;

import com.spike.demo.zk.ZookeeperWatcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SpringConfig {

    public static final String serverZookeeperAddress = "192.168.177.128:2181";

    @Bean
    public Queue orderQueue() {
        return new Queue("order", true);
    }

    @Bean
    public ZooKeeper initZookeeper() throws Exception {
        // 创建观察者
        ZookeeperWatcher watcher = new ZookeeperWatcher();
        // 创建 Zookeeper 客户端
        ZooKeeper zooKeeper = new ZooKeeper(serverZookeeperAddress, 30000, watcher);
        // 将客户端注册给观察者
        watcher.setZooKeeper(zooKeeper);
        // 将配置好的 zookeeper 返回
        return zooKeeper;
    }
}
