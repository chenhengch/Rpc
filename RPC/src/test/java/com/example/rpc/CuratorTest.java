/*
package com.example.rpc;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CuratorTest {


    private CuratorFramework curatorFramework;

    @Before
    public void connect() {
        */
/*
         *
         * @param connectString       连接字符串。zk server 地址和端口 "192.168.149.135:2181,192.168.149.136:2181"
         * @param sessionTimeoutMs    会话超时时间 单位ms
         * @param connectionTimeoutMs 连接超时时间 单位ms
         * @param retryPolicy         重试策略
         *//*


        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retryPolicy)
                .namespace("curator")
                .build();
        curatorFramework.start();
    }

    */
/**
     * 创建节点：create 持久 临时 顺序 数据
     * 1. 基本创建 ：create().forPath("")
     * 2. 创建节点 带有数据:create().forPath("",data)
     * 3. 设置节点的类型：create().withMode().forPath("",data)
     * 4. 创建多级节点  /app1/p1 ：create().creatingParentsIfNeeded().forPath("",data)
     *//*

    @Test
    public void create() throws Exception {
        //如果创建节点，没有指定数据，则默认将当前客户端的ip作为数据存储
        String path = curatorFramework.create().forPath("/node1", "heihei".getBytes());
        System.out.println("path:" + path);
    }

    @Test
    public void create3() throws Exception {
        //默认类型：持久化  EPHEMERAL:临时的  客服端结束就没了
        String path = curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/node3");
        System.out.printf("path:" + path);
    }

    @Test
    public void create4() throws Exception {
        //creatingParentsIfNeeded():如果父节点不存在，则创建父节点
        String path = curatorFramework.create().creatingParentsIfNeeded().forPath("/node4/p1");
        System.out.printf("path:" + path);
    }

    */
/**
     * 查询节点：
     * 1. 查询数据：get: getData().forPath()
     * 2. 查询子节点： ls: getChildren().forPath()
     * 3. 查询节点状态信息：ls -s:getData().storingStatIn(状态对象).forPath()
     *//*


    @Test
    public void get1() throws Exception {
        byte[] bytes = curatorFramework.getData().forPath("/node1");
        System.out.println("get:" + new String(bytes));
    }

    @Test
    public void get2() throws Exception {
        List<String> strings = curatorFramework.getChildren().forPath("/node4");
        System.out.printf("list:" + strings);
    }

    @Test
    public void get3() throws Exception {
        Stat status = new Stat();
        System.out.println(status);
        byte[] bytes = curatorFramework.getData().storingStatIn(status).forPath("/node1");
        System.out.printf("status:" + status);
    }

    */
/**
     * 修改数据
     * 1. 基本修改数据：setData().forPath()
     * 2. 根据版本修改: setData().withVersion().forPath()
     * * version 是通过查询出来的。目的就是为了让其他客户端或者线程不干扰我。
     *//*


    @Test
    public void set1() throws Exception {
        Stat stat = curatorFramework.setData().forPath("/node4", "老六".getBytes());
        System.out.printf("stat:" + stat);
    }

    @Test
    public void set2() throws Exception {
        Stat stat = new Stat();
        System.out.printf("stat:" + stat);
        curatorFramework.getData().storingStatIn(stat).forPath("/node4");
        curatorFramework.setData().withVersion(stat.getVersion()).forPath("/node4", "老七".getBytes());
        System.out.printf("stat:" + stat);
    }

    */
/**
     * 删除节点： delete deleteall
     * 1. 删除单个节点:delete().forPath("/app1");
     * 2. 删除带有子节点的节点:delete().deletingChildrenIfNeeded().forPath("/app1");
     * 3. 必须成功的删除:为了防止网络抖动。本质就是重试。  client.delete().guaranteed().forPath("/app2");
     * 4. 回调：inBackground
     *//*


    @Test
    public void del() throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/node4");
    }

    @Test
    public void del2() throws Exception {
        curatorFramework.delete().forPath("/node");
    }

    @Test
    public void del3() throws Exception {
        curatorFramework.delete().guaranteed().forPath("/node1");
    }

    @Test
    public void del4() throws Exception {
        curatorFramework.delete().guaranteed().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.printf("我被删除成功");
                System.out.printf("event:" + curatorEvent);
            }
        }).forPath("/node");
    }

    @After
    public void close() {
        if (curatorFramework != null) {
            curatorFramework.close();
        }

    }
}*/
