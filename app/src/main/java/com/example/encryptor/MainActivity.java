package com.example.encryptor;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText inputText, inputpassword;
    TextView outputText;
    Button encbtn,decbtn;
    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText) findViewById(R.id.enctext);
        inputpassword = (EditText) findViewById(R.id.password);

        outputText = (TextView) findViewById(R.id.outputtext);
        encbtn = (Button) findViewById(R.id.encryptbtn);
        decbtn = (Button) findViewById(R.id.decryptbtn);
        encbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputString = encrypt(inputText.getText().toString(),inputpassword.getText().toString());
                    outputText.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        decbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputString = decrypt(outputString,inputpassword.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
                outputText.setText(outputString);

            }
        });
    }
    private String decrypt(String outputString,String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance((AES));
        c.init(Cipher.DECRYPT_MODE,key);
        byte [] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private  String encrypt(String Data,String password) throws  Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance((AES));
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encval = c.doFinal(Data.getBytes());
        String encryptesValue = Base64.encodeToString(encval, Base64.DEFAULT);
        return encryptesValue;
    }
    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
}