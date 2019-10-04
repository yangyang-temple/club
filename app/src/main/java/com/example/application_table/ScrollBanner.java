package com.example.application_table;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zt.simplebanner.OnBannerClickListener;
import com.zt.simplebanner.scroll.ScrollBannerPointView;

import java.util.ArrayList;
import java.util.List;

public class ScrollBanner extends AppCompatActivity implements OnBannerClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollbanner);

        ScrollBannerView scrollBannerView = findViewById(R.id.scroll_banner_view);
        scrollBannerView.bindData(getData());
        scrollBannerView.setOnBannerClickListener(this);

        ScrollBannerPointView pointView = findViewById(R.id.scroll_banner_point);

        pointView.setScrollBannerView(scrollBannerView);

    }

    private List<String> getData(){
        List<String> list = new ArrayList<>();
        for (int i=0;i<5;i++){
            list.add(i+ "2");
        }
        return list;
    }

    @Override
    public void onBannerClick(int position) {
        Toast.makeText(this, "clicked"+(position+1), Toast.LENGTH_SHORT).show();
    }
}
