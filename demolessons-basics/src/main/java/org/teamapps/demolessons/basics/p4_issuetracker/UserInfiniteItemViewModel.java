package org.teamapps.demolessons.basics.p4_issuetracker;

import org.teamapps.data.value.SortDirection;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.User;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.UserQuery;
import org.teamapps.universaldb.context.UserContext;
import org.teamapps.ux.component.infiniteitemview.AbstractInfiniteItemViewModel;

import java.util.List;

class UserInfiniteItemViewModel extends AbstractInfiniteItemViewModel<User> {
    private org.teamapps.data.value.Sorting sorting = null;

    @Override
    public int getCount() {
       return userQuery().execute().size();
    }

    @Override
    public List<User> getRecords(int startIndex, int length) {
        // return userQuery().execute(startIndex,length, createUdbSorting(sorting));
        return userQuery().execute(sorting.getFieldName(), true, UserContext.create("de"));
    }

    private org.teamapps.universaldb.query.Sorting createUdbSorting(org.teamapps.data.value.Sorting sorting) {
        if (sorting != null && sorting.getFieldName() != null) {
            return new org.teamapps.universaldb.query.Sorting(sorting.getFieldName(), sorting.getSorting() == SortDirection.ASC);
        } else {
            return null;
        }
    }

    private UserQuery userQuery(){
        UserQuery query = User.filter();

        return query;
    }
}
