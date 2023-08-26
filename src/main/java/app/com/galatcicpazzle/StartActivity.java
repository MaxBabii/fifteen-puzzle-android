package app.com.galatcicpazzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;

public class StartActivity extends AppCompatActivity {
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        ImageButton exit_btn = findViewById(R.id.exit_button);
        ImageButton start_btn = findViewById(R.id.start_button);
        ImageButton info_btn = findViewById(R.id.info_button);

        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        info_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
        });
        start_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
            Intent intent = new Intent(StartActivity.this, GameActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        exit_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
            finish();
        });

        music();
    }

    public void music(){
        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        ImageButton on = findViewById(R.id.sound_on);
        on.setOnClickListener(view -> {
            if (flag){
                view.startAnimation(buttonAnimation);
                on.setImageResource(R.drawable.butt_sound_off);
                flag = false;
            }else {
                view.startAnimation(buttonAnimation);
                on.setImageResource(R.drawable.butt_sound_on);
                flag = true;
            }
        });
    }
}