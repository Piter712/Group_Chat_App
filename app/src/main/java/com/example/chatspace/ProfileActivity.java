package com.example.chatspace;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserId, Current_state, senderUserId;
    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button sendMessageRequestButton, DeclinedRequestButton;
    private DatabaseReference UserRef, ChatRequestRef, ContactRef, NotificationRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();

        senderUserId = mAuth.getCurrentUser().getUid();


        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_profile_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        Current_state = "new";

        // Method for retrieving user information
        RetrieveUserInfo();

    }

    private void RetrieveUserInfo() {
        UserRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(userName);
                    userProfileStatus.setText(userStatus);

                    //ManageChatRequest();
                } else {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    userProfileName.setText(userName);
                    userProfileStatus.setText(userStatus);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


