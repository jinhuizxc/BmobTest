package com.jh.bmobtest.other;


import cn.bmob.v3.BmobObject;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/5/17 15:53
 */
public class Person extends BmobObject {
          private String name;
          private String address;

          public String getName() {
                    return name;
          }
          public void setName(String name) {
                    this.name = name;
          }
          public String getAddress() {
                    return address;
          }
          public void setAddress(String address) {
                    this.address = address;
          }
}
