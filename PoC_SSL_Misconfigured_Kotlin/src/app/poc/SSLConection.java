package app.poc;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLConection {

    private static TrustManager[] trustManagers;

 
    public static class FakeX509TrustManager implements X509TrustManager {

        private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //no valida nada
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //no valida nada
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return ACCEPTED_ISSUERS;
        }
    }


    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());

            // Confía en cualquier certificado
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

          

        } catch (Exception e) {
            throw new RuntimeException("Error configurando SSL permisivo", e);
        }
    }

    

    public static void main(String[] args) {
        String urlString = (args.length > 0)
                ? args[0]
                : "https://example.com:8443/";
        String apiKey = "someKey_!base64";
        String urlConParams = urlString + "?key=" + apiKey;


        System.out.println("Usando URL: " + urlConParams);
        System.out.println("⚠ ATENCIÓN: SSL y hostname verification desactivados (solo para pruebas).");

        // Configura el SSL permisivo
        // allowAllSSL();

        try {
            URL url = new URL(urlConParams);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println("HTTP Status: " + status);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String line;
                int lines = 0;
                System.out.println("=== Primeras líneas de la respuesta ===");
                while ((line = in.readLine()) != null && lines < 20) {
                    System.out.println(line);
                    lines++;
                }
            } finally {
                connection.disconnect();
            }

        } catch (IOException e) {
            System.err.println("Error en la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }
}