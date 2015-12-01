package com.zhuiji7.colorcard;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhuiji7.colorcard.game.GameView;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) this.findViewById(R.id.button);
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
            return true;
        }else if(id == R.id.action_settings2){
            gameView.setLevel(2);
            return true;
        }else if(id == R.id.action_settings3){
            gameView.setLevel(3);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.startGame();
            }
        });
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void onFinish() {
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


}
