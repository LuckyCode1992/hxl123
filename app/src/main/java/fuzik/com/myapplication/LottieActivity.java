package fuzik.com.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

import butterknife.BindView;
import butterknife.OnClick;

public class LottieActivity extends UIBaseActivity {

    @BindView(R.id.lottie1)
    LottieAnimationView lottie1;
    @BindView(R.id.lottie2)
    LottieAnimationView lottie2;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_lottie);

        //初始化，或者叫引入json
        LottieComposition.Factory.fromAssetFileName(this, "qidonghouxiougai.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                //这句话是关键，相当于是将控件和json绑定
                lottie1.setComposition(composition);
            }
        });
        //添加动画更新（进度）的监听，这里的监听可以理解为播放进度（注意：这个进度并不一定是等分的（0.1,0.2,0.3.。。）可能是0.12354,0.3214））
        lottie1.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Log.d("---------", "第一段" + animatedFraction + "");
            }
        });
        //添加动画状态监听（开始，结束，取消，重放）
        lottie1.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                //动画开始播放的时候会调用
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //动画结束的时候会调用
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //动画主动取消的时候会调用
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //loop的时候会调用，注意，是重新播放的时候会调用
            }
        });

        //上述监听对应移除方法
//        lottie1.removeAnimatorListener();
//        lottie1.removeUpdateListener();
        if (false) {


            //动画的播放
            //设置动画的起始播放位置，只能设置开始位置，不能设置结束位置，不写默认从0开始
            lottie1.setProgress(0);
            //播放
            lottie1.setVisibility(View.VISIBLE);//如果控件有显示和隐藏的操作，这句话写在播放前面
            lottie1.playAnimation();
            //暂停
            lottie1.pauseAnimation();//暂停的实质是，保存当前的Progress，然后在cancel这个动画，下次播放的时候，从保存的progress开始播放
            //结束播放，取消播放，终止播放
            lottie1.cancelAnimation();
            lottie1.setVisibility(View.INVISIBLE);//如果控件有显示和隐藏的操作，这句话写在取消的后面
            //不断重复播放，轮播
            lottie1.loop(true);
            //倒放，回放
            lottie1.reverseAnimation();
            //动画是否在播放中
            boolean animating = lottie1.isAnimating();
            //动画播放进度
            long duration = lottie1.getDuration();
            //特别注意，在当前activity销毁时，保持好的习惯
            lottie1.cancelAnimation();
//        lottie1.removeUpdateListener();//如果有监听的话
//        lottie1.removeAnimatorListener();//如果有监听的话


        }

        LottieComposition.Factory.fromAssetFileName(this, "qidonghouxiougai2.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                lottie2.setComposition(composition);
            }
        });
        lottie2.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Log.d("---------", "第二段" + animatedFraction + "");
            }
        });

    }

    @OnClick({R.id.but1, R.id.but2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but1:
                lottie1.playAnimation();
                break;
            case R.id.but2:
                lottie2.playAnimation();
                break;
        }
    }
}
