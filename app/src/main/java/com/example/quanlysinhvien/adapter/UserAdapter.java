package com.example.quanlysinhvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(R.layout.layout_user_item, viewGroup, false);

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvActive = view.findViewById(R.id.tvActive);

        User user = users.get(i);
        tvUsername.setText(user.getUsername());
        tvActive.setText(user.isActive() ? "Đã kích hoạt" : "Chưa kích hoạt");

        return view;
    }
}
