package models;

import com.google.gson.annotations.SerializedName;

public class CurrentSessionResponse {
    @SerializedName("Retailer")
    private Retailer retailer;
    
    @SerializedName("CurrentBranchId")
    private int currentBranchId;
    
    public static class Retailer {
        @SerializedName("Id")
        private int id;
        
        @SerializedName("GroupId")
        private int groupId;

        public int getId() {
            return id;
        }

        public int getGroupId() {
            return groupId;
        }
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public int getCurrentBranchId() {
        return currentBranchId;
    }
}
