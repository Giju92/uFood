package com.polito.group3.mobileproject.activities;

import android.Manifest;
import android.app.Notification;
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
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alfonso-LAPTOP on 06/05/2016.
 */
public class G3ShowEditProfile extends G3BaseActivity {

    private ImageView mProfileImage;
    private EditText mEditName;
    private EditText mEditSurname;
    private EditText mEditMobile;
    private EditText mEditPhone;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private EditText mEditConfirmPassword;
    private TextView mLabelPassword;
    private TextView mLabelConfirmPassword;
    private TextView mLabelEmail;
    private Menu menu;
    private Button mSaveButton;
    private View mFocusView;
    private FloatingActionButton mChangeFotoButton;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private String imagepath;
    private Bitmap bmpToSave;
    private KeyListener mNameListener;
    private KeyListener mSurnameListener;
    private KeyListener mMobileListener;
    private KeyListener mPhoneListener;
    private KeyListener mEmailListener;

    private boolean isEditingMode = false;

    private static final String SHOW_EDIT_FLAG = "SHOW_EDIT_FLAG";
    private static final String USER_PROFILE = "USER_PROFILE";
    private final static int MY_WRITE_PERMISSION = 11;

    private String mImagePath = null;
    private static final int REQUEST_CAMERA = 40;
    private static final int SELECT_FILE = 50;

    private G3UserObj mProfileObj;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        setTitle(getString(R.string.profile));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
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
        loadData(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);

        if (isEditingMode) {
            menu.findItem(R.id.edit_action).setVisible(false);
            menu.findItem(R.id.save_action).setVisible(true);
        } else {
            menu.findItem(R.id.edit_action).setVisible(true);
            menu.findItem(R.id.save_action).setVisible(false);
        }

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(isEditingMode){
                    isEditingMode = false;
                    setShowOrEditMode();
                    return true;
                }
                onBackPressed();
                return true;
            case R.id.edit_action:
                isEditingMode = true;
                setShowOrEditMode();
                return true;
            case R.id.save_action:
                checkForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShowOrEditMode() {

        if (isEditingMode) {

            mEditName.setKeyListener(mNameListener);
            mEditName.setFocusableInTouchMode(true);
            mEditSurname.setKeyListener(mSurnameListener);
            mEditSurname.setFocusableInTouchMode(true);
            mEditMobile.setKeyListener(mMobileListener);
            mEditMobile.setFocusableInTouchMode(true);
            mEditPhone.setKeyListener(mPhoneListener);
            mEditPhone.setFocusableInTouchMode(true);
//            mEditEmail.setKeyListener(mEmailListener);
//            mEditEmail.setFocusableInTouchMode(true);
//            mEditEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            mEditPhone.setVisibility(View.VISIBLE);
            mEditMobile.setVisibility(View.VISIBLE);

            mEditEmail.setVisibility(View.GONE);
            mLabelEmail.setVisibility(View.GONE);
            mChangeFotoButton.setVisibility(View.VISIBLE);

            if (menu != null) {
                menu.findItem(R.id.edit_action).setVisible(false);
                menu.findItem(R.id.save_action).setVisible(true);
            }

        } else {

            // Hide Soft-Keyboard
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }

            mEditName.setKeyListener(null);
            mEditName.setFocusable(false);
            mEditSurname.setKeyListener(null);
            mEditSurname.setFocusable(false);
            mEditMobile.setKeyListener(null);
            mEditMobile.setFocusable(false);
            mEditPhone.setKeyListener(null);
            mEditPhone.setFocusable(false);
            mEditEmail.setKeyListener(null);
            mEditEmail.setFocusable(false);

            if (mEditPhone.getText().toString().isEmpty())
                mEditPhone.setVisibility(View.GONE);
            if (mEditMobile.getText().toString().isEmpty())
                mEditMobile.setVisibility(View.GONE);

            mEditEmail.setVisibility(View.VISIBLE);
            mLabelEmail.setVisibility(View.VISIBLE);
            mChangeFotoButton.setVisibility(View.GONE);

            if (menu != null) {
                menu.findItem(R.id.edit_action).setVisible(true);
                menu.findItem(R.id.save_action).setVisible(false);
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(JsonKey.NAME, mEditName.getText().toString());
        outState.putString(JsonKey.SURNAME, mEditSurname.getText().toString());
        outState.putString(JsonKey.PHONENUMBER, mEditPhone.getText().toString());
        outState.putString(JsonKey.MOBILENUMBER, mEditMobile.getText().toString());
        outState.putString(JsonKey.EMAIL, mEditEmail.getText().toString());
        outState.putString(JsonKey.PASSWORD, mEditPassword.getText().toString());
        outState.putString(JsonKey.CONFIRM_PASSWORD, mEditConfirmPassword.getText().toString());
        outState.putString(JsonKey.PHOTOPATH, imagepath);
        outState.putBoolean(SHOW_EDIT_FLAG, isEditingMode);
    }


    private void loadData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            progress = TransitionHelper.getProgress(this);
            progress.show();

            try {
                G3Application.fManager.getUserProfile(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progress.dismiss();
                        mProfileObj = dataSnapshot.getValue(G3UserObj.class);
                        loadProfile();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NotAuthenticatedException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            } catch (NetworkDownException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        } else {
            mEditName.setText(savedInstanceState.getString(JsonKey.NAME, null));
            mEditSurname.setText(savedInstanceState.getString(JsonKey.SURNAME, null));
            mEditPhone.setText(savedInstanceState.getString(JsonKey.PHONENUMBER, null));
            mEditMobile.setText(savedInstanceState.getString(JsonKey.MOBILENUMBER, null));
            mEditEmail.setText(savedInstanceState.getString(JsonKey.EMAIL, null));
            mEditPassword.setText(savedInstanceState.getString(JsonKey.PASSWORD, null));
            mEditConfirmPassword.setText(savedInstanceState.getString(JsonKey.CONFIRM_PASSWORD, null));
            mImagePath = savedInstanceState.getString(JsonKey.PHOTOPATH, null);
            isEditingMode = savedInstanceState.getBoolean(SHOW_EDIT_FLAG, false);
        }

    }


    void loadProfile(){
        if (mProfileObj != null) {

            mEditName.setText(mProfileObj.getFirstName());
            mEditSurname.setText(mProfileObj.getLastName());

            mImagePath = mProfileObj.getPhotoPath();
            mEditPhone.setText(mProfileObj.getPhoneNumber());
            mEditMobile.setText(mProfileObj.getMobileNumber());
            mEditEmail.setText(mProfileObj.getEmail());
            String url  = mProfileObj.getPhotoPath();
            if(url != null && url.length()>0)
            {
                Picasso.with(G3Application.getAppContext()).load(url).into(mProfileImage);
            }
        }
        setShowOrEditMode();

    }

    private void loadView() {
        mProfileImage = (ImageView) findViewById(R.id.bgheader);
        mEditName = (EditText) findViewById(R.id.editname);
        mEditSurname = (EditText) findViewById(R.id.editsurname);
        mEditMobile = (EditText) findViewById(R.id.editmobile);
        mEditPhone = (EditText) findViewById(R.id.editphone);
        mEditEmail = (EditText) findViewById(R.id.editemail);
        mEditPassword = (EditText) findViewById(R.id.editpassword);
        mEditConfirmPassword = (EditText) findViewById(R.id.editconfirmpassword);
        mSaveButton = (Button) findViewById(R.id.addimbutt);
        mChangeFotoButton = (FloatingActionButton) findViewById(R.id.change_photo);
        mLabelPassword = (TextView) findViewById(R.id.password);
        mLabelConfirmPassword = (TextView) findViewById(R.id.confirmpassword);
        mLabelEmail = (TextView) findViewById(R.id.email);

        mEditPassword.setVisibility(View.GONE);
        mEditConfirmPassword.setVisibility(View.GONE);
        mLabelPassword.setVisibility(View.GONE);
        mLabelConfirmPassword.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.GONE);
    }

    private void setListeners() {

        //Saving default keylistener of edittexts for later use.
        mNameListener = mEditName.getKeyListener();
        mSurnameListener = mEditSurname.getKeyListener();
        mPhoneListener = mEditPhone.getKeyListener();
        mMobileListener = mEditMobile.getKeyListener();
        mEmailListener = mEditEmail.getKeyListener();


        mChangeFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public void selectImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(G3ShowEditProfile.this);


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
                bmpToSave= bm;
            }
        }

    }

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

        if (mEditPhone.getText().toString().isEmpty() && mEditMobile.getText().toString().isEmpty()) {
            mEditMobile.setError(getString(R.string.phoneerror));
            mFocusView = mEditMobile;
            mFocusView.requestFocus();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(G3ShowEditProfile.this);
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

        mProfileObj = new G3UserObj(mEditName.getText().toString(),mEditSurname.getText().toString(),"not Used",mEditEmail.getText().toString(), mEditPhone.getText().toString(),
                mEditMobile.getText().toString(),mEditPassword.getText().toString(),mImagePath);

        progress = TransitionHelper.getProgress(this);
        progress.show();

        SaveImage();
    }

    private void SaveImage() {

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

    private void SaveProfile(String so) {

        if(mProfileObj != null) {
            mProfileObj.setPhotoPath(so);

            try {
                G3Application.fManager.saveUserProfile(mProfileObj, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successprogress), Toast.LENGTH_SHORT).show();
                            isEditingMode = false;
                            setShowOrEditMode();
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
