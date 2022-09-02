package com.r0adkll.slidr.example.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TextFile implements Parcelable {
    public String text_title;
    public String text_path;
    public String text_context;
    public String text_date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TextFile(File path) throws IOException {
        text_title = path.getName();
        text_path = path.getPath();
        List<String> lines = Files.readAllLines(Paths.get(path.getPath()));
        String tem ="";
        for(int i=0;i<lines.size();i++){
            tem = tem.concat(lines.get(i));
            if(i!=lines.size()-1) {
                tem = tem.concat("\n");
            }
        }
        text_context = tem;
        BasicFileAttributes attrs = Files.readAttributes(path.toPath(),BasicFileAttributes.class);
        FileTime time = attrs.creationTime();
        String datePattern ="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(datePattern);
        String formatted = simpleDateFormat.format(new Date(time.toMillis()));
        text_date = formatted;
    }

    protected TextFile(Parcel in) {
        text_title = in.readString();
        text_path = in.readString();
        text_context = in.readString();
        text_date = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text_title);
        dest.writeString(text_path);
        dest.writeString(text_context);
        dest.writeString(text_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TextFile> CREATOR = new Creator<TextFile>() {

        @Override
        public TextFile createFromParcel(Parcel parcel) {
            return new TextFile(parcel);
        }

        @Override
        public TextFile[] newArray(int size) {
            return new TextFile[size];
        }
    };
}
