package com.polito.group3.ufoodfusion.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alfonso-LAPTOP on 27/04/2016.
 */
public class G3FilterAdapter extends RecyclerView.Adapter<G3FilterAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<String> mData;
    private onFilterSelect mListener;
    protected RecyclerView.LayoutManager mLayout;
    public int selected_item = 0;

    public void add(String s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public interface onFilterSelect{

        public void onFilterClick(String filter);

    }

    public void setOnfilterSelectListener(final onFilterSelect listener){
        this.mListener = listener;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public final TextView title;
        public LinearLayout mView;

        public SimpleViewHolder(View view) {
            super(view);
            mView = (LinearLayout) view;
            title = (TextView) view.findViewById(R.id.simple_text);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView vv = (TextView) v.findViewById(R.id.simple_text);
            mListener.onFilterClick(vv.getText().toString());
            selected_item = getAdapterPosition();
            mLayout.scrollToPosition(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

    public G3FilterAdapter(Context context, String[] data, LinearLayoutManager mLayout) {
        mContext = context;
        this.mLayout = mLayout;
        if (data != null)
            mData = new ArrayList<String>(Arrays.asList(data));
        else mData = new ArrayList<String>();
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_fragment_filter_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.title.setText(mData.get(position));
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        holder.title.setWidth(width/3);
        if(position == selected_item)
        {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        else
        {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
