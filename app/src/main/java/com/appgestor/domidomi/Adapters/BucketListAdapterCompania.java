package com.appgestor.domidomi.Adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.appgestor.domidomi.Entities.Companias;
import com.appgestor.domidomi.R;

import java.util.List;

public abstract class BucketListAdapterCompania<T> extends BaseAdapter {

    protected List<Companias> elements;
    protected Activity ctx;
    protected Integer bucketSize;

    /**
     * Basic constructor, takes an Activity context and the list of elements.
     * Assumes a 1 column view by default.
     *  @param ctx
     *            The Activity context.
     * @param elements
     */
    public BucketListAdapterCompania(Activity ctx, List<Companias> elements) {
        this(ctx, elements, 1);
    }

    public BucketListAdapterCompania(Activity ctx, List<Companias> elements, Integer bucketSize) {
        this.elements = elements;
        this.ctx = ctx;
        this.bucketSize = bucketSize;
    }

    /**
     * Calculates the required number of columns based on the actual screen
     * width (in DIP) and the given minimum element width (in DIP).
     *
     * @param minBucketElementWidthDip
     *            The minimum width in DIP of an element.
     */
    public void enableAutoMeasure(float minBucketElementWidthDip) {
        float screenWidth = getScreenWidthInDip();

        if (minBucketElementWidthDip >= screenWidth) {
            bucketSize = 1;
        } else {
            bucketSize = (int) (screenWidth / minBucketElementWidthDip);
        }
    }
    @Override
    public int getCount() {
        return (elements.size() + bucketSize - 1) / bucketSize;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int bucketPosition, View convertView, ViewGroup parent) {
        ViewGroup bucket = (ViewGroup) View.inflate(ctx, R.layout.bucket, null);

        for (int i = (bucketPosition * bucketSize); i < ((bucketPosition * bucketSize) + bucketSize); i++) {
            FrameLayout bucketElementFrame = new FrameLayout(ctx);
            bucketElementFrame.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            bucketElementFrame.setPadding(3, 0, 3, 0);

            if (i < elements.size()) {
                View current = getBucketElement(i, elements.get(i));

                bucketElementFrame.addView(current);
            }

            bucket.addView(bucketElementFrame);
        }

        return bucket;
    }

    /**
     * Extending classes should return a bucket-element with this method. Each
     * row in the list contains bucketSize total elements.
     *
     * @param position
     *            The absolute, global position of the current item.
     * @param currentElement
     *            The current element for which the View should be constructed
     * @return The View that should be presented in the corresponding bucket.
     */
    protected abstract View getBucketElement(final int position, Companias currentElement);

    protected float getScreenWidthInDip() {
        WindowManager wm = ctx.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth_in_pixel = dm.widthPixels;
        float screenWidth_in_dip = screenWidth_in_pixel / dm.density;

        return screenWidth_in_dip;
    }
}

class ViewHolder2 {

    private static FrameLayout card;

    ViewHolder2(View row) {
    }
}