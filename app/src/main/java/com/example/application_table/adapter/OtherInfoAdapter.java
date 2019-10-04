package com.example.application_table.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.application_table.R;
import com.example.application_table.entity.OtherInfoBean;

import java.util.List;


//只显示一条数据，每条数据下的黑线取消，最多显示的行数更改
public class OtherInfoAdapter extends RecyclerView.Adapter<OtherInfoAdapter.MyViewHolder> {

    private Activity mContent;

    private final int MAX_LINE_COUNT = 3;//最大显示行数

    private final int STATE_UNKNOW = -1;//未知状态

    private final int STATE_NOT_OVERFLOW = 1;//文本行数小于最大可显示行数

    private final int STATE_COLLAPSED = 2;//折叠状态

    private final int STATE_EXPANDED = 3;//展开状态

    private SparseArray<Integer> mTextStateList;//保存文本状态集合
    private List<OtherInfoBean> mList;

    @SuppressLint("UseSparseArrays")
    public OtherInfoAdapter(List<OtherInfoBean> list, Activity context) {
        mContent = context;
        this.mList = list;
        mTextStateList = new SparseArray<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(mContent.getLayoutInflater().inflate(R.layout.adapter_self, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        int state = mTextStateList.get(mList.get(i).getId(), STATE_UNKNOW);
        //第一次初始化，未知状态
        if (state == STATE_UNKNOW) {
            myViewHolder.otherInfo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回掉会调用多次，获取完行数后记得注销监听
                    myViewHolder.otherInfo.getViewTreeObserver().removeOnPreDrawListener(this);
                    //myViewHolder.otherInfo.getViewTreeObserver().addOnPreDrawListener(null);
                    //如果内容显示的行数大于最大显示行数
                    if (myViewHolder.otherInfo.getLineCount() > MAX_LINE_COUNT) {
                        myViewHolder.otherInfo.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        myViewHolder.expandOrFold.setVisibility(View.VISIBLE);//显示“全文”
                        myViewHolder.expandOrFold.setText("全文");
                        mTextStateList.put(mList.get(i).getId(), STATE_COLLAPSED);//保存状态
                    } else {
                        myViewHolder.expandOrFold.setVisibility(View.GONE);
                        mTextStateList.put(mList.get(i).getId(), STATE_NOT_OVERFLOW);
                    }
                    return true;
                }
            });

            myViewHolder.otherInfo.setMaxLines(Integer.MAX_VALUE);//设置文本的最大行数，为整数的最大数值
            myViewHolder.otherInfo.setText(mList.get(i).getOtherInfo());
        } else {
            //如果之前已经初始化过了，则使用保存的状态。
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    myViewHolder.expandOrFold.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    myViewHolder.otherInfo.setMaxLines(MAX_LINE_COUNT);
                    myViewHolder.expandOrFold.setVisibility(View.VISIBLE);
                    myViewHolder.expandOrFold.setText("全文");
                    break;
                case STATE_EXPANDED:
                    myViewHolder.otherInfo.setMaxLines(Integer.MAX_VALUE);
                    myViewHolder.expandOrFold.setVisibility(View.VISIBLE);
                    myViewHolder.expandOrFold.setText("收起");
                    break;
            }
            myViewHolder.otherInfo.setText(mList.get(i).getOtherInfo());
        }
        //全文和收起的点击事件
        myViewHolder.expandOrFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(mList.get(i).getId(), STATE_UNKNOW);
                if (state == STATE_COLLAPSED) {
                    myViewHolder.otherInfo.setMaxLines(Integer.MAX_VALUE);
                    myViewHolder.expandOrFold.setText("收起");
                    mTextStateList.put(mList.get(i).getId(), STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    myViewHolder.otherInfo.setMaxLines(MAX_LINE_COUNT);
                    myViewHolder.expandOrFold.setText("全文");
                    mTextStateList.put(mList.get(i).getId(), STATE_COLLAPSED);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
       return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView otherName;
        public TextView otherInfo;
        public TextView expandOrFold;

        public MyViewHolder(View itemView) {
            super(itemView);
            otherName = itemView.findViewById(R.id.other_name);
            otherInfo = itemView.findViewById(R.id.other_self);
            expandOrFold = itemView.findViewById(R.id.tv_expand_or_fold);
        }
    }
}
