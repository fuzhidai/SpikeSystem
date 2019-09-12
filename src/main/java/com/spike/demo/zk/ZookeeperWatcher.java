package com.spike.demo.zk;

import com.spike.demo.controller.SpikeController;
import com.spike.demo.global.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

@Service
public class ZookeeperWatcher implements Watcher {

    private ZooKeeper zooKeeper;

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("get notification.");

        if (watchedEvent.getType() == Event.EventType.None && watchedEvent.getPath() == null) {
            System.out.println("connect successfully.");

            try {
                // 创建 zookeeper 商品售罄信息根节点
                String path = "/" + Constants.PRODUCT_STOCK_PREFIX;
                if (zooKeeper != null && zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }

        } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            try {
                // 获取节点路径
                String path = watchedEvent.getPath();
                // 获取节点数据
                String soldOut = new String(zooKeeper.getData(path, true, new Stat()));
                // 处理当前服务器对应 JVM 缓存
                if ("false".equals(soldOut)) {
                    // 获取商品 Id
                    String productId = path.substring(path.lastIndexOf("/") + 1, path.length());
                    System.out.println("productId：" + productId);
                    // 同步当前 JVM 缓存
                    if (SpikeController.getProductSoldOutMap().contains(productId)) {
                        SpikeController.getProductSoldOutMap().remove(productId);
                    }
                }

            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
