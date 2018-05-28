package redlor.it.minitask;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import redlor.it.minitask.viewholder.FirebaseViewHolder;

public class WeekFragment extends TaskFragment {

    Date itemDate;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            firebaseLoad();
        }
    }

    @Override
    void firebaseLoad() {
        final FirebaseRecyclerOptions<ToDoItem> options = getToDoItemFirebaseRecyclerOptions();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ToDoItem, FirebaseViewHolder>(options
        ) {

            @Override
            public FirebaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todo_item, parent, false);
                return new FirebaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FirebaseViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
                Calendar calendarWeek = Calendar.getInstance();
                Calendar calendarItem = Calendar.getInstance();
                Calendar calendarToday = Calendar.getInstance();
                calendarWeek.add(Calendar.DAY_OF_YEAR, 7);
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                String completeTime = toDoItem.getReminderDate();

                if (completeTime != null && !completeTime.equals(" ")) {
                    try {
                        itemDate = myFormat.parse(completeTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calendarItem.setTime(itemDate);

                } else {
                    calendarItem.add(Calendar.DAY_OF_YEAR, 8);
                }

                if (calendarItem.after(calendarToday) && calendarItem.before(calendarWeek)) {

                    // set the content of the item
                    viewHolder.content.setText(toDoItem.getContent());
                    // set the checkbox status of the item
                    viewHolder.checkDone.setChecked(toDoItem.getDone());
                    // check if checkbox is checked, then strike through the text
                    // this is for the first time UI render
                    if (viewHolder.checkDone.isChecked()) {
                        viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        viewHolder.content.setPaintFlags(viewHolder.content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }

                    // render the clock icon if the item has a reminder
                    if (toDoItem.getHasReminder())
                        viewHolder.clockReminder.setVisibility(View.VISIBLE);
                    else
                        viewHolder.clockReminder.setVisibility(View.INVISIBLE);

                    createOnCheckedListener(viewHolder, position, toDoItem);
                    createOnClickListener(viewHolder, toDoItem);

                } else {
                    viewHolder.LayoutHide();
                }
            }
        };
        
        getRecyclerView();
        mFirebaseAdapter.startListening();
    }
}