package cn.demomaster.huan.quickdevelop.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class StudtInfo implements Parcelable {

    private String name;

    private int mathScore;

    private int englishScore;

    public StudtInfo(String name, int mathScore, int englishScore){
        this.name = name;
        this.mathScore = mathScore;
        this.englishScore = englishScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMathScore() {
        return mathScore;
    }

    public void setMathScore(int mathScore) {
        this.mathScore = mathScore;
    }

    public int getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(int englishScore) {
        this.englishScore = englishScore;
    }

    public static Creator<StudtInfo> getCREATOR() {
        return CREATOR;
    }

    private StudtInfo(Parcel in) {
        name = in.readString();
        mathScore = in.readInt();
        englishScore = in.readInt();
    }

    public static final Creator<StudtInfo> CREATOR = new Creator<StudtInfo>() {
        @Override
        public StudtInfo createFromParcel(Parcel in) {
            return new StudtInfo(in);
        }

        @Override
        public StudtInfo[] newArray(int size) {
            return new StudtInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(mathScore);
        parcel.writeInt(englishScore);
    }
}