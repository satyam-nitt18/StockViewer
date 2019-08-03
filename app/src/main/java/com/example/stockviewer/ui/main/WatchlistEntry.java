package com.example.stockviewer.ui.main;

import java.time.LocalDateTime;

public class WatchlistEntry {

    Company company;
    LocalDateTime dateSaved;

    public WatchlistEntry(){

    }

    public WatchlistEntry(Company company, LocalDateTime dateSaved) {
        this.company = company;
        this.dateSaved = dateSaved;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(LocalDateTime dateSaved) {
        this.dateSaved = dateSaved;
    }

  /*  @Override
    public String toString() {
        return "WatchListEntry{" +
                "company=" + company +
                ", dateSaved=" + dateSaved +
                '}';
    }*/
}