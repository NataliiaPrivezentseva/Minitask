package redlor.it.minitask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class PageFragment extends TaskFragment {

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            firebaseLoad();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }
}