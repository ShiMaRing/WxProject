package com.xgs.controller;


import com.xgs.dao.dataDao.GeneralDao;
import com.xgs.dao.dataDao.ProvinceDao;
import com.xgs.dao.dataDao.SubclassDao;
import com.xgs.pojo.General;
import com.xgs.pojo.GraphData;
import com.xgs.pojo.Province;
import com.xgs.pojo.Subclass;
import com.xgs.spider.DataSpider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

  /**
   * 该方法用来获取所有的大类
   */
  @Autowired
  GeneralDao generalDao;
  @Autowired
  ProvinceDao provinceDao;
  @Autowired
  SubclassDao subclassDao;
  @Autowired
  DataSpider dataSpider;

  //获取所有的大类
  @RequestMapping(value = "/getGeneral", method = RequestMethod.GET)
  public List<General> getGeneral() {
    List<General> all = generalDao.findAll();
    return all;
  }

  //传入大类的名字获取小类
  @RequestMapping(value = "/getSubclass/{name}", method = RequestMethod.GET)
  public List<Subclass> getSubclass(@PathVariable("name") String name) {
    return subclassDao.findByName(name);
  }

  //获取所有的省份名字
  @RequestMapping(value = "/getProvince", method = RequestMethod.GET)
  public List<Province> getAllProvince() {
    return provinceDao.findAll();
  }

  //提供全国范围的查询接口
  @RequestMapping(value = "/searchNation/{name}/{cycle}", method = RequestMethod.GET)
  public GraphData getNationData(@PathVariable("name") String name,
      @PathVariable("cycle") String cycle) {
    GraphData countryPrice = dataSpider.getCountryPrice(name, cycle);
    return countryPrice;
  }

  @RequestMapping(value = "/searchProvince/{proName}/{name}/{cycle}", method = RequestMethod.GET)
  public GraphData getProvinceData(@PathVariable("proName") String proName,
      @PathVariable("name") String name, @PathVariable("cycle") String cycle) {
    String id = provinceDao.getId(proName);
    return dataSpider.getProvincePrice(name, id, cycle);
  }
}
