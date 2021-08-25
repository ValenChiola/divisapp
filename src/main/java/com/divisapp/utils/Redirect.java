package com.divisapp.utils;

import com.divisapp.entities.Folder;

public class Redirect {

    public static String getRedirectUrl(Folder folder) {
        String url = "/user/profile/" + folder.getUser().getId();

        if (folder.getParent() != null) {
            url = "/folder/" + folder.getParent().getId();
        }
        
        return "redirect:" + url;
    }

}
