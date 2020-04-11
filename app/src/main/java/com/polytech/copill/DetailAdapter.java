package com.polytech.copill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DetailAdapter extends ArrayAdapter<Detail> {

    public DetailAdapter(Context context, List<Detail> details){
        super(context,0,details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_day_details,parent, false);
        }

        DetailViewHolder viewHolder = (DetailViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new DetailViewHolder();
            viewHolder.detailName = (TextView) convertView.findViewById(R.id.textView_detailName);
            viewHolder.detailQuantity = (TextView) convertView.findViewById(R.id.textView_detailQuantity);
            viewHolder.detailHour = (TextView) convertView.findViewById(R.id.textView_detailHour);
            convertView.setTag(viewHolder);
        }

        Detail detail = getItem(position);

        viewHolder.detailName.setText(detail.getName());
        viewHolder.detailQuantity.setText(String.valueOf(detail.getQuantity()));
        viewHolder.detailHour.setText(detail.getHour());

        return convertView;
    }

    private class DetailViewHolder{
        public TextView detailName;
        public TextView detailQuantity;
        public TextView detailHour;
    }
}
