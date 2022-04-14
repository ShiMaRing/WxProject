package com.xgs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author LENOVO
 */
@Repository
@Mapper
@TableName("subclass")
@Data
public class Subclass {

  @TableField("subclassName")
  String subclassName;
  @TableField("general_pid")
  String generalPid;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGeneralName() {
    return generalName;
  }

  public void setGeneralName(String generalName) {
    this.generalName = generalName;
  }

  public String getGeneralPid() {
    return generalPid;
  }

  public void setGeneralPid(String generalPid) {
    this.generalPid = generalPid;
  }
}
