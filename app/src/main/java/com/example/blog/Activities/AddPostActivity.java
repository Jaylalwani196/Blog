package com.example.blog.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.Model.Blog;
import com.example.blog.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

private ImageButton mPostImage;
private EditText mPostTitle;
private EditText mPostDesc;
private Button mSubmitButton;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Uri mImageUri;
    private ProgressDialog mProgress;
    private StorageReference mStorage;

    private static final int GALLERY_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mPostDatabase = mDatabase.getReference().child("Mblog");
        mPostImage =  findViewById(R.id.imageButton);
        mPostTitle = (EditText) findViewById(R.id.postTitleEt);
        mPostDesc = (EditText)findViewById(R.id.descriptionEt);
        mSubmitButton = (Button)findViewById(R.id.submitPostEt);
        mProgress = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent ,GALLERY_CODE );
            }
        });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //POSTING TO DATABSE
                startPosting();


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK ){

            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    //VERY VERY IMPORTANT SECTION
    private void startPosting() {
    mProgress.setMessage("Posting to blog.....");
    mProgress.show();

    final String titleVal = mPostTitle.getText().toString().trim();
    final String discVal = mPostDesc.getText().toString().trim();

    if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(discVal)
    &&  mImageUri !=null   ){
        //Start Uploading.......
        // GetLas...ent ----- GET PATH LIKE IMAGES/MYPHOTO.ABC.JPG

        StorageReference filepath = mStorage.child("MBlog_images").child(mImageUri.getLastPathSegment());

        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUri = taskSnapshot.getUploadSessionUri();
                DatabaseReference newPost = mPostDatabase.push();
                //Hashmap is better version of Array
                Map<String , String> dataToSave = new HashMap<>();
                dataToSave.put("title" , titleVal);
                dataToSave.put("description" , discVal);
                dataToSave.put("image" , downloadUri.toString());
                dataToSave.put("timestamp" , String.valueOf(java.lang.System.currentTimeMillis()));
                dataToSave.put("userid" , mUser.getUid());

                newPost.setValue(dataToSave);

                //OLD WAY
                //newPost.child("title").setValue(titleVal);
                mProgress.dismiss();

                startActivity(new Intent(AddPostActivity.this , PostListActivity.class));
                finish();

            }
        });

    }

    }
}
