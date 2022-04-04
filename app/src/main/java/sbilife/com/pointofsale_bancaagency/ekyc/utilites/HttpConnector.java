package sbilife.com.pointofsale_bancaagency.ekyc.utilites;

import android.os.StrictMode;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.utility.Tls12SocketFactory;

public class HttpConnector {
	private static HttpConnector instance = null;
	/*private HttpContext httpContext = null;*/

	private String UserId, Password;

	public static HttpConnector getInstance() {
		if (instance == null)
			instance = new HttpConnector();
		return instance;
	}

	private HttpConnector() {

	}

	//
	// public String postData(String xmlData) throws Exception
	// {
	// SchemeRegistry registry = new SchemeRegistry();
	// KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	// trustStore.load(null, null);
	// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	//
	// HttpParams params = new BasicHttpParams();
	// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	// HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	//
	// // Set the timeout in milliseconds until a connection is established.
	// // The default value is zero, that means the timeout is not used.
	// int timeoutConnection = 50000;
	// HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
	// // Set the default socket timeout (SO_TIMEOUT)
	// // in milliseconds which is the timeout for waiting for data.
	// int timeoutSocket = 500000;
	// HttpConnectionParams.setSoTimeout(params, timeoutSocket);
	//
	// SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
	// socketFactory.setHostnameVerifier((X509HostnameVerifier)
	// SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	// registry.register(new Scheme("http",
	// PlainSocketFactory.getSocketFactory(), 80));
	// registry.register(new Scheme("https", sf, 443));
	// ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
	// registry);
	//
	// DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
	//
	// // Set verifier
	// HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	//
	// //System.out.println("KYCServerURI : " + authServiceURI.toASCIIString());
	// HttpPost httpPost = new HttpPost(Global.Url);
	// BasicHttpEntity filter = new BasicHttpEntity();
	// filter.setContent(new ByteArrayInputStream(xmlData.getBytes()));
	// httpPost.setEntity(filter);
	//
	// HttpResponse response = null;
	//
	// if(httpContext == null)
	// response = httpClient.execute(httpPost);
	// else
	// {
	// response = httpClient.execute(httpPost,httpContext);
	// }
	//
	// HttpEntity entity = response.getEntity();
	// if (entity == null)
	// {
	// throw new Exception("No response from server!");
	// }
	// String responseXML = EntityUtils.toString(entity);
	//
	// System.out.println("RESPONSE1: " + responseXML);
	//
	// return responseXML;
	// //return XMLUtilities.parseXML(respClazz, responseXML);
	// }

	public String postData(String xmlData) throws Exception {

		// SchemeRegistry registry = new SchemeRegistry();
		// KeyStore trustStore =
		// KeyStore.getInstance(KeyStore.getDefaultType());
		// trustStore.load(null, null);
		// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		//
		// HttpParams params = new BasicHttpParams();
		// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		//
		// // Set the timeout in milliseconds until a connection is established.
		// // The default value is zero, that means the timeout is not used.
		// int timeoutConnection = 50000;
		// HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		// // Set the default socket timeout (SO_TIMEOUT)
		// // in milliseconds which is the timeout for waiting for data.
		// int timeoutSocket = 500000;
		// HttpConnectionParams.setSoTimeout(params, timeoutSocket);
		//
		// SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
		// sslcontext.init(null, null, null);
		// com.example.sbiassistedonlinepolicyselling.Tls12SocketFactory
		// tls12SocketFactory = new Tls12SocketFactory(
		// sslcontext.getSocketFactory());
		// // SSLSocketFactory NoSSLv3Factory = new
		// // NoSSLv3SocketFactory(sslcontext.getSocketFactory());
		//
		// HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);
		//
		// // SSLSocketFactory socketFactory =
		// SSLSocketFactory.getSocketFactory();
		// // socketFactory.setHostnameVerifier((X509HostnameVerifier)
		// // SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		// registry.register(new Scheme("http", PlainSocketFactory
		// .getSocketFactory(), 80));
		// registry.register(new Scheme("https", sf, 443));
		// ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
		// registry);

		/*com.example.sbiassistedonlinepolicyselling.Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
				sslcontext.getSocketFactory());
		// SSLSocketFactory NoSSLv3Factory = new
		// NoSSLv3SocketFactory(sslcontext.getSocketFactory());

		HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);*/

		//new CommonMethods().TLSv12Enable();

		//
		/*DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Global.eKYC_PodUrl);
		BasicHttpEntity filter = new BasicHttpEntity();
		filter.setContent(new ByteArrayInputStream(xmlData.getBytes()));
		httpPost.setEntity(filter);

		HttpResponse response = null;

		if (httpContext == null)
			response = httpClient.execute(httpPost);
		else {
			response = httpClient.execute(httpPost, httpContext);
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new Exception("No response from server!");
		}
		String responseXML = EntityUtils.toString(entity);*/

		//nought changes changes

		HttpsURLConnection urlConnection = null;
		String strResponce = "";

		try {
			URL urlObj = new URL(Global.eKYC_PodUrl);

			urlConnection = (HttpsURLConnection) urlObj.openConnection();

			//urlConnection.setSSLSocketFactory(sslsocketfactory);

			SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
			sslcontext.init(null, null, null);
			Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
					sslcontext.getSocketFactory());

			HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);

			OutputStream output = new BufferedOutputStream(urlConnection.getOutputStream());
			output.write(xmlData.getBytes());
			output.flush();

			//urlConnection.connect();

			int responseCode = urlConnection.getResponseCode();

			if(responseCode == HttpURLConnection.HTTP_OK){

				BufferedReader reader = null;
				StringBuffer response = new StringBuffer();
				try {
					reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					String line = "";
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					strResponce = response.toString();

				} catch (IOException e) {
					e.printStackTrace();
					strResponce = "";
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
							strResponce = "";
						}
					}
				}

				Log.v("CatalogClient", strResponce);
			}

		} catch (Exception e) {
			e.printStackTrace();
			strResponce = "";
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return strResponce;
	}

	public String eKYCWrapper(String str_kycXML) {
		try {
			String SOAP_ACTION_eKYC = "http://tempuri.org/eKYC";
			String METHOD_NAME_eKYC = "eKYC";
			String NAMESPACE = "http://tempuri.org/";
			String str_eKYC_Status = "";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_eKYC);

			request.addProperty("xmlObject", str_kycXML);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			new MarshalBase64().register(envelope); // serialization

			envelope.setOutputSoapObject(request);

			allowAllSSL();

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpTransportSE androidHttpTranport = new HttpTransportSE(
					ServiceURL.SERVICE_URL, 100000);

			androidHttpTranport.call(SOAP_ACTION_eKYC, envelope);
			Object response = envelope.getResponse();
			str_eKYC_Status = response.toString();

			return str_eKYC_Status;
		} catch (Exception e) {
			e.printStackTrace();
			return "Server not responding,Please try again";
		}

	}

	public void allowAllSSL() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");

			sslcontext.init(null, null, null);
			/*Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
					sslcontext.getSocketFactory());
			// SSLSocketFactory NoSSLv3Factory = new
			// NoSSLv3SocketFactory(sslcontext.getSocketFactory());

			HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);*/

			new CommonMethods().TLSv12Enable();

		} catch (Exception e) {
			Log.e("allowAllSSL", e.toString());
		}

	}
}
