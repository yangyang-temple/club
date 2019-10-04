package com.example.application_table;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application_table.entity.User_Apply;
import com.leaf.library.StatusBarUtil;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Self extends AppCompatActivity implements View.OnClickListener {
    private int j=0;
    //标题栏控件
    private Toolbar toolbar;
    private ActionBar actionBar;


    private EditText etSelf;//自我介绍编辑框
    private String self;//自我介绍
    private TextView tvRefresh;//"刷新"：添加点击事件，更新显示他人自我介绍
    private ImageView imgEye;//是否显示他人自我介绍的开关——"眼睛"图标->默认状态为打开
    private static final int OPEN = 1; //眼睛控制开关打开-显示
    private static final int CLOSE = 0;//眼睛控制开关关闭-不显示
    private int STATE = OPEN;//眼睛开关默认为打开

    private ImageView imgOther;
    private TextView tvOtherName;
    private TextView tvOtherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        Bmob.initialize(this, "9a2acdeeb4019669e12e1226b8ffb45d");

        etSelf = findViewById(R.id.et_self);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//自定义标题栏
        StatusBarUtil.setGradientColor(this, toolbar);//设置沉浸式状态栏
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);//返回键
        tvRefresh = findViewById(R.id.tv_refresh);//刷新
        imgEye = findViewById(R.id.img_eye);//眼睛控制开关

        tvRefresh.setOnClickListener(this);
        imgEye.setOnClickListener(this);

        //他人的自我介绍控件
        imgOther = findViewById(R.id.other_image);
        tvOtherName = findViewById(R.id.other_name);
        tvOtherInfo = findViewById(R.id.other_self);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.color.aa);
        imgOther.setImageDrawable(rectRoundBitmap(bitmap));


        //别人的自我介绍相关控件初始化
        findSelfInfo();//默认加载一个他人的自我介绍


}


    /**
     * 重新定义了标题栏:"完成"
     * @param menu:自定义的标题栏
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_success,menu);
        return true;
    }

    /**
     * 点击"完成"，上传个人申请数据到后台
     * @param item:标题栏上的控件
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_next://下一步
                //接受上一个页面的部门信息和手机号码
                Intent intent = getIntent();
                String sector = intent.getStringExtra("sector");
                Log.d("TAG", "sector "+sector);
                String phoneNum = intent.getStringExtra("phoneNum");
                //得到"自我介绍"字符串内容
                self = etSelf.getText().toString();
            //将 数据上传到服务端
                final User_Apply user = new User_Apply();
                user.setHeadImage(Constant.file);
                user.setSelf(self);
                user.setSector(sector);
                user.setPhoneNum(phoneNum);
                user.setName(Constant.name);
                user.setSex(Constant.sex);
                user.setClazz(Constant.clazz);
                user.setBirth(Constant.birth);
                user.setClub(Constant.club);
                // user.setHeadImage(file);
                user.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Toast.makeText(Self.this, "添加成功"+s, Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "done: "+s);
                            Constant.s = s;
                        }else {
                            Toast.makeText(Self.this, "失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                while (Constant.file != null) {
                    Constant.file.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                user.setHeadImage(Constant.file);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Log.d("picture", "图片上传成功");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击"刷新",调用一次此方法，循环调用
    //查询用户的所以信息，提取需要的信息
    public void findSelfInfo(){
        BmobQuery<User_Apply> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<User_Apply>() {
            @Override
            public void done(List<User_Apply> list, BmobException e) {
                if (e == null) {
                        User_Apply userApply = new User_Apply();
                        userApply.setName(list.get(j).getName());
                        tvOtherName.setText(userApply.getName());
                        Log.d("TAG", "username"+userApply.getName());
                        userApply.setSelf(list.get(j).getSelf());
                        tvOtherInfo.setText(userApply.getSelf());
                        j++;
                }else {
                    Log.d("error", "done: 查询失败"+e.getMessage());
                }
            }
        });
    }


    @Override

    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_refresh://刷新按钮
                    findSelfInfo();//查询他人的自我介绍并显示
                    Log.d("TAG", "onClick: 点击刷新");
                    //adapter.notifyDataSetChanged();//刷新页面
                   // mList.removeAll(mList);
                    break;
                case R.id.img_eye://眼睛控制开关
                    Log.d("******", "onClick: 点击小眼睛");
                    if (STATE == OPEN){//此时"刷新"不可见,RecyclerView不可见
                        STATE = CLOSE;
                        tvRefresh.setVisibility(View.GONE);
                        imgOther.setVisibility(View.GONE);
                        tvOtherName.setVisibility(View.GONE);
                        tvOtherInfo.setVisibility(View.GONE);
                        imgEye.setImageResource(R.mipmap.eye_close_512px);
                    }else if (STATE == CLOSE){//打开眼睛，"刷新"可见
                        STATE = OPEN;
                        tvRefresh.setVisibility(View.VISIBLE);
                        imgOther.setVisibility(View.VISIBLE);
                        tvOtherName.setVisibility(View.VISIBLE);
                        tvOtherInfo.setVisibility(View.VISIBLE);
                        imgEye.setImageResource(R.mipmap.view_off);
                    }
                    break;
            }
    }

    private RoundedBitmapDrawable rectRoundBitmap(Bitmap bitmap){
//创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//抗锯齿
        roundImg.setAntiAlias(true);
//设置圆角半径
        roundImg.setCornerRadius(100);
        return roundImg;
    }
}

/**
 * 问题：点击刷新时，系统认为我点击了小眼睛和刷新
 */

/**
 *目标：每次只显示一条数据  --> 尝试解决：点击"刷新"以后，更新界面，adapter置零 ---> 放弃使用RecycleView,重新定义布局
 * 点击刷新时，不误认为点击了小眼睛(已解决)----> 添加break；
 * 每次点击刷新后刷新页面(解决) ---> 283行代码
 */
