package sky.foodstep.obj;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentObj implements Parcelable{
	public static Creator<CommentObj> getCreator() {
		return CREATOR;
	}



	String KEY_INDEX;
	String DATA_INDEX;
    String ID;
    String BODY;
    String DATE;

    public CommentObj(String KEY_INDEX, String DATA_INDEX, String ID, String BODY, String DATE) {
        this.KEY_INDEX = KEY_INDEX;
        this.DATA_INDEX = DATA_INDEX;
        this.ID = ID;
        this.BODY = BODY;
        this.DATE = DATE;
    }

    public String getKEY_INDEX() {
        return KEY_INDEX;
    }

    public void setKEY_INDEX(String KEY_INDEX) {
        this.KEY_INDEX = KEY_INDEX;
    }

    public String getDATA_INDEX() {
        return DATA_INDEX;
    }

    public void setDATA_INDEX(String DATA_INDEX) {
        this.DATA_INDEX = DATA_INDEX;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBODY() {
        return BODY;
    }

    public void setBODY(String BODY) {
        this.BODY = BODY;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public CommentObj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(KEY_INDEX);
		dest.writeString(DATA_INDEX);
        dest.writeString(ID);
        dest.writeString(BODY);
        dest.writeString(DATE);
    }
	private void readFromParcel(Parcel in){

        KEY_INDEX = in.readString();
        DATA_INDEX = in.readString();
        ID = in.readString();
        BODY = in.readString();
        DATE = in.readString();
    }
	@SuppressWarnings("rawtypes")
	public static final Creator<CommentObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new CommentObj(in);
		}

		public Object[] newArray(int size) {
			return new CommentObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
