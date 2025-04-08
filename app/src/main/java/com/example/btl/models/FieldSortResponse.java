package com.example.btl.models;

import java.util.List;

public class FieldSortResponse {
    private List<Field> data;
    private Filter filter;

    public List<Field> getData() {
        return data;
    }

    public Filter getFilter() {
        return filter;
    }

    public static class Filter {
        private String sort;
        public String getSort() {
            return sort;
        }
    }
}