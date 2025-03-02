import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import com.sun.net.httpserver.*;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import com.sun.net.httpserver.HttpServer;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;



public class RestHttpServer {
	
	private static JDBCDriver jdbc;
    private static KeycloakHandler keycloak;
    
    static public int port = 5000;

	public static void main(String[] args) throws IOException {
		if (args.length > 1 && args[0].equals("-port"))
			try {
				port = Integer.parseInt(args[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
	
		try {
			/*
			SSLContext sslContext = SSLContext.getInstance("TLS");

			// initialise the keystore
			char[] password = "miapasswd".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");
			FileInputStream fis = new FileInputStream("lig.keystore");
			ks.load(fis, password);

			// setup the key manager factory
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, password);

			// setup the trust manager factory
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);

			// setup the HTTPS context and parameters
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
				public void configure(HttpsParameters params) {
					try {
						// initialise the SSL context
						SSLContext context = getSSLContext();
						SSLEngine engine = context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());

						// Set the SSL parameters
						SSLParameters sslParameters = context.getSupportedSSLParameters();
						params.setSSLParameters(sslParameters);

					} catch (Exception ex) {
						System.out.println("Failed to create HTTPS port");
					}
				}
			});
			*/

			server.createContext("/apirest/gruppo3", new ApiRestHttpHandler());
			server.createContext("/install", new InstallHttpHandler());
			server.createContext("/start", new StartHttpHandler());
			server.createContext("/stop", new StopHttpHandler());
			server.createContext("/delete", new DeleteHttpHandler());
			server.setExecutor(threadPoolExecutor);
			server.start();
			System.out.println("Server started on port " + port);
		} catch (Exception exception) {
			System.out.println("Failed to create HTTPS server on port " + port + " of localhost");
			exception.printStackTrace();
		}

}
}

    
