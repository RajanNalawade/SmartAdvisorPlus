package sbilife.com.pointofsale_bancaagency.new_bussiness.pivc;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStoreHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContextHC4;
import org.apache.http.util.EntityUtilsHC4;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Prabu on 8/6/2016.
 */
public class HttpConnector1 {
    private static HttpConnector1 instance = null;
    private static InputStream serverCert;

    private static SSLContext sslContext = null;
    private static SSLConnectionSocketFactory sslsf = null;
    private static RequestConfig config = null;
    private static X509HostnameVerifier hostnameVerifier = null;

    private static BasicHttpContextHC4 httpContext = null;

    public static HttpConnector1 getInstance() {
        if (instance == null)
            instance = new HttpConnector1();
        return instance;
    }

    public static void setServerCert(InputStream serverCrt) {

        serverCert = serverCrt;

        try {
            if (serverCert != null) {
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = new BufferedInputStream(serverCert);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                } finally {
                    caInput.close();
                }

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, tmf.getTrustManagers(), null);

                // Create an HostnameVerifier that hardwires the expected hostname.
                // Note that is different than the URL's hostname:
                // example.com versus example.org
                hostnameVerifier = new X509HostnameVerifier() {
                    public void verify(String string, SSLSocket ssls) throws IOException {
                    }

                    public void verify(String string, X509Certificate xc) throws SSLException {
                    }

                    public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                    }

                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                };
            } else {
                System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

                sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, new TrustStrategy() {

                            @Override
                            public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                                return true;
                            }
                        })
                        .useTLS()
                        .build();
            }
            sslsf = new SSLConnectionSocketFactory(
                    sslContext,
                    //new String[] { "TLSv1","TLSv1.1", "TLSv1.2","SSLv3" },
                    new String[]{"TLSv1.2"},
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
            );

            config = RequestConfig.custom()
                    .setSocketTimeout(10 * 10000)
                    .setConnectTimeout(10 * 10000)
                    .setConnectionRequestTimeout(10 * 10000)
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HttpConnector1() {
        BasicCookieStoreHC4 cookieStore = new BasicCookieStoreHC4();
        httpContext = new BasicHttpContextHC4();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    /* public String postData(final String url, final String xmlData) throws Exception {

          HttpEntity entity = null;
          CloseableHttpClient httpclient = null;
          CloseableHttpResponse httpres = null;

          try {
              HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config);

              if (hostnameVerifier != null)
                  builder = builder.setHostnameVerifier(hostnameVerifier);

              httpclient = builder.build();

              HttpPostHC4 httpost = new HttpPostHC4(url);
              httpost.setHeader("x-ibm-client-id",
                      "636d99cd-8d7a-4e0c-a088-9afc7a02b2b7");
              httpost.setHeader("x-ibm-client-secret",
                      "M7oX5kT0cP6bG1eB8vW8tR8hN0wG5cQ6hV1vH6wA4yW4kX5jR0");
              httpost.setHeader("content-type", "application/json");
              httpost.setHeader("accept", "application/json");
             // String encodedXmlData = URLEncoder.encode(xmlData, "UTF-8");
              httpost.setEntity(new StringEntityHC4(xmlData));

              httpres = httpclient.execute(httpost, httpContext);
              entity = httpres.getEntity();

              if (entity == null) {
                  throw new Exception("No response from Server");
              }

              return EntityUtilsHC4.toString(entity, "UTF-8");
          } finally {
              if (entity != null) {
                  try {
                      entity.getContent().close();
                  } catch (Exception ex) {
                      System.out.println("Exception in entity.getContent().close()");
                  }

                  try {
                      EntityUtilsHC4.consume(entity); // Close the connection
                  } catch (Exception ex) {
                      System.out.println("EntityUtils.consume(entity)");
                  }
              }

              if (httpres != null) {
                  try {
                      httpres.close();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }

              try {
                  httpclient.close();
              } catch (Exception ex) {
              }
          }
      }*/
    public String postData(final String url, final String xmlData) throws Exception {

        HttpEntity entity = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpres = null;

        try {
            HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config);

            if (hostnameVerifier != null)
                builder = builder.setHostnameVerifier(hostnameVerifier);

            httpclient = builder.build();

            HttpPostHC4 httpost = new HttpPostHC4(url);

            /*UAT*/
            // httpost.addHeader("x-ibm-client-id", "636d99cd-8d7a-4e0c-a088-9afc7a02b2b7");
            // httpost.addHeader("x-ibm-client-secret", "M7oX5kT0cP6bG1eB8vW8tR8hN0wG5cQ6hV1vH6wA4yW4kX5jR0");
            /*UAT*/

            /*PROD*/
            httpost.addHeader("x-ibm-client-id", "882b28ed-b80b-4ee3-9fca-5b581048ca65");
            httpost.addHeader("x-ibm-client-secret", "A6eQ5gM5eS3dK4nN6vI0nN0sJ1dF3xM0iO2bP1kM7lE0xK0nY2");
            /*PROD*/


            httpost.addHeader("content-type", "application/json");
            httpost.setHeader("accept", "application/json");
            StringEntity entity1 = new StringEntity(xmlData);
            httpost.setEntity(entity1);
            // String encodedXmlData = URLEncoder.encode(xmlData, "UTF-8");
           /* BasicHttpEntity filter = new BasicHttpEntity();
            filter.setContent(new ByteArrayInputStream(xmlData.getBytes()));
            httpost.setEntity(filter);*/
            // httpost.setEntity(new StringEntityHC4(xmlData));
            //AppLogger.WriteIntoFile(xmlData);

            httpres = httpclient.execute(httpost, httpContext);
            entity = httpres.getEntity();

            if (entity == null) {
                throw new Exception("No response from Server");
            }

            return EntityUtilsHC4.toString(entity, "UTF-8");
        } finally {
            if (entity != null) {
                try {
                    entity.getContent().close();
                } catch (Exception ex) {
                    System.out.println("Exception in entity.getContent().close()");
                }

                try {
                    EntityUtilsHC4.consume(entity); // Close the connection
                } catch (Exception ex) {
                    System.out.println("EntityUtils.consume(entity)");
                }
            }

            if (httpres != null) {
                try {
                    httpres.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                httpclient.close();
            } catch (Exception ex) {
            }
        }
    }


    public String postData_PIVCGETLINK(final String url, String Proposal_Number, String RequestData) throws Exception {

      /*  HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config);

        if (hostnameVerifier != null)
            builder = builder.setHostnameVerifier(hostnameVerifier);
        httpclient = builder.build();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_proposal_no\"\r\n\r\n35YA036723\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_source\"\r\n\r\nPOS\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_data\"\r\n\r\n<NewDataSet><Table><APP_SOURCE>POS</APP_SOURCE>  <PROPOSAL_NUMBER>35YA036723</PROPOSAL_NUMBER> <GENDER>M</GENDER> <CUSTOMER_NAME>Mr. SHANKAR THAKUR </CUSTOMER_NAME> <MOBILE_NUMBER>9769765613</MOBILE_NUMBER> <DOB_PH>12-Oct-1985</DOB_PH> <SUM_ASSURED>250000</SUM_ASSURED> <FREQUENCY>annuelle</FREQUENCY> <PAYMENT_TERM>15</PAYMENT_TERM> <BENEFIT_TERM>15</BENEFIT_TERM> <PRODUCT>Shubh Nivesh</PRODUCT> <PRODUCT_CATEGORY>TRADITION</PRODUCT_CATEGORY> <PLAN>Endowment with Whole Life Option</PLAN> <RIDER2_SA>250000</RIDER2_SA> <RIDER2_TERM>15</RIDER2_TERM> <MAILINGADDRESS1>GANGA RAM THAKUR </MAILINGADDRESS1> <MAILINGADDRESS2>VILL-TILOBADAR,PO-KURMICHAK</MAILINGADDRESS2> <MAILINGADDRESS3>DIST-GODDA</MAILINGADDRESS3> <MAILINGCITY>GODDA</MAILINGCITY> <MAILINGPINCODE>814156</MAILINGPINCODE> <MAILINGSTATE>JHARKHAND</MAILINGSTATE> <PERMANENTADDRESS1>GANGA RAM THAKUR </PERMANENTADDRESS1> <PERMANENTADDRESS2>VILL-TILOBADAR,PO-KURMICHAK</PERMANENTADDRESS2> <PERMANENTADDRESS3>DIST-GODDA</PERMANENTADDRESS3> <PERMANENTCITY>GODDA</PERMANENTCITY> <PERMANENTPINCODE>814156</PERMANENTPINCODE> <PERMANENTSTATE>JHARKHAND</PERMANENTSTATE> <EMAIL>tshankar081@gmail.com</EMAIL> <NOMINEE_NAME>Mrs. KSHMA DEVI</NOMINEE_NAME> <NOMINEE_RELATION>Wife</NOMINEE_RELATION> <PREFERED_LANG>Hindi</PREFERED_LANG> <PROPOSER_OCCUPATION>Service</PROPOSER_OCCUPATION> </Table> </NewDataSet>\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("https://pivc.sbilife.co.in/dev/portal/api/kfd/getPosPIVCLink")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("client_key", "ed0b5a98fb49499c76002690")
                .addHeader("client_name", "sbilpo")
                .addHeader("'content-type", "multipart/form-data")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "8b38fd1a-fe9e-b22b-8c81-5ea143fbf275")
                .build();

        Response response = client.newCall(request).execute();

        return response.toString();
*/


        HttpEntity entity = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpres = null;

        try {
            HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config);

            if (hostnameVerifier != null)
                builder = builder.setHostnameVerifier(hostnameVerifier);

            httpclient = builder.build();

            HttpPostHC4 httpost = new HttpPostHC4(url);


            /*For Prod*/
            httpost.addHeader("client_key", "ed0b5a98fb49499c76002690");
            httpost.addHeader("client_name", "sbilpo");
            httpost.addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            httpost.addHeader("content-type", "multipart/form-data");

            //For UAT
            /*httpost.addHeader("client_key", "1d9b3b70a84b2db7173e9628");
            httpost.addHeader("client_name", "sbilpivc");
            httpost.addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            httpost.addHeader("content-type", "multipart/form-data");*/

            StringEntity entity1 = new StringEntity(RequestData);
            httpost.setEntity(entity1);
            // String encodedXmlData = URLEncoder.encode(xmlData, "UTF-8");
           /* BasicHttpEntity filter = new BasicHttpEntity();
            filter.setContent(new ByteArrayInputStream(xmlData.getBytes()));
            httpost.setEntity(filter);*/
            // httpost.setEntity(new StringEntityHC4(xmlData));
            //AppLogger.WriteIntoFile(xmlData);

            httpres = httpclient.execute(httpost, httpContext);
            entity = httpres.getEntity();

            if (entity == null) {
                throw new Exception("No response from Server");
            }

            return EntityUtilsHC4.toString(entity, "UTF-8");
        } finally {
            if (entity != null) {
                try {
                    entity.getContent().close();
                } catch (Exception ex) {
                    System.out.println("Exception in entity.getContent().close()");
                }

                try {
                    EntityUtilsHC4.consume(entity); // Close the connection
                } catch (Exception ex) {
                    System.out.println("EntityUtils.consume(entity)");
                }
            }

            if (httpres != null) {
                try {
                    httpres.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                httpclient.close();
            } catch (Exception ex) {
            }
        }
    }
    /*  public String postData(String xmlData, String json_obj) throws Exception {


     *//*  SSLContext  = SSLContext.getInstance("TLSv1.2");
        sslcontext.init(null, null, null);
        com.example.sbiassistedonlinepolicyselling.Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
                sslcontext.getSocketFactory());
        // SSLSocketFactory NoSSLv3Factory = new
        // NoSSLv3SocketFactory(sslcontext.getSocketFactory());

        HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);
*//*
        //
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //
        // // Set verifier
        // HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // System.out.println("KYCServerURI : " +
        // authServiceURI.toASCIIString());
        HttpPost httpPost = new HttpPost("https://apione.sbilife.co.in/sbilife/uat/esb/Js/productcombo-v1/getDetails");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json_obj.toString());
        httpPost.addHeader("x-ibm-client-id", "636d99cd-8d7a-4e0c-a088-9afc7a02b2b7");
        httpPost.addHeader("x-ibm-client-secret", "M7oX5kT0cP6bG1eB8vW8tR8hN0wG5cQ6hV1vH6wA4yW4kX5jR0");
        httpPost.addHeader("content-type", "application/json");
        BasicHttpEntity filter = new BasicHttpEntity();
        filter.setContent(new ByteArrayInputStream(json_obj.getBytes()));
        httpPost.setEntity(filter);
        HttpResponse response = null;

        *//*if (httpContext == null)
            response = httpClient.execute(httpPost);
        else {*//*
            response = httpClient.execute(httpPost, httpContext);
      //  }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new Exception("No response from server!");
        }
        String responseXML = EntityUtils.toString(entity);

        System.out.println("RESPONSE1: " + responseXML);

        return responseXML;
        // return XMLUtilities.parseXML(respClazz, responseXML);

        // return XMLUtilities.parseXML(respClazz, responseXML);
    }*/


   /* public void allowAllSSL() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");

            sslcontext.init(null, null, null);
            Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
                    sslcontext.getSocketFactory());
            // SSLSocketFactory NoSSLv3Factory = new
            // NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);

        } catch (Exception e) {
            Log.e("allowAllSSL", e.toString());
        }

    }*/
}
