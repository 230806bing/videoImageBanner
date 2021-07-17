package com.example.bannertest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    protected ViewPager mViewpager;
    private List<View> mViewList = new ArrayList<>();
    List<Banner> ads=new ArrayList<>();
    String videoUrl = "android.resource://" + "com.example.videoimagebanner" + "/" + R.raw.test;
    int tag=0;//判断视频是否处于播放状态  0播放  1没播放
    private final Timer timer = new Timer();
    private TimerTask task;
    VideoView videoView;
    MediaController mediaController;
    TextView textView;
    TextView textView2;



    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ToolClass.hide(this);
        initViewpager();
        String uri1 = "android.resource://" + getPackageName() + "/" + R.drawable.img1;
        String uri2 = "android.resource://" + getPackageName() + "/" + R.drawable.img2;
        String uri3 = "android.resource://" + getPackageName() + "/" + R.drawable.img3;
        String uri = "android.resource://" + getPackageName() + "/" + R.drawable.img4;
        Banner banner = new Banner(uri, "1");
        Banner banner1 = new Banner(uri1, "2");
        Banner banner2 = new Banner(uri2, "2");
        Banner banner3 = new Banner(uri3, "2");
        ads.add(banner);
        ads.add(banner1);
        ads.add(banner2);
        ads.add(banner3);

        setBannerview(ads);

        tag = 0; //默认播放

    }

    private void initViewpager() {
        mViewpager = findViewById(R.id.viewPager);


        //添加viewpager监听  https://www.cnblogs.com/Dionexin/p/5727297.html
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                //默认首个就是视频文件，其他都为图片文件，默认开始自动轮播
                if (arg0==0){
                    videoView.pause();
                    tag=1;
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            tag=1;
                        }
                    });
                }else {//切换到别的界面后 不播放
                    videoView.pause();
                    tag=1;
                }
                textView2 = new TextView(getBaseContext());
                textView2 = findViewById(R.id.num);
                textView2.setText(mViewpager.getCurrentItem()+1 + "/4");
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        //设置自动轮播时长
        //Timer是jdk中提供的一个定时器工具，使用的时候会在主线程之外起一个单独的线程执行指定的计划任务，可以指定执行一次或者反复执行多次。
        //　　TimerTask是一个实现了Runnable接口的抽象类，代表一个可以被Timer执行的任务。
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 3000, 3000);
    }

    //自动轮播的实现 通过handler处理信息
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (tag==1){//如果视频没在播放，则自动轮播
                if(mViewpager.getCurrentItem()==3){//切到最后一个 重新回到第一个
                    mViewpager.setCurrentItem(0);
                }else {
                    mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
                }
            }
        }
    };

//设置轮播视图
    private void setBannerview(List<Banner> ads) {
//区分图片和视频布局
        for (int i=0;i<ads.size();i++){
            if (ads.get(i).getTag().equals("1")) {
                View view = LayoutInflater.from(this).inflate(R.layout.view_guide_video, null);
//                //使用videoview播放视频
                textView = new TextView(this);
                textView = view.findViewById(R.id.start);
                videoView = new VideoView(this);
                videoView = view.findViewById(R.id.video);
                videoView.setVideoURI(Uri.parse(videoUrl));
                mediaController = new MediaController(this);
                mediaController.setMediaPlayer(videoView);
                mediaController.setVisibility(View.GONE);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        tag=1;
                        textView.setText("▶");
                        videoView.setBackground(getResources().getDrawable(R.drawable.img4));
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.start();
                        videoView.setBackground(null);
                        tag=0;
                        textView.setText("");
                    }
                });
                mViewList.add(view);
            } else {
                View view = LayoutInflater.from(this).inflate(R.layout.view_guide_img, null);
                ImageView imageview = view.findViewById(R.id.iv_center);
                MyGlideImageLoader.getInstance().onNetUrl2(this, imageview, ads.get(i).getUrl());
                mViewList.add(view);
            }
        }
        mViewpager.setAdapter(new BannerAdapter(mViewList));

    }



    class Banner {
        String url;
        String tag;

        public Banner(String url, String tag) {
            this.url = url;
            this.tag = tag;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }


}

