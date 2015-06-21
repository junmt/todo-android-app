package junmt.info.todo.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.runtime.DBTransactionInfo;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.UpdateTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by jun on 2015/05/07.
 */
@Table(databaseName = ToDoDatabase.NAME)
public class Todo extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String title;

    @Column
    public Date limitDate;

    @Column
    public boolean isFinished;

    @Column
    public String note;

    @Column
    public Date registDate;

    @Column
    public Date updateDate;

    public static Todo findById(long id) {
        Todo todo = new Select().from(Todo.class)
                .where(Condition.column(Todo$Table.ID).eq(id)).querySingle();
        return todo;
    }

    @Override
    public void save() {

        if(id == 0)
            this.registDate = new Date();

        this.updateDate = new Date();

        super.save();
    }

}
