package com.petbooking.API.Generic;

/**
 * Created by Luciano José on 15/04/2017.
 */

public class AvatarResp {

    public String url;
    public ImageURL tiny;
    public ImageURL thumb;
    public ImageURL medium;
    public ImageURL large;

    public AvatarResp() {
    }

    public static class ImageURL {
        public String url;
    }

    public static class Avatar_ {
        public AvatarResp avatar;
    }

}
