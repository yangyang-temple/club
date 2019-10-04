package com.example.application_table;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zt.simplebanner.OnBannerClickListener;
import com.zt.simplebanner.scroll.AbsScrollBannerView;

/**
 * scrollview形式的banner的具体实现
 */
public class ScrollBannerView extends AbsScrollBannerView<String> implements OnBannerClickListener {

    private int[] imageId  = {R.drawable.d,R.drawable.c,R.drawable.a,R.drawable.f,R.drawable.g};

    public ScrollBannerView(Context context) {
        super(context);
    }

    public ScrollBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getExView(int postion, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adp_scrollview_item, parent, false);
        TextView titleView = view.findViewById(R.id.title);
        titleView.setText(data.get(postion));
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageResource(imageId[postion]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//填充满布局
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.d);
//        imageView.setImageDrawable(rectRoundBitmap(bitmap));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);


        return view;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    public void onBannerClick(int position) {
        //点击图片，跳转到相应的社团介绍页面
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
