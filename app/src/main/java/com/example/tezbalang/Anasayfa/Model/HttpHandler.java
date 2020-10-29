package com.example.tezbalang.Anasayfa.Model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {
    public HttpHandler() {
    }
    public String makeServiceCall(String requestUrl){
        String response = null;
        try {
            URL url = new URL(requestUrl);
            //githuba gider veriyi okur.Get ve post işemide yapabilir.
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamtoString(in);
        }
        catch (MalformedURLException e ){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  response;
    }
     private String convertStreamtoString(InputStream is){
         BufferedReader reader = new BufferedReader(new InputStreamReader( (is)));
         StringBuilder sb = new StringBuilder();

         String satir ="";
         try{
             while ((satir = reader.readLine()) != null){
                 sb.append(satir).append("\n");
             }
         }catch (IOException e){

         }finally {
             try {
                 is.close();
             }catch (IOException e){

             }
         }
         return  sb.toString();
     }
}
