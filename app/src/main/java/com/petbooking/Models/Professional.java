package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 06/08/2017.
 */

public class Professional {

    public String id;
    public String name;
    public AvatarResp avatar;

    public Professional(String id, String name, AvatarResp avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
}
