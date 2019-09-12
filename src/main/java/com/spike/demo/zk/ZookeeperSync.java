package com.spike.demo.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZookeeperSync implements Watcher {

    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeperClient = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception {
        // 文件存放路径
        String path = "/username";
        // 连接 zookeeper 并注册一个默认监听器
        zooKeeperClient = new ZooKeeper("192.168.177.128:2181", 5000, new ZookeeperSync());
        // 等待连接 zookeeper 成功通知
        connectSemaphore.await();
        // 获取 path 目录节点下的配置数据，并注册默认的监听器
        System.out.println(new String(zooKeeperClient.getData(path, true, stat)));

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.None && watchedEvent.getPath() == null){
            connectSemaphore.countDown();
        }else if (watchedEvent.getType() == Event.EventType.NodeDataChanged){
            try {
                System.out.println("新值为：" + new String(zooKeeperClient.getData(watchedEvent.getPath(), true, stat)));
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
