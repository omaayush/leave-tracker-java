package com.hashedin.hu.huLeaveTracker;


import org.junit.Test;


public class CSVFileReaderTest {

    private EmployeeStore employeeStore;

    public CSVFileReaderTest() {
        this.employeeStore = new EmployeeStore();
    }

    @Test
    public void testToGenerateObjectFromEmployeeCsv() {
        String path = "/home/aayush_varshney/leave-tracker-java/src/main/CSV_Files/employees.csv";

        CSVFileReader reader = new CSVFileReader(this.employeeStore);
        reader.loadEmployeeFromCSV(path);

        //insert an assert for this later
    }
}