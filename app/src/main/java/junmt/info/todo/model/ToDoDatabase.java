package junmt.info.todo.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by jun on 2015/05/07.
 */
@Database(name= ToDoDatabase.NAME, version= ToDoDatabase.VERSION)
public class ToDoDatabase {
    public static final String NAME = "Todo";
    public static final int VERSION = 1;
}
