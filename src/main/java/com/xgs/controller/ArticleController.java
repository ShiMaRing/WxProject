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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private static final Map<String, String> myMap = new HashMap<String, String>();
    static{
        myMap.put("fx", "分析");
        myMap.put("jd", "解读");
        myMap.put("sj", "时政");
        myMap.put("zc", "政策");
        myMap.put("wh", "文化");
        myMap.put("rw", "人物");
        myMap.put("zf", "致富");
        myMap.put("al", "案例");
        myMap.put("rd", "热点");
    }

    @Autowired
    PolicySpider policySpider;
    @Autowired
    ArticleDao articleDao;

    //返回所有类型
    @RequestMapping(value = "/getType",method = RequestMethod.GET)
    List<String> getAllType(){

        List<String> yinwenList = articleDao.selectAllType();
        List<String> wenzhiList = new ArrayList<>();
        for(String yinwen:yinwenList){
            wenzhiList.add(myMap.get(yinwen));
        }
        return wenzhiList;
    }

    //前端传入get请求 获取该类下的某一页下 一定条数的文章
    @RequestMapping(value = "/getList/{typename}/{page}/{size}",method = RequestMethod.GET)
    List<Article> getArticles(@PathVariable("typename")String typename,@PathVariable("page")int page,
                              @PathVariable("size")int size){
        List<String> yinwenList = articleDao.selectAllType();
        String yinwen = "";
        for(String y:yinwenList){
            if(typename.equals(myMap.get(y))){
                yinwen = y;
            }
        }
        PageHelper.startPage(page,size);
        List<Article> articles = articleDao.selectByType(yinwen);
        return articles;
    }
}
