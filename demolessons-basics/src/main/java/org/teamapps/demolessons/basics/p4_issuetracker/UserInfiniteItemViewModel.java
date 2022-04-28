package org.teamapps.demolessons.basics.p4_issuetracker;

import org.teamapps.data.value.SortDirection;
import org.teamapps.demolessons.issuetracker.model.issuetracker.User;
import org.teamapps.demolessons.issuetracker.model.issuetracker.UserQuery;
import org.teamapps.universaldb.context.UserContext;
import org.teamapps.ux.component.infiniteitemview.AbstractInfiniteItemViewModel;

import java.util.List;

class UserInfiniteItemViewModel extends AbstractInfiniteItemViewModel<User> {

    @Override
    public int getCount() {
       return userQuery().execute().size();
    }

    @Override
    public List<User> getRecords(int startIndex, int length) {
        // return userQuery().execute(startIndex,length, createUdbSorting(sorting));
        return userQuery().execute(User.FIELD_NAME, true, UserContext.create("de"));
    }

    private UserQuery userQuery(){

        return User.filter();
    }
}
