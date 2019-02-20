package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class OptionalHolidays {
    HashMap <LocalDate, String> holidaysSet1;
    HashMap <LocalDate, String> holidaysSet2;

    public OptionalHolidays() {
        this.holidaysSet1 = new HashMap<LocalDate, String>();
        this.holidaysSet1.put(LocalDate.of(2019, Month.MARCH, 10), "Holi");
        this.holidaysSet1.put(LocalDate.of(2019, Month.OCTOBER, 30), "Eid");

        this.holidaysSet2 = new HashMap<LocalDate, String>();
        this.holidaysSet2.put(LocalDate.of(2019, Month.MARCH, 20), "Eid");
        this.holidaysSet2.put(LocalDate.of(2019, Month.OCTOBER, 20), "Ramzan");

    }

    public boolean isAnOptionalHoliday(LocalDate date) {
        return this.holidaysSet1.containsKey(date) || this.holidaysSet2.containsKey(date);
    }
}