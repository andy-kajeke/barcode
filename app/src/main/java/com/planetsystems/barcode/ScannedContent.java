package com.planetsystems.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ScannedContent extends AppCompatActivity {

    TextView company_name, company_name1, product_name, manufacture_Date, expiry_Date, city_, state_, province_, locality_, box_number, product_barcode;
    TextView phone_number, email_;
    String data, content_extra;
    String companyName, productNname, manufactureDate, expiryDate, city, state, province, locality, boxNumber, productBarcode;
    String phoneNumber;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_content);
        setTitle("Details");

        company_name = findViewById(R.id.company_name);
        company_name1 = findViewById(R.id.company_name1);
        product_name = findViewById(R.id.product_name);
        manufacture_Date = findViewById(R.id.manufactureDate);
        expiry_Date = findViewById(R.id.expiryDate);
        city_ = findViewById(R.id.city);
        state_ = findViewById(R.id.state);
        province_ =findViewById(R.id.province);
        locality_ = findViewById(R.id.locality);
        box_number = findViewById(R.id.boxNumber);
        product_barcode = findViewById(R.id.product_barcode);
        phone_number = findViewById(R.id.phone_number);

        Bundle bundle = getIntent().getExtras();
        content_extra = bundle.getString("scan_content");

        //company_name.setText(content_extra);
//        if (!isConnected()) {
//            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
//        } else {
//            new Fetch_API_JSONAsyncTask().execute(Url.VERIFICATION + content_extra);
//        }

        new Fetch_API_JSONAsyncTask().execute(Url.VERIFICATION + content_extra);
    }

    //getting tasks online
    class Fetch_API_JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ScannedContent.this);
            dialog.setMessage("Getting device ownership...");
            //dialog.setTitle("Loading ..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {


                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("id","cmd"));
                URI uri = new URI(urls[0] + "?" + URLEncodedUtils.format(params, "utf-8"));
                HttpGet httppost = new HttpGet(uri);
                HttpClient httpclient = new DefaultHttpClient();


                HttpResponse response = httpclient.execute(httppost);
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    //mResponse = jsono.getBoolean("response");

                    //JSONObject objectEmp = jsono.getJSONObject("school");

                    JSONArray jarray = jsono.getJSONArray("products");


                    for(int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        companyName = object.getString("company_name");
                        productNname = object.getString("product_name");
                        manufactureDate = object.getString("manufactured_date");
                        expiryDate = object.getString("expiry_date");
                        city = object.getString("city");
                        state = object.getString("state");
                        province = object.getString("province");
                        locality = object.getString("location");
                        boxNumber = object.getString("postal_address");
                        productBarcode = object.getString("product_barcode");
                        phoneNumber = object.getString("contact");

                    }

                    return true;
                }else {

                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (URISyntaxException es) {
                es.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            //adapter.notifyDataSetChanged();
            if(result == false){

            }else{
                //Toast.makeText(getApplicationContext(), "Unable to fetch", Toast.LENGTH_LONG).show();
                company_name.setText(companyName);
                company_name1.setText(companyName);
                product_name.setText(productNname);
                manufacture_Date.setText(manufactureDate);
                expiry_Date.setText(expiryDate);
                city_.setText(city);
                state_.setText(state);
                province_.setText(province);
                locality_.setText(locality);
                box_number.setText(boxNumber);
                product_barcode.setText(productBarcode);
                phone_number.setText(phoneNumber);

               if (company_name.getText().toString().equals("")){
                   new AlertDialog.Builder(ScannedContent.this)
                           .setTitle("Confirmation")
                           .setMessage("This product doesn't exist on GS1 Uganda")
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .setPositiveButton("Alright", new DialogInterface.OnClickListener() {

                               public void onClick(DialogInterface dialog, int whichButton) {
                                   ScannedContent.this.finish();
                               }}).show();
               }
            }
        }
    }

//    public boolean isConnected(){
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }

}
