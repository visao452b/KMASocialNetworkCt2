package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.activity.ChatGroupActivity;
import com.example.kmasocialnetworkct2.activity.GroupActivity;
import com.example.kmasocialnetworkct2.databinding.SampleShowGroupChatBinding;
import com.example.kmasocialnetworkct2.databinding.SampleShowGroupsBinding;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    ArrayList<UserGroups> list;
    Context context;

    public GroupAdapter(ArrayList<UserGroups> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_groups, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupAdapter.ViewHolder holder, int position) {
        UserGroups userGroups = list.get(position);

        holder.binding.groupName.setText(userGroups.getGroupName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((userGroups.getGroupName() != null) && (userGroups.getGroupId() != null)) {
                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.putExtra("groupId", userGroups.getGroupId());
                    intent.putExtra("groupName", userGroups.getGroupName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SampleShowGroupsBinding binding;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleShowGroupsBinding.bind(itemView);
        }
    }

}
