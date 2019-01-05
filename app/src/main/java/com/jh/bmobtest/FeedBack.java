package com.jh.bmobtest;

import cn.bmob.v3.BmobObject;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2019/1/5.
 */
public class FeedBack extends BmobObject {

    private String name;
    private String feedBack;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }
}
