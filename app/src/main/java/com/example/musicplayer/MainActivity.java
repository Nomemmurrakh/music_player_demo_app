package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import com.example.musicplayer.databinding.ActivityMainBinding;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private ActivityMainBinding binding;

    private MediaPlayer mediaPlayer;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        handler = new Handler();

        AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.song);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                mediaPlayer.setDataSource(fd);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnFastForward.setOnClickListener(v -> {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10 * 1000);
            binding.seekBar.setProgress(mediaPlayer.getCurrentPosition() + 10 * 1000);
        });

        binding.btnFastRewind.setOnClickListener(v -> {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10 * 1000);
            binding.seekBar.setProgress(mediaPlayer.getCurrentPosition() - 10 * 1000);
        });

        binding.btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()){
                binding.btnPlayPause.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }else {
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        binding.seekBar.setMax(mp.getDuration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.seekBar.setMin(0);
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.seekBar.setProgress(mediaPlayer.getDuration());
                handler.postDelayed(this, 1000);
            }
        });
    }
}