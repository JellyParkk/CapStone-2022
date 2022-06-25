package com.example.Capstone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainviewActivity extends AppCompatActivity{
//    private val rotateOpen: Animation by lazy {
//        AnimationUtils.loadAnimation(createContext(this).anim.rotate_open_anim)
//    }
    boolean i = true;
    private Animation fab_open, fab_close, rotateForward, rotateBackward;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_main, fab_storage, fab_store, fab_conversion;
    private TextView textView;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    String text = "";
    private final String path = "/storage/emulated/0/Download/TestDir";
    File directory = new File(path);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        CheckPermission();

        //Toolbar mytoolbar = (findViewById(R.id.maintoolbar));
        //setSupportActionBar(mytoolbar);
        ImageButton Start = (ImageButton) findViewById(R.id.imageButtonStart);
        ImageButton Stop = (ImageButton) findViewById(R.id.imageButtonStore);
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        fab_main = (FloatingActionButton)findViewById(R.id.fab_main);
        fab_storage = (FloatingActionButton)findViewById(R.id.fab_storage);
        fab_store = (FloatingActionButton)findViewById(R.id.fab_store);
        fab_conversion = (FloatingActionButton)findViewById(R.id.fab_conversion);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab_storage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TextListActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        fab_store.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
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
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                textView.setText("");
                textView.setScrollY(0);
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
        /*Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i){
                    Start.setSelected(true);
                    if(spinner.getSelectedItem().toString().equals("한글")) {
                        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko_KR");
                    } else if (spinner.getSelectedItem().toString().equals("영어")) {
                        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en_US");
                    }
                    speech = SpeechRecognizer.createSpeechRecognizer(this);

                    i = false;}
                else {
                    Start.setSelected(false);
                    if(spinner.getSelectedItem().toString().equals("한글")) {
                        Log.i("spinner check",spinner.getSelectedItem().toString()+"finish");
                    }
                    i = true;}
            }


        });*/
    }

    public void animateFab(){
        if(isFabOpen){
            fab_main.startAnimation(rotateForward);
            fab_storage.startAnimation((fab_open));
            fab_conversion.startAnimation((fab_open));
            fab_store.startAnimation((fab_open));
            fab_storage.setClickable(true);
            fab_conversion.setClickable(true);
            fab_store.setClickable(true);
            isFabOpen=false;
        }
        else{
            fab_main.startAnimation(rotateBackward);
            fab_storage.startAnimation((fab_close));
            fab_conversion.startAnimation((fab_close));
            fab_store.startAnimation((fab_close));
            fab_storage.setClickable(false);
            fab_conversion.setClickable(false);
            fab_store.setClickable(false);
            isFabOpen=true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void CheckPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if((ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO},1);
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
}
