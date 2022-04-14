package com.xgs.pojo;

import lombok.Data;

/**
 *存储查询到的价格数据
 */

@Data
public class Price {
  //需要存储的有名字以及价格，时间，周期
  String name;
  double price;
  String date;
  String type;
}
