package sky.foodstep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private ArrayList<DataObj> arr_copy = new ArrayList<DataObj>();
    private String []val = {"KEY_INDEX","NAME","ADDRESS","MENU","TYPE","NUMBER","LO_WI","LO_GY" };
    final CharSequence[] items = {"500m","1km"};


    private double my_wi = 0;
    private double my_gy = 0;
    private Map<String, String> map = new HashMap<String, String>();
    private AccumThread mThread;
    private String idBySerialNumber;

    private String allcheck = "";
    Boolean GPSSTATUS = false;

    private int distance_position = 0;
    private Button distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        distance = (Button)findViewById(R.id.distance);
        distance.setText("" + items[0]);
        if (myLocationManager == null) {
            myLocationManager = (LocationManager)getSystemService(
                    Context.LOCATION_SERVICE);
        }
        Boolean isGpsEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("SKY" , "isGpsEnabled :: " + isGpsEnabled);
        


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
        findViewById(R.id.distance).setOnClickListener(btnListener1);
        if (!isGpsEnabled) {
            GPSSTATUS = false;
            alertCheckGPS();
            return;
        }else{
            settingGPS();
        }

    }
    private void settingGPS() {
        // Acquire a reference to the system Location Manager

        // GPS 프로바이더 사용가능여부
        Boolean isGPSEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        Boolean isNetworkEnabled = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.e("Main", "isGPSEnabled=" + isGPSEnabled);
        Log.e("Main", "isNetworkEnabled=" + isNetworkEnabled);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                my_wi = location.getLatitude();
                my_gy = location.getLongitude();

//                Log.e("Main", "222my_wi=" + my_wi);
//                Log.e("Main", "222my_gy=" + my_gy);

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }
        myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        myLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = myLocationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }

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

            //위도 경도 가져올때까지 기다려..
            if(my_wi == 0){
                Log.e("SKY" , "자신의 위치를 가져오는 중입니다 :: " + isGpsEnabled);

                Toast.makeText(MainActivity.this , "자신의 위치를 가져오는 중입니다 " , Toast.LENGTH_SHORT).show();
                return;
            }

            customProgressPop();
            map.put("url", DEFINE.SERVER_URL + "FOODSTEP_SELECT.php");
            switch (v.getId()) {
                case R.id.btn1:
                    allcheck = "";
                    map.put("TYPE",    "한식");
                    break;
                case R.id.btn2:
                    allcheck = "";
                    map.put("TYPE",    "일식");
                    break;
                case R.id.btn3:
                    allcheck = "";
                    map.put("TYPE",    "양식");
                    break;
                case R.id.btn4:
                    allcheck = "";
                    map.put("TYPE",    "중식");
                    break;
                case R.id.btn5:
                    allcheck = "";
                    map.put("TYPE",    "그 외");
                    break;
                case R.id.btn6:
                    allcheck = "all";
                    map.put("TYPE",    "ALL");
                    break;
            }
            //스레드 생성
            mThread = new AccumThread(MainActivity.this , mAfterAccum , map , 1 , 1 , val);
            mThread.start();		//스레드 시작!!
        }
    };
    View.OnClickListener btnListener1 = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.distance:

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("선택하세요")
                            .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                                public void onClick(DialogInterface dialog, int index){
                                    distance.setText((String) items[index]);
                                    distance_position = index;
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기
                    break;
            }
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
                arr_copy.clear();
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
                if (!allcheck.equals("all")){



                    for (int i = 0; i < (Object_Array[0].length); i++){
                            if (Object_Array[0][i] != null) {
                                double setting_distance = 0;

                                if (distance_position == 0){
                                    //500m
                                    setting_distance = 0.5;
                                }else{
                                    //1km
                                    setting_distance = 1.0;
                                }
                                Log.e("SKY", "1setting_distance :: " + setting_distance);

                                //거리 계산
                                Log.e("SKY", "1my_wi :: " + my_wi);
                                Log.e("SKY", "1my_gy :: " + my_gy);

                                double food_wi = Double.parseDouble(Object_Array[6][i]);
                                double food_gy = Double.parseDouble(Object_Array[7][i]);
                                Log.e("SKY", "food_wi :: " + food_wi);
                                Log.e("SKY", "food_gy :: " + food_gy);


                                double distance_km = distance(my_wi, my_gy, food_wi, food_gy, "kilometer");
                                Log.e("SKY", "333distance_km :: " + distance_km);

                                if (setting_distance > distance_km){
                                    arr_copy.add(new DataObj(""+Object_Array[0][i],
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

                    }
                    if(arr_copy.size() == 0){
                        Log.e("SKY" , "거리안에 들어오는 음식점이 존재하지않습니다 :: ");

                        Toast.makeText(MainActivity.this , "거리안에 들어오는 음식점이 존재하지않습니다 " , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //랜덤
                    Random generator = new Random();
                    int random = generator.nextInt(arr_copy.size());
                    Log.e("SKY" , "random  -> " + random);

                    for (int i = 0; i < arr_copy.size(); i++){
                        if(random == i){

                                arr.add(new DataObj(""+arr_copy.get(i).getKEY_INDEX(),
                                        arr_copy.get(i).getNAME(),
                                        arr_copy.get(i).getADDRESS(),
                                        arr_copy.get(i).getMENU(),
                                        arr_copy.get(i).getTYPE(),
                                        arr_copy.get(i).getNUMBER(),
                                        arr_copy.get(i).getLO_WI(),
                                        arr_copy.get(i).getLO_GY()
                            ));
                        }
                    }
                }else{
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
                }

                if(arr.size() == 0){
                    Log.e("SKY" , "거리안에 들어오는 음식점이 존재하지않습니다 :: ");

                    Toast.makeText(getApplicationContext() , "거리안에 들어오는 음식점이 존재하지않습니다 " , Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent6 = new Intent(MainActivity.this, DetailActivity.class);
                intent6.putParcelableArrayListExtra("obj" , arr);
                if (distance_position == 0){
                    //500m
                    intent6.putExtra("distance" , 0.5);
                }else{
                    //1km
                    intent6.putExtra("distance" , 1.0);
                }
                intent6.putExtra("allcheck" , allcheck);



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
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("SKY" , "RESULT :: " + requestCode);
        Log.e("SKY" , "resultCode :: " + resultCode);
        Log.e("SKY" , "data :: " + data);
        switch (requestCode) {
            case 1:
                settingGPS();
                break;

        }

    }
}
