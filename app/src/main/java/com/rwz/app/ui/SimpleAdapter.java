package com.rwz.app.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * date： 2019/9/16 17:48
 * author： rwz
 * description：
 **/
public class SimpleAdapter extends BaseAdapter {

    private Context mContext;

    private List<Pair<String, View.OnClickListener>> mData;

    public SimpleAdapter(Context context, List<Pair<String, View.OnClickListener>> data) {
        mContext = context;
        mData = data;
    }

    public SimpleAdapter addData(String text, View.OnClickListener listener) {
        mData.add(new Pair<>(text, listener));
        return this;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair<String, View.OnClickListener> entity = mData.get(position);
        if (convertView == null) {
            convertView = new AppCompatButton(mContext);
        }
        AppCompatButton view = (AppCompatButton) convertView;
        view.setText(entity.first);
        view.setOnClickListener(entity.second);
        return view;
    }
}
