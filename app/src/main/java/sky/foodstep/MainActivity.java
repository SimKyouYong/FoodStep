package sky.foodstep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import sky.foodstep.common.ActivityEx;
import sky.foodstep.common.Check_Preferences;
import sky.foodstep.common.DEFINE;
import sky.foodstep.obj.DataObj;

/*
* AIzaSyC1bRHhlzxfHtELQLq1gqK2yV2XvfzXlgA
* */
public class MainActivity extends ActivityEx {
    LocationManager myLocationManager;

    String [][]Object_Array;
    private ArrayList<DataObj> arr = new ArrayList<DataObj>();
    private String []val = {"KEY_INDEX","NAME","ADDRESS","MENU","TYPE","NUMBER","LO_WI","LO_GY" };

    private Map<String, String> map = new HashMap<String, String>();
    private AccumThread mThread;
    private String idBySerialNumber;

    Boolean GPSSTATUS = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (myLocationManager == null) {
            myLocationManager = (LocationManager)getSystemService(
                    Context.LOCATION_SERVICE);
        }




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
            map.put("url", DEFINE.SERVER_URL + "FOODSTEP_JOIN.php");
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


        findViewById(R.id.btn1).setOnClickListener(btnListener);
        findViewById(R.id.btn2).setOnClickListener(btnListener);
        findViewById(R.id.btn3).setOnClickListener(btnListener);
        findViewById(R.id.btn4).setOnClickListener(btnListener);
        findViewById(R.id.btn5).setOnClickListener(btnListener);
        findViewById(R.id.btn6).setOnClickListener(btnListener);


    }
    // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsOptionsIntent , 1);
    }
    private void alertCheckGPS() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setMessage("원활한 서비스를 위해\nGPS를 활성화를 부탁 드립니다.");
        builder.setCancelable(false);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveConfigGPS();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GPSSTATUS = false;
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            Boolean isGpsEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.e("SKY" , "isGpsEnabled :: " + isGpsEnabled);
            if (!isGpsEnabled) {
                GPSSTATUS = false;
                alertCheckGPS();
                return;
            }

            customProgressPop();
            map.put("url", DEFINE.SERVER_URL + "FOODSTEP_SELECT.php");
            switch (v.getId()) {
                case R.id.btn1:
                    map.put("TYPE",    "한식");
                    break;
                case R.id.btn2:
                    map.put("TYPE",    "일식");
                    break;
                case R.id.btn3:
                    map.put("TYPE",    "양식");
                    break;
                case R.id.btn4:
                    map.put("TYPE",    "중식");
                    break;
                case R.id.btn5:
                    map.put("TYPE",    "그 외");
                    break;
                case R.id.btn6:
                    map.put("TYPE",    "ALL");
                    break;
            }
            //스레드 생성
            mThread = new AccumThread(MainActivity.this , mAfterAccum , map , 1 , 1 , val);
            mThread.start();		//스레드 시작!!
        }
    };
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
                map.put("url", DEFINE.SERVER_URL + "FOODSTEP_LOGIN.php");
                map.put("DEVICE_KEY",    idBySerialNumber);
                //스레드 생성
                mThread = new AccumThread(MainActivity.this , mAfterAccum , map , 0 , 2 , null);
                mThread.start();		//스레드 시작!!
            }else if (msg.arg1  == 1 ) {
                customProgressClose();
                arr.clear();
                Object_Array = (String [][]) msg.obj;
                if (Object_Array.length == 0) {
                    return;
                }
                //				Log.e("CHECK" ,"**********************  --->" + Object_Array[0].length);
                for (int i = 0; i < Object_Array.length; i++) {
                    for (int j = 0; j < Object_Array[0].length; j++) {
                        Log.e("CHECK" ,"value----> ---> Object_Array [" +i+"]["+j+"]"+  Object_Array[i][j]);
                    }
                }
                for (int i = 0; i < (Object_Array[0].length); i++){
                    if (Object_Array[0][i] != null) {
                      arr.add(new DataObj(""+Object_Array[0][i],
                                Object_Array[1][i],
                                Object_Array[2][i],
                                Object_Array[3][i],
                                Object_Array[4][i],
                                Object_Array[5][i],
                                Object_Array[6][i],
                                Object_Array[7][i]
                        ));
                    }
                }
                Intent intent6 = new Intent(MainActivity.this, DetailActivity.class);
                intent6.putParcelableArrayListExtra("obj" , arr);
                startActivity(intent6);

        }else if(msg.arg1  == 2){
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
