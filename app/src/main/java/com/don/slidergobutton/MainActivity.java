package com.don.slidergobutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView tv = (TextView) findViewById(R.id.tv_go);
    AudioDragButton btnAudioDrag = (AudioDragButton) findViewById(R.id.btn_audio_drag);
    btnAudioDrag.addViewWithMove(tv);
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
