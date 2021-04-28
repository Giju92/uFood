package com.example.ufoodrestaurant.dialogfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * Created by Mattia on 09/06/2016.
 */
public class G3ForgotPasswordDialogFragment extends android.support.v4.app.DialogFragment{

    private EditText mEmail;
    private Button mSendEmailButton;

    public static G3ForgotPasswordDialogFragment newInstance() {
        G3ForgotPasswordDialogFragment fragment = new G3ForgotPasswordDialogFragment();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(com.example.ufoodlibrary.R.layout.forgot_password_dialog, container, false);
        setCancelable(true);

        mEmail = (EditText) v.findViewById(R.id.emailedittext);
        mSendEmailButton = (Button) v.findViewById(R.id.sendemailbutton);
        setlisteners();
        return v;

    }

    private void setlisteners() {
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmail.getText().toString().isEmpty()){
                    mEmail.setError(getResources().getString(R.string.emailerror));
                } else{
                    try {
                        G3Application.fManager.recoveryPasswordByEmail(mEmail.getText().toString(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.successprogress), Toast.LENGTH_SHORT).show();

                                }
                                dismiss();
                            }
                        });
                    } catch (NetworkDownException e) {
                        Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
            }
        });
    }
}
