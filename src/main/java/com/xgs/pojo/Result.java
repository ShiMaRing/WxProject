package com.xgs.pojo;

import lombok.Data;

@Data
/*
封装response
*/
public class Result {
  boolean flag;
  int statusCode;
  Object data;
  String desc;
}
