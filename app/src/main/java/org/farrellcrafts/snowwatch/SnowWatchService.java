package org.farrellcrafts.snowwatch;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SnowWatchService extends Service {
    private Report latestReport;
    private final SnowWatchService self = this;
    private ListenerRegistration registration;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(this);
        final int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Query reportQuery = firestore.collection("reports")
                .orderBy("ReportTime", Query.Direction.DESCENDING)
                .limit(1L);

        registration = reportQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                if(!queryDocumentSnapshots.isEmpty()){
                    latestReport = queryDocumentSnapshots.getDocuments()
                            .get(0)
                            .toObject(Report.class);

                    if(ids != null){
                        for (int widgetId: ids) {
                            assert latestReport != null;
                            SnowWatchWidget.updateAppWidget(self, manager, widgetId, latestReport);
                        }
                    }
                }
            }
        });

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
       registration.remove();
       registration = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
