package com.example.medmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.mydatabase.MedicalDB;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor user_list;
    public Context context;
    public MedicalDB helper;

    public UserListAdapter(Context context, MedicalDB helper) {
        this.context = context;
        this.helper = helper;
    }

    public void setUserData(Cursor cursor) {
        this.user_list = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (user_list != null && user_list.moveToPosition(position)) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.tv.setText(user_list.getString(1));
            myHolder.id = user_list.getInt(0);

            myHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.deleteUser(helper.getWritableDatabase(), "" + myHolder.id);
                    user_list = helper.getUserList(helper.getWritableDatabase());
                    setUserData(user_list);
                    if (user_list.getCount() == 0) {
                        MainActivity.empty_view.setText(R.string.empty_users);
                    }
                }
            });

            myHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineActivity.class);
                    intent.putExtra("userId", myHolder.id);
                    // Add this line to fix the exception
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return user_list != null ? user_list.getCount() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageButton deleteBtn;
        int id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.user_name);
            deleteBtn = itemView.findViewById(R.id.deleteUser);
        }
    }
}
