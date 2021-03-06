package com.joemarini.fcmexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "FCMExample: ";

    private final String CONFIG_PROMO_MESSAGE_KEY="promo_message";
    private final String CONFIG_PROMO_ENABLED_KEY="promo_enabled";
    private long  PROMO_CACHE_DURATION=1800;
    private final int MIN_SESSION_DURATION=5000;
    private String m_FCMtoken;
    private TextView tvMsg;

    private FirebaseRemoteConfig mFRConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: get the FCM instance default token
         m_FCMtoken= FirebaseInstanceId.getInstance().getToken();
        tvMsg = (TextView)findViewById(R.id.textView2);
        mFRConfig = FirebaseRemoteConfig.getInstance();
        // TODO: Log the token to debug output so we can copy it
        ((Button)findViewById(R.id.btnLogToken)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Log.d(TAG, msg: "FCM token:"+m_FCMtoken);
                Log.d(TAG, "  FCM_FCM ttoken:"+m_FCMtoken);
               // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
               // Log.d("CHICHI", "Refreshed ttoken: " + refreshedToken);
            }
        });

        // TODO: subscribe to a topic
        ((Button)findViewById(R.id.btnSubscribe)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic("test-topic");
            }

        });

        // TODO: unsubscribe from a topic
        ((Button)findViewById(R.id.btnUnsubscribe)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("test-topic");
            }
        });

        FirebaseRemoteConfigSettings configSettings = new
                FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();

        mFRConfig.setConfigSettings(configSettings);
        mFRConfig.setDefaults(R.xml.firstlook_config_params);


        // TODO: When the activity starts up, look for intent information
        // that may have been passed in from the Notification tap
        if (getIntent().getExtras() != null) {
           String lmsg="";
           for (String key:getIntent().getExtras().keySet()){
            Object val=getIntent().getExtras().get(key);
               Log.d(TAG,"Key:"+key+" val:"+val+"\n");
               lmsg += "Key:"+key+" val:"+ val+"\n";

           }
            tvMsg.setText(lmsg);
        }
        else {
            tvMsg.setText("No launch information");
        }

        checkPromoEnabled();
    } //end of oncreate

   private void checkPromoEnabled(){
        if(mFRConfig.getInfo().getConfigSettings().isDeveloperModeEnabled() ){
            PROMO_CACHE_DURATION=0;
        }
        mFRConfig.fetch(PROMO_CACHE_DURATION).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(TAG,"suc");
                    mFRConfig.activateFetched();
                }else{
                    Log.e(TAG,"failed");
                }

            }
        });
   }

   private void showPromoButton(){
       boolean showBtn=false;
       String promoMsg="";

       //logic
       showBtn=mFRConfig.getBoolean(CONFIG_PROMO_ENABLED_KEY);
promoMsg=mFRConfig.getString(CONFIG_PROMO_MESSAGE_KEY);

       Button btn=(Button)findViewById(R.id.btnPromo);
       btn.setVisibility(showBtn ? View.VISIBLE : View.INVISIBLE);
       btn.setText(promoMsg);
   }

}
