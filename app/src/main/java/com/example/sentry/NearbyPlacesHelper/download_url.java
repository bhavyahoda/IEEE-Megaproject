package com.example.sentry.NearbyPlacesHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.HashMap;

//retrieve data from url
public class download_url {


    //retrieve data from url
    public String readUrl(String myUrl) throws IOException {
        String data=null;
        //file handling methods to read data from the url
        InputStream inputStream = null;
        //object for url connection
        HttpURLConnection connection = null;
        try {
            URL url = new URL(myUrl);
            //specify the request from the user initializing the error
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();
            //passing an object of input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer builder = new StringBuffer();
            //strings are immutable that is once i create a string object and assign it with some value and then change it it does not make the change in that object it creates a new object for me
            //to avoid this that is i have to update the changes i make in that very object i do that by using the class string buffer
            //it makes the string mutable it is thread safe that is the operation in those class does not affect the other class/thread safe

           // reading each line of the url
            String line = "";
            //we are checking if the line in that file is null or not and if not null we are appending it
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //convert the string buffer to string type and store it in the data variable
            data = builder.toString();
            //we have read all the data we need hence we are closing the buffered reader
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //try executes the statements if any of them do not work catch block will catch it and it will display the
            //message using e.----- and if we have to execute something even if their is a exception we use finally block
                inputStream.close();
            connection.disconnect();
        }
        return data;
    }
}


