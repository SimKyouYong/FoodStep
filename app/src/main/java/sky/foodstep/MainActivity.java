package sky.foodstep;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import sky.foodstep.common.ActivityEx;
import sky.foodstep.common.Check_Preferences;
import sky.foodstep.common.DEFINE;

/*
* AIzaSyC1bRHhlzxfHtELQLq1gqK2yV2XvfzXlgA
* */
public class MainActivity extends ActivityEx {


    private Map<String, String> map = new HashMap<String, String>();
    private AccumThread mThread;
    private String idBySerialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            idBySerialNumber = (String) Build.class.getField("SERIAL").get(null);
            Log.e("SKY" , "idBySerialNumber :: " + idBySerialNumber);
        } catch (Exception e) {
            idBySerialNumber = "";
            e.printStackTrace();;
        }

        if(Check_Preferences.getAppPreferences(this , "KEY_INDEX").equals("")){
            //회원가입
            customProgressPop();
            map.put("url", DEFINE.SERVER_URL + "MEMBER_JOIN.php");
            map.put("DEVICE_KEY",    idBySerialNumber);
            //스레드 생성
            mThread = new AccumThread(this , mAfterAccum , map , 0 , 0 , null);
            mThread.start();		//스레드 시작!!
        }else{
            //이미 로그인 됨!.
            Log.e("SKY" , "KEY_INDEX :: " + Check_Preferences.getAppPreferences(this , "KEY_INDEX"));
            Log.e("SKY" , "DEVICE_KEY :: " + Check_Preferences.getAppPreferences(this , "DEVICE_KEY"));
            Log.e("SKY" , "DATE :: " + Check_Preferences.getAppPreferences(this , "DATE"));
        }

        //TABLE : FOODSTEP_MEMBER(KEY_INDEX , DEVICE_KEY , DATE)



        //Intent intent4 = new Intent(MainActivity.this, DetailActivity.class);
        //startActivity(intent4);
    }

    Handler mAfterAccum = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            customProgressClose();
            if (msg.arg1  == 0 ) {
                String res = (String)msg.obj;
                Log.e("CHECK" , "RESULT  -> " + res);

                //회원가입
                customProgressPop();
                map.put("url", DEFINE.SERVER_URL + "MEMBER_LOGIN.php");
                map.put("DEVICE_KEY",    idBySerialNumber);
                //스레드 생성
                mThread = new AccumThread(MainActivity.this , mAfterAccum , map , 0 , 1 , null);
                mThread.start();		//스레드 시작!!
            }else{
                //로그인 성공
                String res = (String)msg.obj;
                Log.e("CHECK" , "RESULT  -> " + res);
                String val[] = res.split(",");
                if (val[0].equals("true")){
                    //로그인 성공
                    Check_Preferences.setAppPreferences(MainActivity.this , "KEY_INDEX"    , val[1]);
                    Check_Preferences.setAppPreferences(MainActivity.this , "DEVICE_KEY"    , val[2]);
                    Check_Preferences.setAppPreferences(MainActivity.this , "DATE"    , val[3]);
                }else{
                    Toast.makeText(getApplicationContext() , "가입한 이력이 없음." , Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
}
