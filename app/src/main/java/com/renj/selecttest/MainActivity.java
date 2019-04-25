package com.renj.selecttest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.listener.OnSelectedImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.selecttest.utils.ImageLoaderManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button clickSelect;
    private ImageView imageView;
    private EditText imageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickSelect = findViewById(R.id.click_select);
        imageView = findViewById(R.id.imageview);
        imageNum = findViewById(R.id.et_image_num);

        clickSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        String imageNum = this.imageNum.getText().toString().trim();
        if (TextUtils.isEmpty(imageNum)) {
            Toast.makeText(MainActivity.this, "输入选择的图片张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNumInt = Integer.parseInt(imageNum);
        if (imageNumInt > 1) moreImages(imageNumInt);
        else singleImage();
    }

    // 多张图片
    private void moreImages(int imageNumInt) {
        ImageSelectConfig imageSelectConfig1 = new ImageSelectConfig
                .Builder()
                .width(600)
                .height(800)
                .clipBorderWidth(0.5f)
                .selectCount(imageNumInt)
                .isShowCamera(true)
                .maskColor(Color.parseColor("#80000000"))
                .clipBorderColor(Color.parseColor("#ffffff"))
                .isClip(true)
                .isCircleClip(true)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .build();
        ImageSelectUtils.newInstance().create()
                .selectedLayoutId(R.layout.my_selected_iamge_layout)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Toast.makeText(MainActivity.this, isSelected + " : " + selectedCount, Toast.LENGTH_SHORT).show();
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onClipImageChange(new OnClipImageChange() {
                    @Override
                    public void onClipChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> clipResultList,
                                             boolean isCircleClip, int clipCount, int totalCount) {
                        Log.i("MainActivity", "clipView = [" + clipView + "], cancelView = [" + cancelView + "], imageModel = [" + imageModel + "], clipResultList = [" + clipResultList + "], isCircleClip = [" + isCircleClip + "], clipCount = [" + clipCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .clipConfig(imageSelectConfig1)
                .openImageSelectPage(MainActivity.this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> selectResults) {
                        Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
                        ImageLoaderManager.loadImageForFile(MainActivity.this, selectResults.get(0).path, imageView);
                    }
                });
    }

    // 单张图片
    private void singleImage() {
        ImageSelectConfig imageSelectConfig = new ImageSelectConfig
                .Builder()
                .width(200)
                .height(300)
                .clipBorderWidth(3)
                .selectCount(1)
                .isShowCamera(true)
                .maskColor(Color.parseColor("#88000000"))
                .clipBorderColor(Color.parseColor("#ff0000"))
                .isClip(true)
                .isCircleClip(false)
                .build();
        ImageSelectUtils.newInstance().create()
                .clipConfig(imageSelectConfig)
                .openImageSelectPage(MainActivity.this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> selectResults) {
                        ImageLoaderManager.loadImageForFile(MainActivity.this, selectResults.get(0).path, imageView);
                    }
                });
    }
}