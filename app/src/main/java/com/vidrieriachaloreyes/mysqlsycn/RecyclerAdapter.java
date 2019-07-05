package com.vidrieriachaloreyes.mysqlsycn;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyVH> {

    private ArrayList<Contact> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Contact> arrayList) {
        this.arrayList = arrayList;
    }

    //Init method Implement

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent, false);
        MyVH myVH = new MyVH(view);
        return myVH;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH myVH, int position) {

        myVH.Name.setText(arrayList.get(position).getName());
        int sync_status = arrayList.get(position).getSync_status();

        if (sync_status == DbContract.SYNC_STATUS_OK){
            myVH.Sync_Status.setImageResource(R.drawable.img_checked);
        }else if(sync_status == DbContract.SYNC_STATUS_FAILIDE){
            myVH.Sync_Status.setImageResource(R.drawable.img_sync);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //Clase externa
    public class MyVH extends RecyclerView.ViewHolder {

        ImageView Sync_Status;
        TextView Name;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            Sync_Status = itemView.findViewById(R.id.imgSync);
            Name = itemView.findViewById(R.id.txtName);
        }
    }
}
