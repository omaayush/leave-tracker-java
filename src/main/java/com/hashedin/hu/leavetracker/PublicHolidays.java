package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class PublicHolidays {
    HashMap <LocalDate, String> holidays;

    public PublicHolidays() {
        this.holidays = new HashMap<LocalDate, String>();
        this.holidays.put(LocalDate.of(2019, Month.AUGUST, 15), "Independence Day");
        this.holidays.put(LocalDate.of(2019, Month.JANUARY, 26), "Republic Day");
        this.holidays.put(LocalDate.of(2019, Month.FEBRUARY, 20), "Random Public Holiday");
    }

    public boolean isAPublicHoliday(LocalDate date) {

        return this.holidays.containsKey(date);
    }
}
