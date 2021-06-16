import org.teamapps.universaldb.schema.*;

// You need to configure this class in your pom.xml
// When making changes to the Schema, you need to run maven clean install again to update the Java Pojo Classes
public class IssueTrackerModel implements SchemaInfoProvider {

    public Schema getSchema() {
        // New Schema. creates pojos in the specified namespace
        Schema schema = Schema.create("org.teamapps.demolessons.issuetracker.model");
        schema.setSchemaName("IssueTrackerSchema");

        // When you create or change your database schema, you have to execute mvn clean install
        Database database = schema.addDatabase("issueTrackerDb");

        // Define the Tables, add creation and modification timestamp fields, keep deleted objects ("recycle bin")
        Table user = database.addTable("user", TableOption.TRACK_CREATION, TableOption.TRACK_MODIFICATION, TableOption.KEEP_DELETED);
        Table group = database.addTable("group", TableOption.TRACK_CREATION, TableOption.TRACK_MODIFICATION, TableOption.KEEP_DELETED);
        Table issue = database.addTable("issue", TableOption.TRACK_CREATION, TableOption.TRACK_MODIFICATION, TableOption.KEEP_DELETED);

        user
                .addText("name")
                .addText("email")
                .addBinary("avatar")
                .addReference("assignedIssues", issue, true, "assignedTo")
                .addReference("reportedIssues", issue, true, "reporter")
                .addReference("groups", group, true, "members");

        group
                .addText("name")
                .addReference("members", user, true, "groups");

        issue
                .addText("type")
                .addText("priority")
                .addText("state")
                .addText("summary")
                .addText("description")
                .addReference("assignedTo", user, true, "assignedIssues")
                .addReference("reporter", user, false, "reportedIssues");

        return schema;
    }
}