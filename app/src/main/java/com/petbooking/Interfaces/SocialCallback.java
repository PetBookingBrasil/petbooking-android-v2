package com.petbooking.Interfaces;

import com.petbooking.Models.User;

/**
 * Created by Luciano José on 19/12/2016.
 */

public interface SocialCallback {

    void onFacebookLoginSuccess(User user);

}
