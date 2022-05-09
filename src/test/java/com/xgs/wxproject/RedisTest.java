package com.xgs.wxproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootRedisApplicationTests {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Test
  void contextLoads() {
    stringRedisTemplate.opsForValue().append("msg","coder");
    //列表操作
    stringRedisTemplate.opsForList().leftPush("mylist","1");
    stringRedisTemplate.opsForList().leftPush("mylist","2");
  }

  @Test
  void testString() {
    ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();//操作字符串类型
    forValue.set("k1","v1");
    System.out.println(forValue.get("k1"));

    Map<String,String> map=new HashMap<>();
    map.put("k9","v9");
    map.put("k10","v10");
    forValue.multiSet(map);
  }
}
