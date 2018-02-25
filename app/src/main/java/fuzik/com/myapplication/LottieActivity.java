package fuzik.com.myapplication;

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

        LottieComposition.Factory.fromAssetFileName(this, "qidonghouxiougai.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                lottie1.setComposition(composition);
            }
        });
        lottie1.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Log.d("---------", "第一段"+animatedFraction + "");
            }
        });
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
                Log.d("---------", "第二段"+animatedFraction + "");
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
