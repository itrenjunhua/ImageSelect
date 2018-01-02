package com.renj.imageselect.weight;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-02   14:55
 * <p>
 * 描述：多张图片裁剪控件封装
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageClipMoreLayout extends LinearLayout implements View.OnClickListener {
    private TextView tvCancel;
    private TextView tvClip;
    private NoScrollViewPager vpClipMore;

    private int currentIndex = 0;
    private List<ImageModel> srcImages = new ArrayList<>();
    private List<ImageModel> resoutImages = new ArrayList<>();
    private ClipMorePagerAdapter clipMorePagerAdapter;

    public ImageClipMoreLayout(Context context) {
        this(context, null);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageClipMoreLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipMoreLayout = LayoutInflater.from(context).inflate(R.layout.image_clip_more_layout, null);
        tvCancel = clipMoreLayout.findViewById(R.id.tv_cancel_more);
        tvClip = clipMoreLayout.findViewById(R.id.tv_clip_more);
        vpClipMore = clipMoreLayout.findViewById(R.id.vp_clip_more);
        addView(clipMoreLayout);

        setListener();

        clipMorePagerAdapter = new ClipMorePagerAdapter();
        vpClipMore.setAdapter(clipMorePagerAdapter);
    }

    private void setListener() {
        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    public void setImageData(List<ImageModel> srcImages) {
        this.srcImages = srcImages;
        clipMorePagerAdapter.notifyDataSetChanged();
    }

    private OnImageClipMoreListener onImageClipMoreListener;

    public OnImageClipMoreListener getOnImageClipMoreListener() {
        return onImageClipMoreListener;
    }

    public void setOnImageClipMoreListener(OnImageClipMoreListener onImageClipMoreListener) {
        this.onImageClipMoreListener = onImageClipMoreListener;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.tv_cancel_more:
                if (onImageClipMoreListener != null)
                    onImageClipMoreListener.cancel();
                break;
            case R.id.tv_clip_more:
                ImageClipLayout focusedChild = clipMorePagerAdapter.getPrimaryItem();
                ImageModel imageModel = focusedChild.cut();
                resoutImages.add(imageModel);

                currentIndex += 1;
                if (currentIndex >= srcImages.size()) {
                    if (onImageClipMoreListener != null)
                        onImageClipMoreListener.finish(resoutImages);
                    return;
                }
                vpClipMore.setCurrentItem(currentIndex);
                tvClip.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
                break;
            default:
                break;
        }
    }

    public interface OnImageClipMoreListener {
        void cancel();

        void finish(List<ImageModel> clipResult);
    }

    class ClipMorePagerAdapter extends PagerAdapter {
        private ImageClipLayout mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (ImageClipLayout) object;
        }

        public ImageClipLayout getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return srcImages.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageClipLayout imageClipLayout = new ImageClipLayout(getContext());
            imageClipLayout.setImage(srcImages.get(position).path);
            container.addView(imageClipLayout);
            return imageClipLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}