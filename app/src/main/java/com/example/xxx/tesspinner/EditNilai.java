package com.example.xxx.tesspinner;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditNilai extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nilai);


        String fileNameString = "nimAkhir";
        StringBuffer stringBuffer = new StringBuffer(); //untuk menyusun String yang terdaat pada file

        try {
            //untuk membaca stream yang berasal dari file yang hendak dibaca
            InputStreamReader inputStreamReader = new InputStreamReader(openFileInput(fileNameString));

            //untuk membaca buffer yang berasal dari stream yang diinput sebelumnya
            BufferedReader inputReader = new BufferedReader(inputStreamReader);

            //untuk menyimpan string dari setiap baris buffer yang dibaca
            String inputString;

            while ((inputString = inputReader.readLine()) != null){

                //menyusun string yang berasal dari setiap baris buffer yang telah dibaca sebelumnya, dengan stringBuffer
                stringBuffer.append(inputString+"\n");
            }

            setTitle(stringBuffer);

        }catch (IOException e){

        }


    }
}
