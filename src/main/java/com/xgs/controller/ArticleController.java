package com.xgs.controller;

import com.github.pagehelper.PageHelper;
import com.xgs.dao.ArticleDao;
import com.xgs.pojo.Article;
import com.xgs.spider.PolicySpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    PolicySpider policySpider;
    @Autowired
    ArticleDao articleDao;

    //返回所有类型
    @RequestMapping(value = "/getType",method = RequestMethod.GET)
    List<String> getAllType(){
        return articleDao.selectAllType();
    }

    //前端传入get请求 获取该类下的某一页下 一定条数的文章
    @RequestMapping(value = "/getList/{typename}/{page}/{size}",method = RequestMethod.GET)
    List<Article> getArticles(@PathVariable("typename")String typename,@PathVariable("page")int page,
                              @PathVariable("size")int size){
        PageHelper.startPage(page,size);
        List<Article> articles = articleDao.selectByType(typename);
        return articles;
    }
}
