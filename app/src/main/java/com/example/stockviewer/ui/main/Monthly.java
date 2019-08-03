package com.example.stockviewer.ui.main;

import com.annimon.stream.Stream;
import com.google.gson.JsonObject;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Monthly extends TimeSeriesResponse {

    public Monthly(){
    }

    private Monthly(final Map<String, String> metaData,
                    final ArrayList<Stock> stocks) {
        super(metaData, stocks);
    }

    public static Monthly from(JsonObject json) {
        Parser parser = new Parser();
        return parser.parseJson(json);
    }

    private static class Parser extends TimeSeriesParser<Monthly> {

        @Override
        String getStockDataKey() {
            return "Monthly Time Series";
        }

        @Override
        Monthly resolve(Map<String, String> metaData,
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
                throw new RuntimeException("Monthly api change", e);
            }
            return new Monthly(metaData, stocks);
        }
    }
}
