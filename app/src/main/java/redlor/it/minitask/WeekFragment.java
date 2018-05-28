package redlor.it.minitask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ToDoItem, FirebaseViewHolder>(options) {

            @Override
            public FirebaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todo_item, parent, false);
                return new FirebaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FirebaseViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
                Calendar calendarWeek = Calendar.getInstance();
                calendarWeek.add(Calendar.DAY_OF_YEAR, 7);

                Calendar calendarItem = Calendar.getInstance();
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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

                Calendar calendarToday = Calendar.getInstance();

                if (calendarItem.after(calendarToday) && calendarItem.before(calendarWeek)) {
                    setViewHolderTextAndPaintFlag(viewHolder, toDoItem);

                    // render the clock icon for the item
                    viewHolder.clockReminder.setVisibility(View.VISIBLE);

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