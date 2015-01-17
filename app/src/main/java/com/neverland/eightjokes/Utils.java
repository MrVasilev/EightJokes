package com.neverland.eightjokes;

import com.parse.ParseUser;

/**
 * Created by Vasilev on 17.1.2015 г..
 */
public class Utils {

    public static boolean isCurrentUserExists() {
        return ParseUser.getCurrentUser() != null;
    }
}
