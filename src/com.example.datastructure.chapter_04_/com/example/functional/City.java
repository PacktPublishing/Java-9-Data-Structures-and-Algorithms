package com.example.functional;

/**
 * Created by debasishc on 4/9/16.
 */
public class City {
    private String name;
    private Country country;

    public City(Country country, String name) {
        this.country = country;
        this.name = name;
    }

    public Option<Country> getCountry() {
        return Option.optionOf(country);
    }

    public Option<String> getName() {
        return Option.optionOf(name);
    }

}
