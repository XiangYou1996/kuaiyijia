package com.example.kuaiyijia.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJson {
    public String result;
    public GetJson(String urlString, String postStr, String method) throws IOException {
        HttpURLConnection urlConnection;

        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        urlConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");

        urlConnection.getOutputStream().write(postStr.getBytes("utf-8"));
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();

        if (urlConnection.getResponseCode() == 200){
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            StringBuffer temp = new StringBuffer();

            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line);
                //temp.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            result =  temp.toString();
        }
    }
}

