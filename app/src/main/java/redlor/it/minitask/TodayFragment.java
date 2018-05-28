package redlor.it.minitask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import redlor.it.minitask.viewholder.FirebaseViewHolder;

public class TodayFragment extends TaskFragment {

    String toDoItemDay;

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

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ToDoItem, FirebaseViewHolder>(options) {

            @Override
            public FirebaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todo_item, parent, false);
                return new FirebaseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FirebaseViewHolder viewHolder, final int position, final ToDoItem toDoItem) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = myFormat.format(Calendar.getInstance().getTime());

                String completeTime = toDoItem.getReminderDate();
                if (completeTime != null && !completeTime.equals(" ")) {
                    String[] parts = completeTime.split(" ");
                    toDoItemDay = parts[0];
                }

                if (toDoItem.getHasReminder() && today.equals(toDoItemDay)) {
                    setViewHolderTextAndPaintFlag(viewHolder, toDoItem);

                    // render the clock icon if the item has a reminder
                    viewHolder.clockReminder.setVisibility(View.VISIBLE);
                    // add an animation for the expired item
                    setAnimationIfTaskIsExpired(viewHolder, toDoItem);

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