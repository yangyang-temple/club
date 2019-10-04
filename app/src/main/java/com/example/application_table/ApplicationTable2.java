package com.example.application_table;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leaf.library.StatusBarUtil;

public class ApplicationTable2 extends AppCompatActivity {
    //标题栏控件
    private Toolbar toolbar;
    private ActionBar actionBar;

    //申请部门编辑控件
    private TextView etSector;
    //手机号码编辑控件
    private TextView etPhone;

    //申请部门字符串
    private String sector;
    //手机号码字符串
    private String phoneNum;


    //TextView"自我介绍"跳转
    private TextView tvSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_table2);

        etSector = findViewById(R.id.et_selector);//申请部门编辑控件
        etPhone = findViewById(R.id.et_phone);//手机号码编辑控件
        tvSelf = findViewById(R.id.tv_self);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置沉浸式状态栏
        StatusBarUtil.setGradientColor(this, toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        /**
         * "请点击此处填写自我介绍"---> Self界面(携带本界面已填写的申请部门和手机号码)
         */
        tvSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationTable2.this,Self.class);
                startActivity(intent);
            }
        });

    }
//    /**
//     * 重新定义了标题栏:"下一步"
//     * @param menu:自定义的标题栏
//     * @return
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbar,menu);
//        return true;
//    }
//
//    /**
//     * 下一步
//     * 如果需要监听标题栏上的控件，重写onOptionsItemSelected方法
//     * @param item:标题栏上的控件
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_next://下一步
//                sector = etSector.getText().toString();//得到申请部门
//                phoneNum = etPhone.getText().toString();//得到手机号码
//                Intent intent = new Intent(ApplicationTable2.this,Self.class);
//                intent.putExtra("sector",sector);
//                intent.putExtra("phoneNum",phoneNum);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
