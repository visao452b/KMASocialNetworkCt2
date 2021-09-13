package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.databinding.SampleShowListFriendCreateGroupBinding;
import com.example.kmasocialnetworkct2.function.ListMemberGroupChat;
import com.example.kmasocialnetworkct2.models.Friends;
import com.example.kmasocialnetworkct2.models.Groups;
import com.example.kmasocialnetworkct2.models.Participants;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class FriendsCreateGroupChatAdapter extends RecyclerView.Adapter<FriendsCreateGroupChatAdapter.ViewHolderFriendsCreateGroupChat>{

    ArrayList<Friends> list;
    Context context;
    String nameGroup;
    String idGroup;
    FirebaseDatabase database;
    String member;

    Date date = new Date();

    public FriendsCreateGroupChatAdapter(ArrayList<Friends> list, Context context, String nameGroup, String idGroup) {
        this.list = list;
        this.context = context;
        this.nameGroup = nameGroup;
        this.idGroup = idGroup;
    }

    @NonNull
    @NotNull
    @Override
    public FriendsCreateGroupChatAdapter.ViewHolderFriendsCreateGroupChat onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_list_friend_create_group, parent, false);
        return new ViewHolderFriendsCreateGroupChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsCreateGroupChatAdapter.ViewHolderFriendsCreateGroupChat holder, int position) {
        database = FirebaseDatabase.getInstance();
        Long timestamp = date.getTime();

        Friends friend = list.get(position);
        if (friend.getProfilepic()!= null){
            Picasso.get().load(friend.getProfilepic()).placeholder(R.drawable.ic_friend).into(holder.binding.profileImageShowFriend);
        }
        holder.binding.userNameListShowFriend.setText(friend.getFriendName());
        holder.binding.emailShowFriend.setText(friend.getEmailFriend());

        Participants participant = new Participants("participant", friend.getFriendId(),friend.getFriendName(),timestamp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database, database1;
                database = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();

                database1.getReference()
                        .child("Groups")
                        .child(idGroup)
                        .child("participants")
                        .child(participant.getUserId())
                        .setValue(participant);

//                database.getReference().child("UserGroups").child(participant.getUserId()).child(groupId).setValue();

                member = member + friend.getFriendName();
                Toast.makeText(context, "Thêm thành công thành công!!! "+ friend.getFriendName(), Toast.LENGTH_SHORT).show();

                database.getReference().child("Groups").child(idGroup).child("groupName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        String groupName = task.getResult().getValue().toString();
                        UserGroups userGroup = new UserGroups(idGroup, groupName, friend.getFriendId());
                        database.getReference().child("UserGroups").child(participant.getUserId()).child(idGroup).setValue(userGroup);
                    }
                });


            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderFriendsCreateGroupChat extends RecyclerView.ViewHolder {

        SampleShowListFriendCreateGroupBinding binding;

        public ViewHolderFriendsCreateGroupChat(@NonNull @NotNull View itemView) {
            super(itemView);
            binding  = SampleShowListFriendCreateGroupBinding.bind(itemView);
        }
    }
}
