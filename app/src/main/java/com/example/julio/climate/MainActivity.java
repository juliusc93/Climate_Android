package com.example.julio.climate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private TextView header;
    private TextView temp;
    private TextView fore_temp;
    private ProgressDialog pDialog;
    private static String current="http://api.openweathermap.org/data/2.5/weather?q=Barranquilla,co&units=metric";
    private static String forecast="http://api.openweathermap.org/data/2.5/forecast/daily?id=3689147&units=metric";
    JSONObject climate = null;
    JSONArray climate2 = null;
    JSONObject climatef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header = (TextView) findViewById(R.id.header);
        temp = (TextView) findViewById(R.id.currentWeather);
        fore_temp = (TextView) findViewById(R.id.forecastWeather);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GetWeather(View view){
        new GetData().execute();
    }

    public void show() throws Exception{
        temp.setText(climate.getString("temp")+ "ºC");
        fore_temp.setText(climatef.getString("min")+" ºC");
    }
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(current, ServiceHandler.GET);
            DataEntry dataEntry = new DataEntry();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    climate = jsonObj.getJSONObject("main");
                    //JSONObject c = climate.getJSONObject(i).getJSONObject("weather1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            String jsonStr2 = sh.makeServiceCall(forecast, ServiceHandler.GET);
            if (jsonStr2 != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr2);
                    climate2 = jsonObj.getJSONArray("list");
                    climatef = climate2.getJSONObject(4).getJSONObject("temp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
