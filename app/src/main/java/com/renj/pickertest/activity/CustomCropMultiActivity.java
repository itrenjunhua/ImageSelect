package com.renj.pickertest.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imagepicker.ImagePageStyle;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.imagepicker.custom.ImagePickerCropLayout;
import com.renj.imagepicker.custom.ImagePickerLayout;
import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.model.ImagePickerTouchType;
import com.renj.pickertest.R;
import com.renj.pickertest.adapter.ImageShowAdapter;
import com.renj.pickertest.custom.CustomImageCropMultiView;
import com.renj.pickertest.custom.CustomImagePickerView;
import com.renj.pickertest.utils.Utils;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CustomCropMultiActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.crop_multi_activity;
    }

    @Override
    protected void initView() {
        imageShowAdapter = new ImageShowAdapter(this);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        rvImages.setAdapter(imageShowAdapter);

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreImages();
            }
        });
    }

    private void moreImages() {
        String textViewContent = Utils.getTextViewContent(etCount);
        if (Utils.isEmpty(textViewContent)) {
            Toast.makeText(CustomCropMultiActivity.this, "请输入张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNum = Utils.parseInteger(textViewContent);
        ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .maxCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(240)
                .height(320)
                .isOvalCrop(true)
                .cellLineCount(0)
                .touchHandlerType(ImagePickerTouchType.TOUCH_NONE)
                .cropBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .cropBorderColor(Color.parseColor("#ffffff"))
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImagePickerModel> resultList) {
                        imageShowAdapter.setItemDataList(resultList);
                    }
                })
                .build();
        ImagePageStyle pageStyle = new ImagePageStyle.Builder()
                .statusBarDark(true)
                .statusBarColor(Color.WHITE)
                .build();
        ImagePickerUtils.start(this, pageStyle, imagePickerParams, new ImagePickerViewModule() {
            @Override
            public ImagePickerLayout onCreateImagePickerView(AppCompatActivity activity) {
                return new CustomImagePickerView(activity);
            }

            @Override
            public ImagePickerCropLayout onCreateImagePickerCropMultiView(AppCompatActivity activity) {
                // CustomImageCropMultiView extends ImagePickerCropLayout
                return new CustomImageCropMultiView(activity);
            }
        });
    }
}
