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

  @TableField("subclass_name")
  String name;
  @TableField("general_name")
  String generalName;
  @TableField("general_pid")
  String generalPid;

}
