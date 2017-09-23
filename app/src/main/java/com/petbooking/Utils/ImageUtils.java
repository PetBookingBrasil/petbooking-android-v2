package com.petbooking.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Luciano José on 19/09/2017.
 */

public class ImageUtils {

    public static Bitmap modifyOrientation(Context context, Bitmap bitmap, Uri uri) throws IOException {
        int orientation = getOrientation(context, uri);
        if (orientation <= 0) {
            return bitmap;
        }

        return rotate(bitmap, orientation);
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedImg;
    }

    private static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor;
        int orientation;

        cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();

        orientation = cursor.getInt(0);

        cursor.close();
        cursor = null;

        return orientation;
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
