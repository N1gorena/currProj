package com.guango.society;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nijeguan on 10/12/16.
 */

public class GRelation extends GXMLObject {
    private Map<String,String> relationInfo;
    private Map<String,String> tagInfo;
    private ArrayList<Member> memberInfo;


    public GRelation(){
        relationInfo = new HashMap<>();
        tagInfo = new HashMap<>();
        memberInfo = new ArrayList<>();
    }


    @Override
    public boolean addTag(String key, String val) {
        tagInfo.put(key,val);
        return true;
    }

    @Override
    public boolean attachInfo(String key, String val) {
        relationInfo.put(key,val);
        return true;
    }

    @Override
    public boolean addMember(String type, String ref,String role) {
        memberInfo.add(new Member(type,ref,role));
        return true;
    }

    @Override
    public void dump() {
        Log.d("TESTDUMP","RELATION");
        Iterator<String> i = relationInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST",  "Key:"+key+" Val:"+relationInfo.get(key) );
        }
        Log.d("TESTTAG", "Tag KV");
        i = tagInfo.keySet().iterator();
        while(i.hasNext()){
            String key = i.next();
            Log.d( "TEST", "Key:"+key+" Val:"+tagInfo.get(key) );
        }
        Log.d("TESTTAG", "Member KV");
        Iterator<Member> iM = memberInfo.iterator();
        while(iM.hasNext()){
            Log.d( "TESTm",iM.next().toString() );
        }

    }
    public class Member{
        private String mType;
        private String mRef;
        private String mRole;
        public Member(String type,String ref,String role){
            mType = type;
            mRole = role;
            mRef = ref;
        }
        @Override
        public String toString(){
            return "Type:"+mType+" Ref:"+mRef+" Role:"+mRole;
        }
    }
}
