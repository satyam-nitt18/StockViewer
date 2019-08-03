package com.example.stockviewer.ui.main;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Map;

public class Daily extends TimeSeriesResponse {

    public Daily() {
    }

    private Daily(final Map<String, String> metaData,
                  final ArrayList<Stock> stockData) {
        super(metaData, stockData);
    }

    public static Daily from(JsonObject json) {
         Parser parser = new Parser();
        return parser.parseJson(json);
    }

    private static class Parser extends TimeSeriesParser<Daily> {

        @Override
        String getStockDataKey() {
            return "Time Series (Daily)";
        }


        @Override
        Daily resolve(Map<String, String> metaData,
                      Map<String, Map<String, String>> stockData) {
            ArrayList<Stock> stocks = new ArrayList<>();

            try {
                Stream.of(stockData.entrySet()).forEach(
                        m -> {

                            Stock stock = new Stock();
                            String d = m.getKey();
                            stock.setDate(LocalDateTime.parse(d, SIMPLE_DATE_FORMAT));

                            Stream.of(m.getValue()).forEach(
                                    value -> {
                                        stock.setOpen(Double.parseDouble(value.get("1. open")));
                                        stock.setHigh(Double.parseDouble(value.get("2. high")));
                                        stock.setLow(Double.parseDouble(value.get("3. low")));
                                        stock.setClose(Double.parseDouble(value.get("4. close")));
                                        stock.setVolume(Long.parseLong(value.get("5. volume")));
                                        stocks.add(stock);
                                    }

                            );
                        }
                );
            } catch (Exception e) {
                throw new RuntimeException("Daily api change", e);
            }
            return new Daily(metaData, stocks);
        }

    }
}




