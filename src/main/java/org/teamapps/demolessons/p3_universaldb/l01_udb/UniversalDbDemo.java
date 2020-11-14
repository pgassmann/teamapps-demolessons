package org.teamapps.demolessons.p3_universaldb.l01_udb;


import org.teamapps.demolessons.model.SchemaInfo;
import org.teamapps.demolessons.model.myfirstudb.Company;
import org.teamapps.demolessons.model.myfirstudb.Employee;
import org.teamapps.demolessons.model.myfirstudb.EmployeeQuery;
import org.teamapps.demolessons.model.myfirstudb.Rank;
import org.teamapps.universaldb.UniversalDB;
import org.teamapps.universaldb.index.enumeration.EnumFilterType;
import org.teamapps.universaldb.index.numeric.NumericFilter;
import org.teamapps.universaldb.index.text.TextFilter;
import org.teamapps.universaldb.pojo.Entity;

import java.io.File;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UniversalDbDemo {

    // This Demo uses the Model defined in src/main/model/UniversalDbDemoModel

    public static void main(String[] args) throws Exception {
        startDb();
        createDemoData();
        query1();
        filterQuery1();
        filterQuery2();

        // clearDbEntries();
    }

    private static void startDb() throws Exception {
        File storagePath = new File("./server-data/db-storage");
        if (! storagePath.exists()) {
            if (! storagePath.mkdirs()) System.out.println("Error creating Database directory!");
        }
        UniversalDB.createStandalone(storagePath, SchemaInfo.create());
    }
    private static void clearDbEntries() {
        System.out.println("\nClear all Employees and Companies");
        Employee.getAll().forEach(Entity::delete);
        Company.getAll().forEach(Entity::delete);
    }

    private static void createDemoData() {
        if (Company.getCount() > 0) {
            System.out.println("DB already contains data, don't create new entries");
            // Delete folder ./server-data/db-storage/myFirstUdb to start fresh
            return;
        }
        System.out.println("Creating new entries");
        Employee firstEmployee = Employee.create()
                .setName("John First")
                .setRank(Rank.MANAGER)
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
        firstCompany.getEmployee().forEach(employee -> System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate()));

        // Create some more companies
        Company.create().setName("DemoComp").setDescription("Company organising Demonstrations").setValue(100).save();
        Company.create().setName("AcmeCorp").setDescription("A Companty that Makes Everything").setValue(1000).save();
        Company.create().setName("Broke Comp").setDescription("Company that makes Nothng").setValue(0).save();
        Company.create().setName("Super Comp").setDescription("Company creating super stuff").setValue(800).save();
        Company megaCorp = Company.create().setName("MegaCorp").setDescription("Company with a lot of Employees").setValue(80_000).save();

        // Create 10'000 Employees for MegaCorp
        for (int i = 0; i < 1000; i++) {
            Rank rank;
            if ( i < 25 ){
                rank = Rank.MANAGER;
            } else if ( i < 500){
                rank = Rank.SENIOR;
            } else {
                rank = Rank.JUNIOR;
            }
            Employee newEmployee = Employee.create().setName("Mark "+ i).setRank(rank).setJoinedDateAsEpochMilli(100000000000L + 1500000000L * i);
            megaCorp.addEmployee(newEmployee);
        }
        // Save megaCorp and automatically all new Employees
        megaCorp.save();

    }

    private static void query1() {
        System.out.println("\nquery1()");
        System.out.println("Employee count: " + Employee.getCount());

        // Get all Entries from Database
        Employee.getAll().forEach(employee -> {
            // Manual filtering, see filterQuery1() for a more efficient solution
            if (employee.getEmployer().getName().equals("FirstComp")) {
                System.out.println("Employee Name: '" + employee.getName() + "' works at '" + employee.getEmployer().getName() + "'");
            }
        });

        Company firstCompany = Company.getById(1);
        System.out.println("firstCompany Name: " + firstCompany.getName());
        System.out.println("firstCompany Employees:");
        firstCompany.getEmployee().forEach(employee -> System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate()));
    }

    private static void filterQuery1() {
        System.out.println("\nfilterQuery1()");

        EmployeeQuery employeeQuery1 = Employee.filter()
                // Filter by Company
                .filterEmployer(
                        // All Companies that have the word firstComp in their name
                        Company.filter()
                                .name(TextFilter.termEqualsFilter("firstComp"))
                );
        System.out.println("employeeQuery1 as Text: " + employeeQuery1);
        // Execute query and save as List
        List<Employee> employeeList1 = employeeQuery1.execute();

        // Iterate over list, print some information:
        System.out.println("\nEmployees of firstComp:");
        employeeList1.forEach(employee -> System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate()));
    }
    private static void filterQuery2() {
        System.out.println("\nfilterQuery2()");


        System.out.println("\nAll Managers:");
        EmployeeQuery employeeQuery2 = Employee.filter()
                .rank(EnumFilterType.EQUALS, Rank.MANAGER);
        List<Employee> employeeList = employeeQuery2.execute();
        employeeList.forEach(employee -> System.out.println(employee.getName() + ", Company: " + employee.getEmployer().getName() + " joined at " + employee.getJoinedDate()));

        System.out.println("\nEmployees joined after 2020-01-01:");
        long compareDateinMillis = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTimeInMillis();
        List<Employee> freshJoinedEmployees = Employee.filter()
                .joinedDate(NumericFilter.greaterFilter(compareDateinMillis))
                .execute();
        for (Employee employee : freshJoinedEmployees) {
            System.out.println(employee.getName() + ", Company: " + employee.getEmployer().getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate());
        }
    }



}

