package com.viztushar.osumwalls.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.activities.ApplyWallpaper;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Utils;
import com.viztushar.osumwalls.tasks.ColorGridTask;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.MyViewHolder> {

    private List<WallpaperItem> images;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView name,author;
        public RelativeLayout realBackground;
        public ProgressBar pb;
        public View mainView;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.wall_grid_art);
            author = (TextView) view.findViewById(R.id.wall_grid_desc);
            realBackground = (RelativeLayout) view.findViewById(R.id.wall_real_background);
            name = (TextView) view.findViewById(R.id.wall_grid_name);
            pb = (ProgressBar) view.findViewById(R.id.progressBar_wall_grid);
            mainView = view;
        }
    }


    public WallAdapter(Context context, List<WallpaperItem> images) {
        mContext = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_thum, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        WallpaperItem image = images.get(position);
        holder.name.setText(images.get(position).getName());
        holder.author.setText(images.get(position).getAuthor());
        if(Utils.darkTheme) holder.realBackground.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
        Glide.with(mContext)
                .load(image.getThumb())
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.pb.setVisibility(View.GONE);
                        holder.realBackground.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
                        new ColorGridTask(mContext, resource, holder).execute();
                        return false;
                    }
                })
                .thumbnail(0.2f)
                .into(holder.thumbnail);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bundle = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) mContext,holder.thumbnail,"wall")
                            .toBundle();
                }*/

                mContext.startActivity(
                        new Intent(mContext, ApplyWallpaper.class).putExtra(ApplyWallpaper.EXTRA_WALLPAPER, images.get(holder.getAdapterPosition())),
                        ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle()
                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    /*
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    */
}