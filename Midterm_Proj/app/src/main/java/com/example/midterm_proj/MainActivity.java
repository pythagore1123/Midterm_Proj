package com.example.midterm_proj;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.midterm_proj.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageViewModel mImageViewModel;
    private ActivityMainBinding binding;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final Context mContext = this;

    public void onRequestPermissionsResult (int requestCode,
                                    String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults != null && grantResults.length == 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initializeViewModel();
                } else {
                    Toast.makeText(MainActivity.this, "Cấp quyền thất bại", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), new ArrayList<Image>());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                123);

        initializeViewModel();
        initSize();

    }

    private void initSize(){
        WindowManager w = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        w.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        SizeConfig.init(height, width);

    }

    void initializeViewModel() {
        //ViewModel - observer
        mImageViewModel = new ImageViewModel(this.getApplication());
        final Observer<List<Image>> imageObserver = new Observer<List<Image>> () {
            @Override
            public void onChanged(@Nullable List<Image> imageList) {
                Log.d("ImageList", "onChanged: " + imageList.size());
                mSectionsPagerAdapter.setImageList(imageList);
            }
        };
        mImageViewModel.getAllImages().observe(this, imageObserver);
    }
}