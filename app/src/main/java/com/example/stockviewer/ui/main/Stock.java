package com.example.stockviewer.ui.main;

import org.joda.time.LocalDateTime;

public class Stock {

    private LocalDateTime date;
    private double open;
    private double low;
    private double high;
    private double close;
    private long volume;
    private double splitCoefficient;
    private double adjustedClose;
    private double dividendAmount;

    public Stock() {
    }

    public Stock(LocalDateTime date, double open, double low, double high, double close, long volume) {
        this.date = date;
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
        this.splitCoefficient = 0;
        this.adjustedClose = 0;
        this.dividendAmount = 0;

    }

    public Stock(LocalDateTime date,
                 double open,
                 double high,
                 double low,
                 double close,
                 double adjustedClose,
                 long volume,
                 double dividendAmount,
                 double splitCoefficient) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
        this.dividendAmount = dividendAmount;
        this.splitCoefficient = splitCoefficient;
    }

    public Stock(LocalDateTime date,
                 double open,
                 double high,
                 double low,
                 double close,
                 double adjustedClose,
                 long volume,
                 double dividendAmount) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
        this.dividendAmount = dividendAmount;
        this.splitCoefficient = 0;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getSplitCoefficient() {
        return splitCoefficient;
    }

    public void setSplitCoefficient(double splitCoefficient) {
        this.splitCoefficient = splitCoefficient;
    }

    public double getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(double adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public double getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(double dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

   /* @Override
    public String toString() {
        return "Stock{" +
                "date=" + date +
                ", open=" + open +
                ", low=" + low +
                ", high=" + high +
                ", close=" + close +
                ", volume=" + volume +
                ", splitCoefficient=" + splitCoefficient +
                ", adjustedClose=" + adjustedClose +
                ", dividendAmount=" + dividendAmount +
                '}';
    }*/
}
