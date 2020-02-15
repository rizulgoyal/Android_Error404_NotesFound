package com.example.android_error404_notesfound.ModelClasses;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "notes")
public class Notes implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("lat")
    @Expose
    private Double lat;


    @SerializedName("lng")
    @Expose
    private Double lng;

    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;

    @SerializedName("dateModified")
    @Expose
    private String dateModified;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;



    @SerializedName("category")
    @Expose
    private String category;


    public Notes(Double lat, Double lng, String dateCreated, String title, String description, String category) {
        this.lat = lat;
        this.lng = lng;
        this.dateCreated = dateCreated;
        this.dateModified = "";
        this.title = title;
        this.description = description;
        this.category = category;
    }

    protected Notes(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        dateCreated = in.readString();
        dateModified = in.readString();
        title = in.readString();
        description = in.readString();
        category = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes( in );
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( id );
        }
        if (lat == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeDouble( lat );
        }
        if (lng == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeDouble( lng );
        }
        dest.writeString( dateCreated );
        dest.writeString( dateModified );
        dest.writeString( title );
        dest.writeString( description );
        dest.writeString( category );
    }
}
