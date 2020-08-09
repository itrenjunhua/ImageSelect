package com.renj.selecttest.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imagepicker.listener.OnCropImageChange;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.listener.OnSelectedImageChange;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ImagePickerUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.adapter.ImageShowAdapter;
import com.renj.selecttest.utils.Utils;

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
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipMoreMyActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.gv_images)
    GridView gvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.clip_more_activity;
    }

    @Override
    protected void initView() {
        imageShowAdapter = new ImageShowAdapter(this);
        gvImages.setAdapter(imageShowAdapter);

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
            Toast.makeText(ClipMoreMyActivity.this, "请输入张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNum = Utils.parseInteger(textViewContent);
        ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(240)
                .height(320)
                .isOvalCrop(true)
                .cropBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .cropBorderColor(Color.parseColor("#ffffff"))
                .selectedLayoutId(R.layout.my_selected_layout)
                .cropMoreLayoutId(R.layout.my_clip_more_layout)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "imageModel = [" + imageModel + "], isSelected = [" + isSelected + "], selectedList = [" + selectedList + "], selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onCropImageChange(new OnCropImageChange() {
                    @Override
                    public void onCropChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> cropResultList,
                                             boolean isOvalCrop, int cropCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "imageModel = [" + imageModel + "], clipResultList = [" + cropResultList + "], isCircleClip = [" + isOvalCrop + "], clipCount = [" + cropCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
        ImagePickerUtils.start(this, imagePickerParams);
    }
}
