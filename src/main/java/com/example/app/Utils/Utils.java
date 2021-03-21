package com.example.app.Utils;

import com.google.gson.Gson;
import org.apache.http.HttpException;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * This is an utility class
 */

public class Utils {

    private static final Gson gson = new Gson();

    /**
     * this method converts the input stream response from an external api to a string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)
            result.append(line);
        inputStream.close();
        return result.toString();
    }

    /**
     * This method prepares the request
     *
     * @param connection
     * @param requestMethod
     * @param contentType
     * @throws IOException
     */
    public static void prepareRequest(HttpURLConnection connection, String requestMethod, String contentType) throws IOException {
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
    }

    /**
     * Generic method which handles the servers response
     *
     * @param connection
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static <T> T handleResponse(HttpURLConnection connection, Class<T> tClass) throws IOException, HttpException {
        if (connection.getResponseCode() != 200) {
            InputStream in = new BufferedInputStream(connection.getErrorStream());
            String result = convertInputStreamToString(in);
            throw new HttpException(result);
        }

        InputStream in = new BufferedInputStream(connection.getInputStream());
        String result = convertInputStreamToString(in);

        in.close();
        return gson.fromJson(result, tClass);
    }
}
