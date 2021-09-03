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
import com.example.kmasocialnetworkct2.databinding.AlertDialogDeleteFriendBinding;
import com.example.kmasocialnetworkct2.databinding.SampleShowFriendBinding;
import com.example.kmasocialnetworkct2.models.Friends;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolderFriends>{
    ArrayList<Friends> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uId = auth.getUid();

    public FriendsAdapter(ArrayList<Friends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderFriends onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_friend, parent, false);
        return new ViewHolderFriends(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsAdapter.ViewHolderFriends holder, int position) {

        database = FirebaseDatabase.getInstance();

        Friends friend = list.get(position);
        if (friend.getProfilepic()!= null){
            Picasso.get().load(friend.getProfilepic()).placeholder(R.drawable.ic_friend).into(holder.binding.profileImageFriendFragment);
        }
        holder.binding.userNameFriendFragment.setText(friend.getFriendName());
        holder.binding.emailFriendFragment.setText(friend.getEmailFriend());

        holder.binding.btnRemoveFriendFriendFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriend(friend.getFriendId());
            }
        });

        holder.binding.btnMessageFriendFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", friend.getFriendId());
                intent.putExtra("profilePic", friend.getProfilepic());
                intent.putExtra("userName", friend.getFriendName());
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolderFriends extends RecyclerView.ViewHolder{
        SampleShowFriendBinding binding;

        public ViewHolderFriends(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleShowFriendBinding.bind(itemView);
        }
    }

    private void removeFriend(String friendId) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_delete_friend, null);
        AlertDialogDeleteFriendBinding binding = AlertDialogDeleteFriendBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete Friend")
                .setView(binding.getRoot())
                .create();
        binding.deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Friends").child(uId).child(friendId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Friends").child(friendId).child(uId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Xóa bạn bè thành công!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    }

}
