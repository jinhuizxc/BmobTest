package com.jh.bmobtest;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jh.bmobtest.other.Person;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 创建Android后端服务之Bmob
 * https://www.imooc.com/learn/254
 * <p>
 * APP ID: 0eddf0a9f2b5c0c88931962b58032711
 * <p>
 * <p>
 * 如果你使用Android Studio来进行基于BmobSDK的项目开发，有两种方式：
 * (地址: http://doc.bmob.cn/data/android/)
 * <p>
 * 自动导入(推荐)
 * 手动导入
 *
 * bmob初始化报错
 * 解决方案: https://blog.csdn.net/itworkermk/article/details/52238631
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText et_name, et_feedBack, et_query;
    private Button bt_submit, bt_query, bt_queryOne, bt_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Bmob
        Bmob.initialize(this, "0eddf0a9f2b5c0c88931962b58032711");
        // 使用推送服务时的初始化操作
        /**
         * 获取设备唯一标志：
         * BmobInstallationManager.getInstallationId();
         * 获取当前设备信息：
         * BmobInstallationManager.getInstance().getCurrentInstallation();
         */
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Log.e(TAG,bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);

        et_name = findViewById(R.id.et_name);
        et_feedBack = findViewById(R.id.et_feedBack);
        bt_submit = findViewById(R.id.bt_submit);
        bt_query = findViewById(R.id.bt_query);
        et_query = findViewById(R.id.et_query);
        bt_queryOne = findViewById(R.id.bt_queryOne);
        bt_push = findViewById(R.id.bt_push);

        // 提交数据
        submitData();
       // 查询数据
        queryData();
        // 查询单个数据
        queryOneData();
        // 消息推送
        pushMsg();


        // 其他
        other();

    }

    private void other() {
        //第一：默认初始化
//        Bmob.initialize(this, "0eddf0a9f2b5c0c88931962b58032711");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("0eddf0a9f2b5c0c88931962b58032711")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        adddata();
    }

    //添加一条数据
    private void adddata() {
            Person p2 = new Person();
            p2.setName("lucky");
            p2.setAddress("北京海淀");
            p2.save(new SaveListener<String>() {
                @Override
                public void done(String objectId,BmobException e) {
                    if(e==null){
                        Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为："+ objectId, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    private void pushMsg() {
        bt_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BmobPushManager manager = new BmobPushManager();
//                manager.pushMessageAll("test");
                BmobPushManager bmobPushManager = new BmobPushManager();
                bmobPushManager.pushMessageAll("消息内容", new PushListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(MainActivity.this, "推送成功！", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void queryOneData() {
        bt_queryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query_ = et_query.getText().toString();
                if (query_.equals("")) {
                    Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobQuery<FeedBack> query = new BmobQuery<FeedBack>();
                query.addWhereEqualTo("name", query_);  // 增加条件查询
                query.findObjects(new FindListener<FeedBack>() {
                    @Override
                    public void done(List<FeedBack> list, BmobException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Query");
                        StringBuilder string = new StringBuilder();
                        for (FeedBack feedBack: list) {
                            string.append(feedBack.getName()).append(", ").append(feedBack.getFeedBack()).append("\n");
                        }
                        builder.setMessage(string.toString());
                        builder.create().show();
                    }
                });
            }
        });
    }

    private void queryData() {
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<FeedBack> query = new BmobQuery<FeedBack>();
                query.findObjects(new FindListener<FeedBack>() {
                    @Override
                    public void done(List<FeedBack> list, BmobException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Query");
                        StringBuilder string = new StringBuilder();
                        for (FeedBack feedBack: list) {
                            string.append(feedBack.getName()).append(", ").append(feedBack.getFeedBack()).append("\n");
                        }
                        builder.setMessage(string.toString());
                        builder.create().show();
                    }
                });
            }
        });
    }

    private void submitData() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交数据
                String name = et_name.getText().toString();
                String feedback = et_feedBack.getText().toString();
                if (name.equals("") && feedback.equals("")) {
                    Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                FeedBack feedBackObj = new FeedBack();
                feedBackObj.setName(name);
                feedBackObj.setFeedBack(feedback);
                /**
                 * //新版的bmob，save方法不用传Context
                 * feedBackObject.save(new SaveListener<String>() {
                 * @Override
                 * public void done(String s, BmobException e) {
                 * Toast.makeText(MainActivity.this,"done",Toast.LENGTH_LONG).show();
                 * }
                 * });
                 */
                feedBackObj.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
