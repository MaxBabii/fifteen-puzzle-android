package app.com.galatcicpazzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    boolean flag = true;
    private int emptyX = 3;
    private int emptyY = 3;
    private RelativeLayout group;
    private ImageButton[][] buttons;
    private int[] tiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        ImageButton exit_btn = findViewById(R.id.exit_button_game);
        ImageButton back_btn = findViewById(R.id.back_button);
        ImageButton restart_btn = findViewById(R.id.restart_button);
        ImageButton info_btn = findViewById(R.id.info_button_game);
        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        info_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
        });
        restart_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
            shuffleImages();
        });
        back_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
            Intent intent = new Intent(GameActivity.this, StartActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        exit_btn.setOnClickListener(view -> {
            view.startAnimation(buttonAnimation);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();
        music();
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            buttons[i / 4][i % 4].setBackgroundResource(R.drawable.empty_cell);
        }
        buttons[emptyX][emptyY].setImageResource(android.R.color.transparent);
        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this, R.color.colorFreeButton));
    }
    private void shuffleImages() {
        generateNumbers();
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != emptyX || j != emptyY) {
                    buttons[i][j].setImageResource(getResources().getIdentifier("cell_" + tiles[index], "drawable", getPackageName()));
                    index++;
                }
            }
        }
    }
    private void generateNumbers() {
        int n = 15;
        Random random = new Random();
        while (n > 1) {
            int randomNum = random.nextInt(n--);
            int temp = tiles[randomNum];
            tiles[randomNum] = tiles[n];
            tiles[n] = temp;
        }
        if (!isSolvable()) {
            generateNumbers();
        } else {
            int index = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i != emptyX || j != emptyY) {
                        buttons[i][j].setImageResource(getResources().getIdentifier("cell_" + tiles[index], "drawable", getPackageName()));
                        index++;
                    }
                }
            }
        }
    }


    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i]) {
                    countInversions++;
                }
            }
        }
        return countInversions % 2 == 0;
    }

    private void loadNumbers() {
        tiles = new int[16];
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            tiles[i] = i + 1;
        }
    }

    private void loadViews() {
        group = findViewById(R.id.group);
        buttons = new ImageButton[4][4];

        for (int i = 0; i < group.getChildCount(); i++) {
            buttons[i / 4][i % 4] = (ImageButton) group.getChildAt(i);
        }
    }

    public void buttonClick(View view) {
        ImageButton button = (ImageButton) view;
        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';
        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            int targetX = emptyX;
            int targetY = emptyY;
            emptyX = x;
            emptyY = y;

            float startX = button.getLeft();
            float startY = button.getTop();
            float endX = buttons[targetX][targetY].getLeft();
            float endY = buttons[targetX][targetY].getTop();

            Animation slideAnimation = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, endX - startX,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, endY - startY
            );
            slideAnimation.setDuration(300);

            slideAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    buttons[targetX][targetY].setImageDrawable(button.getDrawable());
                    buttons[targetX][targetY].setBackgroundResource(R.drawable.empty_cell);
                    button.setImageDrawable(null);
                    button.setBackgroundColor(ContextCompat.getColor(GameActivity.this, R.color.colorFreeButton));
                    checkWin();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            button.startAnimation(slideAnimation);
        }

    }

    private void checkWin() {
        ImageView checkWin = findViewById(R.id.checkWin);
        boolean isWin = false;
        if (emptyX == 3 && emptyY == 3) {
            for (int i = 0; i < group.getChildCount() - 1; i++) {
                if (buttons[i / 4][i % 4].getDrawable() != null &&
                        buttons[i / 4][i % 4].getDrawable().getConstantState() != null &&
                        buttons[i / 4][i % 4].getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(this, getResources().getIdentifier("cell_" + (i + 1), "drawable", getPackageName())).getConstantState())) {
                    isWin = true;
                } else {
                    isWin = false;
                    break;
                }
            }
        }
        if (isWin) {
            checkWin.setVisibility(View.VISIBLE);
            for (int i = 0; i < group.getChildCount(); i++) {
                buttons[i / 4][i % 4].setClickable(false);
            }
        }
    }


    public void music() {
        final Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        ImageButton on = findViewById(R.id.sound_on_game);
        on.setOnClickListener(view -> {
            if (flag) {
                view.startAnimation(buttonAnimation);

                on.setImageResource(R.drawable.butt_sound_off);
                flag = false;
            } else {
                view.startAnimation(buttonAnimation);

                on.setImageResource(R.drawable.butt_sound_on);
                flag = true;
            }
        });
    }
}

