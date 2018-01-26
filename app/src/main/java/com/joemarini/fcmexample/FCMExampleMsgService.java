package com.joemarini.fcmexample;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMExampleMsgService extends FirebaseMessagingService {
    private static final String TAG = "FCMExampleMsgService";

    @Override
    public void onMessageReceived(RemoteMessage theMsg){
        Log.d(TAG,"Message reciev from : "+ theMsg.getFrom());

        if (theMsg.getData().size()>0){
            Log.d(TAG,"Msg size:  "+theMsg.getData().size());


            for (String key:theMsg.getData().keySet()){
                //Object val=getIntent().getExtras().get(key);
                Log.d(TAG,"Key:"+key+" val:"+theMsg.getData().get(key)+"\n");


            }
        }
    }

    @Override
    public void onDeletedMessages(){

    }

}
