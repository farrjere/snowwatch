package org.farrellcrafts.snowwatch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.weathericons.WeatherIcons;
import com.mikepenz.iconics.view.IconicsImageView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textViewDepth;
    private TextView textViewTemp;
    private TextView textViewTrails;
    private TextView textViewGroomed;
    private TextView textViewLifts;
    private TextView textViewForecast12;
    private TextView textViewForecast24;
    private TextView textViewWeather;
    private TextView textViewLastUpdated;
    private IconicsImageView imageViewWeather;
    private Report latestReport;

    public void initFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        super.onResume();
        Query reportQuery = firestore.collection("reports")
                .orderBy("ReportTime", Query.Direction.DESCENDING)
                .limit(1L);

        reportQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.i(TAG, "updateSnapshotListener event received");
                assert queryDocumentSnapshots != null;
                if(!queryDocumentSnapshots.isEmpty()){
                    latestReport = queryDocumentSnapshots.getDocuments()
                            .get(0)
                            .toObject(Report.class);
                    Log.i(TAG, "updateSnapshotListener updating view with new report");

                    updateView();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initFirestore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewDepth = findViewById(R.id.text_view_base_depth);
        textViewTemp = findViewById(R.id.text_view_temp);
        textViewTrails = findViewById(R.id.text_view_trails);
        textViewGroomed = findViewById(R.id.text_view_groomed);
        textViewLifts = findViewById(R.id.text_view_lifts);
        textViewWeather = findViewById(R.id.text_weather);
        textViewForecast12 = findViewById(R.id.text_view_forecast12);
        textViewForecast24 = findViewById(R.id.text_view_forecast24);
        textViewLastUpdated = findViewById(R.id.text_view_updated);
        imageViewWeather = findViewById(R.id.weather_icon);
        initFirestore();
    }

    private void updateView(){
        textViewDepth.setText(latestReport.formatDepth(latestReport.getBaseDepth()));
        textViewTemp.setText(latestReport.formatTemp(latestReport.getTemperature()));
        textViewTrails.setText(String.valueOf(latestReport.getOpenTrails()));
        textViewGroomed.setText(String.valueOf(latestReport.getGroomedTrails()));
        textViewLifts.setText(String.valueOf(latestReport.getOpenLifts()));
        textViewWeather.setText(latestReport.getWeather());
        textViewForecast12.setText(latestReport.getForecast12());
        textViewForecast24.setText(latestReport.getForecast24());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("America/Boise"));
        String formattedDate = formatter.format(latestReport.getReportTime());
        textViewLastUpdated.setText(formattedDate);
        IconicsDrawable weather = getWeatherIcon(latestReport.getWeatherIcon(), this);
        imageViewWeather.setIcon(weather);
    }

    public static IconicsDrawable getWeatherIcon(String weather, Context context){
        weather = weather == null ? "" : weather;
        switch (weather) {
            case "cloudy":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_cloudy);
            case "snow":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_snow);
            case "windy":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_windy);
            case "rainy":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_rain);
            case "partly-cloudy--day":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_day_cloudy);
            case "partly-cloudy--night":
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_night_alt_partly_cloudy);
            default:
                return new IconicsDrawable(context)
                        .icon(WeatherIcons.Icon.wic_day_sunny);
        }

    }
}
