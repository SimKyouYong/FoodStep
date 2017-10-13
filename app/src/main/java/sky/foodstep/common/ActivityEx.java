package sky.foodstep.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;


public class ActivityEx extends Activity{
	private static Typeface mTypeface = null;
    protected ProgressDialog  customDialog = null;
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}
	protected void drawBigImage(ImageView imageView, int resId) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inSampleSize = 1;
			options.inPurgeable = true;
			Bitmap src = BitmapFactory.decodeResource(getResources(), resId, options);
			Bitmap resize = Bitmap.createScaledBitmap(src, options.outWidth, options.outHeight, true);
			imageView.setImageBitmap(resize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//이미지 셋팅
	public int setimg(String _resName){
		String resName = "@drawable/" + _resName;
		String packName = this.getPackageName(); // 패키지명
		int resID = this.getResources().getIdentifier(resName, "drawable", packName);
		return resID;
	}
	private void setGlobalFont(View view) {
		if (view != null) {
			if(view instanceof ViewGroup){
				ViewGroup vg = (ViewGroup)view;
				int vgCnt = vg.getChildCount();
				for(int i=0; i < vgCnt; i++){
					View v = vg.getChildAt(i);
					if(v instanceof TextView){
						((TextView) v).setTypeface(mTypeface);
					}
					setGlobalFont(v);
				}
			}
		}
	}


	public boolean checkNetwordState() {
		ConnectivityManager connManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo state_3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo state_blue = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

		return state_3g.isConnected() || state_wifi.isConnected()|| state_blue.isConnected();
	}
	protected void DialogSimple()
	{
		AlertDialog.Builder alt_shut = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
		alt_shut.setMessage("종료하시겠습니까?")
		.setCancelable(false)
		.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		AlertDialog alert_shut = alt_shut.create();
		alert_shut.show();
	}
	public boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;    
	}
	public void confirmDialog(String message) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
		//		.setTitle("부적결제 후 전화상담 서비스로 연결 되며 12시간 동안 재연결 무료 입니다.\n(운수대톡 )")
		ab.setMessage(message);
		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				return;
			}
		})
		.show();
	}

	public static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}  
	// 특정 폴더의 파일 목록을 구해서 반환
	public String[] getFileList(String strPath) {
		// 폴더 경로를 지정해서 File 객체 생성
		File fileRoot = new File(strPath);
		// 해당 경로가 폴더가 아니라면 함수 탈출
		if( fileRoot.isDirectory() == false )
			return null;
		// 파일 목록을 구한다
		String[] fileList = fileRoot.list();
		return fileList;
	}
	/**
     * 위도,경도로 주소구하기
     * @param lat
     * @param lng
     * @return 주소
     */
    public static String getAddress(Context mContext,double lat, double lng) {                                             
        String nowAddress ="현재 위치를 확인 할 수 없습니다."; 
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List <Address> address;
        try {
          if (geocoder != null) {
                  //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로 
                  //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);
               if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;                   
                }
            }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return nowAddress;
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
}
