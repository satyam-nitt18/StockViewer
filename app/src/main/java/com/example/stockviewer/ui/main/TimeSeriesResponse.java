package com.example.stockviewer.ui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSeriesResponse {

    private Map<String, String> metaData;
    private ArrayList<Stock> stockData;

    public TimeSeriesResponse() {
        metaData = new HashMap<>();
        stockData = new ArrayList<>();
    }

    TimeSeriesResponse(final Map<String, String> metaData, final ArrayList<Stock> stockData) {
        this.stockData = stockData;
        this.metaData = metaData;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public List<Stock> getStockData() {
        return stockData;
    }
}
