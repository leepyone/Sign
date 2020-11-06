package com.surewang.signteacher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surewang.signteacher.Adapter.classAdapter;
import com.surewang.signteacher.entity.classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

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
        List<classes> classList= gson.fromJson(message, new TypeToken<List<classes>>(){}.getType());
        classAdapter classAdapter = new classAdapter(this,classList);
        listView.setAdapter(classAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.d("点击","点击了");
                Intent intent = new Intent(HomeActivity.this,studentActivity.class);
                TextView classID = (TextView) arg1.findViewById(R.id.classId);
                String classId = classID.getText().toString();
                intent.putExtra("classId",classId);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//悬浮的按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        listView = findViewById(R.id.allClass);
        String account = getIntent().getStringExtra("account");
//        System.out.println(account);
//        Log.d("intent account ",account);
        getClassData(account);
//        classAdapter classAdapter = new classAdapter(this,listItem);
//        listView.setAdapter(classAdapter);


    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(HomeActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(HomeActivity.this);
        inputDialog.setTitle("请输入班级名称！").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this,
                               "创建成功！",
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private ArrayList<HashMap<String,Object>> getClassData(String account ){
        ArrayList<HashMap<String,Object>> listItem = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//        String account = sharedPreferences.getString("account","0");
//        Log.d("account",account);
        new Thread(){
            @Override
            public void run() {
                String message= null;
                try{
                    final OkHttpClient client = new OkHttpClient();
                    String url = "http://10.0.2.2:8081/class/allClass";
                    RequestBody formBody = new FormBody.Builder().add("account",account).build();
                    Request request = new Request.Builder().post(formBody).url(url).build();
                    Response response = client.newCall((request)).execute();
                    if(response.isSuccessful()) {
                        message = response.body().string();
                        Message message1 = new Message();
                        message1.what = 1;
                        message1.obj = message;
                        handler.sendMessage(message1);
                        Log.d("是否请求成功",message);
                    }else
                        Log.d("是否请求成功","fall:");
                }catch (Exception e){
                    e.printStackTrace();
                }

//                if(message.equals("true"))
//                    handler.sendEmptyMessage(MSG_SUCCESS);
//                else
//                    handler.sendEmptyMessage(MSG_FALL);
            }
        }.start();

        return  listItem;
    }
}