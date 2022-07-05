package com.consiti.wsdl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConvertWSDL {
    public static void main(String[] args) throws Exception {
        TrustManager[] trustAllCerts = { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        } };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception: " + e.getMessage());
        }
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        InputStream fis = new FileInputStream("/home/integra/h2h/certificado/soabus.cer");
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        while (bis.available() > 0) {
            Certificate cert = cf.generateCertificate(bis);
            trustStore.setCertificateEntry("fiddler" + bis.available(), cert);
        }
        try {
            String rutaGuardado = args[1];
            URL ficheroUrl = new URL(args[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(ficheroUrl.openStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                System.err.println("--> Se encontraron datos URL: " + ficheroUrl);
                File archivo = new File(rutaGuardado);
                FileWriter escribir = new FileWriter(archivo, true);
                escribir.write(linea);
                escribir.close();
                System.out.println("--> Datos guardados en: " + rutaGuardado);
            }
            System.out.println("--> Cerrando conexion a archivo.");
            in.close();
        } catch (IOException i) {
            System.out.println("IOException: " + i.getMessage());
        }
    }
}
