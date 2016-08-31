package com.flickrfeed.adapter;

/**
 * Created by VIJAYAKUMAR MUNIAPPA on 31-08-2016.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.flickrfeed.R;
import com.flickrfeed.networkutils.ImageLoader;
import com.flickrfeed.utils.FeedStructure;


public class GridAdapter extends ArrayAdapter<FeedStructure> {

    private Context mContext;
    private int layoutResourceId;
    ImageLoader imageLodaer;
    private ArrayList<FeedStructure> mGridData = new ArrayList<FeedStructure>();

    public GridAdapter(Context mContext, int layoutResourceId, ArrayList<FeedStructure> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        imageLodaer =  new ImageLoader(mContext);
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<FeedStructure> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
      final  ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

       final FeedStructure item = mGridData.get(position);

        //visible selected textview
        if(item.isSelected()){
            holder.imageView.setVisibility(View.GONE);
            holder.titleTextView.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.setVisibility(View.VISIBLE);
            holder.titleTextView.setVisibility(View.GONE);
        }

     //
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imageView.setVisibility(View.GONE);
                Animation flip = AnimationUtils.loadAnimation(mContext,
                com.flickrfeed.R.anim.grow_from_middle);
                holder.titleTextView.startAnimation(flip);
                holder.titleTextView.setVisibility(View.VISIBLE);
                item.setSelected(true);
            }
        });
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.titleTextView.setVisibility(View.GONE);
                Animation flip = AnimationUtils.loadAnimation(mContext, R.anim.grow_from_middle);
                holder.imageView.startAnimation(flip);
                holder.imageView.setVisibility(View.VISIBLE);
                item.setSelected(false);

            }
        });
        //DisplayImage function from ImageLoader Class
        imageLodaer.DisplayImage(item.getmImageUrl(),holder.imageView);

        //Set title to textview
        holder.titleTextView.setText(item.getmImageName());
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}