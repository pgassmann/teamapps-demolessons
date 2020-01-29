import org.teamapps.universaldb.schema.*;

// You need to configure this class in your pom.xml
public class UniversalDbModel implements SchemaInfoProvider {

    public String getSchema() {
        Schema schema = Schema.create();

        // When you create or change your database schema, you have to execute mvn clean install
        Database database = schema.addDatabase("myFirstUdb");

        Table company = database.addTable("company");
        Table employee = database.addTable("employee");

        // Add Fields to Table company
        company
                .addText("name")
                .addText("description")
                .addLong("value")

                // References: In the field "reference" link to a item of table2
                // In UniversalDB, usually every reference has a backReference in the other table
                .addReference("employee", employee, true, "employer");

        // Add Fields to Table employee
        employee
                .addText("name")
                .addEnum("rank", "junior", "senior", "manager")
                .addDate("joinedDate")

                /// Reference from employee to employer
                .addReference("employer", company, false, "employee");

        return schema.getSchema();
    }
}