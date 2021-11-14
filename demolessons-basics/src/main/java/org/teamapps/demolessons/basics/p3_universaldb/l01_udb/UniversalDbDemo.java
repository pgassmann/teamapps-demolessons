package org.teamapps.demolessons.basics.p3_universaldb.l01_udb;


import org.teamapps.demolessons.model.DemoLessonSchema;
import org.teamapps.demolessons.model.myfirstudb.*;
import org.teamapps.universaldb.UniversalDB;
import org.teamapps.universaldb.context.UserContext;
import org.teamapps.universaldb.index.enumeration.EnumFilterType;
import org.teamapps.universaldb.index.numeric.NumericFilter;
import org.teamapps.universaldb.index.text.TextFilter;
import org.teamapps.universaldb.pojo.Entity;
import org.teamapps.universaldb.query.Sorting;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class UniversalDbDemo {

    // This Demo uses the Model defined in src/main/model/UniversalDbDemoModel

    public static void main(String[] args) throws Exception {
        startDb();
          // deleteDemoData();
        createDemoData();
        printSummary();
        basicQuery();
        filterQuery();
        printAllManagers();
        printNewEmployees();

        printSummary();
        complexQuery("J");
        // first execution is slow in freshly started jvm. run more queries to see the real performance
        complexQuery("M");
//        complexQuery("P");
//        complexQuery("J");
//        complexQuery("L");
//        complexQuery("E");
//        complexQuery("A");
    }

    private static void startDb() throws Exception {
        File storagePath = new File("./server-data/db-storage");
        if (! storagePath.exists()) {
            if (! storagePath.mkdirs()) System.out.println("Error creating Database directory!");
        }
        UniversalDB.createStandalone(storagePath, new DemoLessonSchema());
    }

    private static void printSummary(){
        System.out.println("\nSummary of Database");
        System.out.println("Employee count: " + Employee.getCount());
        System.out.println("Company count: " + Company.getCount());
        System.out.println("Location count: " + Location.getCount());
        System.out.println("Region count: " + Region.getCount());
    }

    private static void basicQuery() {
        System.out.println("\nbasicQuery()");
        System.out.println("Employee count: " + Employee.getCount());

        // Get all Entries from Database
        Employee.getAll().forEach(employee -> {
            // Manual filtering, see filterQuery() for a more efficient solution
            if (employee.getEmployer().getName().equals("FirstComp")) {
                System.out.println("Employee Name: '" + employee.getName() + "' works at '" + employee.getEmployer().getName() + "'");
            }
        });

        Company firstCompany = Company.getById(1);
        System.out.println("firstCompany Name: " + firstCompany.getName());
        System.out.println("firstCompany Employees:");
        firstCompany.getEmployee().forEach(employee -> System.out.println(employee.getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate()));
    }

    private static void filterQuery() {
        System.out.println("\nfilterQuery()");

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


    private static void printNewEmployees() {
        System.out.println("\nEmployees joined after 2021-07-01:");
        long compareDateinMillis = new GregorianCalendar(2021, Calendar.JULY, 1).getTimeInMillis();
        List<Employee> freshJoinedEmployees = Employee.filter()
                .joinedDate(NumericFilter.greaterFilter(compareDateinMillis))
                .execute();
        for (Employee employee : freshJoinedEmployees) {
            System.out.println(employee.getName() + ", Company: " + employee.getEmployer().getName() + ", Rank " + employee.getRank() + " joined at " + employee.getJoinedDate());
        }
    }

    private static void printAllManagers() {
        System.out.println("\nAll Managers");
        EmployeeQuery employeeQuery2 = Employee.filter()
                .rank(EnumFilterType.EQUALS, Rank.MANAGER)
                .filterEmployer(Company.filter().name(TextFilter.termContainsNotFilter("RandomCompany"))); // exclude Managers of
        List<Employee> employeeList = employeeQuery2.execute();
        employeeList.forEach(employee -> System.out.println(employee.getName() + ", Company: " + employee.getEmployer().getName() + " joined at " + employee.getJoinedDate()));
    }

    private static void complexQuery(String employeeNameStartsWith){
        System.out.println("\nComplex Query Example");

        // of companies that don't contain "66" in their name
        EmployeeQuery complexEmployeeQuery = Employee.filter()
                // find employees with a Name starting with J
                .name(TextFilter.termStartsWithFilter(employeeNameStartsWith))
                // of companies in the region "mittelland"
                .filterEmployer(
                        Company.filter()
                                .filterLocation(
                                        Location.filter().filterRegion(
                                                Region.filter().name(
                                                        TextFilter.termEqualsFilter("mittelland")
                                                )
                                        )
                                ).andOr(  // of companies that contain "7" or "9" in their name
                                        Company.filter().name(TextFilter.termContainsFilter("09")),
                                        Company.filter().name(TextFilter.termContainsFilter("10")),
                                        Company.filter().name(TextFilter.termContainsFilter("11")),
                                        Company.filter().name(TextFilter.termContainsFilter("12"))
                                )
                );
        // extend query, limit by joinedDate
        complexEmployeeQuery
                .joinedDate(NumericFilter.betweenFilter(
                        new GregorianCalendar(2021, Calendar.JANUARY, 1).getTimeInMillis(),
                        new GregorianCalendar(2021, Calendar.MAY, 1).getTimeInMillis()
                ));
        // exclude JUNIORS
        complexEmployeeQuery.rank(EnumFilterType.NOT_EQUALS, Rank.JUNIOR);
        long startTime = System.currentTimeMillis();
        // execute query for resultCount
        int resultCount = complexEmployeeQuery.execute().size();
        // execute query, sorted ascending by joinedDate, get first 50,
        List<Employee> firstMatchingEmployeesSorted = complexEmployeeQuery.execute(0, 50, new Sorting(Employee.FIELD_JOINED_DATE, true), UserContext.create("de"));
        long stopTime = System.currentTimeMillis();

        System.out.println("Total results: " + resultCount);
        System.out.println("Execution Time: " + (stopTime-startTime) + "ms");

        firstMatchingEmployeesSorted.forEach(employee -> {
            Company employer = employee.getEmployer();
            Location location = employer.getLocation();
            Region region = location.getRegion();
            System.out.println(
                    employee.getRank() +
                    "\t " + employee.getName() +
                    "\t Company: " + employer.getName() +
                    "\t joined at " + employee.getJoinedDate() +
                    "\t Location: " + location.getName() +
                    "\t Region: " + region.getName()
            );
        });
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
        generateEmployees(megaCorp, 10_00);

        // Locations
        Location basel_park = Location.create().setName("Basel Park").setRent(2234).save();
        Location bern_downtown = Location.create().setName("Bern Downtown").setRent(8234).save();
        Location zurich_cityCenter = Location.create().setName("Zürich CityCenter").setRent(10_234).save();

        Region mittelland = Region.create().setName("Mittelland").setLocations(basel_park, bern_downtown, zurich_cityCenter).save();
        Region mountain = Region.create().setName("mountain").save();
        Location.create().setName("Alpen Park").setRent(1145).setRegion(mountain).save();

        generateCompanies();

        // assign random location to all Companies
        List<Location> locations = Location.getAll();
        Random random = new Random();
        Company.getAll().forEach(company -> {
            if (company.getLocation() == null) {
                company.setLocation(locations.get(random.nextInt(locations.size()-1))).save();
            }
        });

    }

    private static void generateCompanies() {
        // creating an object of Random class
        Random random = new Random();
        int amount = 100;
        for (int i = 0; i < amount; i++) {
            Company company = Company.create();
            int numberOfEmployees = random.nextInt(6000);
            String companyName = "RandomCompany"+ i + "for" + random.nextInt(500);
            System.out.println("Creating Company \"" + companyName + "\" with " + numberOfEmployees + " employees");
            company.setName(companyName).setDescription("description"+random.nextInt(4000)).save();
            generateEmployees(company, numberOfEmployees);
        }
    }

    private static void generateEmployees(Company company, int numberOfEmployees) {
        Random random = new Random();
        for (int i = 0; i < numberOfEmployees; i++) {
            Rank rank;
            if ( random.nextBoolean() && random.nextBoolean()){
                rank = Rank.MANAGER;
            } else if ( i < numberOfEmployees/2 && random.nextBoolean()){
                rank = Rank.SENIOR;
            } else {
                rank = Rank.JUNIOR;
            }
            List<String> namelist = List.of("Aaron", "Abigail", "Adam", "Alan", "Albert", "Alexander", "Alexis", "Alice", "Amanda", "Amber", "Amy", "Andrea", "Andrew", "Angela", "Ann", "Anna", "Anthony", "Arthur", "Ashley", "Austin", "Barbara", "Benjamin", "Betty", "Beverly", "Billy", "Bobby", "Brandon", "Brenda", "Brian", "Brittany", "Bruce", "Bryan", "Carl", "Carol", "Carolyn", "Catherine", "Charles", "Charlotte", "Cheryl", "Christian", "Christina", "Christine", "Christopher", "Cynthia", "Daniel", "Danielle", "David", "Deborah", "Debra", "Denise", "Dennis", "Diana", "Diane", "Donald", "Donna", "Doris", "Dorothy", "Douglas", "Dylan", "Edward", "Elijah", "Elizabeth", "Emily", "Emma", "Eric", "Ethan", "Eugene", "Evelyn", "Frances", "Frank", "Gabriel", "Gary", "George", "Gerald", "Gloria", "Grace", "Gregory", "Hannah", "Harold", "Heather", "Helen", "Henry", "Isabella", "Jack", "Jacob", "Jacqueline", "James", "Janet", "Janice", "Jason", "Jean", "Jeffrey", "Jennifer", "Jeremy", "Jerry", "Jesse", "Jessica", "Joan", "Joe", "John", "Johnny", "Jonathan", "Jordan", "Jose", "Joseph", "Joshua", "Joyce", "Juan", "Judith", "Judy", "Julia", "Julie", "Justin", "Karen", "Katherine", "Kathleen", "Kathryn", "Kayla", "Keith", "Kelly", "Kenneth", "Kevin", "Kimberly", "Kyle", "Larry", "Laura", "Lauren", "Lawrence", "Linda", "Lisa", "Logan", "Louis", "Madison", "Margaret", "Maria", "Marie", "Marilyn", "Mark", "Martha", "Mary", "Matthew", "Megan", "Melissa", "Michael", "Michelle", "Miles", "Nancy", "Natalie", "Nathan", "Nicholas", "Nicole", "Noah", "Olivia", "Pamela", "Patricia", "Patrick", "Paul", "Peter", "Philipp", "Rachel", "Ralph", "Randy", "Raymond", "Rebecca", "Richard", "Robert", "Roger", "Ronald", "Rose", "Roy", "Russell", "Ruth", "Ryan", "Samantha", "Samuel", "Sandra", "Sara", "Sarah", "Scott", "Sean", "Sharon", "Shirley", "Sophia", "Stephanie", "Stephen", "Steven", "Susan", "Teresa", "Terry", "Theresa", "Thomas", "Timothy", "Tyler", "Victoria", "Vincent", "Virginia", "Walter", "Wayne", "William", "Willie", "Zachary");
            ArrayList<String> names = new ArrayList<>(namelist);
            Instant joinedDate = LocalDate.ofYearDay(2021, 200).minusDays(Math.round(i * 20 * Math.random())).atStartOfDay(ZoneId.systemDefault()).toInstant();
            Employee newEmployee = Employee.create().setName(names.get(random.nextInt(names.size()-1))).setRank(rank).setJoinedDate(joinedDate);
            company.addEmployee(newEmployee);
        }
        // Save megaCorp and automatically all new Employees
        company.save();
    }

    private static void deleteDemoData() {
        System.out.println("\nClear all Employees and Companies");
        Employee.getAll().forEach(Entity::delete);
        Company.getAll().forEach(Entity::delete);
        Location.getAll().forEach(Entity::delete);
        Region.getAll().forEach(Entity::delete);
    }

}

