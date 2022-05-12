package com.example.Capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TextReadActivity extends AppCompatActivity {



    TextView txtRead;
    TextView filecontents;
    EditText edtxt;

    String Filename;
    String path0;


    public void SetName(View v){
        filecontents.setText(Filename);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    private File f,d;



    public void save(String newfilename){
        try {
            d = new File(path0, (Filename + ".txt"));
            f = new File(path0, (newfilename + ".txt"));
            Log.d("TESTTAG",f.getName());
            BufferedWriter file = new BufferedWriter(new FileWriter(f));
            String data = txtRead.getText().toString();
            Log.d("TESTTAG",data);
            file.write(data);
            file.flush();
            file.close();
            d.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testTag","save fail");
        }

        MediaScanner ms = MediaScanner.newInstance(TextReadActivity.this);
        try {
            ms.mediaScanning(path0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MediaScan", "ERROR" + e);
        }
        finally {

        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textread);
        checkPermission();

        Filename = getIntent().getStringExtra("title");
        path0 = getIntent().getStringExtra("path");

        txtRead = (TextView)findViewById(R.id.txtRead);

        filecontents = (TextView)findViewById(R.id.filecontents);
        edtxt =(EditText) findViewById(R.id.filecontents);

        SetName(filecontents);
        mOnFileRead(txtRead);
        //save(Filename);
    }

    public void mOnFileRead(View v){
        String read = ReadTextFile(path0);
        txtRead.setText(read);
    }

    public void savefile(View v) {
        save(edtxt.getText().toString());
    }

    //경로의 텍스트 파일읽기
    public String ReadTextFile(String path){
        StringBuffer strBuffer = new StringBuffer();
        Log.d("태그",path0);
        try{
            InputStream is = new FileInputStream(path+ Filename+".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";
            while((line=reader.readLine())!=null){
                strBuffer.append(line+"\n");
            }

            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
        return strBuffer.toString();
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);  //마지막 인자는 체크해야될 권한 갯수
                } else {
                    Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
                }
        }

    }



}