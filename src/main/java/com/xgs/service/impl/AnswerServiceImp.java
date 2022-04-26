package com.xgs.service.impl;

import com.xgs.dao.BaseDao;
import com.xgs.pojo.Subclass;
import com.xgs.spider.QuestionSpider;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author cjh
 * @date 2022/4/26 - 19:30
 */
public class AnswerServiceImp extends BaseDao {

    public void insertAll() throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = getConnection();

        String sql ="insert into answer(ask_content,answer_content)values(?,?)";

        QuestionSpider questionSpider = new QuestionSpider();

        String query = "select * from subclass";
        ArrayListHandler handler = new ArrayListHandler();
    }


}
