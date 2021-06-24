package com.example.planner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.List;

public class POI extends BaseObservable implements Parcelable,Comparable<POI> {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("snippet")
    @Expose
    private String snippet;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("images")
    @Expose
    private List<CountryImage> images;
    @SerializedName("structured_content")
    @Expose
    private StructuredContent structured_content;
    @SerializedName("booking_info")
    @Expose
    private BookingInfo bookingInfo;
    @SerializedName("score")
    @Expose
    private double score;
    @SerializedName("properties")
    @Expose
    private List<POIProperties> poiProperties;
    @SerializedName("attribution")
    @Expose
    private List<Attribution> attribution;


    private String uniqueID;

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    DecimalFormat formatter = new DecimalFormat( "#.00" );



    protected POI(Parcel in) {
        name = in.readString();
//        countryId = in.readString();
        snippet = in.readString();
//        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
//        images = in.createTypedArrayList(CountryImage.CREATOR);
    }

    public POI(){}

    public static final Creator<POI> CREATOR = new Creator<POI>() {
        @Override
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        @Override
        public POI[] newArray(int size) {
            return new POI[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
//        dest.writeString(countryId);
        dest.writeString(snippet);
//        dest.writeParcelable(coordinates, flags);
//        dest.writeTypedList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Bindable
    public String getCountryId() {
        return countryId;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);

    }

    @Bindable
    public double getScore() {
        return Double.parseDouble(formatter.format(score));
    }

    public void setScore(double score) {
        this.score = score;
        notifyPropertyChanged(BR.score);

    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);

    }

    @Bindable
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Bindable
    public List<CountryImage> getImages() {
        return images;
    }

    public void setImages(List<CountryImage> images) {
        this.images = images;
        notifyPropertyChanged(BR.images);

    }

    @Bindable
    public StructuredContent getStructured_content() {
        return structured_content;
    }

    public void setStructured_content(StructuredContent structured_content) {
        this.structured_content = structured_content;
        notifyPropertyChanged(BR.structured_content);
    }

    @Bindable
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
        notifyPropertyChanged(BR.snippet);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    @Bindable
    public List<POIProperties> getPoiProperties() {
        return poiProperties;
    }

    public void setPoiProperties(List<POIProperties> poiProperties) {
        this.poiProperties = poiProperties;
        notifyPropertyChanged(BR.poiProperties);

    }

    @Override
    public int compareTo(POI o) {
            double c1 = Double.parseDouble(this.getBookingInfo().getPrice().getAmount());
            double c2 = Double.parseDouble(o.getBookingInfo().getPrice().getAmount());

        return Double.compare(c1, c2);
    }







}

