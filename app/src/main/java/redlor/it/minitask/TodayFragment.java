package redlor.it.minitask;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import redlor.it.minitask.viewholder.FirebaseViewHolder;

public class TodayFragment extends TaskFragment {

    Date today;
    String day;
    String myDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            firebaseLoad();
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                boolean hasReminder = toDoItem.getHasReminder();
                Calendar calendar = Calendar.getInstance();
                today = calendar.getTime();
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                myDay = myFormat.format(today);
                String completeTime = toDoItem.getReminderDate();
                if (completeTime != null && !completeTime.equals(" ")) {
                    String[] parts = completeTime.split(" ");
                    day = parts[0];
                }

                if (hasReminder && myDay.equals(day)) {
                    boolean todayB = true;
                    System.out.println(myDay + " " + day);
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
                    // and add a animation for expired item
                    if (toDoItem.getHasReminder()) {
                        viewHolder.clockReminder.setVisibility(View.VISIBLE);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateTime = sdf.format(c.getTime());
                        if (currentDateTime.compareTo(toDoItem.getReminderDate()) > 0) {
                            setAnimation(viewHolder);
                        }
                    } else {
                        viewHolder.clockReminder.setVisibility(View.INVISIBLE);
                    }

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