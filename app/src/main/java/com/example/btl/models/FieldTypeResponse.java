package com.example.btl.models;

import java.util.List;

public class FieldTypeResponse {
    private List<Field> data;
    private Filter filter;

    public List<Field> getData() {
        return data;
    }

    public Filter getFilter() {
        return filter;
    }

    public static class Filter {
        private String type;
        public String getType() {
            return type;
        }
    }
}