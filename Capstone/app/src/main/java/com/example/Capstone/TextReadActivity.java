package com.example.Capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TextReadActivity extends AppCompatActivity {



    TextView txtRead;
    TextView filecontents;
    TextView tv1;
    EditText et1;
    EditText edtxt;
    View v_d;

    String Filename;
    String path0;

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
    }

    public void SetName(View v){
        filecontents.setText(Filename);
    }


    public class DeleteFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d("TESTTAG","dialog");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    private File f,d;

    public void delete(){
        d = new File(path0, (Filename + ".txt"));
        AlertDialog.Builder builder = new AlertDialog.Builder(TextReadActivity.this);
        builder.setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        d.delete();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog deletedialog = builder.create();
        deletedialog.show();

    }



    public void save(String newfilename){
        try{
            d = new File(path0, (Filename + ".txt"));
            f = new File(path0, (newfilename + ".txt"));
            int numberingName = 1;
            while (f.exists()) {
                f = new File(path0+ "/" + newfilename + "(" + numberingName + ").txt");
                numberingName++;
            }

            Log.d("TESTTAG",d.getName());
            Log.d("TESTTAG",f.getName());
            BufferedWriter file = new BufferedWriter(new FileWriter(f));
            String data = txtRead.getText().toString();
            file.write(data);
            file.flush();
            file.close();
            if(!d.getName().equals(f.getName()))
            {
                d.delete();
            }

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


    public void mOnFileRead(View v){
        String read = ReadTextFile(path0);
        txtRead.setText(read);
    }

    public void savefile(View v) {
        AlertDialog.Builder changedialog = new AlertDialog.Builder(TextReadActivity.this);

        changedialog.setTitle("제목");

        v_d = (View) View.inflate(TextReadActivity.this, R.layout.editdialog, null);
        changedialog.setView(v_d);
        changedialog
                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        et1 = (EditText) v_d.findViewById(R.id.et_title);
                        Log.d("TESTTAG", et1.getText().toString());
                        File tem = new File(path0, (et1.getText().toString() + ".txt"));
                        if(et1.getText().toString().equals(""))
                        {
                            Toast.makeText(getApplicationContext(), "새로운 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if(tem.exists())
                        {
                            Toast.makeText(getApplicationContext(), "이미 있는 파일 이름입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(!et1.getText().toString().equals(""))
                        {
                            edtxt.setText(et1.getText().toString());
                            save(edtxt.getText().toString());
                        }

                        Log.d("TESTTAG","onclick");

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        changedialog.show();
    }
    public void deletefile(View v) {
        delete();

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

                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);  //마지막 인자는 체크해야될 권한 갯수
                } else {

                }
        }

    }



}