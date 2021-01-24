package com.agnet.uza.models;

public class ImgIcon {

    private String name, iconUrl;


   public ImgIcon(String name, String iconUrl){
       this.name = name;
       this.iconUrl = iconUrl;
   }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
