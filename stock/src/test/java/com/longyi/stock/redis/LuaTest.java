package com.longyi.stock.redis;

import com.longyi.stock.redis.lock.ReadSession;
import com.longyi.stock.redis.lock.RedisLock;
import com.longyi.stock.redis.lua.LuaScript;
import com.longyi.stock.redis.util.RedisUtils;
import jodd.util.concurrent.ThreadFactoryBuilder;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaTest {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private ReadSession readSession;


    @Test
    public void testAddLua() {
        String json = LuaScript.SCRIPT_ADD_INCREASE_REALQTY;
        List<String> values = new ArrayList<String>(7);
        String vwInfoJson = "{sc:10,vi:201}";
        Long realQtyL = 2000L;
        Long id = 2390L;
        values.add(realQtyL + "");//增加的库存数
        values.add("rq");//真实库存加
        values.add("asale");//可售库存加
        values.add("id_");//库存ID(操作db用的) 操作符号
        values.add(id + "");//库存ID(操作db用的)  值
        values.add("vw");//虚拟仓信息json字符串 操作符号
        values.add(vwInfoJson);//虚拟仓信息json字符串  值
        redisUtils.evalScriptObject("stock-test_001", values, json);
    }

    @Test
    public void lockMaxAble() {
        long lockQtyL = 10;
        String json = LuaScript.SCRIPT_MAX_ABLE_LOCKQTY;
        List<String> values = new ArrayList<String>(7);
        values.add("asale");//可售库存减
        values.add((-lockQtyL) + "");//减的值
        values.add(lockQtyL + "");//减失败的值,即还回去的值
        values.add("lq");//冻结库存加
        values.add("id_");//库存ID(操作db用的) 操作符号
        values.add("rwid");//虚拟仓对应的真实仓id
        values.add("gid");//虚拟仓组ID
        List<Object> objects = redisUtils.evalScriptObject("test_002", values, json);
        System.out.println(JSONArray.toJSONString(objects));
    }


    @Test
    public void redisLock() {
        boolean lock = redisLock.lock("leiyi", "xxx03434", 2000);
    }


    @Test
    public void unLock() {
        boolean unlock = redisLock.unLock("leiyi", "xxx03434");
        System.out.println(unlock);
    }


    @Test
    public void lockRedis() {
        boolean test_sotck_order = redisLock.lock("test_sotck_order", System.currentTimeMillis() + 2 * 60 * 1000 + "");
        Assert.assertTrue(test_sotck_order);
    }


    @Test
    public void testRemove() {
        List<String> list = new ArrayList<>(10);
        list.add("1");
        list.add("2");
        for (String str : list) {
            if ("2".equals(str)) {
                list.remove(str);
            }
        }
        System.out.println(list.toString());
    }


    @Test
    public void readSession() {
        boolean order = readSession.createOrder();
        Assert.assertTrue(order);
    }

}
   