package com.xgs.pojo;

import lombok.Data;

@Data
public class WechatLoginRequest {


  //  @NotNull(message = "code不能为空")
//  @ApiModelProperty(value = "微信code", required = true)
  private String code;
  //  @ApiModelProperty(value = "用户非敏感字段")
  private String rawData;

}