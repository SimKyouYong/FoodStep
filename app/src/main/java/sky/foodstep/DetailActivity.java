package sky.foodstep;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.kr.sky.AccumThread;
import sky.foodstep.adapter.Comment_Adapter;
import sky.foodstep.common.Check_Preferences;
import sky.foodstep.common.DEFINE;
import sky.foodstep.obj.CommentObj;
import sky.foodstep.obj.DataObj;



/*
* AIzaSyC1bRHhlzxfHtELQLq1gqK2yV2XvfzXlgA
* */
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    String [][]Object_Array;

    private Map<String, String> map = new HashMap<String, String>();
    private AccumThread mThread;
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    protected ProgressDialog  customDialog = null;

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    private String []val = {"KEY_INDEX","DATA_INDEX","ID","BODY","DATE"};



    private  String ThisDataIndex = "";
    Comment_Adapter m_Adapter;
    ListView list_number;

    private Button comment_ok;
    private Boolean FirstFlag = false;
    private EditText edit_comment;
    private TextView name , address , menu;
    private MapFragment mapFragment;
    private ArrayList<DataObj> arr = new ArrayList<DataObj>();
    private ArrayList<CommentObj> arr2 = new ArrayList<CommentObj>();
    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        list_number = (ListView)findViewById(R.id.list);
        edit_comment = (EditText)findViewById(R.id.edit_comment);
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        menu = (TextView)findViewById(R.id.menu);
        comment_ok = (Button)findViewById(R.id.comment_ok);

        mActivity = this;
        Intent i = getIntent();
        //Bundle bundle = getIntent().getExtras();
        arr = i.getParcelableArrayListExtra("obj");

        mGoogleApiClient = new GoogleApiClient.Builder(DetailActivity.this)
                .addConnectionCallbacks(DetailActivity.this)
                .addOnConnectionFailedListener(DetailActivity.this)
                .addApi(LocationServices.API)
                .build();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(DetailActivity.this);



    }
    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.comment_ok:
                    if(edit_comment.getText().toString().length() ==0){
                        Toast.makeText(getApplicationContext() , "댓글을 입력해주세요" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    customProgressPop();
                    map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT_WRITE.php");
                    map.put("ID", Check_Preferences.getAppPreferences(DetailActivity.this , "DEVICE_KEY").substring(0 , 5));
                    map.put("DATA_INDEX", ThisDataIndex);
                    map.put("BODY", edit_comment.getText().toString());
                    //스레드 생성
                    mThread = new AccumThread(DetailActivity.this , mAfterAccum , map , 0 , 0 , null);
                    mThread.start();		//스레드 시작!!
                    break;
            }
        }
    };
    Handler mAfterAccum = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.arg1  == 0){
                customProgressClose();
                customProgressPop();
                map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT.php");
                ThisDataIndex = arr.get(arr.size()-1).getKEY_INDEX();
                map.put("DATA_INDEX", ThisDataIndex);
                //스레드 생성
                mThread = new AccumThread(DetailActivity.this , mAfterAccum , map , 1 , 2 , val);
                mThread.start();		//스레드 시작!!
            }
            else if (msg.arg1  == 2 ) {
                customProgressClose();
                arr2.clear();
                Object_Array = (String [][]) msg.obj;
                if (Object_Array.length == 0) {
                    m_Adapter = new Comment_Adapter( DetailActivity.this , arr2 );
                    //list_number.setOnItemClickListener(mItemClickListener);
                    list_number.setAdapter(m_Adapter);
                    return;
                }
                for (int i = 0; i < Object_Array.length; i++) {
                    for (int j = 0; j < Object_Array[0].length; j++) {
                        Log.e("CHECK" ,"value----> ---> Object_Array [" +i+"]["+j+"]"+  Object_Array[i][j]);
                    }
                }
                for (int i = 0; i < (Object_Array[0].length); i++){
                    if (Object_Array[0][i] != null) {
                        arr2.add(new CommentObj(""+Object_Array[0][i],
                                Object_Array[1][i],
                                Object_Array[2][i],
                                Object_Array[3][i],
                                Object_Array[4][i]
                        ));
                    }
                }
                m_Adapter = new Comment_Adapter( DetailActivity.this , arr2 );
                //list_number.setOnItemClickListener(mItemClickListener);
                list_number.setAdapter(m_Adapter);
            }
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("SKY", "onMapReady :");
        mGoogleMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        //setDefaultLocation();
        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {
                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d( TAG, "onMapClick :");
            }
        });
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {
                if (mMoveMapByUser == true && mRequestingLocationUpdates){
                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }
                mMoveMapByUser = true;
            }
        });
        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
            }
        });

        if (!getIntent().getStringExtra("allcheck").equals("all")){
            return;
        }
        for (int i= 0; i < arr.size(); i++){
            if(i == 0){
                customProgressPop();
                map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT.php");
                ThisDataIndex = arr.get(arr.size()-1).getKEY_INDEX();
                map.put("DATA_INDEX", ThisDataIndex);
                //스레드 생성
                mThread = new AccumThread(this , mAfterAccum , map , 1 , 2 , val);
                mThread.start();		//스레드 시작!!
            }
            double wi = Double.parseDouble( arr.get(i).getLO_WI() );
            double gy = Double.parseDouble( arr.get(i).getLO_GY() );

            Log.e("SKY" , "위도 :: " + wi);
            Log.e("SKY" , "경도 :: " + gy);
            LatLng location = new LatLng(wi, gy);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(i+". " + arr.get(i).getNAME());
            markerOptions.snippet(arr.get(i).getADDRESS());
            mGoogleMap.addMarker(markerOptions);

            mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                public boolean onMarkerClick(Marker marker) {

                    if(!marker.getTitle().contains(".")){
                        return true;
                    }
                    String text = "[마커 클릭 이벤트] latitude ="
                            + marker.getPosition().latitude + ", longitude ="
                            + marker.getPosition().longitude;
                    Log.e("SKY" , "marker.getTitle()" + marker.getTitle());
                    String[] posittion = marker.getTitle().split(". ");
                    //Toast.makeText(getApplicationContext(), text  + posittion[0], Toast.LENGTH_LONG).show();
                    ThisDataIndex = posittion[0];

                    name.setText("" + arr.get(Integer.parseInt(posittion[0])).getNAME());
                    address.setText("주소 : " + arr.get(Integer.parseInt(posittion[0])).getADDRESS());
                    menu.setText("메뉴\n" + arr.get(Integer.parseInt(posittion[0])).getMENU());


                    customProgressPop();
                    map.clear();
                    map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT.php");
                    map.put("DATA_INDEX", posittion[0]);
                    //스레드 생성
                    mThread = new AccumThread(DetailActivity.this , mAfterAccum , map , 1 , 2 , val);
                    mThread.start();		//스레드 시작!!
                    return false;
                }
            });



            if(i == (arr.size()-1)){
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }

        }
    }
    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }


        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);

        }

    }



    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }
    @Override
    public void onLocationChanged(Location location) {


        Log.e("SKY", "onLocationChanged : ");

        if (!FirstFlag){
            FirstFlag = true;
            String markerTitle = getCurrentAddress(location);
            String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                    + " 경도:" + String.valueOf(location.getLongitude());
            Log.e("SKY", "onLocationChanged111 : "+ markerSnippet);
            //현재 위치에 마커 생성하고 이동
            setCurrentLocation(location, markerTitle, markerSnippet);

            mCurrentLocatiion = location;

            Intent i = getIntent();
            //Bundle bundle = getIntent().getExtras();
            arr = i.getParcelableArrayListExtra("obj");
            Log.e("SKY", "arr SIZE :: " + arr.size());

            Log.e("SKY", "allcheck :: " + getIntent().getStringExtra("allcheck"));


            if (!getIntent().getStringExtra("allcheck").equals("all")){
                double setting_distance = i.getDoubleExtra("distance" , 0);
                Log.e("SKY", "setting_distance :: " + setting_distance);

                //거리 계산
                double my_wi = location.getLatitude();
                double my_gy = location.getLongitude();
                Log.e("SKY", "my_wi :: " + my_wi);
                Log.e("SKY", "my_gy :: " + my_gy);

                double food_wi = Double.parseDouble(arr.get(0).getLO_WI());
                double food_gy = Double.parseDouble(arr.get(0).getLO_GY());
                Log.e("SKY", "food_wi :: " + food_wi);
                Log.e("SKY", "food_gy :: " + food_gy);


                double distance_km = distance(my_wi, my_gy, food_wi, food_gy, "kilometer");
                Log.e("SKY", "distance_km :: " + distance_km);

                if (setting_distance > distance_km){

                    double wi = Double.parseDouble( arr.get(0).getLO_WI() );
                    double gy = Double.parseDouble( arr.get(0).getLO_GY() );

                    //Log.e("SKY" , "위도 :: " + wi);
                    //Log.e("SKY" , "경도 :: " + gy);
                    LatLng location1 = new LatLng(wi, gy);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location1);
                    markerOptions.title(i+". " + arr.get(0).getNAME());
                    markerOptions.snippet(arr.get(0).getADDRESS());
                    mGoogleMap.addMarker(markerOptions);

                    mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                        public boolean onMarkerClick(Marker marker) {

                            if(!marker.getTitle().contains(".")){
                                return true;
                            }
                            String text = "[마커 클릭 이벤트] latitude ="
                                    + marker.getPosition().latitude + ", longitude ="
                                    + marker.getPosition().longitude;
                            Log.e("SKY" , "marker.getTitle()" + marker.getTitle());
                            String[] posittion = marker.getTitle().split(". ");
                            //Toast.makeText(getApplicationContext(), text  + posittion[0], Toast.LENGTH_LONG).show();
                            ThisDataIndex = posittion[0];

                            name.setText("" + arr.get(Integer.parseInt(posittion[0])).getNAME());
                            address.setText("주소 : " + arr.get(Integer.parseInt(posittion[0])).getADDRESS());
                            menu.setText("메뉴\n" + arr.get(Integer.parseInt(posittion[0])).getMENU());


                            customProgressPop();
                            map.clear();
                            map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT.php");
                            map.put("DATA_INDEX", posittion[0]);
                            //스레드 생성
                            mThread = new AccumThread(DetailActivity.this , mAfterAccum , map , 1 , 2 , val);
                            mThread.start();		//스레드 시작!!
                            return false;
                        }
                    });
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));



                    //보여 줘야함.
                    name.setText("" + arr.get(arr.size()-1).getNAME());
                    address.setText("주소 : " + arr.get(arr.size()-1).getADDRESS());
                    menu.setText("메뉴\n" + arr.get(arr.size()-1).getMENU());

                    findViewById(R.id.comment_ok).setOnClickListener(btnListener);
                    customProgressPop();
                    map.put("url", DEFINE.SERVER_URL + "FOODSTEP_COMMENT.php");
                    ThisDataIndex = arr.get(arr.size()-1).getKEY_INDEX();
                    map.put("DATA_INDEX", ThisDataIndex);
                    //스레드 생성
                    mThread = new AccumThread(this , mAfterAccum , map , 1 , 2 , val);
                    mThread.start();		//스레드 시작!!
                }else{
                    name.setText("");
                    address.setText("");
                    menu.setText("");

                    comment_ok.setVisibility(View.GONE);
                    edit_comment.setVisibility(View.GONE);
                }
            }






        }else{
            Log.e("SKY", "onLocationChanged else: ");
        }

    }


    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(Location location) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //구글맵의 디폴트 현재 위치는 파란색 동그라미로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정해야함.
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = mGoogleMap.addMarker(markerOptions);


        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }


    public void setDefaultLocation() {

        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }



            } else {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
    }
    public void customProgressPop(){
        try{
            if (customDialog==null){
                customDialog = new ProgressDialog( this , ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                customDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                customDialog.setMessage("로딩중 입니다.");
                customDialog.show();
            }
        }catch(Exception ex){}
    }
    public void customProgressClose(){
        if (customDialog!=null && customDialog.isShowing()){
            try{
                customDialog.cancel();
                customDialog.dismiss();
                customDialog = null;
            }catch(Exception e)
            {}
        }
    }

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


}