package com.example.brokenmirror;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class RSAKeyManager {

    private static final String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "my_rsa_key";

    // RSA 키 쌍 생성 및 KeyStore에 저장
    public static void generateRSAKeyPairAndStore() {
        try {
            // KeyPairGenerator를 사용하여 RSA 키 쌍 생성
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER_ANDROID_KEYSTORE);

            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setKeySize(2048) // 키 크기 설정
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build();

            keyPairGenerator.initialize(keyGenParameterSpec);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 생성된 키 쌍을 KeyStore에 저장
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            keyStore.load(null);
            // 인증서를 키 스토어에 저장
            Certificate certificate = keyStore.getCertificate(KEY_ALIAS);
            if (certificate == null) {
                throw new IllegalStateException("Failed to retrieve certificate for alias: " + KEY_ALIAS);
            }
            keyStore.setKeyEntry(KEY_ALIAS, keyPair.getPrivate(), null, new Certificate[]{certificate});

            // RSA 키 쌍 출력
            System.out.println("RSA Public Key: " + keyPair.getPublic());
            System.out.println("RSA Private Key: " + keyPair.getPrivate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RSA 공개키를 가져오는 메소드
    public static PublicKey getRSAPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            keyStore.load(null);
            Certificate certificate = keyStore.getCertificate(KEY_ALIAS);
            PublicKey publicKey = certificate.getPublicKey();
            System.out.println("메서드의 RSA Public Key: " + publicKey);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // RSA 개인키를 가져오는 메소드
    public static PrivateKey getRSAPrivateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            keyStore.load(null);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
            System.out.println("메서드의 RSA Private Key: " + privateKey);
            return privateKey;
        } catch (Exception e) {
            System.out.println("메서드의 RSA Private Key 안됨 ");
            e.printStackTrace();
            return null;
        }
    }

}