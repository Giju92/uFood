package com.example.ufoodrestaurant.dialogfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ufoodrestaurant.R;

/**
 * Created by Alfonso-LAPTOP on 09/06/2016.
 */
public class G3ConnectonDownDialogFragment extends android.support.v4.app.DialogFragment {

    private onTryConnect mListener;
    private Button mTryButton;

    public interface onTryConnect{
        void onTryClick();
    }

    public static G3ConnectonDownDialogFragment newInstance(){
        G3ConnectonDownDialogFragment fragment = new G3ConnectonDownDialogFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (onTryConnect) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Listener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_fragment_network_down, container, false);
        getDialog().setTitle(getResources().getString(R.string.network_dwn));
        setCancelable(false);
        mTryButton = (Button) v.findViewById(R.id.trybutton);

        mTryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTryClick();
                dismiss();
            }
        });



        return v;
    }

}
