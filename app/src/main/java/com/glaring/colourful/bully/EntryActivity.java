package com.glaring.colourful.bully;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.glaring.colourful.bully.fs.ak.AdSelectorImpl;
import com.glaring.colourful.bully.fs.ak.IAdInfo;
import com.glaring.colourful.bully.fs.ak.IAdInit;
import com.glaring.colourful.bully.fs.lib.AAAHelper;

public abstract class EntryActivity extends AppCompatActivity implements IAdInit {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示等待界面
        loadWaitView();
        AAAHelper.mAdSelector.adInit(null, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }

    protected abstract String getEntryA();

    protected abstract String getEntryB();

    private void loadWaitView() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final int viewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, dm);
        ProgressBar progress = new ProgressBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewSize, viewSize);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, dm);

        FrameLayout layout = new FrameLayout(this);
        ImageView bg = new ImageView(this);
        bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        bg.setImageResource(R.drawable.qdt);

        layout.addView(bg, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(progress, params);

        setContentView(layout);
    }

    private void gotoMain(final boolean RefOn) {
        final String pkgName = getPackageName();
        final ComponentName entry = RefOn ? new ComponentName(pkgName, getEntryB()) : new ComponentName(pkgName, getEntryA());
        int flag1 = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP;
        startActivity(new Intent().setPackage(pkgName).setComponent(entry).addFlags(flag1));
        finish();
    }

    @Override
    public void onAdInit(IAdInfo adInfo) {
        boolean RefOn = AdSelectorImpl.getImpl(this).IsRefOn();
        gotoMain(RefOn);
    }

}
