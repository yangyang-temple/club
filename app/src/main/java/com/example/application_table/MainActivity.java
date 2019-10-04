package com.example.application_table;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.application_table.adapter.AdapterNewClub;
import com.example.application_table.entity.NewClubBean;
import com.leaf.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 从mainactivity通过广播传到Self的数据接受不到？？？
 * 点击右上角下一步，页面跳转到第二张申请表，数据传输到Self页面
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private Toolbar toolbar;
    private ListView listView;
    //  private AdpNewClub adapter;
    private AdapterNewClub adapter;
    private List<NewClubBean> clubList;
    private List<NewClubBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        tv.setText("所有");
        tv.setOnClickListener(this);
        tv1 = findViewById(R.id.tv1);
        tv1.setText("文化类");
        tv1.setOnClickListener(this);
        tv2 = findViewById(R.id.tv2);
        tv2.setText("公益类");
        tv2.setOnClickListener(this);
        tv3 = findViewById(R.id.tv3);
        tv3.setText("体育类");
        tv3.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//自定义标题栏
        StatusBarUtil.setGradientColor(this, toolbar);//设置沉浸式状态栏
        clubList = new ArrayList<>();
        list = new ArrayList<>();
        NewClubBean bean1 = new NewClubBean("青年志愿者协会","1220名会员","公益类");
        clubList.add(bean1);
        NewClubBean bean2 = new NewClubBean("衿易社团","350名会员","文化类");
        clubList.add(bean2);
        NewClubBean bean3 = new NewClubBean("篮球社团","320名会员","体育类");
        clubList.add(bean3);
        adapter = new AdapterNewClub(MainActivity.this,R.layout.adp_new_club,clubList);
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv:
                adapter = new AdapterNewClub(MainActivity.this,R.layout.adp_new_club,clubList);
                listView.setAdapter(adapter);
                break;
            case R.id.tv1://文化类
                list = new ArrayList<>();
                for (NewClubBean club:clubList){
                    if (club.getType() == tv1.getText()){
                        list.add(club);
                    }
                }
                adapter = new AdapterNewClub(MainActivity.this,R.layout.adp_new_club,list);
                listView.setAdapter(adapter);

                break;
            case R.id.tv2:
                list = new ArrayList<>();
                for (NewClubBean club:clubList){
                    if (club.getType() == tv2.getText()){
                        list.add(club);
                    }
                }
                adapter = new AdapterNewClub(MainActivity.this,R.layout.adp_new_club,list);
                listView.setAdapter(adapter);
                break;
            case R.id.tv3:
                list = new ArrayList<>();
                for (NewClubBean club:clubList){
                    if (club.getType() == tv3.getText()){
                        list.add(club);
                    }
                }
                adapter = new AdapterNewClub(MainActivity.this,R.layout.adp_new_club,list);
                listView.setAdapter(adapter);
                break;
//        }
        }
    }
}
