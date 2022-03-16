package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class play_film extends AppCompatActivity {
    ImageButton foward, rewind, pause_start;
    SeekBar video_seekbar, brightness, volume;
    TextView timing,title;
    VideoView view;
    ImageButton back;
    AudioManager audioManager;
    int i = 0;
    ImageView volume_view, bright_view;
    ConstraintLayout constraintLayout;
    //Content resolver used as a handle to the system's settings
    int volumelevel;
    //Window object, that will store a reference to the current window
    View view_custom,space_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_film);
        foward = findViewById(R.id.videoView_forward);
        rewind = findViewById(R.id.videoView_rewind);
        volume = findViewById(R.id.videoView_vollum);
        back=findViewById(R.id.videoView_go_back);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volume_view = findViewById(R.id.videoView_vollum_image);
        bright_view = findViewById(R.id.videoView_brightness_image);
        brightness = findViewById(R.id.videoView_brightness);
        pause_start = findViewById(R.id.videoView_play_pause_btn);
        video_seekbar = findViewById(R.id.videoView_seekbar);
        timing = findViewById(R.id.videoView_endtime);
        view = findViewById(R.id.video_view);
        title=findViewById(R.id.videoView_title);
        space_click=findViewById(R.id.custom_view_1);
        view_custom = findViewById(R.id.custom_view);
        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumelevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume.setProgress(volumelevel);
        countdownhide();
        title.setText(getIntent().getExtras().getString("name"));
        view.setVideoPath(getIntent().getExtras().getString("url"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_custom.setVisibility(View.VISIBLE);
                countdownhide();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumelevel = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumelevel, 0);
                if (progress == 0) {
                    volume_view.setImageResource(R.drawable.ic_baseline_volume_off_24);
                } else {
                    if (progress == AudioManager.STREAM_MUSIC) {
                        volume_view.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    } else {
                        if (progress <= AudioManager.STREAM_MUSIC / 2) {
                            volume_view.setImageResource(R.drawable.ic_baseline_volume_mute_24);
                        } else {

                            volume_view.setImageResource(R.drawable.ic_baseline_volume_down_24);

                        }

                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float BackLightValue = (float) progress / 100f;
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
                layoutParams.screenBrightness = BackLightValue; // Set Value
                getWindow().setAttributes(layoutParams); // Set params
                if (progress == 0) {
                    bright_view.setImageResource(R.drawable.netflix_brightness_two);
                } else {
                    if (progress == 255) {
                        bright_view.setImageResource(R.drawable.netflix_brightness_one);
                    } else {
                        if (progress < 127.5) {
                            bright_view.setImageResource(R.drawable.netflix_brightness_three);
                        } else {

                            bright_view.setImageResource(R.drawable.netflix_brightness_four);

                        }

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        space_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                view_custom.setVisibility(View.GONE);
                return false;
            }
        });
        space_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i==2){
                            Log.e("Click","true");
                            view_custom.setVisibility(View.GONE);
                        }
                        i=0;
                    }
                },200);

            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Films").document("Shark tank tập 8");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("firebase", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("firebase", "No such document");
                    }
                } else {
                    Log.d("firebase", "get failed with ", task.getException());
                }
            }
        });


//        try {
//            view.setVideoPath(downloadUrl("https://drive.google.com/file/d/1PVni18FFqws9cvY7eornVhGCvJdTOhxy/view?usp=sharing"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video_seekbar.setMax(view.getDuration());
                view.start();
            }
        });
        video_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                view.seekTo(seekBar.getProgress());
            }
        });
        foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.isPlaying()) {
                    view.pause();
                    view.seekTo(view.getCurrentPosition() + 10000);
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.start();
                } else {
                    view.seekTo(view.getCurrentPosition() + 10000);
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.isPlaying()) {
                    view.pause();
                    view.seekTo(view.getCurrentPosition() - 10000);
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.start();
                } else {
                    view.seekTo(view.getCurrentPosition() - 10000);
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        setHandle(constraintLayout);
        pause_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.isPlaying()) {
                    view.pause();
                    pause_start.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    view.start();
                    pause_start.setImageResource(R.drawable.netflix_pause_button);
                }
            }
        });
    }
    private String convertTine(int ns) {
        String tine;
        int x, seconds, minutes, hours;
        x = ns / 1080;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0) {
            tine = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        } else {
            tine = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
        return tine;
    }
    public void setHandle(ConstraintLayout constraintLayout) {
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (view.getDuration() > 0) {
                    int cuspos = view.getCurrentPosition();
                    video_seekbar.setProgress(cuspos);
                    timing.setText("" + convertTine(cuspos));
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 500);
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {

                if (constraintLayout.getVisibility() == View.VISIBLE) {
                    constraintLayout.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 5000);
    }
    private void countdownhide() {
        Log.e("nacsag", "Runh");
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int i = 0;
                Log.e("countdown", i++ + "");
            }

            @Override
            public void onFinish() {
                view_custom.setVisibility(View.GONE);
            }
        }.start();
    }

}