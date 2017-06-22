package palav.sarvesh.com.digicardseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by sarveshpalav on 24/12/16.
 */
public class LoggedinActivity extends AppCompatActivity {
EditText Bill;
    Button Generate;

    String bill,generate;
    SharedPreferences sharedpreferences;
    TextView Update;
    TextView Balance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_loggedin);

        sharedpreferences = getSharedPreferences(CONFIG.MyPREFERENCES, Context.MODE_PRIVATE);

        Bill =(EditText)findViewById(R.id.bill);
        Generate =(Button)findViewById(R.id.generate);
        Update =(TextView)findViewById((R.id.textView5));
        Balance = (TextView)findViewById(R.id.amttransacted);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatebal();
            }
        });


        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bill = Bill.getText().toString().trim();
                generate = Generate.getText().toString().trim();
                generate(bill);

            }
        });
    }



    public void updatebal( )
    {



        class update extends AsyncTask<String,String,String>
        {
            @Override
            protected void onPostExecute(final String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Balance.setText(s);
                    }
                });

                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                String cardid = sharedpreferences.getString(CONFIG.sharedprefcardid,"");
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("cardid",cardid);


                RequestHandler ruc = new RequestHandler();
                String result =    ruc.sendPostRequest("http://digicardservices.in/Android/getbalance.php",data);

                return  result;
            }
        }
        update a = new update();
        a.execute();
    }

    public void generate(String bill)
    {
        class generate extends AsyncTask<String,String,String>
        {
            ProgressDialog loading;

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
loading.dismiss();
                Intent i = new Intent(LoggedinActivity.this,QRPage.class);
                i.putExtra("otp",s);
                startActivity(i);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoggedinActivity.this, "Fetching Your Bill", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String bill = params[0];
                String cardid = sharedpreferences.getString(CONFIG.sharedprefcardid,"");
                String cardname = sharedpreferences.getString(CONFIG.sharedprefcardname,"");
                System.out.println("ello"+cardname);
                String sellerid = sharedpreferences.getString(CONFIG.sharedprefsellerid,"");

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("bill",bill);
                data.put("cardid",cardid);
                data.put("cardname",cardname);
                data.put("sellerid",sellerid);


                RequestHandler ruc = new RequestHandler();
            String result =    ruc.sendPostRequest(CONFIG.INSERT_TRANS,data);

                System.out.println("Sid"+result);


                return result;
            }
        }
        generate generate = new generate();
        generate.execute(bill);
    }



}
