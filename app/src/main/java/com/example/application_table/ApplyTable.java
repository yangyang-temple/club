package com.example.application_table;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.application_table.Util.SharedPreferencesUtil;
import com.leaf.library.StatusBarUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;

public class ApplyTable extends AppCompatActivity implements View.OnClickListener {

    Button button;
    //标题栏控件
    Toolbar toolbar;
    ActionBar actionBar;

    private EditText etName;
    private RadioGroup sexChoose=null;
    private RadioButton male=null;
    private RadioButton female=null;
    private TextView tvBirthTime;
    private TextView tvClass;
    private TimePickerView pvTime;//出生日期
    private ImageView imageHead;//显示头像的控件

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    private ArrayList<ClazzData> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    //默认选中项（第一次为0,0,0）
    private int select1, select2, select3;

    //需要上传到后台的数据
    private  String sex = "";
    private String name = "";
    private String birth = "";
    private String clazz = "";
    private String headImage = "";
    // BmobFile file = new BmobFile(new File(headImage));
    //private Map<String ,String> map = new HashMap<>();

    private Bitmap bitmap;


    /**
     * 重新定义了标题栏:"下一步"
     * @param menu:自定义的标题栏
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return true;
    }

    /**
     * 如果需要监听标题栏上的控件，重写onOptionsItemSelected方法
     * @param item:标题栏上的控件
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_next://下一步
                //获取输入的姓名
                name = etName.getText().toString();
//                Intent intent1 = new Intent("com.example.application_table.user_info");
//                intent1.putExtra("sex",sex);
//                intent1.putExtra("name",name);
//                intent1.putExtra("birth",birth);
//                intent1.putExtra("clazz",clazz);
//               intent1.setComponent(new ComponentName("com.example.application_table","com.example.application_table.Self"));
//                getApplication().sendBroadcast(intent1);
                //跳转到下一个界面
                Intent intent = new Intent(ApplyTable.this,ApplicationTable2.class);
                Constant.name = name;
                Constant.sex = sex;
                Constant.birth = birth;
                Constant.clazz = clazz;
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_table);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTable.this,ScrollBanner.class);
                startActivity(intent);
            }
        });

        Bmob.initialize(this, "9a2acdeeb4019669e12e1226b8ffb45d");
        SharedPreferencesUtil.getInstance(this, "clazz");//选择班级的表

        etName = findViewById(R.id.et_name);//输入姓名的控件
        tvBirthTime  =findViewById(R.id.tv_birth_time);
        tvClass = findViewById(R.id.tv_class);


        //自定义标题栏
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        StatusBarUtil.setGradientColor(this, toolbar);//设置沉浸式状态栏

        //头像控件
        imageHead = findViewById(R.id.image_head);
        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog(v);
            }
        });


//        //HomeAsUp按钮
//        ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setHomeButtonEnabled(true);
//        mActionBar.setDisplayHomeAsUpEnabled(true);

        //性别选择按钮
        sexChoose=findViewById(R.id.sex);
        female=findViewById(R.id.female);
        male = findViewById(R.id.male);

        sexChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.female:
                        sex = "女";
                    case R.id.male:
                        sex = "男";
                        break;
                    default:
                        break;
                }
            }
        });

        //出生日期选择
        BirthTimeChoose();

        /**
         * 显示班级
         */
        tvClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClazzChoose();
            }
        });

        /**
         * 显示出生日期
         */
        tvBirthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvTime != null){
                    //弹出时间选择器，传递参数过去，回调的时候可以选择绑定此view
                    pvTime.show(v);
                }
            }
        });
    }


    /**
     * 出生日期滚轮选择器
     */
    public void BirthTimeChoose(){
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvBirthTime.setText(getTime(date));
                birth = getTime(date);
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

            }
        }).setType(new boolean[]{true,true,true,false,false,false})
                .isDialog(true)
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLabel("年","月","日","","","")
                .build();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
            params.leftMargin = 0 ;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null){
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }
    private String getTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    /**
     * 返回上一个界面
     * HomeAsUp
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 班级选择器
     * 数据从Clazz表中获取
     * 表中有多条数据：每条数据包含list(专业),每个专业下包含多条list(班级)
     * list存储学院(key:clazzData)
     * 根据(String clazzData)查找表中的数据(<ClazzData.MajorBean> majorBeans)--->将得到的majorBeans添加到相应的学院下(clazzData)
     * 每个学院下使用list存储专业(key:major)
     * 根据专业名(majorName)查询班级
     * 每个专业下使用list存储班级
     */
    public void ClazzChoose(){

        //学院
        ArrayList<ClazzData> list = new ArrayList<>();
        list = null;
        //sp中获取学院名称，并添加到list中
        //下一步：为list中每个元素添加专业
        list.addAll(SharedPreferencesUtil.getListData("clazzData",ClazzData.class));
        ArrayList<ClazzData.MajorBean> majorBeans = new ArrayList<>();
        majorBeans = null;
        String clazzData = "";
        for (int i=0;i<list.size();i++){
            clazzData = list.get(i).getName();
            //根据学院名称clazz查找数据库中
            majorBeans.addAll(SharedPreferencesUtil.getListData(clazzData,ClazzData.MajorBean.class));
            list.get(i).setMajor(majorBeans);
        }
        String major = "";
        for (int i=0;i<list.size();i++){//遍历学院
            List<ClazzData.MajorBean> majorList = list.get(i).getMajor();
            for (int j=0;j<majorList.size();j++){//遍历第i个学院下的专业
                //正在查询的第j个专业名字
                String majorName = majorList.get(j).getName();
                List<String> clazz = new ArrayList<>();
                //此专业下的班级列表
                clazz.addAll(SharedPreferencesUtil.getListData(majorName,String.class));
                //将班级添加到此专业下
                majorList.get(j).setClazz(clazz);
            }
        }
//        //添加3个学院
//        ClazzData data1 = new ClazzData();
//        ClazzData data2 = new ClazzData();
//        ClazzData data3 = new ClazzData();
//        data1.setName("电子与信息工程学院");
//        data2.setName("经管学院");
//        data3.setName("土木学院");
//        //添加专业（学院下面的数据）
//        ArrayList<ClazzData.MajorBean> majorBeans1 = new ArrayList<>();
//        ArrayList<ClazzData.MajorBean> majorBeans2 = new ArrayList<>();
//        ArrayList<ClazzData.MajorBean> majorBeans3 = new ArrayList<>();
//        //第一个学院下面的专业数据
//        ClazzData.MajorBean majorBean1 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean2 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean3 = new ClazzData.MajorBean();
//        majorBean1.setName("物联网");
//        majorBean2.setName("计算机科学与技术");
//        majorBean3.setName("网络工程");
//        majorBeans1.add(majorBean1);
//        majorBeans1.add(majorBean2);
//        majorBeans1.add(majorBean3);
//        //第二个学院下面的专业数据
//        ClazzData.MajorBean majorBean4 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean5 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean6 = new ClazzData.MajorBean();
//        majorBean4.setName("A");
//        majorBean5.setName("B");
//        majorBean6.setName("C");
//        majorBeans2.add(majorBean4);
//        majorBeans2.add(majorBean5);
//        majorBeans2.add(majorBean6);
//        //第三个学院下面的专业数据
//        ClazzData.MajorBean majorBean7 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean8 = new ClazzData.MajorBean();
//        ClazzData.MajorBean majorBean9 = new ClazzData.MajorBean();
//        majorBean7.setName("1");
//        majorBean8.setName("2");
//        majorBean9.setName("3");
//        majorBeans3.add(majorBean7);
//        majorBeans3.add(majorBean8);
//        majorBeans3.add(majorBean9);
//        //为专业添加班级
//        ArrayList<String> clazz1 = new ArrayList<>();
//        ArrayList<String> clazz2 = new ArrayList<>();
//        ArrayList<String> clazz3 = new ArrayList<>();
//        ArrayList<String> clazz4 = new ArrayList<>();
//        ArrayList<String> clazz5 = new ArrayList<>();
//        ArrayList<String> clazz6 = new ArrayList<>();
//        ArrayList<String> clazz7 = new ArrayList<>();
//        ArrayList<String> clazz8 = new ArrayList<>();
//        ArrayList<String> clazz9 = new ArrayList<>();
//        clazz1.add("1班");
//        clazz1.add("2班");
//        clazz1.add("3班");
//        clazz2.add("1班");
//        clazz2.add("2班");
//        clazz2.add("3班");
//        clazz3.add("1班");
//        clazz3.add("2班");
//        clazz3.add("3班");
//        clazz4.add("1班");
//        clazz4.add("2班");
//        clazz4.add("3班");
//        clazz5.add("1班");
//        clazz5.add("2班");
//        clazz5.add("3班");
//        clazz6.add("1班");
//        clazz6.add("2班");
//        clazz6.add("3班");
//        clazz7.add("1班");
//        clazz7.add("2班");
//        clazz7.add("3班");
//        clazz8.add("1班");
//        clazz8.add("2班");
//        clazz8.add("3班");
//        clazz9.add("1班");
//        clazz9.add("2班");
//        clazz9.add("3班");
//
//        //将班级添加到专业中
//        majorBean1.setClazz(clazz1);
//        majorBean2.setClazz(clazz2);
//        majorBean3.setClazz(clazz3);
//        majorBean4.setClazz(clazz4);
//        majorBean5.setClazz(clazz5);
//        majorBean6.setClazz(clazz6);
//        majorBean7.setClazz(clazz7);
//        majorBean8.setClazz(clazz8);
//        majorBean9.setClazz(clazz9);
//        //将专业添加到学院下
//        data1.setMajor(majorBeans1);
//        data2.setMajor(majorBeans2);
//        data3.setMajor(majorBeans3);
//
//        list.add(data1);
//        list.add(data2);
//        list.add(data3);

        options1Items = list;
        for (int i = 0; i < list.size(); i++) {//遍历学院
            ArrayList<String> MajorList = new ArrayList<>();//该学院的专业列表（第二级）
            ArrayList<ArrayList<String>> Major_ClazzList = new ArrayList<>();//该学院的所有班级列表（第三级）
            for (int c = 0; c < list.get(i).getMajor().size(); c++) {//遍历该学院的所有专业
                String MajorName = list.get(i).getMajor().get(c).getName();
                MajorList.add(MajorName);//添加专业
                ArrayList<String> ClazzList = new ArrayList<>();//该专业的所有班级列表
                //如果无班级数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (list.get(i).getMajor().get(c).getClazz() == null
                        || list.get(i).getMajor().get(c).getClazz().size() == 0) {
                    ClazzList.add("");
                } else {
                    ClazzList.addAll(list.get(i).getMajor().get(c).getClazz());
                }
                Major_ClazzList.add(ClazzList);//添加该省所有地区数据
            }
            options2Items.add(MajorList);
            options3Items.add(Major_ClazzList);
        }

        new OptionsPicker(this, select1, select2, select3,
                options1Items, options2Items, options3Items,
                new OptionsPicker.OnPickerOptionsClickListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View view) {
                        //返回的分别是三个级别的选中位置
                        String string = options1Items.get(options1).getPickerViewText() +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3);
                        tvClass.setText(string);
                        clazz = string;
                        //将选择后的选中项赋值
                        select1 = options1;
                        select2 = options2;
                        select3 = options3;
                    }
                }).show();
    }


    @Override
    public void onClick(View v) {

    }
    //https://blog.csdn.net/ydxlt/article/details/48024017 ：修改头像
    /**
     * 显示修改头像的对话框
     */
    public void showChoosePicDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;

                }
            }
        });
        builder.create().show();
    }

    /**
     * 拍照功能
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(ApplyTable.this, "com.example.application_table.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 修改头像回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            //  Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //  Log.d(TAG,"setImageToView:"+photo);
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            imageHead.setImageBitmap(photo);
            uploadPic(photo);
        }else {
            bitmap = BitmapFactory.decodeResource(this.getApplicationContext().getResources(),R.drawable.bitmap);
            uploadPic(bitmap);
            //itmap=BitmapFactory.decodeResource(this.getContext().getResources()
        }
    }

    /**
     * 将更换的头像上传至服务器
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        Log.d("2222222222222222", "uploadPic: "+bitmap.getDensity());
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));//存入sd卡
        //将Bitmap转为File
        File file1=new File(imagePath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将File转为URI
        URI uri = file1.toURI();
        try {
            //将URI转为URL
            URL url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BmobFile file = new BmobFile(file1);
        Log.d("TAG", "uploadPic: 头像图片"+file);
        Log.d("rty", "uploadPic: 头像图片"+file.equals(""));
        Constant.file = file;

        // headImage = imagePath;
        //   Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
            //  Log.d(TAG,"imagePath:"+imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }



}
