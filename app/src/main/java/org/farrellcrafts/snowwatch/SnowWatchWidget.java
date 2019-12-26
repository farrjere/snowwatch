package org.farrellcrafts.snowwatch;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;

public class SnowWatchWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                Report latestReport) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.snow_watch_widget);
        views.setTextViewText(R.id.widget_depth, latestReport.formatDepth(latestReport.getBaseDepth()));
        views.setTextViewText(R.id.widget_temp, latestReport.formatTemp(latestReport.getTemperature()));
        views.setContentDescription(R.id.widget_icon, latestReport.getWeatherIcon());
        IconicsDrawable weatherIcon = MainActivity.getWeatherIcon(latestReport.getWeatherIcon(), context);

        Bitmap weatherBitmap = weatherIcon
                .color(IconicsColor.parse("white"))
                .toBitmap();

        views.setImageViewBitmap(R.id.widget_icon, weatherBitmap);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context.getApplicationContext(), SnowWatchService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(intent);
    }
}
