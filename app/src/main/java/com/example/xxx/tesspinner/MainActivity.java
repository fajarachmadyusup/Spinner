package com.example.xxx.tesspinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerKelas;
    ListView listViewMhs;

    HashMap<String, String> kelas;
    ArrayList<String> listKelas;

    ArrayAdapter<String> adapterKelas;
    ArrayAdapter<String> adapterMhs;

    String[] key;
    String[] value;
    String[] nama;
    String[] nim;

    String kd = "380";
    int images = R.mipmap.ic_person;

    GetData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerKelas = (Spinner) findViewById(R.id.spinnerKelas);
        listViewMhs = (ListView) findViewById(R.id.listViewMhs);

        data = new GetData();

        initSpiner();

        spinnerKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        GetData a = new GetData();
                        a.execute("3801");
                        break;
                    case 1:
                        GetData b = new GetData();
                        b.execute("3802");
                        break;
                    case 2:
                        GetData c = new GetData();
                        c.execute("3803");
                        break;
                    case 3:
                        GetData d = new GetData();
                        d.execute("3804");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        listViewMhs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String fileNameString = "nimAkhir";
                String textDataString = nim[position].substring(6,10)+" "+nama[position];

                try {
                    FileOutputStream fos = openFileOutput(fileNameString, MODE_PRIVATE);
                    fos.write(textDataString.getBytes());
                    fos.close();
                    Toast.makeText(getApplicationContext(), fileNameString+" saved", Toast.LENGTH_SHORT).show();
                }catch (FileNotFoundException e){

                }catch (IOException e){

                }

                Intent intent = new Intent(getBaseContext(), EditNilai.class);
                startActivity(intent);
            }
        });

    }

    public void initSpiner() {
        key = getResources().getStringArray(R.array.key);
        value = getResources().getStringArray(R.array.value);

        kelas = new HashMap<String, String>();
        listKelas = new ArrayList<>();

        /*kelas.put("3801", "D3IF-38-01");
        kelas.put("3802", "D3IF-38-02");
        kelas.put("3803", "D3IF-38-03");
        kelas.put("3804", "D3IF-38-04");*/

        for (int i = 0; i < key.length; i++) {
            kelas.put(key[i], value[i]);
        }

        for (int i = 0; i < kelas.size(); i++) {
            int temp = i;
            String kode = kd + Integer.toString(temp += 1);
            listKelas.add(kelas.get(kode));
            System.out.println(kelas.get(kode));
        }

        adapterKelas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listKelas);
        adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelas.setAdapter(adapterKelas);
    }

    /*public void initListView(String[] nama, String[] nim) {
        CustomAdapter adapter = new CustomAdapter(this, nama, images, nim);

        listViewMhs.setAdapter(adapter);
    }*/


    public class CustomAdapter extends ArrayAdapter<String> {

        Context context;

        int image;

        String[] nama;

        String[] nim;

        public CustomAdapter(Context context, String[] titles, int imgs, String[] desc) {

            super(context, R.layout.single_row, titles);

            this.context = context;

            this.image = imgs;

            this.nama = titles;

            this.nim = desc;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.single_row, parent, false);

            ImageView myimage = (ImageView) row.findViewById(R.id.imageView);

            TextView mytitle = (TextView) row.findViewById(R.id.textView);

            TextView mydesc = (TextView) row.findViewById(R.id.textView2);

            myimage.setImageResource(image);

            mytitle.setText(nama[position]);

            mydesc.setText(nim[position]);

            return row;


        }
    }

    public class GetData extends AsyncTask<String, Void, String> {


        BufferedReader reader;



        @Override
        protected String doInBackground(String... params) {

            String strUrl = "http://dif.indraazimi.com/mhs.php?q="+params[0];
            HttpURLConnection urlConnection = null;
            String raw = null;



            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();//untuk membaca stream/data dari url tadi
                StringBuffer buffer = new StringBuffer();//untuk menyimpan raw json
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                //instansiasi BufferedReader yang sebelumnya telah dibuat
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                raw = buffer.toString();
            } catch (IOException e) {
                Log.e("XXX", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("XXX", "Error closing stream", e);
                    }
                }
            }
            return raw;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray x = new JSONArray(s);
                nama = new String[x.length()];
                nim = new String[x.length()];

                for (int i = 0; i < x.length(); i++) {
                    JSONObject o = x.getJSONObject(i);
                    nama[i] =o.getString("nama");
                    nim[i] =o.getString("nim");
                }
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), nim, images, nama);
                listViewMhs.setAdapter(adapter);
            }catch (JSONException e){

            }

        }
    }
}

