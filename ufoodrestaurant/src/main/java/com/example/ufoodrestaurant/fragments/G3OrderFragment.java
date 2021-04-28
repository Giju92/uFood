package com.example.ufoodrestaurant.fragments;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.CustomComparator;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.example.ufoodrestaurant.activities.G3TabsActivity;
import com.example.ufoodrestaurant.adapters.G3OrderRestaurantAdapter;
import com.example.ufoodrestaurant.dialogfragments.G3OrderDetailsDialogFragment;
import com.example.ufoodrestaurant.dialogfragments.G3OrdersFilterDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Alfonso on 02-Apr-16.
 */
public class G3OrderFragment extends Fragment implements G3FragmentLifecycle {


    private Context mCtx;

    private TextView tutorial;
    private Toolbar toolbar;
    private View v;
    private Button floatingButton;
    private TextView mTypeFilterText;
    private TextView mStatusFilterText;
    private TextView mTypeFilterLabel;
    private TextView mStatusFilterLabel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3OrderRestaurantAdapter mAdapter;
    private LinkedList<G3OrderObj> mdataSet;
    private LinkedList<G3UserObj> mUsersDataSet;
    private G3OrderDetailsDialogFragment mDialogFrag;
    private G3OrdersFilterDialogFragment mFilterDialogFrag;
    private CountDownLatch latch ;
    private ProgressDialog progressD;
    static final String ORDERS = "Orders";
    private String typefilter;
    private String statusfilter;

    private boolean firstCaching;
    public static boolean visible;
    public static boolean refreshNeeded;
    private BroadcastReceiver mReceiver;

    public G3OrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCtx = this.getContext();
        mdataSet = new LinkedList<>();
        mUsersDataSet = new LinkedList<>();

        firstCaching = true;
        visible = false;
        refreshNeeded = false;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadData();
            }
        };

        v = inflater.inflate(R.layout.order_restaurant_fragment, container, false);

        tutorial = (TextView) v.findViewById(R.id.noorderstext);
        toolbar =  (Toolbar) this.getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingButton = (Button) v.findViewById(R.id.floting_button);

        mTypeFilterText = (TextView) v.findViewById(R.id.help_button);
        mTypeFilterText.setVisibility(View.VISIBLE);
        mStatusFilterText = (TextView) v.findViewById(R.id.more_button);
        mStatusFilterText.setVisibility(View.VISIBLE);
        mTypeFilterLabel = (TextView) v.findViewById(R.id.textView5);
        mTypeFilterLabel.setVisibility(View.VISIBLE);
        mStatusFilterLabel = (TextView) v.findViewById(R.id.textView6);
        mStatusFilterLabel.setVisibility(View.VISIBLE);

        Drawable top = getResources().getDrawable(com.example.ufoodlibrary.R.drawable.floating_eye);
        floatingButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);

        setFilters("all", "pending");

        loadData();

        return v;
    }

    @Override
    public void onPauseFragment() {
        visible = false;
    }

    @Override
    public void onResumeFragment() {
        visible = true;
        if (refreshNeeded) {
            refreshNeeded = false;
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter("RELOAD_DATA"));
        if(!firstCaching && G3TabsActivity.getCurrentFragmentPosition() == 1) {
            onResumeFragment();
        }
        firstCaching = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
        onPauseFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData() {

        mdataSet.clear();
        mUsersDataSet.clear();

        progressD = TransitionHelper.getProgress(this.getActivity());
        progressD.show();

        // Set unread notification to zero
        final SharedPreferences prefs = getActivity().getSharedPreferences(G3Application.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(G3Application.UNREAD_NOTIFICATION, 0);
        editor.commit();

        // Cancel eventually displayed push notification
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(10);

        try {
            G3Application.fManager.getRestaurantOrders(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("ORders_retive", dataSnapshot.getKey());
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("ORders_retive", data.getKey());
                        Log.d("ORders_retive", data.getValue().toString());
                        G3OrderObj order = data.getValue(G3OrderObj.class);

                        if(DateUtils.isToday(order.getDate().getTime())) {
                            boolean orderStatusOk = false;

                            switch (statusfilter) {
                                case "all":
                                    orderStatusOk = true;
                                    break;
                                case "pending":
                                    if (order.getOrderState() == 0) {
                                        orderStatusOk = true;
                                    }
                                    break;
                                case "cancelled":
                                    if (order.getOrderState() == 2) {
                                        orderStatusOk = true;
                                    }
                                    break;
                                case "done":
                                    if (order.getOrderState() == 1) {
                                        orderStatusOk = true;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            if (orderStatusOk) {
                                switch (typefilter) {
                                    case "all":
                                        mdataSet.add(order);
                                        break;
                                    case "takeaway":
                                        if (order.getSeats() == 0) {
                                            mdataSet.add(order);
                                        }
                                        break;
                                    case "tablebooking":
                                        if (order.getSeats() > 0) {
                                            mdataSet.add(order);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                    }
                    getUsers();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            progressD.dismiss();
        }

    }

    private void setFilters(String type, String status) {
        typefilter = type;
        statusfilter = status;

        switch(typefilter) {
            case "all":
                mTypeFilterText.setText(R.string.all);
                break;
            case "takeaway":
                mTypeFilterText.setText(R.string.takeaway);
                break;
            case "tablebooking":
                mTypeFilterText.setText(R.string.table_booking);
                break;
            default:
                break;
        }

        switch(statusfilter) {
            case "all":
                mStatusFilterText.setText(R.string.all);
                break;
            case "pending":
                mStatusFilterText.setText(R.string.pending_status);
                break;
            case "cancelled":
                mStatusFilterText.setText(R.string.rejected_status);
                break;
            case "done":
                mStatusFilterText.setText(R.string.done_status);
            default:
                break;
        }

    }

    private void saveOrderViewed(G3OrderObj order) {
        order.setVisualized(true);

        try {
            G3Application.fManager.setOrderViewed(order.getOrderid());
        } catch (NetworkDownException e) {

        }
    }

    private void getUsers() {

        latch = new CountDownLatch(mdataSet.size());

        for(final G3OrderObj order: mdataSet){

            try {
                G3Application.fManager.getUserProfileWithKey(order.getUserid(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        G3UserObj temp = dataSnapshot.getValue(G3UserObj.class);
                        mUsersDataSet.add(temp);

                        latch.countDown();
                        Log.d("latch", "contdown");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch ( NetworkDownException e) {
                e.printStackTrace();
            }

        }


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("latch", "wait");

                    latch.await();
                    Log.d("latch", "release");
                    if (progressD.isShowing())
                        progressD.dismiss();
                    initData();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    private void initData() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (progressD.isShowing())
                    progressD.dismiss();

                Collections.sort(mdataSet, new CustomComparator());
                mRecyclerView = (RecyclerView) v.findViewById(R.id.orderresrecycler);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);

//                ItemDecoration idec = new ItemDecoration(getContext());
//                mRecyclerView.addItemDecoration(idec);

                mAdapter = new G3OrderRestaurantAdapter(mdataSet, mUsersDataSet, getContext());
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new G3OrderRestaurantAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(View v, G3OrderObj order, G3UserObj user, int position) {
                        saveOrderViewed(mdataSet.get(position));
                        mDialogFrag = G3OrderDetailsDialogFragment.newInstance(order, user, position, mCtx);
                        mDialogFrag.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                        mDialogFrag.show(getFragmentManager(), "dialogewiewitem");
                    }

                    @Override
                    public void onCancelButtonClick(int position) {
                        cancelOrder(mdataSet.get(position));
                        mAdapter.mDataset.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.mDataset.size());
                        if (mdataSet.size() == 0) {
                            tutorial.setVisibility(View.VISIBLE);
                            tutorial.setText(getResources().getString(R.string.tutorial_no_orders));
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            tutorial.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onEvadeButtonClick(int position) {
                        evadeOrder(mdataSet.get(position));
                        mAdapter.mDataset.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.mDataset.size());
                        if (mdataSet.size() == 0) {
                            tutorial.setVisibility(View.VISIBLE);
                            tutorial.setText(getResources().getString(R.string.tutorial_no_orders));
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            tutorial.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

                if (mdataSet.size() == 0) {
                    tutorial.setVisibility(View.VISIBLE);
                    tutorial.setText(getResources().getString(R.string.tutorial_no_orders));
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    tutorial.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                floatingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showdialog();
                    }
                });
            }
        });

    }

    private void showdialog() {
        mFilterDialogFrag = G3OrdersFilterDialogFragment.newInstance(mCtx, typefilter, statusfilter);
        mFilterDialogFrag.setTargetFragment(this,1);
        mFilterDialogFrag.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        mFilterDialogFrag.show(getFragmentManager(), "dialogewiewitem");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setFilters(data.getStringExtra("type"), data.getStringExtra("status"));
        loadData();
    }

    private void cancelOrder(G3OrderObj order) {
        order.setOrderState(2);

        try {
            G3Application.fManager.setOrderDeleted(order.getOrderid());
        } catch (NetworkDownException e) {

        }
    }

    private void evadeOrder(G3OrderObj order) {
        order.setOrderState(1);

        try {
            G3Application.fManager.setOrderEvaded(order.getOrderid());
        } catch (NetworkDownException e) {

        }

    }

}