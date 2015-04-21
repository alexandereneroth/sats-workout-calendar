package se.greatbrain.sats;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListGroup implements Parcelable {


    public final String title;
    public final List<String> children;

    public ListGroup(String title, List<String> children) {
        this.title = title;
        this.children = children;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeList(this.children);
    }

    private ListGroup(Parcel in) {
        this.title = in.readString();
        this.children = in.readArrayList(String.class.getClassLoader());
    }

    public static final Parcelable.Creator<ListGroup> CREATOR = new Parcelable.Creator<ListGroup>() {
        public ListGroup createFromParcel(Parcel source) {
            return new ListGroup(source);
        }

        public ListGroup[] newArray(int size) {
            return new ListGroup[size];
        }
    };
}
