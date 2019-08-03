package com.example.stockviewer.ui.main;

import com.annimon.stream.Stream;
import com.google.gson.JsonObject;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Map;

public class IntraDay extends TimeSeriesResponse {

    public IntraDay() {
    }

    private IntraDay(final Map<String, String> metaData,
                     final ArrayList<Stock> stocks) {
        super(metaData, stocks);
    }

    public static IntraDay from(String interval, JsonObject json) {
        Parser parser = new Parser(interval);
        return parser.parseJson(json);
    }


    private static class Parser extends TimeSeriesParser<IntraDay> {
        private final String interval;

        Parser(String interval) {
            this.interval = interval;
        }

        @Override
        String getStockDataKey() {
            return "Time Series (" + interval + ")";
        }

        @Override
        IntraDay resolve(Map<String, String> metaData,
                         Map<String, Map<String, String>> stockData) {
            ArrayList<Stock> stocks = new ArrayList<>();
            try {
                Stream.of(stockData.entrySet()).forEach(
                        m -> {

                            Stock stock = new Stock();
                            String d = m.getKey();
                            stock.setDate(LocalDateTime.parse(d, DATE_WITH_TIME_FORMAT));

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
                throw new RuntimeException("Intraday api change", e);
            }
            return new IntraDay(metaData, stocks);
        }
    }
}