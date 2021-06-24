package com.example.planner.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

// Setting images with Glide using Image URL
public class ImageBindingAdapter {
    @BindingAdapter("android:srcc")
    public static void bindThumbnail(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext()).load(imageURL).dontAnimate().into(imageView);
    }

    @BindingAdapter("android:src")
    public static void bindSearchThumbnail(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext()).load(imageURL).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
