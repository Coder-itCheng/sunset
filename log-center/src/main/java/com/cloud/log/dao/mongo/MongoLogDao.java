package com.cloud.log.dao.mongo;

import com.cloud.model.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: huzc
 * @date: 2022/6/22 20:15
 * @description:
 */
@Repository
public class MongoLogDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Log log) {
        mongoTemplate.save(log,"log");
    }

    public int count(Map<String, Object> params) {
        Query query = new Query();
        String username = (String) params.get("username");
        String module = (String) params.get("module");
        String flag = (String) params.get("flag");
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");

        Criteria criteria = new Criteria();
        if (username != null && !username.equals("")) {
            criteria.andOperator(Criteria.where("username").regex(".*?" + username + ".*"));
        }
        if (module != null && !module.equals("")) {
            criteria.andOperator(criteria.and("module").regex(".*?" + module + ".*"));
        }
        if (flag != null && !flag.equals("")) {
            criteria.andOperator(criteria.andOperator(criteria.and("flag").is(flag)));
        }
        if (beginTime != null && !beginTime.equals("")) {
            criteria.andOperator(criteria.and("beginTime").gte(new Date(beginTime)));
        }
        if (endTime != null && !endTime.equals("")) {
            criteria.andOperator(criteria.and("endTime").lte(new Date(beginTime)));
        }

        query.addCriteria(criteria);
        return (int) mongoTemplate.count(query, Log.class);
    }

    public List<Log> findData(Map<String, Object> params) {
        Query query = new Query();
        String username = (String) params.get("username");
        String module = (String) params.get("module");
        String flag = (String) params.get("flag");
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");

        Criteria criteria = new Criteria();
        if (username != null && !username.equals("")) {
            criteria.andOperator(Criteria.where("username").regex(".*?" + username + ".*"));
        }
        if (module != null && !module.equals("")) {
            criteria.andOperator(criteria.and("module").regex(".*?" + module + ".*"));
        }
        if (flag != null && !flag.equals("")) {
            criteria.andOperator(criteria.andOperator(criteria.and("flag").is(flag)));
        }
        if (beginTime != null && !beginTime.equals("")) {
            criteria.andOperator(criteria.and("beginTime").gte(new Date(beginTime)));
        }
        if (endTime != null && !endTime.equals("")) {
            criteria.andOperator(criteria.and("endTime").lte(new Date(beginTime)));
        }
        Object start = params.get("start");
        Object length = params.get("length");
        if (start != null && length != null) {
            PageRequest pageRequest = new PageRequest(((Integer) start) / (Integer) length, (Integer) length);
            query.with(pageRequest);
        }
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Log.class);
    }
}
