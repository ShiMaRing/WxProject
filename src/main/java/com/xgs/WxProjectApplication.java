package com.xgs;

import com.xgs.controller.ArticleController;
import com.xgs.service.impl.ArticleServieceImp;
import com.xgs.util.DataWriter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xgs.dao")
public class WxProjectApplication {


  public static void main(String[] args) {
    SpringApplication.run(WxProjectApplication.class, args);


  }

}
