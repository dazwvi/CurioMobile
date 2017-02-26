package com.example.cs349_a4;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import android.os.*;
import android.util.Log;

class GetRequestTask extends AsyncTask<String, String, String>{

    public GetRequestTask(){
    }

    protected String doInBackground(String... urls){
        String response = "";

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    response = response + inputStr;
                }
                //JSONArray jsonObject = new JSONArray(responseStr.toString());
            } catch (IOException e1) {
                Log.d("IOException", e1.getMessage());
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            Log.d("Exception", e.getMessage());
        } catch (IOException e) {
            Log.d("Exception", e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
