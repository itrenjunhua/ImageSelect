package com.renj.selecttest.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.imageselect.utils.Logger;
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
 * 描述：使用自定义样式裁剪单张图片
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipSingleMyActivity extends BaseActivity {
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
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleImage();
            }
        });
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
        ImageSelectUtils.newInstance().create()
                // .selectedLayoutId(R.layout.my_selected_layout) // 自定义选择图片布局
                .clipSingleLayoutId(R.layout.my_clip_single_layout) // 自定义单张裁剪部分布局
                .onClipImageChange(new OnClipImageChange() {
                    @Override
                    public void onClipChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> clipResultList,
                                             boolean isCircleClip, int clipCount, int totalCount) {
                        // clipView.setText(clipCount + "/" + totalCount + "裁剪");
                        Logger.i("imageModel = [" + imageModel + "], clipResultList = [" + clipResultList + "], isCircleClip = [" + isCircleClip + "], clipCount = [" + clipCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .imageParamsConfig(imageParamsConfig)
                .openImageSelectPage(this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult);
                    }
                });
    }
}
