import org.teamapps.universaldb.schema.*;

// You need to configure this class in your pom.xml
// When making changes to the Schema, you need to run maven clean install again to update the Java Pojo Classes
public class UniversalDbDemoModel implements SchemaInfoProvider {

    public String getSchema() {
        // New Schema. creates pojos in the specified namespace
        Schema schema = Schema.create("org.teamapps.demolessons.model");

        // When you create or change your database schema, you have to execute mvn clean install
        Database database = schema.addDatabase("myFirstUdb");

        // Define the Tables, add creation and modification timestamp fields, keep deleted objects ("recycle bin")
        Table company = database.addTable("company", TableOption.TRACK_CREATION, TableOption.TRACK_MODIFICATION, TableOption.KEEP_DELETED);
        Table employee = database.addTable("employee", TableOption.TRACK_CREATION, TableOption.TRACK_MODIFICATION, TableOption.KEEP_DELETED);

        // Add Fields to Table company
        company
                .addText("name")
                .addText("description")
                .addLong("value")

                // References: In the field "employee" link to a item of employee
                .addReference("employee", employee, true, "employer");
                // Parameters: addReference(String name, Table referencedTable, boolean multiReference, String backReference)
                // name: reference name
                // referencedTable: other Table that is linked with this reference
                // multiReference: whether this field can reference only one or multiple elements of the other table
                // backReference: name of the reference in the referenced Table that links back to this table.
                // With UniversalDB, usually every reference has a backReference in the other table

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