package com.example.Capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainviewActivity extends AppCompatActivity {
//    private val rotateOpen: Animation by lazy {
//        AnimationUtils.loadAnimation(createContext(this).anim.rotate_open_anim)
//    }
    boolean i = true;
    private Animation fab_open, fab_close, rotateForward, rotateBackward;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_main, fab_storage, fab_store, fab_conversion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        Toolbar mytoolbar = (findViewById(R.id.maintoolbar));
        setSupportActionBar(mytoolbar);
        ImageButton Start = (ImageButton) findViewById(R.id.imageButtonStart);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==true){
                Start.setSelected(true);
                i = false;}
                else {
                Start.setSelected(false);
                i = true;}
            }


        });
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

}
