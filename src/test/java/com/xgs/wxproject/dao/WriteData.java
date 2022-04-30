package com.xgs.wxproject.dao;


import com.xgs.util.DataWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WriteData {

  @Autowired
  DataWriter dataWriter;

  @Test
  public void writeWikiIndex(){
    int count= dataWriter.writeWikiIndex();
    System.out.println(count);
  }

  @Test
  public  void writeWiki(){
    int count = dataWriter.writeWikis();
    System.out.println(count);
  }



}
