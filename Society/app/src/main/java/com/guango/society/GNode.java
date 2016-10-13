package com.guango.society;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nijeguan on 10/12/16.
 */

public class GNode extends GXMLObject{
    private Map<String,String> nodeInfo;
    private Map<String,String> tagInfo;

    public GNode(){
        nodeInfo = new HashMap<>();
        tagInfo = new HashMap<>();
    }
    @Override
    public boolean attachInfo(String key, String val){
        nodeInfo.put(key, val);
        return true;
    }

    @Override
    public void dump() {
        Log.d("TESTDUMP","Node");
        Iterator<String> i = nodeInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d("TEST","KEY:" +key+ " Val:" +nodeInfo.get(key) );
        }
        Log.d("TESTTAG", "Tag KV");
        Log.d("TEST",""+tagInfo.size());
        i = tagInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST" , "KEY:" +key+ " Val:" +tagInfo.get(key) );
        }
        Log.d("TESTEND","Node");
    }


    @Override
    public boolean addTag(String key, String val) {
        tagInfo.put(key,val);
        return true;
    }
}
