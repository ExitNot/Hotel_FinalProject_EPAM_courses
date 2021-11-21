package com.epam.courses.java.final_project.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomType {

    private long id;
    private int capacity;
    private String bedsType;  // S - single, D - double. String in form ("1D, 2S") etc...
    private RoomClass roomClass;
    private String description;
    private String[] amenities;
    private List<Image> imgPaths;

    public RoomType(long id, int capacity, String bedsType, RoomClass roomClass, String description) {
        this.id = id;
        this.capacity = capacity;
        this.bedsType = bedsType;
        this.roomClass = roomClass;
        int amenIndex = description.indexOf("AMENITIES");
        if (amenIndex == -1){
            this.description = description;
            amenities = null;
        } else {
            this.description = description.substring(0, amenIndex);
            amenities = description.substring(amenIndex + 10).split(" \\| ");
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getBedsType() {
        return bedsType;
    }

    public String getParsedBedsType() {
        StringBuilder sb = new StringBuilder();
        Matcher match = Pattern.compile("\\d[SDT]").matcher(bedsType);

        while (match.find()) {
            if (sb.length() > 0)
                sb.append(" and ");
            String type = match.group();
            String bed = " bed";

            if (type.charAt(0) != '1') {
                sb.append(type.charAt(0)).append(" ");
                bed = bed + "s";
            } else if (type.substring(1).equals("T")) {
                bed = bed + "s";
            }
            switch (type.substring(1)) {
                case "S":
                    sb.append("single");
                    break;
                case "D":
                    sb.append("double");
                    break;
                case "T":
                    sb.append("twin");
                    break;
            }
            sb.append(bed);
        }
        return sb.toString();
    }

    public void setBedsType(String bedType) {
        this.bedsType = bedType;
    }

    public RoomClass getRoomClass() {
        return roomClass;
    }

    public void setRoomClass(RoomClass roomClass) {
        this.roomClass = roomClass;
    }

    public List<Image> getImgPaths() {
        return imgPaths;
    }

    public String getAmountOfImg() {
        return String.valueOf(imgPaths.size() - 1);
    }

    public void setImgPaths(List<Image> imgPaths) {
        this.imgPaths = imgPaths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAmenities() {
        return amenities;
    }

    public void setAmenities(String[] amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "roomClass=" + roomClass.name() +
                '}';
    }

    public enum RoomClass {
        Standard(1), Upgraded(2), Deluxe(3), Suite(4);

        int value;

        RoomClass(int num) {
            value = num;
        }

        public int getValue() {
            return value;
        }

        public static RoomClass getRoomClass(int num) {
            if (num == 4)
                return Suite;
            if (num == 3)
                return Deluxe;
            else if (num == 2)
                return Upgraded;
            return Standard;
        }
    }
}