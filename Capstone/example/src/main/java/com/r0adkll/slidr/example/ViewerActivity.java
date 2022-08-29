package com.r0adkll.slidr.example;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.ftinc.kit.util.SizeUtils;
import com.ftinc.kit.util.Utils;
import com.ftinc.kit.widget.AspectRatioImageView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.example.model.TextFile;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewerActivity extends AppCompatActivity {

    public static final String EXTRA_FILE = "Extra_File";

    @BindView(R.id.toolbar)     Toolbar mToolbar;
    @BindView(R.id.cover)       AspectRatioImageView mCover;
    @BindView(R.id.title)       TextView mTitle;
    @BindView(R.id.description) TextView mDescription;


    TextView txtRead;
    TextView title;
    EditText et1;
    View v_d;
    String Filename;
    String path0;
    String filecontents;
    String titlename;


    private TextFile mTextFile;
    private SlidrConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ButterKnife.bind(this);

        int primary = getResources().getColor(R.color.primaryDark);
        int secondary = getResources().getColor(R.color.red_500);

        int numPositions = SlidrPosition.values().length;
        SlidrPosition position = SlidrPosition.values()[Utils.getRandom().nextInt(numPositions)];

        title = findViewById(R.id.title);
        txtRead =  findViewById(R.id.description);
        txtRead.setMovementMethod(new ScrollingMovementMethod());


        mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(SlidrPosition.VERTICAL)
                .velocityThreshold(2400)
//                .distanceThreshold(.25f)
//                .edge(true)
                .touchSize(SizeUtils.dpToPx(this, 32))
                .build();
        //Slidr.attach(this,mConfig);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextFile = getIntent().getParcelableExtra(EXTRA_FILE);
        if(savedInstanceState != null) {
            mTextFile = savedInstanceState.getParcelable(EXTRA_FILE);
            Log.d("tlqkf3", String.valueOf(mTextFile));
        }

        Log.d("tlqkf2", String.valueOf(mTextFile));
        Log.d("tlqkf2", String.valueOf(mTextFile.text_title));
        //mTitle.setText(mTextFile.text_title);
        String[] temp = mTextFile.text_title.split("\\.");
        String title ="";
        for(int i=0;i<temp.length-1;i++) {
            title = title.concat(temp[i]);
            if(i!=temp.length-2) {
                title = title.concat(".");
            }
        }
        Log.d("tlqkfxkdlxmf",title);
        mTitle.setText(title);
        mDescription.setText(mTextFile.text_context);
        Filename = mTextFile.text_title;
        //path0 = mTextFile.text_date;
        path0 = Environment.getExternalStorageDirectory().toString()+"/Download/TestDir";
        filecontents = mTextFile.text_context;
        titlename = mTextFile.text_title;
        Log.d("tlqkf2", Filename);
        Log.d("tlqkf2", path0);
        Log.d("tlqkf2", filecontents);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_FILE,mTextFile);
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

    public void save(String newfilename){
        try{
            d = new File(path0, (Filename));
            f = new File(path0, (newfilename + ".txt"));
            int numberingName = 1;

            Log.d("TESTTAG",d.getName());
            Log.d("TESTTAG",f.getName());
            BufferedWriter file = new BufferedWriter(new FileWriter(f));
            String data = filecontents;
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
        MediaScanner ms = MediaScanner.newInstance(ViewerActivity.this);
        try {
            ms.mediaScanning(path0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MediaScan", "ERROR" + e);
        }
        finally {

        }
    }


    public void savefile(View v) {
        AlertDialog.Builder changedialog = new AlertDialog.Builder(ViewerActivity.this);

        changedialog.setTitle("제목");

        v_d = (View) View.inflate(ViewerActivity.this, R.layout.editdialog, null);
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
                            title.setText(et1.getText().toString());
                            save(title.getText().toString());
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

    private File f,d;

    public void delete(){
        d = new File(path0, (Filename));
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewerActivity.this);
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

    public void deletefile(View v) {
        delete();
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
