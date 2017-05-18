package dev.wildtraveling.Domain;

/**
 * Created by pere on 4/24/17.
 */
public class FoursquareVenue {

    private String name;
    private String location;
    private String category;
    private Double priceRank;
    private Double longitute;
    private Double latitude;
    private String iconURL;
    private Integer distance;

    public FoursquareVenue() {
        this.name = "";
        this.location = "";
        this.setCategory("");
    }

    public Double getLongitute() {
        return longitute;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        if (location.length() > 0) {
            return location;
        }
        return location;
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location.replaceAll("\\(", "").replaceAll("\\)", "");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPriceRank() {
        return priceRank;
    }

    public void setPriceRank(Double priceRank) {
        this.priceRank = priceRank;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}