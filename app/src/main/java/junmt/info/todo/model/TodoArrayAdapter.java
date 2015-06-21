package junmt.info.todo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junmt.info.todo.R;

/**
 * Created by jun on 2015/05/10.
 */
public class TodoArrayAdapter extends ArrayAdapter<Todo> {
    private LayoutInflater layoutInflater;
    private List<Date> viewedDates = new ArrayList<Date>();

    public TodoArrayAdapter(Context context, int textViewResourceId, List<Todo> todos) {
        super(context, textViewResourceId, todos);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.todo_item, null);
        }

        Todo todo = (Todo)getItem(position);

        // タイトルの表示
        TextView titleView = (TextView)convertView.findViewById(R.id.todo_item_title);
        titleView.setText(todo.title);

        // 期限日の表示
        TextView limitDateView = (TextView)convertView.findViewById(R.id.todo_item_limitDate);
        if( ! viewedDates.contains(todo.limitDate )) {
            if(todo.limitDate != null) {
                limitDateView.setText((new SimpleDateFormat("yyyy/MM/dd").format(todo.limitDate)));
            }
            viewedDates.add(todo.limitDate);
        } else {
            limitDateView.setVisibility(View.GONE);
        }

        // 詳細内容の表示
        TextView noteView = (TextView)convertView.findViewById(R.id.todo_item_note);
        if(todo.note != null) {
            noteView.setText(todo.note);
        }

        return convertView;
    }
}
