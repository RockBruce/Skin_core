package cn.edsmall.skin_core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;


/**
 * Created by LZH On 2019/3/15
 */
public class SysUtils {


    /**
     * @param activity
     * @param permission
     * @return if true has permission otherwise false
     */
    public static boolean checkPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity, List<String> permissions) {
        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), 203);
    }

    public static void checkAndRequestPermissions(Activity activity, List<String> permissions) {
        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), 1245);
    }

    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }


    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0M";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";

    }

    public static void writeResponseBodyToDisk( File file,Context context) {

        String TAG = "ToDisk";
        try {
            InputStream inputStream ;
            OutputStream outputStream = null;
            inputStream = context.getResources().getAssets().open("202.apk");
            try {
                byte[] fileReader = new byte[4096];
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;

                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                Log.e(TAG, "下载app成功: ");
            } catch (IOException e) {
                Log.e(TAG, "writeResponseBodyToDisk: ", e);

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "writeResponseBodyToDisk: ", e);

        }
    }

}