package palav.sarvesh.com.digicardseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    EditText editTextemail,editTextpassword;
    String email,password;
    Button Buttonlogin;
    ProgressBar progressBar;
    RelativeLayout loginLayout;
    SharedPreferences sharedpreferences;
    URL_REQUEST url_request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(CONFIG.MyPREFERENCES, Context.MODE_PRIVATE);
url_request = new URL_REQUEST();

        sharedpreferences = getSharedPreferences(CONFIG.MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getBoolean(CONFIG.MyPREFERENCES,false))
        {
            startActivity(new Intent(this,LoggedinActivity.class));
            finish();
        }



        editTextemail = (EditText)findViewById(R.id.loginemail);
        editTextpassword = (EditText)findViewById(R.id.loginpassword);
        progressBar =(ProgressBar)findViewById(R.id.progressBarlogin);
        loginLayout =(RelativeLayout)findViewById(R.id.loginlayout);


        Buttonlogin =(Button)findViewById(R.id.loginbutton);

        Buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginbuyer();
            }
        });



    }

    boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    private void loginbuyer() {


        email = editTextemail.getText().toString().trim();
        password = editTextpassword.getText().toString().trim();

        if(email.equals("") )
        {
            editTextemail.setError("Enter Email");
        }
        else if(isEmailValid(email) == false){
            editTextemail.setError("Enter Valid Email");
        }
        else if (password.equals(""))
        {
            editTextpassword.setError("Enter Password");
        }
        else {
            login(email, password);
        }

    }







    private void login(final String email, String password) {

        class RegisterUser extends AsyncTask<String, Void, String> {



            RequestHandler ruc = new RequestHandler();// Its a Seperate Class file. We make am object of it.

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("email",params[0]);
                data.put("password",params[1]);


                String result = ruc.sendPostRequest(CONFIG.LOGIN_URL,data);

                if(result.trim().equals("1"))
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(CONFIG.sharedprefselleremail,email);
                    editor.putBoolean(CONFIG.sharedprfisloggedin,true);
editor.commit();




                    editor.commit();
                }

                return  result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.INVISIBLE);



                if(s.trim().equals("1"))
                {

                      url_request.getuserdetailsfromemail(email,MainActivity.this);

                   //  System.out.println("Time to login"+jsonObject.toString());
                    startActivity(new Intent(MainActivity.this,LoggedinActivity.class));
                    finish();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(loginLayout, s, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

        }
        //You make an object of RegisterUser  which you defined above as an Async Task.
        //Then you execute it.
        //It goes through preexecute, doin Background and PostExecute in order.

        RegisterUser ru = new RegisterUser();
        ru.execute(email,password);
    }
}



