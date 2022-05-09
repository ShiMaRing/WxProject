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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //为直接搜索出来的内容添加样式
    String addStyleForContent(String contentPast){
        Pattern pattern1 = Pattern.compile("<img  style=\"{1}");
        Matcher matcher1 = pattern1.matcher(contentPast);

        String result1 = matcher1.replaceAll("<img  style=\" height: auto;  width:80%;");

//        Pattern pattern2 = Pattern.compile("<h3>{1}");
//        Matcher matcher2 = pattern2.matcher(result1);
//
//        String result2 = matcher2.replaceAll("<h3 style=\"text-indent: 30px;\n" +
//                "    font-size: 20px;\n" +
//                "    line-height: 30px;\n" +
//                "    color: #EE8262;\">");

        return result1;
    }




    @RequestMapping(value = "/changeAllContent",method = RequestMethod.GET)
    public int changeAllContent(){
        int num=0;
        List<Article> articleList = articleDao.getAll();
        for(Article article:articleList){
            int id = Integer.parseInt(article.getId());
            String contentPast = article.getContent();

            Pattern pattern1 = Pattern.compile("<img{1}");
            Matcher matcher1 = pattern1.matcher(contentPast);

            String result1 = matcher1.replaceAll("<img  style=\"    margin: 0 auto; display: block;\"");

            Pattern pattern2 = Pattern.compile("<h3>{1}");
            Matcher matcher2 = pattern2.matcher(result1);

            String result2 = matcher2.replaceAll("<h3 style=\"text-indent: 30px;\n" +
                    "    font-size: 20px;\n" +
                    "    line-height: 30px;\n" +
                    "    color: #EE8262;\">");
            num+= articleDao.updateContentById(id,result2);

        }

        return num;
    }


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

        //修改每篇文章的content
        for(Article article:articles){
            String contentAfter = addStyleForContent(article.getContent());
            article.setContent(contentAfter);
        }

        return articles;
    }
}
