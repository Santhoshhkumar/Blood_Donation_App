package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.UUID;

public class Userprofile extends AppCompatActivity {

    TextView regusername, regemailid, regmobileno, regdob, regno, regdepartment, regbloodgroup,reggender,regeligibility;
    FirebaseAuth fauth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Button previousbtn, nextbtn;
    ImageButton photobtn;
    Button editprofile;
    ImageView gallery;
    private Uri imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        reggender=(TextView)findViewById(R.id.reggender);
        regeligibility=(TextView)findViewById(R.id.willingnesstext);
        regusername = (TextView) findViewById(R.id.usernameedt);
        regemailid = (TextView) findViewById(R.id.emailidtext);
        regmobileno = (TextView) findViewById(R.id.mobilenoidtext);
        regdob = (TextView) findViewById(R.id.dobidtext);
        regno = (TextView) findViewById(R.id.registernoidtext);
        regdepartment = (TextView) findViewById(R.id.departmentidtext);
        regbloodgroup = (TextView) findViewById(R.id.bgidtext);
        previousbtn = (Button) findViewById(R.id.prvbtn);
        photobtn = (ImageButton) findViewById(R.id.photoimage);
        gallery = (ImageView) findViewById(R.id.usericonid);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        fauth = FirebaseAuth.getInstance();
        FirebaseUser user = fauth.getCurrentUser();
        String uid = user.getUid();
        DocumentReference reference;
        firestore = FirebaseFirestore.getInstance();
        StorageReference downnlsr=storageReference.child("Students/"+fauth.getCurrentUser().getUid()+"/ProfileImages");
        downnlsr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(gallery);
            }
        });
        reference = firestore.collection("student").document(uid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {
                    String username = task.getResult().getString("regusername");
                    String emailid = task.getResult().getString("regemailid");
                    String mobileno = task.getResult().getString("regmobileno");
                    String registerno = task.getResult().getString("regno");
                    String dateofbirth = task.getResult().getString("regdob");
                    String department = task.getResult().getString("regdepartment");
                    String bloodgroup = task.getResult().getString("regbloodgroup");
                    String eligibility = task.getResult().getString("regeligibility");
                    String gender=task.getResult().getString("reggender");
                    regusername.setText(username);
                    regemailid.setText(emailid);
                    regmobileno.setText(mobileno);
                    regno.setText(registerno);
                    regdob.setText(dateofbirth);
                    regdepartment.setText(department);
                    regbloodgroup.setText(bloodgroup);
                    regeligibility.setText(eligibility);
                    reggender.setText(gender);
                }
            }
        });
        previousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Userprofile.this, cardview.class));
                finish();
            }
        });
        photobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        try {
            if (resultcode == RESULT_OK && requestcode == 1 && data != null && data.getData() != null) {
                imageuri = data.getData();
                gallery.setImageURI(imageuri);
                gallery.setMaxWidth(50);
                gallery.setMaxHeight(50);
                Picasso.get().load(imageuri).into(gallery);
                uploadimage();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No Profile Selected",Toast.LENGTH_LONG).show();
        }
    }
    private void uploadimage() {
        final String randomkey = UUID.randomUUID().toString();
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        // StorageReference strref = storageReference.child("images/" + randomkey);
        storageReference.child("Students/"+fauth.getCurrentUser().getUid()+"/ProfileImages").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double percent = (100.00 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                pd.setMessage("Uploading the image");
            }
        });
    }
}