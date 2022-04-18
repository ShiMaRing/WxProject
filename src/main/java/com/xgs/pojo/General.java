package com.xgs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LENOVO
 * 保存大类的信息用来查询小类
 */
@Data
@TableName("general")
public class General {
  @TableField("general_name")
  String generalName;
  @TableField("pid")
  String pid;

  public String getGeneralName() {
    return generalName;
  }

  public void setGeneralName(String generalName) {
    this.generalName = generalName;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }
}
