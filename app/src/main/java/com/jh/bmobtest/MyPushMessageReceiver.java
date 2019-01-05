package com.jh.bmobtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2019/1/5.
 *
 * //TODO 集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String message = "";
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String msg = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context, "客户端收到推送内容："+ msg, Toast.LENGTH_SHORT).show();

            // 解析数据
            JSONTokener jsonTokener = new JSONTokener(msg);
            try {
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                message = jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 可以设置通知，通知栏的知识需要再补充！
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(R.mipmap.ic_launcher,
                    "Bmob test", System.currentTimeMillis());
            notificationManager.notify(1, notification);
        }
    }
}
