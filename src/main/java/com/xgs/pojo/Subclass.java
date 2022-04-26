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

  public String getSubclassName() {
    return subclassName;
  }

  public void setSubclassName(String subclassName) {
    this.subclassName = subclassName;
  }

  public String getGeneralPid() {
    return generalPid;
  }

  public void setGeneralPid(String generalPid) {
    this.generalPid = generalPid;
  }
}
