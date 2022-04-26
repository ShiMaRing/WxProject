package com.xgs.pojo;

import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class GraphData {
  /*
    需要存储的有
    是否有数据
    查询的时间范围
    纵轴坐标
    横轴坐标
    最高价格，最低价格
    最高价格和最低价格发生的地点
    最高价格和最低价格发生的时间
   */

  boolean isBlank;
  String timeRange;
  String[] x;
  double[] y;
  String maxPrice;
  String minPrice;
  String maxPlace, minPlace, maxTime, minTime;

  public boolean isBlank() {
    return isBlank;
  }

  public void setBlank(boolean blank) {
    isBlank = blank;
  }

  public String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public String[] getX() {
    return x;
  }

  public void setX(String[] x) {
    this.x = x;
  }

  public double[] getY() {
    return y;
  }

  public void setY(double[] y) {
    this.y = y;
  }

  public String getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(String maxPrice) {
    this.maxPrice = maxPrice;
  }

  public String getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(String minPrice) {
    this.minPrice = minPrice;
  }

  public String getMaxPlace() {
    return maxPlace;
  }

  public void setMaxPlace(String maxPlace) {
    this.maxPlace = maxPlace;
  }

  public String getMinPlace() {
    return minPlace;
  }

  public void setMinPlace(String minPlace) {
    this.minPlace = minPlace;
  }

  public String getMaxTime() {
    return maxTime;
  }

  public void setMaxTime(String maxTime) {
    this.maxTime = maxTime;
  }

  public String getMinTime() {
    return minTime;
  }

  public void setMinTime(String minTime) {
    this.minTime = minTime;
  }

  @Override
  public String toString() {
    return "GraphData{" +
        "isBlank=" + isBlank +
        ", timeRange='" + timeRange + '\'' +
        ", x=" + Arrays.toString(x) +
        ", y=" + Arrays.toString(y) +
        ", maxPrice='" + maxPrice + '\'' +
        ", minPrice='" + minPrice + '\'' +
        ", maxPlace='" + maxPlace + '\'' +
        ", minPlace='" + minPlace + '\'' +
        ", maxTime='" + maxTime + '\'' +
        ", minTime='" + minTime + '\'' +
        '}';
  }
}
