package com.example.Capstone;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TextListActivity extends AppCompatActivity {

    ArrayList<DataModel> list = new ArrayList<>();
    String path = Environment.getExternalStorageDirectory().toString()+"/Download/TestDir";
    File directory = new File(path);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textlist);

/*
        for(int i=1;i<5;i++) {
            File tem = new File(directory.getPath()+"/TestFile"+i+".txt");
            try{
                if(tem.createNewFile()) {
                    Log.d("Files", tem.getPath() + " created");
                } else {
                    Log.d("Files",tem.getPath()+" is already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i=1;i<3;i++) {
            File tem = new File(directory.getPath()+"/TemFile"+i+".txt");
            try{
                if(tem.createNewFile()) {
                    Log.d("Files", tem.getPath() + " created");
                } else {
                    Log.d("Files",tem.getPath()+" is already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i=1;i<4;i++) {
            File tem = new File(directory.getPath()+"/ExFile"+i+".txt");
            try{
                if(tem.createNewFile()) {
                    Log.d("Files", tem.getPath() + " created");
                } else {
                    Log.d("Files",tem.getPath()+" is already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

        Log.d("Files","====onCreate====");
        File[] files = makeFileList();
        RecyclerView recyclerView = makeRecyclerView(files);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                String targetText = editText.getText().toString();

                ArrayList<DataModel> newList = new ArrayList<>();
                newList = searchText(files,targetText);

                recyclerviewAdapter newAdapter = new recyclerviewAdapter(newList);
                recyclerView.setAdapter(newAdapter);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Files","====onResume====");
        File[] files = makeFileList();
        RecyclerView recyclerView = makeRecyclerView(files);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                String targetText = editText.getText().toString();

                ArrayList<DataModel> newList = new ArrayList<>();
                newList = searchText(files,targetText);

                recyclerviewAdapter newAdapter = new recyclerviewAdapter(newList);
                recyclerView.setAdapter(newAdapter);
            }
        });
    }

    public File[] makeFileList() {
        if(!directory.exists()) {
            try{
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] files = directory.listFiles();
        Log.d("Files","Size: "+ files.length);
        for(int i=0;i<files.length;i++) {
            Log.d("Files","FileName: "+files[i].getName());
        }
        return files;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecyclerView makeRecyclerView(File[] files) {



        list = makeArrayList(list,files);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerviewAdapter adapter = new recyclerviewAdapter(list);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<DataModel> makeArrayList(ArrayList<DataModel> list, File[] files) {
        list.clear();
        for(File f : files) {
            BasicFileAttributes attrs;
            try {
                attrs = Files.readAttributes(f.toPath(),BasicFileAttributes.class);
                FileTime time = attrs.creationTime();

                String datePattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

                String formatted = simpleDateFormat.format(new Date(time.toMillis()));
                String filename = f.getName().split(".txt")[0];

                list.add(new DataModel(filename,formatted));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(list, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dModel2, DataModel dModel1) {
                return dModel1.text_date.compareTo(dModel2.text_date);
            }
        });
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<DataModel> searchText(File[] files, String text) {
        ArrayList<File> fileList = new ArrayList<>();
        for(File f : files) {
            if(f.getName().contains(text)) {
                fileList.add(f);
            }
        }

        File[] newFiles = new File[fileList.size()];
        for(int i=0;i<fileList.size();i++) {
            newFiles[i] = fileList.get(i);
        }

        ArrayList<DataModel> newList = new ArrayList<>();
        newList = makeArrayList(newList,newFiles);
        return newList;
    }
}