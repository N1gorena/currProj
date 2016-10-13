package com.guango.society;

import android.util.Log;

/**
 * Created by nijeguan on 10/12/16.
 */

public abstract class GXMLObject {

    public boolean addTag(String key,String val){return false;}
    public boolean attachInfo(String key, String val){return false;}
    public boolean addNode(String ref){return false;}
    public boolean addMember(String type, String ref, String role){return false;}
    public void dump(){
        Log.d("ERR","DUMP");
    }
}
