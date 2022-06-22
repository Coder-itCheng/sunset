package com.cloud.log;

import com.cloud.log.dao.mongo.MongoLogDao;
import com.cloud.model.log.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author: huzc
 * @date: 2022/6/22 20:52
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDbTest {

    @Autowired
    private MongoLogDao mongoLogDao;

    @Test
    public void test() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("module","修改菜单");
        System.out.println(mongoLogDao.count(map));
    }

    @Test
    public void testSave() {
        Log log = new Log();
        log.set_id("13131sfsf");
        log.setUsername("fdfd");
        mongoLogDao.save(log);
    }

    @Test
    public void testFind() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("start",0);
        params.put("length",10);
        System.out.println(mongoLogDao.findData(params));
    }
}
