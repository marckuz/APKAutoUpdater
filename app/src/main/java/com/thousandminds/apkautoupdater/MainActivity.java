package com.thousandminds.apkautoupdater;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dlAPK task = new dlAPK();
        task.execute(new String[] { "http://192.168.2.17:8080/com.scdroid.apdusender.5.apk" });
    }

    private class dlAPK extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String path = Environment.getExternalStorageDirectory()+"/com.scdroid.apdusender.5.apk";

            //download the apk from your server and save to sdk card here
            try{
                URL url = new URL(urls[0]);
                Log.d("YES","urls[0] = "+urls[0]);

                URLConnection connection = url.openConnection();
                connection.connect();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1)
                {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                Log.d("YES","YES");
            }catch(Exception ignored){
                Log.e("WHAT",ignored.getLocalizedMessage());
                Log.e("WHAT",ignored.getMessage());
                Log.e("WHAT", String.valueOf(ignored.getCause()));
                Log.e("WHAT", String.valueOf(ignored.getStackTrace()));
            }
            Log.d("YES","path = "+path);
            return path;
        }

        @Override
        protected void onPostExecute(String result) {
            Process process = null;

            File file2 = new File(result);
            if(file2.exists()){
                Log.d("MARK","yes");
            }else{
                Log.d("MARK","no");
            }


            //with prompt install
            File file = new File(Environment.getExternalStorageDirectory(), "com.scdroid.apdusender.5.apk");
            file.setReadable(true, false);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);


            //for rooted device
            // call to superuser command, pipe install updated APK without writing over files/DB
//            try
//            {
//
//                process = Runtime.getRuntime().exec("su");
//                DataOutputStream outs = new DataOutputStream(process.getOutputStream());
//
//                String cmd = "pm install -r "+result;
//
//                outs.writeBytes(cmd+"\\n");
//            }catch (IOException e) {
//                Log.e("WHAT","NO! onPostExecute e = "+e.getMessage());
//            }
        }
    }
}
