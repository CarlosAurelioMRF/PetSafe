package com.carlosaurelio.petsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Carlos Aurelio on 02/04/2016.
 */
public class ContextMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<ContextMenuItem> mList;
    private LayoutInflater mLayoutInflater;
    private int origemClick;
    private int extraPadding;

    public ContextMenuAdapter(Context context, List<ContextMenuItem> list, int origem) {
        this.mList = list;
        this.mContext = context;
        this.origemClick = origem;
        mLayoutInflater = LayoutInflater.from(context);

        float scale = context.getResources().getDisplayMetrics().density;
        extraPadding = (int)(8 * scale + 0.5f);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.context_menu, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            viewHolder.txtLabel = (TextView) convertView.findViewById(R.id.txtLabel);
            viewHolder.vwDivider = convertView.findViewById(R.id.vw_divider);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgIcon.setImageResource(mList.get(position).getIcon());
        viewHolder.txtLabel.setText(mList.get(position).getLabel());

        // Background
        if (position == 0) {
            ((ViewGroup) convertView).getChildAt(0).setBackgroundResource(R.drawable.context_menu_top_background);
        } else if (position == mList.size() - 1) {
            ((ViewGroup) convertView).getChildAt(0).setBackgroundResource(R.drawable.context_menu_bottom_background);
        } else {
            ((ViewGroup) convertView).getChildAt(0).setBackgroundResource(R.drawable.context_menu_middle_background);
        }

        // Linha do Ultimo - Clientes
        if (origemClick == 0) {
            viewHolder.vwDivider.setVisibility(position == mList.size() - 2 ? View.VISIBLE : View.GONE);
        }

        // Extra Padding
        ((ViewGroup) convertView).getChildAt(0).setPadding(0,
                position == 0 || position == mList.size() -1 ? extraPadding : 0,
                0,
                position == mList.size() -1 ? extraPadding : 0);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgIcon;
        TextView txtLabel;
        View vwDivider;
    }
}
