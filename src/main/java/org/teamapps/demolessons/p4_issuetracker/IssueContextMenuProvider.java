package org.teamapps.demolessons.p4_issuetracker;

import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.Issue;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.User;
import org.teamapps.event.Event;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.itemview.SimpleItemGroup;
import org.teamapps.ux.component.itemview.SimpleItemView;
import org.teamapps.ux.component.table.Table;
import org.teamapps.ux.session.CurrentSessionContext;

import java.util.function.Function;

public class IssueContextMenuProvider implements Function<Issue, Component> {

    public final Event<Issue> onIssueChanged = new Event<>();
    private final SimpleItemView<Object> contextMenuContent;
    private Issue selectedIssue;
    private User currentUser = User.getById(2); // TODO
    private Table<Issue> table;

    public IssueContextMenuProvider(Table<Issue> table) {
        this.table = table;
        contextMenuContent = new SimpleItemView<>();
        SimpleItemGroup<Object> menuItemGroup = contextMenuContent.addSingleColumnGroup(MaterialIcon.SWAP_VERT, "Issue Quick Edit");
        menuItemGroup.addItem(MaterialIcon.PEOPLE, "Assign to Me", "I will take care of that")
                .onClick.addListener(s -> {
            assignIssueToMe(selectedIssue);
            table.closeContextMenu();
            //table.refreshData();
            onIssueChanged.fire(selectedIssue);
        });
        menuItemGroup.addItem(MaterialIcon.BUG_REPORT, "DONE", "mark as DONE")
                .onClick.addListener(s -> {
            table.closeContextMenu();
        });
    }

    private void assignIssueToMe(Issue selectedIssue) {
        selectedIssue.setAssignedTo(currentUser);
        selectedIssue.save();
        CurrentSessionContext.get().showNotification(MaterialIcon.BUG_REPORT, "Issue assigned to You");
    }

    @Override
    public Component apply(Issue issue) {
        selectedIssue = issue;

        return contextMenuContent;
    }
}
