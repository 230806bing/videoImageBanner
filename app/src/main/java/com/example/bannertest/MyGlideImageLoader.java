package com.example.bannertest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MyGlideImageLoader implements ImageLoader{

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {

    }

    private static class GlideImageLoaderHolder {
        /**
         * 单例对象实例
         */
        static final com.example.bannertest.MyGlideImageLoader INSTANCE = new com.example.bannertest.MyGlideImageLoader();
    }

    public static com.example.bannertest.MyGlideImageLoader getInstance() {
        return GlideImageLoaderHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    public MyGlideImageLoader() {
    }


    //无加载中的图
    public void onNetUrl(Context context, ImageView imageView, String url) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)    //加载成功之前占位图
                .error(R.mipmap.ic_launcher)    //加载错误之后的错误图
                .fitCenter()
                .centerCrop()
                .skipMemoryCache(true)    //不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.ALL)    //不使用硬盘本地缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片
        try {
            if (context != null && imageView != null)
                Glide.with(context).load(url)
                        .apply(options)
                        .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //无加载中的图
    public void onNetUrl2(Context context, ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
//                .centerCrop()
                .skipMemoryCache(true)    //不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.ALL)    //不使用硬盘本地缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片
        try {
            if (context != null && imageView != null)
                Glide.with(context).load(url)

                        .apply(options)
                        .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //?x-oss-process=image/resize,m_fixed,h_234,w_234/rounded-corners,r_15/format,png
    public void onRoundSquareImageView(Context context, ImageView imageView, String image, String jiao, String width, String height){
        String url = image+"?x-oss-process=image/resize,m_fixed,h_"+height+",w_"+width+"/rounded-corners,r_"+jiao+"/format,png";
        RequestOptions options = new RequestOptions()
                .fitCenter()
//                .centerCrop()
                .skipMemoryCache(true)    //不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.ALL)    //不使用硬盘本地缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片
        try {
            if (context != null && imageView != null)
                Glide.with(context).load(url)

                        .apply(options)
                        .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 圆角照片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public void onRoundImageview(Context context, String path, ImageView imageView) {
        try {
            if (context != null && imageView != null)
                Glide.with(context)
                        .load(path)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自己写的加载网络图片的方法
     * img_url 图片的网址
     */
    public Bitmap initNetWorkImage(Context mContext, String url) {

        FutureTarget<File> future = Glide.with(mContext)
                .load(url)
                .downloadOnly(500, 500);
        try {
            File param = future.get();
            return BitmapFactory.decodeFile(param.getPath());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
