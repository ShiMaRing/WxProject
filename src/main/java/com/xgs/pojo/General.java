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
}
