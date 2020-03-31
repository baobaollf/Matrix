package com.laioffer.matrix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> list;
    private LayoutInflater layoutInflater;
    private OnClickListener onClickListener;

    public interface OnClickListener{
        public void setItem(String item);
    }

    public void setClickListener(ReportRecyclerViewAdapter.OnClickListener callback) {
        onClickListener = callback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText(list.get(position).getDrawable_label());
        viewHolder.imageView.setImageResource(list.get(position).getDrawable_id());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.setItem(list.get(position).getDrawable_label());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReportRecyclerViewAdapter(Context context, List<Item> items) {
        layoutInflater = LayoutInflater.from(context);
        list = items;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView = (TextView) view.findViewById(R.id.info_text);
            imageView = (ImageView) view.findViewById(R.id.info_img);

        }
    }
}
