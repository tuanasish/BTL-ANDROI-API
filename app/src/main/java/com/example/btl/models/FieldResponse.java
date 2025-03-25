package com.example.btl.models;

import java.util.List;

public class FieldResponse {
    private List<Field> data;
    private Paging paging;

    public List<Field> getData() {
        return data;
    }

    public static class Paging {
        private int page;
        private int limit;
        private int total;
    }
}