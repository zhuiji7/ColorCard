package com.zhuiji7.colorcard;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuiji7.colorcard.game.GameView;

public class MainActivity extends AppCompatActivity {
    private TextView time_tv;
    private GameView gameView;

    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time_tv = (TextView)this.findViewById(R.id.time_tv);
        gameView = (GameView) this.findViewById(R.id.game_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings1) {
            gameView.setLevel(1);
            time_tv.setText("开始");
            time_tv.setClickable(true);
            countDownTimer.cancel();
            return true;
        }else if(id == R.id.action_settings2){
            gameView.setLevel(2);
            time_tv.setText("开始");
            time_tv.setClickable(true);
            countDownTimer.cancel();
            return true;
        }else if(id == R.id.action_settings3){
            gameView.setLevel(3);
            time_tv.setText("开始");
            time_tv.setClickable(true);
            countDownTimer.cancel();
            return true;
        }else if(id == R.id.action_settings4){
            Uri uri = Uri.parse(getString(R.string.project_url));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListener() {
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_tv.setClickable(false);
                gameView.showFace();
                showCountDownTimer(20);
            }
        });
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void onFinish() {
                time_tv.setClickable(true);
                time_tv.setText("开始");
                Toast.makeText(MainActivity.this, "恭喜你完成游戏", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                volumevDown();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                volumevUP();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void volumevUP() {
        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        int nowVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (nowVolume < maxVolume) {
            nowVolume++;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nowVolume, 0);
        }

    }


    private void volumevDown() {
        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        int nowVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (nowVolume > 0) {
            nowVolume--;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nowVolume, 0);
        }
    }

    private void showCountDownTimer(int countDown){
        countDownTimer = new CountDownTimer(countDown * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_tv.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                gameView.startGame();
            }
        };
        countDownTimer.start();
    }



}
