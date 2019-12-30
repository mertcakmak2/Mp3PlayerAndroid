package com.example.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Spinner spinner ;
    Button playBtn;
    TextView songLabel;
    ImageView img;
    SeekBar positionBar;
    MediaPlayer mp;
    int totalTime;
    int songPosition;

    public String [] songsName = {"gunna", "lsd", "canttouchthis", "ceza"};
    public Integer [] songsImage = {R.drawable.gunna, R.drawable.lsd, R.drawable.canttouchthis, R.drawable.ceza};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songLabel = findViewById(R.id.songLabel);
        spinner = findViewById(R.id.playList);
        playBtn = (Button) findViewById(R.id.playBtn);
        img  = findViewById(R.id.songImage);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,songsName);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                img.setBackgroundResource(songsImage[position]);
                String item = parent.getSelectedItem().toString();
                int rawId = getResources().getIdentifier(item, "raw", getPackageName());
                songPosition = rawId;
                ChangeSong(rawId);
                ChangeLabel(parent.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mp = MediaPlayer.create(this, R.raw.gunna);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime = mp.getDuration();

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp != null){
                    try{
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch(InterruptedException e){}
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);

        }
    };

    public void ChangeSong(int position){
        if(mp.isPlaying()){
            mp.stop();
            mp = MediaPlayer.create(this, position);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f,0.5f);
            totalTime = mp.getDuration();
            mp.start();
        }
        else{
            mp = MediaPlayer.create(this, position);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f,0.5f);
            totalTime = mp.getDuration();
            mp.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        }
    }
    public void playBtnClick(View view){
        if(!mp.isPlaying()){
            mp.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        }
        else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    public void pauseBtnClick(View view){
            mp.stop();
            mp = MediaPlayer.create(this, songPosition);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f,0.5f);
            playBtn.setBackgroundResource(R.drawable.play);
    }
    public void ChangeLabel(String name){
        switch (name){
            case "gunna":
                songLabel.setText("The Phantom");
                break;
            case "lsd":
                songLabel.setText("LSD - Genius");
                break;
            case "canttouchthis":
                songLabel.setText("MC HAMMER - U Can't Touch This");
                break;
            case "ceza":
                songLabel.setText("CEZA - Ben Aglamazken");
                break;
        }
    }
}
