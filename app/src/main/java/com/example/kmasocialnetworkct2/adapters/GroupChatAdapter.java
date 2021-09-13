package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.activity.ChatGroupActivity;
import com.example.kmasocialnetworkct2.databinding.SampleShowGroupChatBinding;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    ArrayList<UserGroups> list;
    Context context;

    public GroupChatAdapter(ArrayList<UserGroups> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public GroupChatAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_group_chat, parent, false);
        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupChatAdapter.ViewHolder holder, int position) {
        UserGroups userGroups = list.get(position);

        holder.binding.groupName.setText(userGroups.getGroupName());
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(userGroups.getGroupId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.binding.lastMessageGroup.setText(snapshot1.child("message").getValue().toString());
                                long time = snapshot1.child("timestamp").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                holder.binding.msgTimeGroup.setText(dateFormat.format(new Date(time)));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((userGroups.getGroupName()!= null) &&(userGroups.getGroupId()!=null)){
                    Intent intent = new Intent(context, ChatGroupActivity.class);
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
        SampleShowGroupChatBinding binding;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleShowGroupChatBinding.bind(itemView);
        }
    }
}
