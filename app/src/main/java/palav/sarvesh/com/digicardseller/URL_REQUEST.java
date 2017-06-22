package palav.sarvesh.com.digicardseller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



/**
 * Created by sarveshpalav on 24/12/16.
 */
public  class URL_REQUEST {

 private    SharedPreferences sharedPreferences;
    public JSONObject jsonObject;
    SharedPreferences sharedpreferences;




    public   JSONObject getuserdetailsfromemail(String email,Context context)
   {
       sharedpreferences = context.getSharedPreferences(CONFIG.MyPREFERENCES, Context.MODE_PRIVATE);

       class getuserdeatilsfromemail extends AsyncTask<String,String,String>

       {





           @Override
           protected String doInBackground(String... params) {

               HashMap<String, String> data = new HashMap<String,String>();
               data.put("email",params[0]);


RequestHandler ruc = new RequestHandler();
               String result = ruc.sendPostRequest(CONFIG.GET_USERINFO,data);
               System.out.println("hello"+result);
               try {
                   jsonObject =new JSONObject(result);
                   JSONArray contacts = jsonObject.getJSONArray("result");
                   System.out.println("Coool"+contacts.toString());

                   for (int i = 0; i < contacts.length(); i++) {

                       JSONObject c = contacts.getJSONObject(i);
            System.out.println("hello"+c.getString("S_id"));
                       String id = c.getString("S_id");
                       System.out.println("sarvesh"+id);
                       String name = c.getString("S_name");
                       String email = c.getString("S_email");
                       String phone = c.getString("S_phone");
                       String cardid = c.getString("Card_id");
                       String cardname = c.getString("Card_name");

                      // System.out.println("sarvesh"+id);
                       SharedPreferences.Editor editor = sharedpreferences.edit();
                       editor.putString(CONFIG.sharedprefsellerid,id);
                       editor.putString(CONFIG.sharedprefsellerphone,phone);
                       editor.putString(CONFIG.sharedprefsellername,name);
                       editor.putString(CONFIG.sharedprefselleremail,email);
                       editor.putString(CONFIG.sharedprefcardid,cardid);
                       editor.putString(CONFIG.sharedprefcardname,cardname);
                       editor.commit();
                   }


               } catch (JSONException e) {
                   e.printStackTrace();
               }
               return result;
           }

           @Override
           protected void onPostExecute(String s) {

               super.onPostExecute(s);



           }
       }


       getuserdeatilsfromemail getuserdeatilsfromemail = new getuserdeatilsfromemail();
       getuserdeatilsfromemail.execute(email);
       return jsonObject;

   }





}
