package com.xgs.pojo;

import lombok.Data;

/*
  @author LENOVO
 */
/**
 * 存储省份名称和id
 *
 */
@Data
public class Province {
  int id;
  String  name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
