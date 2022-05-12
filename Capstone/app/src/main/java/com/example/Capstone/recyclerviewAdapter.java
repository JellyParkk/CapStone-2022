package com.example.Capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerviewAdapter extends RecyclerView.Adapter<recyclerviewAdapter.MyViewHolder> {

    private ArrayList<com.example.Capstone.DataModel> list = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDate = itemView.findViewById(R.id.textDate);
        }

        public void setItem(com.example.Capstone.DataModel dataModel) {
            textName.setText(dataModel.getText_title());
            textDate.setText(dataModel.getText_date());
        }
    }

    public recyclerviewAdapter(ArrayList<com.example.Capstone.DataModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.text_list,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        com.example.Capstone.DataModel dataModel = list.get(position);
        holder.setItem(dataModel);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TextReadActivity.class);
                intent.putExtra("title",holder.textName.getText());
                intent.putExtra("path", Environment.getExternalStorageDirectory().toString()+"/Download/TestDir/");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
