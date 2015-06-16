package carlos.figueiredo.com.viracopos;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends ActionBarActivity {

    ListView rankList;

    String url = "http://23.239.18.68:8080/player";

    ArrayAdapter adapter;

    List<String> stringArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        rankList = (ListView) findViewById(R.id.lvReport);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        rankList.setAdapter(adapter);

        if (isConnected()){
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);
        }

    }

    protected void onResume() {
        super.onResume();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_activity, menu);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, JSONArray> {

         @Override
         protected JSONArray doInBackground(String... urls) {
             return GET(urls[0]);
         }

         @Override
         protected void onPostExecute(JSONArray arrayResult) {
             JSONObject objectResult;
             Toast.makeText(getBaseContext(), "sucess", Toast.LENGTH_SHORT).show();

             for (int i = 0; i < arrayResult.length(); i++) {
                 try {
                     objectResult = arrayResult.getJSONObject(i);
                     String result = "Nome: " + objectResult.get("nome") +
                             " Pontos:" + objectResult.get("pontos");
                     stringArray.add(result);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
             adapter.notifyDataSetChanged();
         }
     }

    public static JSONArray GET(String url) {
        InputStream inputStream;
        BufferedReader bufferedReader;
        JSONArray result = null;
        String line;

        try{
            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null){
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                if ((line = bufferedReader.readLine()) != null) {
                    result = new JSONArray(line);

                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
