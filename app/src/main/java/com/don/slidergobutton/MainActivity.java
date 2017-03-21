package com.don.slidergobutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AudioDragButton btnAudioDrag = (AudioDragButton) findViewById(R.id.btn_audio_drag);
    btnAudioDrag.setAudioDragDelegate(new AudioDragButton.AudioDragDelegate() {
      @Override
      public void cancelAudio() {
        Log.i("don", "cancelAudio");
      }

      @Override
      public void startAudio() {
        Log.i("don", "startAudio");
      }

      @Override
      public void endAudio() {
        Log.i("don", "endAudio");
      }
    });
  }
}
