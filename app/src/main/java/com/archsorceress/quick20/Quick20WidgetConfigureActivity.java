package com.archsorceress.quick20;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


/**
 * The configuration screen for the {@link Quick20Widget Quick20Widget} AppWidget.
 */
public class Quick20WidgetConfigureActivity extends Activity implements View.OnClickListener{

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
   // EditText mAppWidgetText;

    ImageView d20,d12,d10,d8,d6,d4;
    int id_dice_image;

    public Quick20WidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.quick20_widget_configure);
         d20 =(ImageView) findViewById(R.id.quick20_widget_imageView_d20);
         d12 =(ImageView) findViewById(R.id.quick20_widget_imageView_d12);
         d10 =(ImageView) findViewById(R.id.quick20_widget_imageView_d10);
         d8 =(ImageView) findViewById(R.id.quick20_widget_imageView_d8);
         d6 =(ImageView) findViewById(R.id.quick20_widget_imageView_d6);
         d4 =(ImageView) findViewById(R.id.quick20_widget_imageView_d4);

        id_dice_image = R.drawable.d20_state;
        d20.setSelected(true);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    public void deselectAllImages()
    {
        d20.setSelected(false);
        d12.setSelected(false);
        d10.setSelected(false);
        d8.setSelected(false);
        d6.setSelected(false);
        d4.setSelected(false);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = Quick20WidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
         //   String widgetText = mAppWidgetText.getText().toString();
            saveDicePref(context, mAppWidgetId, id_dice_image);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Quick20Widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
     void saveDicePref(Context context, int appWidgetId, int dice) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Quick20Widget.PREFS_NAME, 0).edit();
        prefs.putInt(Quick20Widget.PREF_PREFIX_KEY + appWidgetId, dice);
        prefs.apply();
    }


    @Override
    public void onClick(View v) {
        deselectAllImages();
        v.setSelected(true);
       // v.setBackgroundColor(Color.CYAN);
        switch (v.getTag().toString())
        {
            case "d20":  id_dice_image = R.drawable.d20_state; break;
            case "d12":  id_dice_image = R.drawable.d12_state; break;
            case "d10":  id_dice_image = R.drawable.d10_state;break;
            case "d8":  id_dice_image = R.drawable.d8_state;break;
            case "d6":  id_dice_image = R.drawable.d6_state;break;
            case "d4":  id_dice_image = R.drawable.d4_state;break;
        }
    }
}



