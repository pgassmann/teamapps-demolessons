package org.teamapps.demolessons.p4_issuetracker;

import org.teamapps.data.value.SortDirection;
import org.teamapps.data.value.Sorting;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.Issue;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.IssueQuery;
import org.teamapps.ux.component.table.AbstractTableModel;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IssueTableModel extends AbstractTableModel<Issue> {
    private String fullTextSearchString;

    @Override
    public int getCount() {
        return createQuery().execute().size();
    }

    @Override
    public List<Issue> getRecords(int startIndex, int length, Sorting sorting) {
        IssueQuery issueQuery = createQuery();
        return issueQuery.execute(startIndex, length, createUdbSorting(sorting));
    }

    private org.teamapps.universaldb.query.Sorting createUdbSorting(Sorting sorting) {
        if (sorting != null && sorting.getFieldName() != null) {
            return new org.teamapps.universaldb.query.Sorting(sorting.getFieldName(), sorting.getSorting() == SortDirection.ASC);
        } else {
            return null;
        }
    }

    private IssueQuery createQuery() {
        IssueQuery issueQuery = Issue.filter();

        // FullTextFilter
        if (isNotBlank(fullTextSearchString)) {
            issueQuery.parseFullTextFilter(fullTextSearchString);
        }

        return issueQuery;
    }

    public void setFullTextFilter(String searchString) {
        this.fullTextSearchString = searchString;
        onAllDataChanged.fire();
    }

}
