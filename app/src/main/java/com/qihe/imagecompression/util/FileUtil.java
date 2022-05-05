package com.qihe.imagecompression.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.xinqidian.adcommon.util.Common.getApplication;

/**
 * Created by lipei on 2018/10/31.
 */

public class FileUtil {




    public static String StringToUriToString(String path) {

        File file = new File(path);
        if (file.exists()) {
            String fnaily_path = Uri.parse("file://" + file.getAbsolutePath()).toString();
            return fnaily_path;
        }
        return path;

    }

    /**
     * uri得到文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getFilePathByUri(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            //一些三方的文件浏览器会进入到这个方法中，例如ES
            //QQ文件管理器不在此列

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File

        }
        return null;


//        String path = null;
//        // 以 file:// 开头的
//        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
//            path = uri.getPath();
//            return path;
//        }
//        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
//        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    if (columnIndex > -1) {
//                        path = cursor.getString(columnIndex);
//                    }
//                }
//                cursor.close();
//            }
//            return path;
//        }
//        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
//        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (DocumentsContract.isDocumentUri(context, uri)) {
//                if (isExternalStorageDocument(uri)) {
//                    // ExternalStorageProvider
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//                    if ("primary".equalsIgnoreCase(type)) {
//                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
//                        return path;
//                    }
//                } else if (isDownloadsDocument(uri)) {
//                    // DownloadsProvider
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
//                            Long.valueOf(id));
//                    path = getDataColumn(context, contentUri, null, null);
//                    return path;
//                } else if (isMediaDocument(uri)) {
//                    // MediaProvider
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//                    Uri contentUri = null;
//                    if ("image".equals(type)) {
//                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("video".equals(type)) {
//                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("audio".equals(type)) {
//                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    }
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{split[1]};
//                    path = getDataColumn(context, contentUri, selection, selectionArgs);
//                    return path;
//                }
//            }
//        }
//        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 保存图片到相册
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
        Log.i("storePath", storePath);
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file.getPath();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("storePath", file.getPath());
        return file.getPath();


    }
}
