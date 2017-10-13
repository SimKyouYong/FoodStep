package sky.foodstep.common;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class CommonUtil {
	private static CommonUtil _instance;
	public String MEMBER_ID;
	public String MEMEBER_SELF_ID;

	static {
		_instance = new CommonUtil();
		try {								 
			_instance.MEMBER_ID = 	      "";
			_instance.MEMEBER_SELF_ID =   "";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CommonUtil getInstance() {
		return _instance;
	}

	
	public ArrayList<String> Token_String(String url , String token){
		ArrayList<String> Obj = new ArrayList<String>();

		StringTokenizer st1 = new StringTokenizer( url , token);
		while(st1.hasMoreTokens()){
			Obj.add(st1.nextToken());
		}
		return Obj;
	}
}
