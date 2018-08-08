package com.petbooking.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Luciano José on 28/05/2017.
 */

public class Category implements Parcelable{

    public String id;
    public int categoryText;
    public String categoryName;
    public Drawable icon;
    public String iconUrl;

    public Category(String id, int categoryText, String categoryName, Drawable icon, String iconUrl) {
        this.id = id;
        this.categoryText = categoryText;
        this.categoryName = categoryName;
        this.icon = icon;
        this.iconUrl = iconUrl;
    }

    public Category(String id, int categoryText, String categoryName, Drawable icon) {
        this.id = id;
        this.categoryText = categoryText;
        this.categoryName = categoryName;
        this.icon = icon;
        //this.iconUrl = iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.id);
        dest.writeInt(this.categoryText);
        dest.writeString(this.categoryName);
        Bitmap bitmap = getBitmap(icon);
        dest.writeValue(bitmap);
    }

    protected Category(Parcel in){
        this.id = in.readString();
        this.categoryText = in.readInt();
        this.categoryName = in.readString();
        Bitmap bitmap =(Bitmap) in.readValue(null);
        Drawable drawable = new BitmapDrawable(bitmap);
        this.icon = drawable;
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    protected Bitmap getBitmap(Drawable drawable){
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
