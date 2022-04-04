package sbilife.com.pointofsale_bancaagency;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import sbilife.com.pointofsale_bancaagency.authorization.LoginUserActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);


        Context mcontext = this;
        ImageView img_smartlogo = findViewById(R.id.img_smartlogo);
        ImageView img_head = findViewById(R.id.img_head);
        ImageView img_tail = findViewById(R.id.img_tail);


        Animation bottomup = AnimationUtils.loadAnimation(mcontext, R.anim.bounce);

        Animation slideright = AnimationUtils.loadAnimation(mcontext, R.anim.sliderightsplash);
        Animation slideleft = AnimationUtils.loadAnimation(mcontext, R.anim.slideleft);
        img_smartlogo.startAnimation(bottomup);

        img_head.startAnimation(slideright);

        img_tail.startAnimation(slideleft);

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flipper);
        set.setTarget(img_head);
        set.setStartDelay(2500);
        set.start();

        AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flipper);
        set1.setTarget(img_tail);
        set1.setStartDelay(3500);
        set1.start();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(5500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginUserActivity.class);

                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }


}
