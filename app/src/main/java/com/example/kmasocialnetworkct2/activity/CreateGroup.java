package com.example.kmasocialnetworkct2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.databinding.ActivityCreateGroupBinding;
import com.example.kmasocialnetworkct2.models.Groups;
import com.example.kmasocialnetworkct2.models.Participants;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static android.content.ContentValues.TAG;

public class CreateGroup extends AppCompatActivity {

    ActivityCreateGroupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Date date = new Date();
        String uId = auth.getUid();
        Log.e(TAG, uId);

        String groupId = database.getReference().push().getKey();

        database.getReference().child("Users").child(uId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    Toast.makeText(CreateGroupChat.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_SHORT).show();
//                    String userName = String.valueOf(task.getResult().getValue();
                    String userName = task.getResult().getValue().toString();

                    Participants participant = new Participants("admin", uId, userName, date.getTime());



                    binding.btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String groupName = binding.nameGroup.getText().toString();
                            Groups group = new Groups(groupId, groupName, uId, date.getTime());


                            UserGroups userGroup = new UserGroups(groupId, groupName, uId);


                            database.getReference()
                                    .child("GroupsPost")
                                    .child(groupId).setValue(group);
                            database.getReference()
                                    .child("GroupsPost")
                                    .child(groupId)
                                    .child("participants")
                                    .child(participant.getUserId()).setValue(participant);
                            database.getReference()
                                    .child("UserGroupsPost")
                                    .child(uId)
                                    .child(groupId).setValue(userGroup);

                            Groups groupIntent = new Groups(groupId, groupName);
                            Intent intent = new Intent(CreateGroup.this, CreateGroup2.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("group", groupIntent);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    });
                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
}