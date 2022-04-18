package com.xgs.pojo;

import lombok.Data;

@Data
public class Article {
  String createTime;//文章创建时间
  String id;//文章id
  String ovtitle;//存储标题
  String url;//存储具体文章的url
  String source;//存储信息来源
  String thumbImage;//存储图片路径
  String content;//存储文章内容
  String describe;//大致介绍
  String type;


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOvtitle() {
    return ovtitle;
  }

  public void setOvtitle(String ovtitle) {
    this.ovtitle = ovtitle;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getThumbImage() {
    return thumbImage;
  }

  public void setThumbImage(String thumbImage) {
    this.thumbImage = thumbImage;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

}
