package com.renj.selecttest.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：裁剪单张图片
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipSingleActivity extends BaseActivity {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_clip_result)
    ImageView ivClipResult;

    @Override
    protected int getLayoutId() {
        return R.layout.clip_single_activity;
    }

    @Override
    protected void initView() {
        tvSelect.setOnClickListener(v -> singleImage());
    }

    private void singleImage() {
        ImageParamsConfig imageParamsConfig = new ImageParamsConfig
                .Builder()
                .selectCount(1)
                .isShowCamera(true)
                .isClip(true)
                .width(400)
                .height(400)
                .isCircleClip(false)
                .clipBorderWidth(2)
                .maskColor(Color.parseColor("#88000000"))
                .clipBorderColor(Color.parseColor("#ff0000"))
                .build();
        ImageSelectUtils.getInstance().create()
                .imageParamsConfig(imageParamsConfig)
                .openImageSelectPage(this)
                .onResult(resultList -> ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult));
    }

}
