package com.xgs.service.impl;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.xgs.dao.ArticleDao;
import com.xgs.dao.BaseDao;
import com.xgs.pojo.Article;
import com.xgs.spider.PolicySpider;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cjh
 * @date 2022/4/17 - 11:47
 */
@Repository
public class ArticleServieceImp extends BaseDao {






    @Transactional
    public void insertAllType() throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = getConnection();

        String sql ="insert into article(id,type,create_time,ovtitle,url,source,thumb_image,content,`describe`)values(?,?,?,?,?,?,?,?,?)";
        //静态设置导入的页数
        int numOfPage = 5;

        PolicySpider policySpider = new PolicySpider();
        String[] category = {"rd","zc","jd","al","rw","zf","wh","sj","fx"};

        //插入数据量
        int count =0;

        Set<Integer> set = new HashSet<Integer>();

        //遍历不同类型
        for(int j = 0;j<category.length;j++){
            //遍历每一页中的数据
            for(int i = 0;i<numOfPage;i++ ){
                List<Article> articles =policySpider.getArticles(category[j],i);
                //将列表插入到 数据库
                for(Article article :articles){
                    Integer integer =Integer.parseInt(article.getId());
                    if(set.contains(integer)) continue;
                    set.add(Integer.parseInt(article.getId()));
                    count =  queryRunner.update(connection,sql,
                            article.getId(),article.getType(),article.getCreateTime(),
                            article.getOvtitle(), article.getUrl(),article.getSource(),
                            article.getThumbImage(),article.getContent(),article.getDescribe());
                }

            }
        }

    }

}
