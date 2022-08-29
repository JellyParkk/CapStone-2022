package com.r0adkll.slidr.example;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.r0adkll.slidr.example.model.TextFile;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextListActivity extends AppCompatActivity {

    @BindView(R.id.recycler) RecyclerView mRecycler;

    private TextFileAdapter mAdapter;
    String path = Environment.getExternalStorageDirectory().toString()+"/Download/TestDir";



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tlqkf","=========================");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        //Log.d("tlqkf", String.valueOf(savedInstanceState));
        setContentView(R.layout.activity_textlist);
        ButterKnife.bind(this);
        try {
            initRecycler(0,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditText editText = findViewById(R.id.edit_Text);
        Spinner spinner = findViewById(R.id.search_condition);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String target = String.valueOf(editText.getText());
                if(spinner.getSelectedItem().toString().equals("제목")) {
                    try {
                        initRecycler(1,target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        initRecycler(2,target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onResume() {
        super.onResume();
        Log.d("tlqkf","2=========================");
        ButterKnife.bind(this);
        try {
            initRecycler(0,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditText editText = findViewById(R.id.edit_Text);
        Spinner spinner = findViewById(R.id.search_condition);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String target = String.valueOf(editText.getText());
                if(spinner.getSelectedItem().toString().equals("제목")) {
                    try {
                        initRecycler(1,target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        initRecycler(2,target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initRecycler(int condition, @Nullable String target) throws IOException {
        mAdapter = new TextFileAdapter();
        Log.d("tlqkf",condition+"/"+target);
        if(condition==0) {
            mAdapter.addAll(getData1());
        } else if (condition==1) {
            mAdapter.addAll(getData2(target));
        } else if (condition==2) {
            mAdapter.addAll(getData3(target));
        } else {
            mAdapter.addAll(getData1());
        }
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new BetterRecyclerAdapter.OnItemClickListener<TextFile>() {
            @Override
            public void onItemClick(View v, TextFile item, int position) {
                Intent viewer = new Intent(TextListActivity.this, ViewerActivity.class);
                viewer.putExtra(ViewerActivity.EXTRA_FILE,item);
                startActivity(viewer);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<TextFile> getData1() throws IOException {
        File directory = new File(path);
        if(!directory.exists()) {
            try {
                //Log.d("tlqkf","no direcotry");
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] file = directory.listFiles();
        //if(directory.isDirectory()) Log.d("tlqkf","is directory");
        //Log.d("tlqkf",path);
        if(file==null) {
            //Log.d("tlqkf","null");
        } else {
            //Log.d("tlqkf",Integer.toString(file.length));
        }
        //List<TextFile> files = new ArrayList<TextFile>();
        TextFile[] tem = new TextFile[file.length];
        for (int i=0; i<file.length;i++){
            TextFile temp = new TextFile(new File(file[i].getPath()));
            tem[i] = temp;
        }
        tem = sortFiles(tem);
        List<TextFile> files = Arrays.asList(tem);
        //Log.d("tlqkf",Integer.toString(files.size()));
        return files;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<TextFile> getData2(String title) throws IOException {
        File directory = new File(path);
        if(!directory.exists()) {
            try {
                //Log.d("tlqkf","no direcotry");
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] file = directory.listFiles();
        //if(directory.isDirectory()) Log.d("tlqkf","is directory");
        //Log.d("tlqkf",path);
        if(file==null) {
            //Log.d("tlqkf","null");
        } else {
            //Log.d("tlqkf",Integer.toString(file.length));
        }
        //List<TextFile> files = new ArrayList<TextFile>();
        TextFile[] tem = new TextFile[file.length];
        int idx = 0;
        for (int i=0; i<file.length;i++){
            TextFile temp = new TextFile(new File(file[i].getPath()));
            if(temp.text_title.contains(title)) {
                tem[idx] = temp;
                idx++;
            }
        }
        TextFile[] textFiles = Arrays.copyOfRange(tem,0,idx);
        textFiles = sortFiles(textFiles);
        List<TextFile> files = Arrays.asList(textFiles);
        //Log.d("tlqkf",Integer.toString(files.size()));
        return files;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<TextFile> getData3(String context) throws IOException {
        File directory = new File(path);
        if(!directory.exists()) {
            try {
                //Log.d("tlqkf","no direcotry");
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] file = directory.listFiles();
        //if(directory.isDirectory()) Log.d("tlqkf","is directory");
        //Log.d("tlqkf",path);
        if(file==null) {
            //Log.d("tlqkf","null");
        } else {
            //Log.d("tlqkf",Integer.toString(file.length));
        }
        //List<TextFile> files = new ArrayList<TextFile>();
        TextFile[] tem = new TextFile[file.length];
        int idx = 0;
        for (int i=0; i<file.length;i++){
            TextFile temp = new TextFile(new File(file[i].getPath()));
            if(temp.text_context.contains(context)) {
                tem[idx] = temp;
                idx++;
            }
        }
        TextFile[] textFiles = Arrays.copyOfRange(tem,0,idx);
        textFiles = sortFiles(textFiles);
        List<TextFile> files = Arrays.asList(textFiles);
        //Log.d("tlqkf",Integer.toString(files.size()));
        return files;
    }

    private TextFile[] sortFiles(TextFile[] textFiles) {
        Arrays.sort(textFiles,new Comparator<TextFile>() {

            @Override
            public int compare(TextFile t1, TextFile t2) {
                return t2.text_date.compareTo(t1.text_date);
            }
        });
        return textFiles;
    }
}
