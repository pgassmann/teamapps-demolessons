package org.teamapps.demolessons.p3_universaldb.l01_udb;


import com.google.common.io.Files;
import org.teamapps.datamodel.SchemaInfo;
import org.teamapps.datamodel.myfirstudb.*;
import org.teamapps.universaldb.UniversalDB;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class UniversalDbDemo {

    public UniversalDbDemo() {
    }

    public static void main(String[] args) throws Exception {

        File dataDir = new File("server-data");
        if (! dataDir.exists()) {
            dataDir.mkdir();
        }
        UniversalDB.createStandalone(dataDir, SchemaInfo.create());

        if (Employee.getCount() == 0) {
            System.out.println("Creating new entries");
            Employee firstEmployee = Employee.create();
            firstEmployee
                    .setName("John First")
                    .setRank(Rank.JUNIOR)
                    .setJoinedDate(Instant.now())
                    .save();

            Company firstCompany = Company.create()
                    .setName("FirstComp")
                    .setValue(10_000_000)
                    .setDescription("First company Ever!");

            firstCompany.addEmployee(firstEmployee);
            firstCompany.save();
            System.out.println("Employee Name: " + firstEmployee.getName());
            System.out.println("Employer Name: " + firstEmployee.getEmployer().getName());


            Employee secondEmployee = Employee.create()
                    .setJoinedDate(Instant.now())
                    .setName("Marie Second")
                    .setRank(Rank.SENIOR)
                    .save();

            secondEmployee.setEmployer(firstCompany);
            secondEmployee.save();
            System.out.println("Employees of firstCompany:");
            firstCompany.getEmployee().forEach(employee -> {
                System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate());
            });
        }


        // Get Entries from Database

        Employee.getAll().forEach(employee -> {
            System.out.println("Employee Name: '" + employee.getName() + "' works at '" + employee.getEmployer().getName() + "'");
        });
        System.out.println(Company.getAll().get(0).getId());
        Company.getAll().get(0).getEmployee().forEach(employee -> {
            System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate());
        });

        // Null Pointer exception
        Employee.getAll().forEach(employee -> employee.delete());
    }
}

