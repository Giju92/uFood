package com.polito.group3.mobileproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.polito.group3.mobileproject.R;
import com.polito.group3.mobileproject.dialogfragments.G3ForgotPasswordDialogFragment;
import com.polito.group3.mobileproject.dialogfragments.G3OrderByDialogFragment;

/**
 * Created by Giovanni on 20/05/2016.
 */
public class G3LoginActivity extends G3BaseActivity{

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mNewAccountButton;
    private Button mForgotPasswordButton;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_user_login);

        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_without_logo);
        setSupportActionBar(myToolbar);

        //Setting Home hamburger icon
        if(myToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(com.example.ufoodlibrary.R.string.log_in);
        }
        mActivity = this;
        //Loading Views by ID
        loadView();
        setListeners();

    }



    private void loadView() {
        mEmail = (EditText) findViewById(com.example.ufoodlibrary.R.id.name_credential);
        mPassword = (EditText) findViewById(com.example.ufoodlibrary.R.id.password_credential);
        mLoginButton = (Button) findViewById(com.example.ufoodlibrary.R.id.login_button);
        mNewAccountButton = (Button) findViewById(com.example.ufoodlibrary.R.id.new_account_button);
        mForgotPasswordButton = (Button) findViewById(com.example.ufoodlibrary.R.id.forgot_password_button);
        mForgotPasswordButton.setPaintFlags(mForgotPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                    mEmail.setError(getResources().getString(R.string.emailerror)); mEmail.setError(getResources().getString(R.string.emailerror));
                    mEmail.requestFocus();
                    return;

                }
                if (mPassword.getText().toString().isEmpty()) {
                    mPassword.setError(getResources().getString(R.string.password_error));
                    mPassword.requestFocus();
                    return;
                }
                final ProgressDialog progress = TransitionHelper.getProgress(mActivity);
                progress.show();

                try {
                    G3Application.fManager.signIn(mEmail.getText().toString(), mPassword.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplication(),getResources().getString(com.example.ufoodlibrary.R.string.successprogress),Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                                onBackPressed();

                            }
                            else{
                                Toast.makeText(getApplication(),getResources().getString(com.example.ufoodlibrary.R.string.errorprogress),Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                } catch (NetworkDownException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            }
        });


        mNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() ,G3UserCreateProfile.class);
                intent.putExtra("checkout",getIntent().getBooleanExtra("checkout",false));
                startActivity(intent);
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G3ForgotPasswordDialogFragment mDialogFrag;
                mDialogFrag = G3ForgotPasswordDialogFragment.newInstance();
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getSupportFragmentManager(), "dialogpasswordforgotten");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}


