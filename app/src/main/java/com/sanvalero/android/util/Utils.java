package com.sanvalero.android.util;

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
}
