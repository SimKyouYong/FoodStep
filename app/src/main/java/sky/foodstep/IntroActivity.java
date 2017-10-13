package sky.foodstep;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class IntroActivity extends FragmentActivity {
	private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
	private static final int DELAY_TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
		if(permissionCheck== PackageManager.PERMISSION_DENIED){
			// 권한 없음
			Log.e("SKY", "권한 없음");
			ActivityCompat.requestPermissions(this,
					PERMISSIONS_STORAGE,
					1);
		}else{
			// 권한 있음
			Log.e("SKY", "권한 있음");
			ActivityCompat.requestPermissions(this,
					PERMISSIONS_STORAGE,
					1);
		}
		//MainMove();
	}
	private void MainMove(){
		Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
				Intent intent = new Intent(IntroActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, DELAY_TIME);
	}
	public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {
				Log.e("SKY" , "성공 : " + grantResults.length);
                MainMove();
				return;
			}
		}
	}
}
