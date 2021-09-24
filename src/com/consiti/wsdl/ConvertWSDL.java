package com.consiti.wsdl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public class ConvertWSDL {

    public static void main(String[] args) throws IOException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception: "+e.getMessage());
        }
// Now you can access an https URL without having the certificate in the truststore
        try {
            // URL url = new URL("https://hostname/index.html");
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
        }catch(IOException i){
            System.out.println("IOException: "+i.getMessage());
        }
    }
}
