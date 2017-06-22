package palav.sarvesh.com.digicardseller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sarveshpalav on 24/12/16.
 */
public class QRPage extends AppCompatActivity {

ImageView qr;
    public final static int QRcodeWidth = 700 ;
    Bitmap bitmap ;

    public  String otp;
    Timer t =new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qr = (ImageView)findViewById(R.id.qr);

      otp =   getIntent().getExtras().getString("otp");
        Toast.makeText(QRPage.this,otp,Toast.LENGTH_LONG).show();

QRCodeGenerator(otp);

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
checkforstatus(otp);
            }
        }, 0, 5000);


    }



    private void deletetransaction(final String otp)
    {
        class deletetransaction extends AsyncTask<String,String,String>
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                System.out.println("saru"+s);

                if(s.trim().equals("1"))
                {

                     t.cancel();
                    startActivity(new Intent(QRPage.this,SucessTransaction.class));
                     finish();
                }
                else{
                    Toast.makeText(QRPage.this,"Transaction Faied",Toast.LENGTH_LONG).show();
                }
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("otp",otp);

                RequestHandler ruc = new RequestHandler();
                String result =    ruc.sendPostRequest(CONFIG.DELETE,data);

                return result;
            }
        }
        deletetransaction deletetransaction = new deletetransaction();
        deletetransaction.execute(otp);
    }


    private void checkforstatus(final String otp)
    {
        class checkforstatus extends AsyncTask<String,String,String>
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s.trim().equals("1"))
                {
                    deletetransaction(otp);

                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("otp",otp);

                RequestHandler ruc = new RequestHandler();
                String result =    ruc.sendPostRequest(CONFIG.STATUS,data);

                return result;
            }
        }
        checkforstatus checkforstatus = new checkforstatus();
        checkforstatus.execute(otp);

    }

    private void QRCodeGenerator(String x){

        try {
            bitmap = TextToImageEncode(x); //Im not sure if x should be int or string. Lets try.

            qr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                //I added android to R.color.black, since it wasn't working

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(android.R.color.black):getResources().getColor(android.R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 700, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
