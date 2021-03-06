package com.dreamspace.uucampusseller.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

   public static File createTmpFile(Context context){

       String state = Environment.getExternalStorageState();
       if(state.equals(Environment.MEDIA_MOUNTED)){
           File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
           String fileName = "multi_image_"+timeStamp+"";
           File tmpFile = new File(pic, fileName+".jpg");
           return tmpFile;
       }else{
           File cacheDir = context.getCacheDir();
           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
           String fileName = "multi_image_"+timeStamp+"";
           File tmpFile = new File(cacheDir, fileName+".jpg");
           return tmpFile;
       }

   }

}
