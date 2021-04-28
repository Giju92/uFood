package com.polito.group3.mobileproject.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.JsonKey;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alfonso-LAPTOP on 06/05/2016.
 */
public class G3UserCreateProfile extends G3BaseActivity {

    private ImageView mProfileImage;
    private EditText mEditName;
    private EditText mEditSurname;
    private EditText mEditMobile;
    private EditText mEditNunmber;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private EditText mEditConfirmPassword;
    private Button mSaveButton;
    private View mFocusView;
    private FloatingActionButton mChangeFotoButton;
    private String mEmail;
    private String mPassword;
    ProgressDialog progress;
    private Bitmap bmpToSave;
    private Activity mActivity;

    private static final String USER_PROFILE = "USER_PROFILE";
    private final static int MY_WRITE_PERMISSION = 11;

    private String mImagePath = null;
    private static final int REQUEST_CAMERA = 40;
    private static final int SELECT_FILE = 50;
    private SharedPreferences mShared;
    private G3UserObj mProfileObj;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private boolean checkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_user_profile);
        setTitle(getString(R.string.profile));
        mActivity = this;
        mShared = getSharedPreferences(G3Application.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
        checkout = getIntent().getBooleanExtra("checkout",false);
        if(toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPemission();
        }

        mCollapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.ToolbarTheme);
        loadView();
        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadView() {
        mProfileImage = (ImageView) findViewById(R.id.bgheader);
        mEditName = (EditText) findViewById(R.id.editname);
        mEditSurname = (EditText) findViewById(R.id.editsurname);
        mEditMobile = (EditText) findViewById(R.id.editmobile);
        mEditNunmber = (EditText) findViewById(R.id.editphone);
        mEditEmail = (EditText) findViewById(R.id.editemail);
        mEditPassword = (EditText) findViewById(R.id.editpassword);
        mEditConfirmPassword = (EditText) findViewById(R.id.editconfirmpassword);
        mSaveButton = (Button) findViewById(R.id.addimbutt);
        mChangeFotoButton = (FloatingActionButton) findViewById(R.id.change_photo);
    }

    private void setListeners() {

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
            }
        });

        mChangeFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public void selectImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(G3UserCreateProfile.this);


        TextView title = new TextView(this);
        title.setText(R.string.title_add_photo);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setPadding(30, 10, 10, 10);
        title.setBackgroundColor(getResources().getColor(R.color.primary));
        title.setTextSize(30);//TODO cercare come imporre l'AppCompact

        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getString(R.string.library))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mProfileImage.setImageBitmap(thumbnail);
                bmpToSave = thumbnail;


            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                mProfileImage.setImageBitmap(bm);
                bmpToSave = bm;
            }
        }

    }

//    private void loadImage() {
//
//        if (mImagePath != null) {
//
//            Bitmap bm;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(mImagePath, options);
//            final int REQUIRED_SIZE = 200;
//            int scale = 1;
//            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                scale *= 2;
//            options.inSampleSize = scale;
//            options.inJustDecodeBounds = false;
//            bm = BitmapFactory.decodeFile(mImagePath, options);
//            mProfileImage.setImageBitmap(bm);
//
//        }
//    }

    private void checkForm() {

        mFocusView = null;

        if (mEditName.getText().toString().isEmpty()) { // name not correct
            mEditName.setError(getResources().getString(R.string.nameerror));
            mFocusView = mEditName;
            mFocusView.requestFocus();
            return;
        }

        if (mEditSurname.getText().toString().isEmpty()) { // surname not correct
            mEditSurname.setError(getResources().getString(R.string.surname_error));
            mFocusView = mEditSurname;
            mFocusView.requestFocus();
            return;
        }

        if (mEditNunmber.getText().toString().isEmpty() && mEditMobile.getText().toString().isEmpty()) {
            mEditMobile.setError(getString(R.string.phoneerror));
            mFocusView = mEditMobile;
            mFocusView.requestFocus();
            return;
        }

        if (mEditEmail.getText().toString().isEmpty()) {
            mEditEmail.setError(getString(R.string.emailerror));
            mFocusView = mEditEmail;
            mFocusView.requestFocus();
            return;
        }

        if (mEditPassword.getText().toString().isEmpty()) {
            mEditPassword.setError(getString(R.string.password_error));
            mFocusView = mEditPassword;
            mFocusView.requestFocus();
            return;
        }

        if (mEditConfirmPassword.getText().toString().isEmpty()) {
            mEditConfirmPassword.setError(getString(R.string.confirm_pass_err));
            mFocusView = mEditConfirmPassword;
            mFocusView.requestFocus();
            return;
        }

        if(mEditPassword.getText().toString().compareTo(mEditConfirmPassword.getText().toString()) != 0){
            mEditConfirmPassword.setError(getString(R.string.pass_not_match));
            mFocusView = mEditConfirmPassword;
            mFocusView.requestFocus();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(G3UserCreateProfile.this);
        builder.setMessage(R.string.are_you_sure).setTitle(R.string.save);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        saveData();
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void saveData() {

        mProfileObj = new G3UserObj(mEditName.getText().toString(),mEditSurname.getText().toString(),"not Used",mEditEmail.getText().toString(),mEditNunmber.getText().toString(),
                mEditMobile.getText().toString(),mEditPassword.getText().toString(),mImagePath);

        mEmail = mEditEmail.getText().toString();
        mPassword = mEditPassword.getText().toString();
        SignUpProfile();

    }

    private void SignUpProfile() {

        progress = TransitionHelper.getProgress(this);

        try {
            progress.show();
            G3Application.fManager.signUp(mEditEmail.getText().toString(), mEditPassword.getText().toString(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SignIn();
                    } else {
                        progress.dismiss();
                        Log.d("CreateProfile", task.getException().getMessage().toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress) + "  " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (NetworkDownException e) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    private void SignIn() {


        try {
            G3Application.fManager.signIn(mEmail, mPassword, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SaveProfileImage();
                    } else {
                        deleteAccount();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress) + " " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } catch (NetworkDownException e) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }

    private void deleteAccount() {
        try {
            G3Application.fManager.deleteAccount(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NotAuthenticatedException e) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (NetworkDownException e) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveProfileImage() {

        if(!G3Application.fManager.isSignedin()){
            progress.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
        }
        String accountid = G3Application.fManager.getCurrentId();
        if(bmpToSave != null){

            try {
                G3Application.fManager.saveImageToFirebaseStorage("img_" + accountid + ".jpg", bmpToSave, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        SaveProfile(null);
                    }
                }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        SaveProfile(taskSnapshot.getDownloadUrl().toString());
                    }
                });
            } catch (NetworkDownException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            SaveProfile(null);
        }
    }

    private void SaveProfile(String imagePath) {
        if(mProfileObj != null) {
            mProfileObj.setPhotoPath(imagePath);
            mProfileObj.setId(G3Application.fManager.getCurrentId());

            try {
                G3Application.fManager.saveUserProfile(mProfileObj, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successprogress), Toast.LENGTH_SHORT).show();
                            if(checkout){
                                Intent i=new Intent (mActivity, G3BasketActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                            else{
                                NavUtils.navigateUpFromSameTask(mActivity);
                            }
                        } else {
                            deleteAccount();
                        }
                    }
                });
            } catch (NotAuthenticatedException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (NetworkDownException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPemission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_WRITE_PERMISSION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    checkPemission();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}