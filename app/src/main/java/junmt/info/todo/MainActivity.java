package junmt.info.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import junmt.info.todo.model.Todo;
import junmt.info.todo.model.Todo$Table;
import junmt.info.todo.model.TodoArrayAdapter;


public class MainActivity extends AppCompatActivity {
    public static final String INTENT_ACTION_LIST_RELOAD = "info.junmt.todo.list_reload";
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DBFlow initialize
        FlowManager.init(this);

        // show todo list
        showTodo();

        // Accept Broadcast for todo list reload.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_LIST_RELOAD);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showTodo();
            }
        };
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if( id == R.id.action_edit) {
            startEditAcvitity(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startEditAcvitity(long id) {
        Intent intent = new Intent(this, EditActivity.class);
        if(id > 0) {
            intent.putExtra(EditActivity.KEY_TODO_ID, id);
        }
        startActivity(intent);
    }

    private void showTodo() {
        List<Todo> todos = new Select().from(Todo.class)
                .where().orderBy(Todo$Table.LIMITDATE).queryList();

        ListView view = (ListView)findViewById(R.id.todolist);
        TodoArrayAdapter adapter = new TodoArrayAdapter(this, R.layout.todo_item, todos);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = (Todo) parent.getItemAtPosition(position);
                startEditAcvitity(todo.id);
            }
        });
    }

}
