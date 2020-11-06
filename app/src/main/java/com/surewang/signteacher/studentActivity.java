package com.surewang.signteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.surewang.signteacher.Adapter.studentAdapter;
import com.surewang.signteacher.entity.User;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class studentActivity extends AppCompatActivity {
    private ListView listView;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    dealDate((String) msg.obj);

            }
        }
    };

    private void dealDate(String  message) {
        Gson gson = new Gson();
        List<User> studentList= gson.fromJson(message, new TypeToken<List<User>>(){}.getType());
        studentAdapter studentAdapter = new studentAdapter(this,studentList);
        listView.setAdapter(studentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.d("点击","点击了");
//                Intent intent = new Intent(studentActivity.this,studentActivity.class);
//                TextView classID = (TextView) arg1.findViewById(R.id.classId);
//                String classId = classID.getText().toString();
//                intent.putExtra("classId",classId);
//                startActivity(intent);
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = findViewById(R.id.toolbar_student);
        setSupportActionBar(toolbar);
//悬浮的按钮
        FloatingActionButton fab = findViewById(R.id.fab_student);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        listView = findViewById(R.id.allStudent);
        String classId = getIntent().getStringExtra("classId");
        getStudentData(classId);
    }

    private void getStudentData(String classId ){

        new Thread(){
            @Override
            public void run() {
                String message= null;
                try{
                    final OkHttpClient client = new OkHttpClient();
                    String url = "http://10.0.2.2:8081/user/AllStudents";
                    RequestBody formBody = new FormBody.Builder().add("classId",classId).build();
                    Request request = new Request.Builder().post(formBody).url(url).build();
                    Response response = client.newCall((request)).execute();
                    if(response.isSuccessful()) {
                        message = response.body().string();
                        Message message1 = new Message();
                        message1.what = 1;
                        message1.obj = message;
                        handler.sendMessage(message1);
                        Log.d("学生信息请求成功",message);
                    }else
                        Log.d("学生信息请求失败","fall:");
                }catch (Exception e){
                    e.printStackTrace();
                }

//                if(message.equals("true"))
//                    handler.sendEmptyMessage(MSG_SUCCESS);
//                else
//                    handler.sendEmptyMessage(MSG_FALL);
            }
        }.start();

    }
}