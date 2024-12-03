package com.example.brokenmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class androidTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_test);

        TextView ssaidText = findViewById(R.id.ssaid_text);
        TextView rsaPublicKey = findViewById(R.id.RSA_public);
        TextView rsaPrivateKey = findViewById(R.id.RSA_private);

        //RSA
//        try {
//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//            SecureRandom random = new SecureRandom();
//            keyGen.initialize(2048, random);
//            KeyPair pair = keyGen.generateKeyPair();
//            PublicKey publicKey = pair.getPublic();
//            PrivateKey privateKey = pair.getPrivate();
//
//            String publicKeyString = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
//            String privateKeyString = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
//
//            rsaPublicKey.setText(" \uD83D\uDD11 Public Key: " + publicKeyString);
//            rsaPrivateKey.setText(" \uD83D\uDD11 Private Key: " + privateKeyString);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        RSAKeyManager.generateRSAKeyPairAndStore();

        PublicKey publicKey = RSAKeyManager.getRSAPublicKey();
        PrivateKey privateKey = RSAKeyManager.getRSAPrivateKey();

        if (publicKey != null) {
            String publicKeyString = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
            rsaPublicKey.setText(" \uD83D\uDD11 Public Key: " + publicKeyString);
            System.out.println("RSA Public Key 테스트: " + publicKeyString);
        } else {
            rsaPublicKey.setText("Failed to retrieve public key");
        }

        if (privateKey != null) {
            byte[] privateKeyBytes = privateKey.getEncoded();
            if (privateKeyBytes != null) {
                String privateKeyString = new String(privateKeyBytes);
                rsaPrivateKey.setText(" \uD83D\uDD11 Private Key: " + privateKeyString);
                System.out.println("RSA Private Key 테스트: " + privateKeyString);
            } else {
                rsaPrivateKey.setText("Failed to retrieve private key1");
            }
        } else {
            rsaPrivateKey.setText("Failed to retrieve private key2");
        }


        //SSAID
        String ssaid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ssaidText.setText(": " + ssaid);

    }

}