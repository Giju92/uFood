package com.polito.group3.ufoodfusion.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.dialogfragments.G3ForgotPasswordDialogFragment;

/**
 * Created by Alfonso on 19/07/2016.
 */
public class G3LoginActivity extends G3BaseActivity{

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mNewAccountButton;
    private Button mNewRestAccountButton;
    private Activity mActivity;
    private ProgressDialog progress;
    private Button mForgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fusion);

        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //Setting Home hamburger icon
        if(myToolbar != null) {
            getSupportActionBar().setTitle(null);
        }
        mActivity = this;
        //Loading Views by ID
        loadView();
        setListeners();

    }



    private void loadView() {
        mEmail = (EditText) findViewById(R.id.name_credential);
        mPassword = (EditText) findViewById(R.id.password_credential);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mNewAccountButton = (Button) findViewById(R.id.new_useraccount_button);
        mNewRestAccountButton = (Button) findViewById(R.id.new_restaccount_button);
        mForgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);

    }

    private void setListeners() {


        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmail.getText().toString().isEmpty()) {
                    mEmail.setError(getResources().getString(R.string.emailerror));
                    mEmail.requestFocus();
                    return;

                }
                if (mPassword.getText().toString().isEmpty()) {
                    mPassword.setError(getResources().getString(R.string.password_error));
                    mPassword.requestFocus();
                    return;
                }
                progress = TransitionHelper.getProgress(mActivity);
                progress.show();

                try {
                    G3Application.fManager.signIn(mEmail.getText().toString(), mPassword.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                checkisRestaurant();

                            } else {
                                Toast.makeText(getApplication(), getResources().getString(R.string.invaliduserorpassword), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                } catch (NetworkDownException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            }
        });


        mNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() ,G3UserCreateProfile.class);
                startActivity(intent);
            }
        });

        mNewRestAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() ,G3CreateRestaurant.class);
                startActivity(intent);
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G3ForgotPasswordDialogFragment mDialogFrag;
                mDialogFrag = G3ForgotPasswordDialogFragment.newInstance();
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getSupportFragmentManager(), "dialogorder");
            }
        });

    }

    private void checkisRestaurant() {

        try {
            G3Application.fManager.isRestaurant(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress.dismiss();
                    if(dataSnapshot.exists()){
                        Toast.makeText(getApplication(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mActivity,G3TabsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplication(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mActivity,G3UserSearchRestaurant.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {

        if(!G3Application.fManager.isSignedin()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.close_app))
                    .setMessage(getResources().getString(R.string.are_you_sure_close_app))
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }

                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
        }

    }
}


