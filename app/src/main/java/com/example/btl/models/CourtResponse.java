package com.example.btl.models;

import java.util.List;

public class CourtResponse {
    private int courtCount;

    private List<Integer> courtIds;

    // Getters và setters
    public int getCourtCount() {
        return courtCount;
    }

    public void setCourtCount(int courtCount) {
        this.courtCount = courtCount;
    }

    public List<Integer> getCourtIds() {
        return courtIds;
    }

    public void setCourtIds(List<Integer> courtIds) {
        this.courtIds = courtIds;
    }
}

