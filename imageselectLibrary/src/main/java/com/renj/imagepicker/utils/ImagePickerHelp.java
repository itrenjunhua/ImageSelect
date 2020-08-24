package com.renj.imagepicker.utils;

import android.widget.ImageView;

import com.renj.imagepicker.listener.ImageLoaderModule;
import com.renj.imagepicker.listener.OnResultCallBack;

import org.jetbrains.annotations.Contract;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-07-24   16:41
 * <p>
 * 描述：图片加载工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerHelp {
    private volatile static ImagePickerHelp instance = new ImagePickerHelp();

    private ImagePickerHelp() {
    }

    @Contract(pure = true)
    public static ImagePickerHelp getInstance() {
        return instance;
    }

    /*********************图片加载器************************/
    private ImageLoaderModule imageLoaderModule;

    void setImageLoaderModule(ImageLoaderModule imageLoaderModule) {
        this.imageLoaderModule = imageLoaderModule;
    }

    /**
     * 加载图片方法
     *
     * @param path      图片路径
     * @param imageView {@link ImageView} 控件
     */
    public void loadImage(String path, ImageView imageView) {
        if (imageLoaderModule != null)
            imageLoaderModule.loadImage(path, imageView);
    }

    /*********************监听************************/

    private OnResultCallBack onResultCallBack; // 结果回调

    public OnResultCallBack getOnResultCallBack() {
        return onResultCallBack;
    }

    public void setOnResultCallBack(OnResultCallBack onResultCallBack) {
        this.onResultCallBack = onResultCallBack;
    }
}
