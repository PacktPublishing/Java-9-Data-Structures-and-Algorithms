package com.example.functional;

/**
 * Created by debasishc on 4/9/16.
 */
public class Country {
    private String name;
    private String countryCode;


    public Country(String countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public Option<String> getCountryCode() {
        return Option.optionOf(countryCode);
    }

    public Option<String> getName() {
        return Option.optionOf(name);
    }
}
