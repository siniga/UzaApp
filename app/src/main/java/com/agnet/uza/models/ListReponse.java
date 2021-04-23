package com.agnet.uza.models;

import java.util.List;

public class ListReponse {
    private List<Country> countryList;

    public ListReponse(List<Country> countries) {
        this.countryList = countries;
    }

    public List<Country> getCountryList() {
        return countryList;
    }
}
