package sky.foodstep.obj;

import android.os.Parcel;
import android.os.Parcelable;

public class BuySellObj implements Parcelable{
	public static Creator<BuySellObj> getCreator() {
		return CREATOR;
	}

	String KEY_INDEX;
	String PARENT_KEYINDEX;
    String BODY;
    String TITLE;
    String SELF_ID;
    String GOOD_EA;
    String COMMENT_EA;
    String DATE;
    String IMG_1;
    String IMG_2;
    String IMG_3;
    String IMG_4;
    String IMG_5;
    String IMG_6;
    String IMG_7;
    String IMG_8;
    String IMG_9;
    String IMG_10;
    String SELF_ID_KEY_INDEX;
    String CATEGORY_1;
    String CATEGORY_2;
    String CATEGORY_3;
    String CATEGORY_4;
    String GOOD_FLAG;
    int COUNT;

    public BuySellObj(String KEY_INDEX, String PARENT_KEYINDEX, String BODY, String TITLE, String SELF_ID, String GOOD_EA, String COMMENT_EA, String DATE, String IMG_1, String IMG_2, String IMG_3, String IMG_4, String IMG_5, String IMG_6, String IMG_7, String IMG_8, String IMG_9, String IMG_10, String SELF_ID_KEY_INDEX, String CATEGORY_1, String CATEGORY_2, String CATEGORY_3, String CATEGORY_4, String GOOD_FLAG, int COUNT) {
        this.KEY_INDEX = KEY_INDEX;
        this.PARENT_KEYINDEX = PARENT_KEYINDEX;
        this.BODY = BODY;
        this.TITLE = TITLE;
        this.SELF_ID = SELF_ID;
        this.GOOD_EA = GOOD_EA;
        this.COMMENT_EA = COMMENT_EA;
        this.DATE = DATE;
        this.IMG_1 = IMG_1;
        this.IMG_2 = IMG_2;
        this.IMG_3 = IMG_3;
        this.IMG_4 = IMG_4;
        this.IMG_5 = IMG_5;
        this.IMG_6 = IMG_6;
        this.IMG_7 = IMG_7;
        this.IMG_8 = IMG_8;
        this.IMG_9 = IMG_9;
        this.IMG_10 = IMG_10;
        this.SELF_ID_KEY_INDEX = SELF_ID_KEY_INDEX;
        this.CATEGORY_1 = CATEGORY_1;
        this.CATEGORY_2 = CATEGORY_2;
        this.CATEGORY_3 = CATEGORY_3;
        this.CATEGORY_4 = CATEGORY_4;
        this.GOOD_FLAG = GOOD_FLAG;
        this.COUNT = COUNT;
    }

    public String getKEY_INDEX() {
        return KEY_INDEX;
    }

    public void setKEY_INDEX(String KEY_INDEX) {
        this.KEY_INDEX = KEY_INDEX;
    }

    public String getPARENT_KEYINDEX() {
        return PARENT_KEYINDEX;
    }

    public void setPARENT_KEYINDEX(String PARENT_KEYINDEX) {
        this.PARENT_KEYINDEX = PARENT_KEYINDEX;
    }

    public String getBODY() {
        return BODY;
    }

    public void setBODY(String BODY) {
        this.BODY = BODY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSELF_ID() {
        return SELF_ID;
    }

    public void setSELF_ID(String SELF_ID) {
        this.SELF_ID = SELF_ID;
    }

    public String getGOOD_EA() {
        return GOOD_EA;
    }

    public void setGOOD_EA(String GOOD_EA) {
        this.GOOD_EA = GOOD_EA;
    }

    public String getCOMMENT_EA() {
        return COMMENT_EA;
    }

    public void setCOMMENT_EA(String COMMENT_EA) {
        this.COMMENT_EA = COMMENT_EA;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getIMG_1() {
        return IMG_1;
    }

    public void setIMG_1(String IMG_1) {
        this.IMG_1 = IMG_1;
    }

    public String getIMG_2() {
        return IMG_2;
    }

    public void setIMG_2(String IMG_2) {
        this.IMG_2 = IMG_2;
    }

    public String getIMG_3() {
        return IMG_3;
    }

    public void setIMG_3(String IMG_3) {
        this.IMG_3 = IMG_3;
    }

    public String getIMG_4() {
        return IMG_4;
    }

    public void setIMG_4(String IMG_4) {
        this.IMG_4 = IMG_4;
    }

    public String getIMG_5() {
        return IMG_5;
    }

    public void setIMG_5(String IMG_5) {
        this.IMG_5 = IMG_5;
    }

    public String getIMG_6() {
        return IMG_6;
    }

    public void setIMG_6(String IMG_6) {
        this.IMG_6 = IMG_6;
    }

    public String getIMG_7() {
        return IMG_7;
    }

    public void setIMG_7(String IMG_7) {
        this.IMG_7 = IMG_7;
    }

    public String getIMG_8() {
        return IMG_8;
    }

    public void setIMG_8(String IMG_8) {
        this.IMG_8 = IMG_8;
    }

    public String getIMG_9() {
        return IMG_9;
    }

    public void setIMG_9(String IMG_9) {
        this.IMG_9 = IMG_9;
    }

    public String getIMG_10() {
        return IMG_10;
    }

    public void setIMG_10(String IMG_10) {
        this.IMG_10 = IMG_10;
    }

    public String getSELF_ID_KEY_INDEX() {
        return SELF_ID_KEY_INDEX;
    }

    public void setSELF_ID_KEY_INDEX(String SELF_ID_KEY_INDEX) {
        this.SELF_ID_KEY_INDEX = SELF_ID_KEY_INDEX;
    }

    public String getCATEGORY_1() {
        return CATEGORY_1;
    }

    public void setCATEGORY_1(String CATEGORY_1) {
        this.CATEGORY_1 = CATEGORY_1;
    }

    public String getCATEGORY_2() {
        return CATEGORY_2;
    }

    public void setCATEGORY_2(String CATEGORY_2) {
        this.CATEGORY_2 = CATEGORY_2;
    }

    public String getCATEGORY_3() {
        return CATEGORY_3;
    }

    public void setCATEGORY_3(String CATEGORY_3) {
        this.CATEGORY_3 = CATEGORY_3;
    }

    public String getCATEGORY_4() {
        return CATEGORY_4;
    }

    public void setCATEGORY_4(String CATEGORY_4) {
        this.CATEGORY_4 = CATEGORY_4;
    }

    public String getGOOD_FLAG() {
        return GOOD_FLAG;
    }

    public void setGOOD_FLAG(String GOOD_FLAG) {
        this.GOOD_FLAG = GOOD_FLAG;
    }

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }

    public BuySellObj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(KEY_INDEX);
		dest.writeString(PARENT_KEYINDEX);
        dest.writeString(BODY);
        dest.writeString(TITLE);
        dest.writeString(SELF_ID);
        dest.writeString(GOOD_EA);
        dest.writeString(COMMENT_EA);
        dest.writeString(DATE);
        dest.writeString(IMG_1);
        dest.writeString(IMG_2);
        dest.writeString(IMG_3);
        dest.writeString(IMG_4);
        dest.writeString(IMG_5);
        dest.writeString(IMG_6);
        dest.writeString(IMG_7);
        dest.writeString(IMG_8);
        dest.writeString(IMG_9);
        dest.writeString(IMG_10);
        dest.writeString(SELF_ID_KEY_INDEX);
        dest.writeString(CATEGORY_1);
        dest.writeString(CATEGORY_2);
        dest.writeString(CATEGORY_3);
        dest.writeString(CATEGORY_4);
        dest.writeString(GOOD_FLAG);
        dest.writeInt(COUNT);


    }
	private void readFromParcel(Parcel in){

        KEY_INDEX = in.readString();
        PARENT_KEYINDEX = in.readString();
        BODY = in.readString();
        TITLE = in.readString();
        SELF_ID = in.readString();
        GOOD_EA = in.readString();
        COMMENT_EA = in.readString();
        DATE = in.readString();
        IMG_1 = in.readString();
        IMG_2 = in.readString();
        IMG_3 = in.readString();
        IMG_4 = in.readString();
        IMG_5 = in.readString();
        IMG_6 = in.readString();
        IMG_7 = in.readString();
        IMG_8 = in.readString();
        IMG_9 = in.readString();
        IMG_10 = in.readString();
        SELF_ID_KEY_INDEX = in.readString();
        CATEGORY_1 = in.readString();
        CATEGORY_2 = in.readString();
        CATEGORY_3 = in.readString();
        CATEGORY_4 = in.readString();
        GOOD_FLAG = in.readString();

        COUNT = in.readInt();


    }
	@SuppressWarnings("rawtypes")
	public static final Creator<BuySellObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new BuySellObj(in);
		}

		public Object[] newArray(int size) {
			return new BuySellObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
