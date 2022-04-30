package com.xgs.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("wiki")
public class Wiki {
  int typeId;
  //文章标题
  String title;
  //内容是否为空
  boolean blank;
  //文章介绍
  String desc;

  //文章内容,带html富文本
  String content;

  //文章标题
  String img;

  String url;

  @Override
  public String toString() {
    return "Wiki{" +
        "title='" + title + '\'' +
        ", blank=" + blank +
        ", desc='" + desc + '\'' +
        ", img='" + img + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
