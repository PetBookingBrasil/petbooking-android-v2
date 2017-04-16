package com.petbooking.Interfaces;

import com.petbooking.Models.SocialUser;

/**
 * Created by Luciano José on 19/12/2016.
 */

public interface SocialCallback {

    void onFacebookLoginSuccess(SocialUser user);

}
