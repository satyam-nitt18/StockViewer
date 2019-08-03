package com.example.stockviewer.ui.main;
public class Company {

    private String symbol;
    private String name;
    private Double lastSale;
    private String marketCap;
    private String ipoYear;
    private String sector;
    private String industry;
    private String summaryQuote;
    private float close;

    public void setClose(float close) {
        this.close = close;
    }

    public float getClose() {
        return close;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLastSale() {
        return lastSale;
    }

    public void setLastSale(Double lastSale) {
        this.lastSale = lastSale;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getIpoyear() {
        return ipoYear;
    }

    public void setIPOyear(String ipoYear) {
        this.ipoYear = ipoYear;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSummaryQuote() {
        return summaryQuote;
    }

    public void setSummaryQuote(String summaryQuote) {
        this.summaryQuote = summaryQuote;
    }
}


   /* @Override
    public String toString() {
        return "Company{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", lastSale=" + lastSale +
                ", marketCap='" + marketCap + '\'' +
                ", ipoYear='" + ipoYear + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", summaryQuote='" + summaryQuote + '\'' +
                '}';
    }*/
