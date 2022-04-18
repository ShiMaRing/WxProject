package com.xgs.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author cjh
 * @date 2022/4/17 - 11:59
 */
public class BaseDao {
    public  Connection getConnection() throws Exception{
        Properties pro = new Properties();
        ClassLoader classLoader = BaseDao.class.getClassLoader();

        InputStream in =classLoader .getResourceAsStream("druid.properties");
        if(in == null){
            System.out.println("in is null");
        }

        pro.load(in);
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        Connection conn = ds.getConnection();
        return conn;
    }


}
