package com.thegeekylad.cabbit;

import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BotStuff {

    private static String WORKSPACE_ID = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String OMDB_API_KEY = "";

    private Assistant assistant;
    private android.content.Context context;
    private Context botContext;


    public BotStuff(android.content.Context context) {
        this.context = context;
        assistant = new Assistant("2018-02-16");
        assistant.setUsernameAndPassword(USERNAME,PASSWORD); // maximus
        assistant.setEndPoint("https://gateway.watsonplatform.net/conversation/api");
        botContext = new Context();
    }

    public void hey(String message, HtmlTextView messageField, boolean preserveContext) {
        new SendReceive(message, messageField, preserveContext).execute();
    }

    private class SendReceive extends AsyncTask<Void, Void, Void> {

        private String message, output, var;
        private int query;
        private HtmlTextView messageField;
        private boolean preserveContext;

        public SendReceive(String message, HtmlTextView messageField, boolean preserveContext) {
            output = "";
            this.message = message;
            this.messageField = messageField;
            this.preserveContext = preserveContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (!preserveContext) botContext = null;
            MessageOptions messageOptions = new MessageOptions.Builder(WORKSPACE_ID)
                    .input(new InputData.Builder(message).build())
                    .context(botContext)
                    .build();
            MessageResponse response = assistant
                    .message(messageOptions)
                    .execute();
            List<String> responseList = response.getOutput().getText();
            for (String res : responseList) if (res.length() != 0) output += res + "<br>";
            botContext = response.getContext();
            Object t;
            query = ((t = botContext.get("query")) == null) ? 0 : (int) Double.parseDouble(t.toString());

            switch (query) {
                case 1:
                    omdb("Year");
                    break;
                case 2:
                    omdb("Runtime");
                    break;
                case 3:
                    omdb("Genre");
                    break;
                case 4:
                    omdb("Director");
                    break;
                case 5:
                    omdb("Writer");
                    break;
                case 6:
                    omdb("Actors");
                    break;
                case 7:
                    omdb("Plot");
                    break;
                case 8:
                    omdb("Language");
                    break;
                case 9:
                    omdb("Country");
                    break;
                case 10:
                    omdb("imdbRating");
                    break;
                case 11:
                    omdb("BoxOffice");
                    break;
                case 12:
                    omdb("Production");
                    break;
                case 13:
                    omdb("Awards");
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            messageField.setHtml(output.trim());
            super.onPostExecute(aVoid);
        }

        private void omdb(String omdbParam) {
            try {
                URL url = new URL("http://www.omdbapi.com/?apikey="+OMDB_API_KEY+"&t=" + botContext.get("title"));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
                String line, json = "";
                while ((line = reader.readLine()) != null)
                    json += line + "\n";
                urlConnection.disconnect();
                JSONObject jsonObject = new JSONObject(json);
                var = jsonObject.getString(omdbParam);
                output = output.replaceAll("var", var);
            } catch (Exception e) {
                Log.e("IOException", e.getMessage());
            }
        }
    }

}
