package com.guango.society;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nijeguan on 10/12/16.
 */

public class GWay extends GXMLObject {
    private Map<String,String> wayInfo;
    private Map<String,String> tagInfo;
    private ArrayList<String> nodeList;



    public GWay(){
        wayInfo = new HashMap<>();
        tagInfo = new HashMap<>();
        nodeList = new ArrayList<>();

    }
    @Override
    public boolean attachInfo(String key, String val){
        wayInfo.put(key, val);
        return true;
    }

    @Override
    public boolean addNode(String ref) {
        nodeList.add(ref);
        return true;    }


    @Override
    public void dump() {
        Log.d("TESTDUMP","Way");
        Iterator<String> i = wayInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST" , "KEY:" +key+ " Val:" +wayInfo.get(key) );
        }
        Log.d("TESTTAG", "Tag KV");
        i = tagInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST" , "KEY:" +key+ " Val:" +tagInfo.get(key) );
        }
        Log.d("TESTND","Nd ref");
        i = nodeList.iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST" , "Ref:" +key);
        }
        Log.d("TESTEND","Way");
    }

    @Override
    public boolean addTag(String key, String val) {
        tagInfo.put(key,val);
        return true;
    }


}
