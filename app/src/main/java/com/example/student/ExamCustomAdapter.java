package com.example.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExamCustomAdapter extends ArrayAdapter<ExamDataModel> {

    ArrayList<ExamDataModel> dataset;
    Context context;


    private static class ViewHolder {
        TextView name;
        TextView location;
        TextView dateAndTime;
        CheckBox checkBox;
    }

    ExamCustomAdapter(Context context, ArrayList<ExamDataModel> dataSet) {
        super(context, R.layout.activity_exam_row_layout, dataSet);
        this.context = context;
        this.dataset = dataSet;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ExamDataModel dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if(convertView == null ) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_exam_row_layout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location);
            viewHolder.dateAndTime = (TextView) convertView.findViewById(R.id.dataAndTime);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.name.setText(dataModel.getName());
        viewHolder.location.setText(dataModel.getLocation());
        viewHolder.dateAndTime.setText(dataModel.getDateAndTime());
        viewHolder.checkBox.setChecked(dataModel.getChecked());

        return convertView;
    }


}
