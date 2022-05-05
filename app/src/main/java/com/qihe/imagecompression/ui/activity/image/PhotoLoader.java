package com.qihe.imagecompression.ui.activity.image;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class PhotoLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String[] PARAMS_IMAGE = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID
    };
    public FragmentActivity activity;

    private LoadFinishCallback loadFinishCallback;

    public void setLoadFinishCallback(LoadFinishCallback loadFinishCallback) {
        this.loadFinishCallback = loadFinishCallback;
    }

    public interface LoadFinishCallback {
        void wholeImage(List<String> imageWhole);
    }

    public PhotoLoader(FragmentActivity activity) {
        this.activity = activity;
        activity.getSupportLoaderManager().initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PARAMS_IMAGE, null, null, PARAMS_IMAGE[2] + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        List<String> wholeImages = new ArrayList<>();
        int photoCount = data.getCount();
        if (photoCount > 0) {
            data.moveToFirst();
            do {
                String photoPath = data.getString(data.getColumnIndexOrThrow(PARAMS_IMAGE[0]));
                wholeImages.add(photoPath);
            } while (data.moveToNext());
        }
        //调用接口
        loadFinishCallback.wholeImage(wholeImages);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
