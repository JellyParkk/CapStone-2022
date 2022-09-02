package com.r0adkll.slidr.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library2.FloatingNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingNavigationView mFloatingNavigationView;
    boolean i = true;
    private TextView textView;
    private ScrollView scrollView;
    private Spinner spinner;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    String text = "";
    private final String path = "/storage/emulated/0/Download/TestDir";
    File directory = new File(path);

    private void scrollBottom (TextView textView) {
        int lineTop = (textView.getLayout().getLineTop(textView.getLineCount()))+78;
        int scrollY = ((lineTop - textView.getHeight()))+68;
        if (scrollY > 0) {
            textView.scrollTo(0, scrollY);
        } else {
            textView.scrollTo(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CheckPermission();

        final ImageButton Start = (ImageButton) findViewById(R.id.imageButtonStart);
        ImageButton Stop = (ImageButton) findViewById(R.id.imageButtonStore);
        textView = (TextView) findViewById(R.id.textView);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
        scrollView.fullScroll((textView.FOCUS_DOWN));
        spinner = (Spinner) findViewById(R.id.spinner);

        mFloatingNavigationView = (FloatingNavigationView) findViewById(R.id.floating_navigation_view);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingNavigationView.open();
            }
        });
        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getTitle().equals("save")) {
                    if(!directory.exists()) {
                        try {
                            directory.mkdir();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String contents = textView.getText().toString();
                    if(!contents.equals("")) {
                        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));
                        File tem = new File(directory.getPath() + "/" + name + ".txt");

                        int numberingName = 1;
                        while (tem.exists()) {
                            tem = new File(directory.getPath() + "/" + name + "(" + numberingName + ").txt");
                            numberingName++;
                        }

                        try {
                            BufferedWriter bw = new BufferedWriter(new FileWriter(tem));
                            bw.write(contents);
                            bw.flush();
                            bw.close();
                            Toast.makeText(getApplicationContext(), tem.getName() + " saved!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "There is no text to save.", Toast.LENGTH_SHORT).show();
                    }
                } else if(item.getTitle().equals("text_list")) {
                    Intent intent = new Intent(mFloatingNavigationView.getContext(), TextListActivity.class);
                    mFloatingNavigationView.getContext().startActivity(intent);
                    mFloatingNavigationView.close();
                } else if(item.getTitle().equals("exit")) {
                    moveTaskToBack(true);
                    finishAndRemoveTask();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return true;
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                textView.setText("");
                Start.setSelected(false);
                i=true;
                listener.onEndOfSpeech();

            }
        });

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,300000);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);

        Start.setOnClickListener(v -> {
            if(i){
                Start.setSelected(true);
                if(spinner.getSelectedItem().toString().equals("한글")) {
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko_KR");
                } else if (spinner.getSelectedItem().toString().equals("영어")) {
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en_US");
                }
                Log.i("VoiceRecognizer","start Button click");
                speech = SpeechRecognizer.createSpeechRecognizer(this);
                speech.setRecognitionListener(listener);
                speech.startListening(recognizerIntent);
                i = false;}
            else {
                Start.setSelected(false);
                i = true;
                listener.onEndOfSpeech();
                Log.i("VoiceRecognizer","finish Button click");
            }
        });
    }
    void CheckPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            /*if (textView.getText().equals("자막 text")) {
                textView.setText("");
            }*/
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
            if (!i) {
                speech.startListening(recognizerIntent);
            } else {
                speech.stopListening();
                speech.cancel();
                speech.destroy();
            }
        }

        @Override
        public void onError(int i) {
            String errorMessage = getErrorText(i);
            Log.d("SpeechRecognizer Error", "FAILED " + errorMessage);
        }

        @Override
        public void onResults(Bundle bundle) {

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for (String result : matches) {
                scrollBottom(textView);
                int space_idx;
                if (matches.get(0).startsWith(" ")) {
                    result = matches.get(0).replaceFirst(" ", "");
                } //띄어쓰기 제거 부분
                if (result.contains(" ")) {
                    space_idx = result.indexOf(" ");
                } else {
                    space_idx = 0;
                }
                if (text.equals("") && (!(result.equals("")))) {
                    text = result;
                    textView.append(" " + result); //프로그램 처음 시작할 때
                } else if (((result.startsWith(text)) && (!(result.equals(text))))) {
                    textView.append("" + result.replaceFirst(text + "", " "));
                    text = result;
                } else if ((!(result.equals(text))) && (result.equals(""))) {
                    text = result;
                    textView.append(" " + result);//말을 안할때 text와 result가 달라짐
                } else {
                }
                //애국가에서 동해~물과처럼 한단어의 끝이 길면 다음 단어랑 합쳐지면서 중복으로 나와버리는 문제 발생

            }
        }



        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        public String getErrorText(int errorCode) {
            String message;
            switch (errorCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "Audio recording error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "Client side error";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "Insufficient permissions";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "Network error";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "Network timeout";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "No match";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RecognitionService busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "error from server";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "No speech input";
                    break;
                default:
                    message = "Didn't understand, please try again.";
                    break;
            }
            return message;
        }
    };



    @Override
    public void onBackPressed() {
        if (mFloatingNavigationView.isOpened()) {
            mFloatingNavigationView.close();
        } else {
            moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());
            super.onBackPressed();
        }
    }

}