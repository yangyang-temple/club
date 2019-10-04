package com.example.application_table.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application_table.ApplyTable;
import com.example.application_table.Constant;
import com.example.application_table.R;
import com.example.application_table.entity.NewClubBean;

import java.util.List;

public class AdapterNewClub extends ArrayAdapter<NewClubBean> {
    private int resourceId;
    private Button btnApply;
    private Context mContext;
    public AdapterNewClub(Context context, int resource, List<NewClubBean> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        final NewClubBean bean = getItem(position);
        View view ;
        final ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.adp_new_club,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image = view.findViewById(R.id.img_photo);
            viewHolder.name = view.findViewById(R.id.tv_name);
            viewHolder.info = view.findViewById(R.id.tv_info);
            btnApply = view.findViewById(R.id.btn_apply);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (AdapterNewClub.ViewHolder)view.getTag();
        }
        viewHolder.name.setText(bean.getName());
        viewHolder.info.setText(bean.getInfo());
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ApplyTable.class);
                Constant.club = bean.getType();
                v.getContext().startActivity(intent);
                Toast.makeText(mContext, "oop", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public final class ViewHolder{
        public int imageId;//社团图片
        public TextView name;//社团名称
        public TextView info;//社团简介
        public TextView type;//社团所属类型
        public ImageView image;
    }
}
