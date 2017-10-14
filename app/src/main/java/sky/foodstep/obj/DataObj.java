package sky.foodstep.obj;

import android.os.Parcel;
import android.os.Parcelable;

public class DataObj implements Parcelable{
	public static Creator<DataObj> getCreator() {
		return CREATOR;
	}



	String KEY_INDEX;
	String NAME;
    String ADDRESS;
    String MENU;
    String TYPE;
    String NUMBER;
    String LO_WI;
    String LO_GY;

    public DataObj(String KEY_INDEX, String NAME, String ADDRESS, String MENU, String TYPE, String NUMBER, String LO_WI, String LO_GY) {
        this.KEY_INDEX = KEY_INDEX;
        this.NAME = NAME;
        this.ADDRESS = ADDRESS;
        this.MENU = MENU;
        this.TYPE = TYPE;
        this.NUMBER = NUMBER;
        this.LO_WI = LO_WI;
        this.LO_GY = LO_GY;
    }

    public String getKEY_INDEX() {
        return KEY_INDEX;
    }

    public void setKEY_INDEX(String KEY_INDEX) {
        this.KEY_INDEX = KEY_INDEX;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getMENU() {
        return MENU;
    }

    public void setMENU(String MENU) {
        this.MENU = MENU;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getLO_WI() {
        return LO_WI;
    }

    public void setLO_WI(String LO_WI) {
        this.LO_WI = LO_WI;
    }

    public String getLO_GY() {
        return LO_GY;
    }

    public void setLO_GY(String LO_GY) {
        this.LO_GY = LO_GY;
    }

    public DataObj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(KEY_INDEX);
		dest.writeString(NAME);
        dest.writeString(ADDRESS);
        dest.writeString(MENU);
        dest.writeString(TYPE);
        dest.writeString(NUMBER);
        dest.writeString(LO_WI);
        dest.writeString(LO_GY);
    }
	private void readFromParcel(Parcel in){

        KEY_INDEX = in.readString();
        NAME = in.readString();
        ADDRESS = in.readString();
        MENU = in.readString();
        TYPE = in.readString();
        NUMBER = in.readString();
        LO_WI = in.readString();
        LO_GY = in.readString();
    }
	@SuppressWarnings("rawtypes")
	public static final Creator<DataObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new DataObj(in);
		}

		public Object[] newArray(int size) {
			return new DataObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
