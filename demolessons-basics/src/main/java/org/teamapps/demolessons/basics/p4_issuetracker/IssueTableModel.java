package org.teamapps.demolessons.basics.p4_issuetracker;

import org.teamapps.data.value.SortDirection;
import org.teamapps.data.value.Sorting;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.Issue;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.IssueQuery;
import org.teamapps.universaldb.index.text.TextFilter;
import org.teamapps.ux.component.table.AbstractTableModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IssueTableModel extends AbstractTableModel<Issue> {
    private String fullTextSearchString;
    private Map<String, String> textFilterByFieldName = new HashMap<>();

    @Override
    public int getCount() {
        return createQuery().execute().size();
    }

    @Override
    public List<Issue> getRecords(int startIndex, int length, Sorting sorting) {
        IssueQuery issueQuery = createQuery();
        return issueQuery.execute();
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

        // Filters on Fields
        textFilterByFieldName.forEach((fieldName, searchString) -> {
            issueQuery.fullTextFilter(TextFilter.termContainsFilter(searchString), fieldName);
        });

        // FullTextFilter
        if (isNotBlank(fullTextSearchString)) {
            // parseFullTextFilter searches every field by default.
            // parseFullTextFilter has extra features with some syntax.
            // E.g. Add + after a term to enable fuzzy matching
            issueQuery.parseFullTextFilter(fullTextSearchString);
        }

        return issueQuery;
    }

    // Called by the global Search Field in the Table Panel's RightHeaderField
    public void setFullTextFilter(String searchString) {
        this.fullTextSearchString = searchString;
        onAllDataChanged.fire();
    }

    // Called by the Filter Fields in the HeaderRow of a column.
    public void setTextFilter(String fieldName, String searchString) {
        if (isNotBlank(searchString)) {
            textFilterByFieldName.put(fieldName, searchString);
        } else {
            textFilterByFieldName.remove(fieldName);
        }
        onAllDataChanged.fire();
    }

}
