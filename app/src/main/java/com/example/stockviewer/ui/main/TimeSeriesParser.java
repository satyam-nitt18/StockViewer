package com.example.stockviewer.ui.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class TimeSeriesParser<Data> {


    public final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    public final DateTimeFormatter DATE_WITH_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public final DateTimeFormatter DATE_WITH_SIMPLE_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    abstract Data resolve(Map<String, String> metaData,
                          Map<String, Map<String, String>> stockData);

    abstract String getStockDataKey();

    private static com.google.gson.JsonParser PARSER = new com.google.gson.JsonParser();

    protected static Gson GSON = new Gson();

   // protected abstract Data resolve(JsonObject rootObject);


    public Data parseJson(JsonObject rootObject) {
        try {

            JsonElement errorMessage = rootObject.get("Error Message");
            if (errorMessage != null) {
                throw new RuntimeException(errorMessage.getAsString());
            }

            return resolve(rootObject);

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("error parsing json", e);
        }
    }

    public Data resolve(JsonObject rootObject) {
        Type metaDataType = new TypeToken<Map<String, String>>() {
        }.getType();
        Type dataType = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, String> metaData;
        Map<String, Map<String, String>> stockData;
        try {
            metaData = GSON.fromJson(rootObject.get("Meta Data"), metaDataType);
            stockData = GSON.fromJson(rootObject.get(getStockDataKey()), dataType);
            return resolve(metaData, stockData);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("time series api change");
        }
    }
}
