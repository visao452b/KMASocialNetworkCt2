package com.example.kmasocialnetworkct2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.activity.ChatDetailActivity;
import com.example.kmasocialnetworkct2.databinding.DeleteChatBinding;
import com.example.kmasocialnetworkct2.databinding.SampleShowFriendBinding;
import com.example.kmasocialnetworkct2.databinding.SampleShowFriendChatBinding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FriendsChatsAdapter extends RecyclerView.Adapter<FriendsChatsAdapter.ViewHolderFriendsChats> {

    ArrayList<Friends> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;


    public FriendsChatsAdapter(ArrayList<Friends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderFriendsChats onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_friend_chat, parent, false);
        return new ViewHolderFriendsChats(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsChatsAdapter.ViewHolderFriendsChats holder, int position) {
        Friends friend = list.get(position);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Picasso.get().load(friend.getProfilepic()).placeholder(R.drawable.ic_friend).into(holder.binding.profileImageFriendsChatsFragment);
        holder.binding.userNameFriendsChatsFragment.setText(friend.getFriendName());

        database.getReference().child("presence").child(friend.getFriendId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        if(status.equals("Offline")) {
                            holder.binding.statusUserFriendsChatsFragment.setVisibility(View.GONE);
                        } else {
                            holder.binding.statusUserFriendsChatsFragment.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + friend.getFriendId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                holder.binding.lastMessageFriendsChatsFragment.setText(snapshot1.child("message").getValue().toString());
                                long time = snapshot1.child("timestamp").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aaa");
                                holder.binding.msgTimeFriendsChatsFragment.setText(dateFormat.format(new Date(time)));
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
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", friend.getFriendId());
                intent.putExtra("profilePic", friend.getProfilepic());
                intent.putExtra("userName", friend.getFriendName());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.delete_chat, null);
                DeleteChatBinding binding = DeleteChatBinding.bind(view);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Chat")
                        .setView(binding.getRoot())
                        .create();

                binding.deleteChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MessageModel messageModel = new MessageModel();
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(FirebaseAuth.getInstance().getUid() + friend.getFriendId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Deleted chat successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolderFriendsChats extends RecyclerView.ViewHolder{
        SampleShowFriendChatBinding binding;

        public ViewHolderFriendsChats(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleShowFriendChatBinding.bind(itemView);
        }
    }
}
