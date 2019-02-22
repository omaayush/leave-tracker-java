package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class PublicHolidays {
    HashMap <LocalDate, Holiday> holidays;

    public PublicHolidays() {
        this.holidays = new HashMap<LocalDate, Holiday>();

        this.holidays.put(LocalDate.of(2019, Month.AUGUST, 15),
                        new Holiday("Independence Day", false, null));

        this.holidays.put(LocalDate.of(2019, Month.JANUARY, 26),
                        new Holiday("Republic Day", false, null));

        this.holidays.put(LocalDate.of(2019, Month.FEBRUARY, 20),
                        new Holiday("Random Optional Holiday 1", true,
                        LocalDate.of(2019, Month.JUNE, 18)));

        this.holidays.put(LocalDate.of(2019, Month.JUNE, 18),
                                new Holiday("Random Optional Holiday 2",
                                true,
                                LocalDate.of(2019, Month.FEBRUARY, 20)));
    }

    public boolean isAPublicHoliday(LocalDate date) {
        return this.holidays.containsKey(date);
    }

    public Holiday getHolidayInfo(LocalDate date) {
        return this.holidays.get(date);
    }
}
