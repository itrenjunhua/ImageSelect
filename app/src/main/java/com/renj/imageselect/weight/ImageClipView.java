package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-28   17:07
 * <p>
 * 描述：图片裁剪控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageClipView extends RelativeLayout {

    private PhotoView photoView;
    private ClipView clipView;

    public ImageClipView(Context context) {
        this(context, null);
    }

    public ImageClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageClipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipViewLayout = LayoutInflater.from(context).inflate(R.layout.image_clip_view, null);
        photoView = clipViewLayout.findViewById(R.id.photo_view);
        clipView = clipViewLayout.findViewById(R.id.clip_view);

        addView(clipViewLayout);
    }

    public void setImage(@NonNull String path) {
        Glide.with(getContext()).load(path).into(photoView);
    }

    public void setImage(@NonNull Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);
    }

    ImageModel imageModel;

    public ImageModel cut() {
        clipView.confirmCrop(new ClipView.OnCropRangeListener() {
            @Override
            public void cropRange(ClipView.CropShape cropShape, RectF cropRectF) {
                imageModel = photoView.clipBitmap(cropShape, cropRectF);
            }
        });
        return imageModel;
    }
}