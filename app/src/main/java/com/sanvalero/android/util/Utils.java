package com.sanvalero.android.util;

import android.util.Pair;

import java.time.LocalDate;
import java.time.Period;

public class Utils {
    public static int calculateAge(String birthDateString) {
        LocalDate birthDate = LocalDate.parse(birthDateString);
        LocalDate currentDate = LocalDate.now();
        if (birthDate.isAfter(currentDate)) {
            return 0;
        }
        return Period.between(birthDate, currentDate).getYears();
    }

    public static String generateCustomAddress (String address, String coordinates) {
        // Return string with format: "coordinates|address"
        return coordinates + "|" + address;
    }

    public static Pair<String, String> parseCustomAddress(String customAddress) {
        // Return a Pair with format: ("coordinates", "address")
        String[] parts = customAddress.split("\\|", 2);
        if (parts.length == 2) {
            return new Pair<>(parts[0], parts[1]);
        } else {
            return new Pair<>("", "");
        }
    }

    public static Pair<Double, Double> parseCoordinates(String coordinateString) {
        // Return a Pair with format: (latitude, longitude) from string like "-0.972472, 41.700241"
        try {
            String[] parts = coordinateString.split(",");
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());
            return new Pair<>(latitude, longitude);
        } catch (Exception e) {
            return new Pair<>(0.0, 0.0);
        }
    }

}
