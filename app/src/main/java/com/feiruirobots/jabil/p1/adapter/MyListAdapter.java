package com.feiruirobots.jabil.p1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyListAdapter<V, D> extends BaseAdapter {
    private Context mContext;

    private List<D> list;
    private int layoutId;
    private View.OnLongClickListener onLongClickListener;

    /**
     * @param context
     * @param list
     * @param layoutId
     */
    public MyListAdapter(Context context, List<D> list, int layoutId) {
        this.mContext = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    public void setDataList(List<D> list) {
        this.list = list;
    }

    public List<D> getDataList() {
        return this.list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public D getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract V initView(View convertView, V holder);

    public abstract void initContent(V holder, D data);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(this.layoutId, null);
            if (onLongClickListener != null) {
                convertView.setOnLongClickListener(onLongClickListener);
            }
            holder = initView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (V) convertView.getTag();
        }
        D data = list.get(position);
        initContent(holder, data);
        return convertView;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
