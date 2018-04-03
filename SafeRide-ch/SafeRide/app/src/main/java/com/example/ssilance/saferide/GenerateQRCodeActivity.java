package com.example.ssilance.saferide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONObject;

/**
 * Created by Sacha on 2018-01-28.
 */

public class GenerateQRCodeActivity extends Activity {
    private ImageView imageView;
    public final static int QRcodeWidth = 500 ;
    private ProgressBar spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));
        imageView = (ImageView)findViewById(R.id.imageView);
        String nameString = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext()).getString("NAME","");
        String addressString = getIntent().getStringExtra("ADDRESS");

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        try {
            JSONObject json = new JSONObject();
            json.put("name", nameString);
            json.put("address", addressString);


            //Bitmap bitmap = TextToImageEncode(json.toString());

            //imageView.setImageBitmap(bitmap);
            new GenerateCodeTask().execute(json.toString());

        } catch (Exception e) {
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

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private class GenerateCodeTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... Value) {
            spinner.setVisibility(View.VISIBLE);
            try {
                return TextToImageEncode(Value[0]);
            } catch (WriterException e) {
                return null;
            }
        }
        protected void onPostExecute(Bitmap result) {
            spinner.setVisibility(View.GONE);
            imageView.setImageBitmap(result);
        }
    }
}


