package com.qihe.imagecompression.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import com.xinqidian.adcommon.util.ToastUtils;

public class SetRingUtil {

    public static final int REQUEST_CODE_SETTING = 0x1234;
    private static int type;
    private static String strPath;
    private static String strTitle;

    public static void setRing(Activity context) {
        setRing(context, type, strPath, strTitle);
    }

    //调用该方法需选保证已授权相关权限
    public static void setRing(Activity context, int type, String strPath, String strTitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            SetRingUtil.type = type;
            SetRingUtil.strPath = strPath;
            SetRingUtil.strTitle = strTitle;
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, REQUEST_CODE_SETTING);
            return;
        }

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, strPath);
            values.put(MediaStore.MediaColumns.TITLE, strTitle);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, true);
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);

            //更新
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(strPath);
            Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{strPath}, null);
            String deleteId = "";
            if (cursor.moveToFirst()) {
                deleteId = cursor.getString(cursor.getColumnIndex("_id"));
            }
            if (!TextUtils.isEmpty(deleteId)) {
                context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + strPath + "\"", null);
            }
            uri = context.getContentResolver().insert(uri, values);
            cursor.close();

            switch (type) {
                //手机铃声
                case 0:
                    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, uri);
                    ToastUtils.show("设置手机铃声成功");
                    break;
                //通知铃声
                case 1:
                    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, uri);
                    ToastUtils.show("设置通知铃声成功");
                    break;
                //闹钟铃声
                case 2:
                    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, uri);
                    ToastUtils.show("设置闹钟铃声成功");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("设置铃声失败");
        }
        SetRingUtil.type = -1;
        SetRingUtil.strPath = null;
        SetRingUtil.strTitle = null;
    }
}
