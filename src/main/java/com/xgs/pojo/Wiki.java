package com.xgs.pojo;

import lombok.Data;

@Data
public class Wiki {
  //文章标题
  String title;

  //文章介绍
  String desc;

  //文章内容,带html富文本
  String content;

  //文章标题
  String img;

}
