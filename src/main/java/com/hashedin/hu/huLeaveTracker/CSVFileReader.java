package com.hashedin.hu.huLeaveTracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CSVFileReader {
    private EmployeeStore employeeStore;


    public CSVFileReader(EmployeeStore employeeStore) {
        this.employeeStore = employeeStore;
    }

    public void loadEmployeeFromCSV(String path) {
        try {
            String line;
            BufferedReader bufferedReader= new BufferedReader(new FileReader(path));
            line = bufferedReader.readLine();   //reading the headings from csv
            while((line = bufferedReader.readLine())!= null) {
                String[] data = line.split(",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                Employee employee = new Employee(
                Integer.parseInt(data[0]), data[1],
                Integer.parseInt(data[4]),
                LocalDate.parse(data[3], formatter),
                returnGender(data[2]));
                employeeStore.addEmployeeToStore(employee);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    private Gender returnGender(String gender) {
        if (gender.toLowerCase().equals("male")) {
            return Gender.Male;
        } else {
            return Gender.Female;
        }
    }
}
