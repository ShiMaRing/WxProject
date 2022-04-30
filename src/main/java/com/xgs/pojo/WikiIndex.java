package com.xgs.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class WikiIndex {
  String title;
  Map<String,String> map;
}
