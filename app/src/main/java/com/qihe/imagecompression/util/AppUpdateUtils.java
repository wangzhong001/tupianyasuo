package com.qihe.imagecompression.util;

import android.content.Context;

import com.google.gson.Gson;
import com.xinqidian.adcommon.login.UpdateBean;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.proxy.IUpdateParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lipei on 2018/1/30.
 */

public class AppUpdateUtils {
    private final static String TAG = AppUpdateUtils.class.getName();
    private static Map<String ,String> map=new HashMap<>();

    public static void initAppUpdate( String chanel_name) {
        map.put("id",chanel_name);

    }


    public static class CustomUpdateParser implements IUpdateParser {
        @Override
        public UpdateEntity parseJson(String json) throws Exception {
            UpdateBean result = new Gson().fromJson(json, UpdateBean.class);
            if (result != null) {
                return new UpdateEntity()
                        .setHasUpdate(result.isHasUpdate())
                        .setForce(result.isForce())
                        .setVersionCode(result.getVersionCode())
                        .setVersionName(result.getVersionName())
                        .setUpdateContent(result.getModifyContent())
                        .setDownloadUrl(result.getDownloadUrl())
                        .setSize(result.getApkSize());
            }

            return null;
        }

        @Override
        public void parseJson(String json, IUpdateParseCallback callback) throws Exception {

        }

        @Override
        public boolean isAsyncParser() {
            return false;
        }
    }


    public static void update(final Context mContext, final boolean isNeedToast) {





        XUpdate.newBuild(mContext)

                .updateUrl("http://rest.apizza.net/mock/1d90760be4fb704a97582e37acf94f7c/tupiangengxin")
//                .isAutoMode(true)
//                .updateParser(new CustomUpdateParser()) //设置自定义的版本更新解析器

                .update();


//        XUpdate.newBuild(mContext)
//                .updateUrl("https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_forced.json")
//                .update();
    }

}
