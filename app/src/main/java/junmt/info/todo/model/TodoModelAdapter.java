package junmt.info.todo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.builder.Condition;

import junmt.info.todo.R;

/**
 * Created by jun on 2015/05/09.
 */
@Deprecated
public class TodoModelAdapter extends BaseAdapter {
    private FlowCursorList<Todo> flowCursorList;
    private LayoutInflater layoutInflater;

    public TodoModelAdapter(Context context) {
        flowCursorList = new FlowCursorList<Todo>(true, Todo.class, Condition.column(Todo$Table.ID).isNotNull());
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return flowCursorList.getCount();
    }

    @Override
    public Object getItem(int position) {
        return flowCursorList.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return flowCursorList.getItem(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.todo_item, null);
        }

        TextView titleView = (TextView)convertView.findViewById(R.id.todo_item_title);
        titleView.setText(flowCursorList.getItem(position).title);

        return convertView;
    }

    public void close() {
        flowCursorList.close();
    }
}
