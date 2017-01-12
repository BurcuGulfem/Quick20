package com.archsorceress.quick20;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link Quick20WidgetConfigureActivity Quick20WidgetConfigureActivity}
 */
public class Quick20Widget extends AppWidgetProvider {
    protected static final String PREFS_NAME = "com.archsorceress.quick20.Quick20Widget";
    protected static final String PREF_PREFIX_KEY = "appwidget_";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    static protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quick20_widget);

        int resourceId = loadDicePref(context, appWidgetId);

        Intent intent = new Intent(context, Quick20Widget.class);
        intent.setAction("Clicked:"+appWidgetId);
        intent.putExtra("id", appWidgetId);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setImageViewResource(R.id.quick20_widget_imageView,resourceId);
        remoteViews.setOnClickPendingIntent(R.id.quick20_widget_imageView, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quick20_widget);

        int resourceId = loadDicePref(context, intent.getIntExtra("id", 0));
        int dice = 1;
        switch (resourceId)
        {
            case R.drawable.d20_state: dice = 20; break;
            case R.drawable.d12_state: dice = 12; break;
            case R.drawable.d10_state: dice = 10; break;
            case R.drawable.d8_state: dice = 8; break;
            case R.drawable.d6_state: dice = 6; break;
            case R.drawable.d4_state: dice = 4; break;
        }

        int num = new HighQualityRandom().nextInt(dice)+1;
        remoteViews.setTextViewText(R.id.quick20_widget_textView_d20,""+num);
        appWidgetManager.updateAppWidget(intent.getIntExtra("id",0), remoteViews);


    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {
            deleteDicePref(context, appWidgetId);
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        onUpdate(context, AppWidgetManager.getInstance(context), newWidgetIds);
     }


    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadDicePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, R.drawable.d20_state);
    }


    static void deleteDicePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}