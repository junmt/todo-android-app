package junmt.info.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import junmt.info.todo.model.Todo;

/**
 * Created by jun on 2015/05/07.
 */
public class EditActivity extends AppCompatActivity {
    public static final String KEY_TODO_ID = "todo_id";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private long todoId;
    private DatePickerDialog datepickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_todo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // minSdkVersionが11以上なら代わりに以下を使います:
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        //編集の場合はIDが渡ってくるため、取得しておく
        todoId = getIntent().getLongExtra(KEY_TODO_ID, 0L);
        setValues();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accept, menu);

        if(todoId > 0) {
            getMenuInflater().inflate(R.menu.menu_trash, menu);
            setTitle("編集");
        } else {
            setTitle("作成");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_accept ) {
            boolean success = saveTodo();
            if(success) {
                Toast.makeText(this, "保存しました。", Toast.LENGTH_SHORT);
                finishAndReload();

            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            }
            return true;
        }
        else if( id == R.id.action_trash ) {
            removeTodo();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * フォームに値をセットします。
     */
    private void setValues() {
        if(todoId == 0) {
            return ;
        }

        EditText title = (EditText)findViewById(R.id.input_title);
        EditText note = (EditText)findViewById(R.id.input_note);
        EditText limitDate = (EditText)findViewById(R.id.input_limitDate);

        Todo todo = Todo.findById(todoId);

        title.setText(todo.title);
        note.setText(todo.note);
        if(todo.limitDate != null) {
            limitDate.setText(dateFormat.format(todo.limitDate));
        }
    }

    /**
     * フォーム上で発生するイベントをバインドします。
     */
    private void setEvents() {
        final EditText limitDate = (EditText)findViewById(R.id.input_limitDate);
        final Context currentContext = this;

        limitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                String strLimitDate = limitDate.getText().toString();

                try {
                    cal.setTime(dateFormat.parse(strLimitDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                datepickerDialog = new DatePickerDialog(currentContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                limitDate.setText(dateFormat.format(cal.getTime()));
                            }
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datepickerDialog.getDatePicker().setCalendarViewShown(true);
                datepickerDialog.getDatePicker().setSpinnersShown(false);
                datepickerDialog.show();
            }
        });
    }

    /**
     * Save ToDo
     * @return
     */
    private boolean saveTodo() {
        EditText title = (EditText)findViewById(R.id.input_title);
        EditText note = (EditText)findViewById(R.id.input_note);
        EditText limitDate = (EditText)findViewById(R.id.input_limitDate);

        try {
            Todo todo = new Todo();

            if(todoId > 0) {
                todo = Todo.findById(todoId);
            }

            todo.title = title.getText().toString();
            todo.note = note.getText().toString();

            if(limitDate.getText().length() > 0) {
                todo.limitDate = dateFormat.parse(limitDate.getText().toString());
            } else {
                todo.limitDate = null;
            }

            todo.save();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Delete Todo
     */
    private void removeTodo() {
        DialogFragment dialogFragment = new DialogFragment(){
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("削除しますか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Todo todo = Todo.findById(todoId);
                                todo.delete();
                                finishAndReload();
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                return builder.create();
            }
        };
        dialogFragment.show(getFragmentManager(), "削除しますか？");
    }

    /**
     * Back to MainActivity and MainActivity reload.
     */
    private void finishAndReload() {
        Intent intent = new Intent();
        intent.setAction(MainActivity.INTENT_ACTION_LIST_RELOAD);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        finish();
    }
}
