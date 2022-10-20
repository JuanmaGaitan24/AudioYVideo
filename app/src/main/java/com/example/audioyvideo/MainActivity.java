package com.example.audioyvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnplay, btnstop, btnpause;
    TextView txtEstado;
    MediaPlayer mediaPlayer;
    VideoView videoView;
    MediaController mediaController;
    BarraMusica barraMusica = null;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnplay = findViewById(R.id.buttonplay);
        btnpause = findViewById(R.id.buttonPause);
        btnstop = findViewById(R.id.buttonStop);
        txtEstado = findViewById(R.id.textViewestado);
        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);

        //Audio
        mediaPlayer = MediaPlayer.create(this,R.raw.musica);

        //Video
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.magia;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(mediaController);
        //videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                mediaController.show();
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()){
                    txtEstado.setText("Ya esta Sonando");
                }
                else {
                    if (barraMusica == null){
                        barraMusica = new BarraMusica(seekBar, mediaPlayer);
                        barraMusica.execute();
                    }
                    mediaPlayer.start();
                    txtEstado.setText("La cancion esta sonando");
                }

            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    txtEstado.setText("Cancion en Stop");

                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    txtEstado.setText("Ninguna cancion sonando");
                }
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    txtEstado.setText("Cancion Pausada");
                }else {
                    txtEstado.setText("Ninguna cancion sonando");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

    }

    class BarraMusica extends AsyncTask<String, String, String>{

        SeekBar miSeekBar;
        MediaPlayer miMediaPlayer;

        public BarraMusica(SeekBar miSeekBar, MediaPlayer miMediaPlayer) {
            this.miSeekBar = miSeekBar;
            this.miMediaPlayer = miMediaPlayer;
        }

        @Override
        protected void onPreExecute() {

            miSeekBar.setMax(miMediaPlayer.getDuration());

        }

        @Override
        protected void onProgressUpdate(String... values) {

            miSeekBar.setProgress(miMediaPlayer.getCurrentPosition());

        }

        @Override
        protected String doInBackground(String... strings) {
            while (true){

                if (miMediaPlayer.isPlaying()){
                    publishProgress();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}