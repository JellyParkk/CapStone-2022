package com.r0adkll.slidr.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.r0adkll.slidr.example.model.TextFile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextFileAdapter extends BetterRecyclerAdapter<TextFile, TextFileAdapter.TextViewHolder> {

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item,parent,false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder,i);
        TextFile textFile = getItem(i);
        viewHolder.title.setText(textFile.text_title);
        viewHolder.date.setText(textFile.text_date);
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title)   public TextView title;
        @BindView(R.id.description)    public TextView date;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
