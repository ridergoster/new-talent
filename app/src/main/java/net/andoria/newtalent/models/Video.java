package net.andoria.newtalent.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by maxime on 08/07/16.
 */
@JsonObject
public class Video implements Parcelable {
    @JsonField
    private String id;

    @JsonField
    private String idYoutube;

    @JsonField
    private String title;

    @JsonField
    private String description;

    @JsonField
    private String imageUrl;

    @JsonField
    private String videoUrl;

    @JsonField
    private float rate;

    @JsonField
    private boolean hasVote;

    public Video() {
        //noop
    }


    protected Video(Parcel in) {
        id = in.readString();
        idYoutube = in.readString();
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        videoUrl = in.readString();
        rate = in.readFloat();
        hasVote = in.readByte() != 0;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdYoutube() {
        return idYoutube;
    }

    public void setIdYoutube(String idYoutube) {
        this.idYoutube = idYoutube;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean isHasVote() {
        return hasVote;
    }

    public void setHasVote(boolean hasVote) {
        this.hasVote = hasVote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idYoutube);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeString(videoUrl);
        parcel.writeFloat(rate);
        parcel.writeByte((byte) (hasVote ? 1 : 0));
    }
}
