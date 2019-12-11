package com.renj.imageselect.weight;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imageselect.R;
import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.CommonUtils;

import java.lang.reflect.Field;
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
    private List<ImageModel> resultImages = new ArrayList<>();
    private ClipMorePagerAdapter clipMorePagerAdapter;
    private ImageParamsConfig imageParamsConfig;

    private FixedSpeedScroller mScroller;
    private LoadingDialog loadingDialog;

    private OnClipImageChange onClipImageChange;

    public ImageClipMoreLayout(Context context) {
        this(context, null);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageClipMoreLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView(@LayoutRes int clipMoreLayoutId, OnClipImageChange onClipImageChange) {
        this.onClipImageChange = onClipImageChange;
        View clipMoreLayout = LayoutInflater.from(getContext()).inflate(clipMoreLayoutId, null);
        tvCancel = clipMoreLayout.findViewById(R.id.tv_cancel_more);
        tvClip = clipMoreLayout.findViewById(R.id.tv_clip_more);
        vpClipMore = clipMoreLayout.findViewById(R.id.vp_clip_more);
        addView(clipMoreLayout);

        setListener();

        clipMorePagerAdapter = new ClipMorePagerAdapter();
        vpClipMore.setAdapter(clipMorePagerAdapter);

        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(vpClipMore.getContext(), new AccelerateInterpolator());
            mField.set(vpClipMore, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(getContext());
    }

    private void setListener() {
        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    public void setImageData(List<ImageModel> srcImages) {
        this.srcImages = srcImages;
        if (CommonUtils.isShowLogger())
            CommonUtils.i("裁剪：(" + (currentIndex + 1) + " / " + srcImages.size() + ")");
        tvClip.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
        if (onClipImageChange != null) {
            onClipImageChange.onDefault(tvClip, tvCancel, currentIndex + 1, srcImages.size());
        }
        clipMorePagerAdapter.notifyDataSetChanged();
    }

    private OnImageClipMoreListener onImageClipMoreListener;

    public void setOnImageClipMoreListener(OnImageClipMoreListener onImageClipMoreListener) {
        this.onImageClipMoreListener = onImageClipMoreListener;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (R.id.tv_cancel_more == vId) {
            if (onImageClipMoreListener != null)
                onImageClipMoreListener.cancel();
        } else if (R.id.tv_clip_more == vId) {
            currentIndex += 1;
            ImageClipView focusedChild = clipMorePagerAdapter.getPrimaryItem();
            focusedChild.cut(new ImageClipView.CutListener() {
                @Override
                public void cutFinish(final ImageModel imageModel) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    resultImages.add(imageModel);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (currentIndex < srcImages.size()) {
                                if (CommonUtils.isShowLogger())
                                    CommonUtils.i("裁剪：(" + (currentIndex + 1) + " / " + srcImages.size() + ")");
                                tvClip.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
                                if (onClipImageChange != null) {
                                    onClipImageChange.onClipChange(tvClip, tvCancel, imageModel, resultImages, imageParamsConfig.isCircleClip(), (currentIndex + 1), srcImages.size());
                                }
                            }
                        }
                    });
                    if (srcImages.size() <= resultImages.size()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismiss();
                                if (onImageClipMoreListener != null)
                                    onImageClipMoreListener.finish(resultImages);
                            }
                        });
                    }
                }
            });

            if (currentIndex >= srcImages.size()) {
                loadingDialog.show();
                tvClip.setEnabled(false);
//                    if (onImageClipMoreListener != null)
//                        onImageClipMoreListener.finish(resultImages);
                return;
            }
            mScroller.setDuration(500);// 切换时间，毫秒值
            vpClipMore.setCurrentItem(currentIndex);
        }
    }


    /**
     * 设置裁剪控件参数
     *
     * @param imageParamsConfig
     */
    public void setClipViewParams(@NonNull ImageParamsConfig imageParamsConfig) {
        this.imageParamsConfig = imageParamsConfig;
    }

    public interface OnImageClipMoreListener {
        void cancel();

        void finish(List<ImageModel> clipResult);
    }

    class ClipMorePagerAdapter extends PagerAdapter {
        private ImageClipView mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (ImageClipView) object;
        }

        public ImageClipView getPrimaryItem() {
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
            ImageClipView imageClipView = new ImageClipView(getContext());
            imageClipView.setClipViewParams(imageParamsConfig);
            imageClipView.setImage(srcImages.get(position).path);
            container.addView(imageClipView);
            return imageClipView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
