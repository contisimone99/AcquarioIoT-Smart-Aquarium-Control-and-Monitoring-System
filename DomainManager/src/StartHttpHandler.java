import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.concurrent.CompletableFuture;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



public class StartHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		String response = "";

		if (requestMethod.compareTo("GET") == 0 || requestMethod.compareTo("get") == 0) {
			handleGetRequest(httpExchange);
		} else {
			httpExchange.sendResponseHeaders(501, response.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
}

	private void handleGetRequest(HttpExchange httpExchange) throws IOException {
		
		try {
			HttpClient client = HttpClient.newBuilder()
		            .version(HttpClient.Version.HTTP_1_1).build();
			HttpRequest request = HttpRequest.newBuilder()
					  .uri(URI.create("http://localhost:3000/start"))
					  .version(Version.HTTP_1_1)
					 // .header("Content-Type", "text/plain; charset=UTF-8")
					  .POST(HttpRequest.BodyPublishers.ofString("{\"domain\":\"gruppo3\"}"))
					  .build();
			/*CompletableFuture<HttpResponse<String>> res = HttpClient.newBuilder()
					  .build()
					  .sendAsync(request, HttpResponse.BodyHandlers.ofString()); */
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			//HttpResponse response = res.get();
			System.out.println(response.body());
			
		/*
		String httpURL = "http://127.0.0.1:3000/start";
		String body = "{domain: gruppo3}";
		InputStream is;
		InputStreamReader isr;
		BufferedReader br;
		URL myUrl = new URL(httpURL);
		// SSLUtilities.trustAllHttpsCertificates();
		// SSLUtilities.trustAllHostnames();
		HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

		conn.setReadTimeout(7000);
		conn.setConnectTimeout(7000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		conn.connect();
		
		OutputStream outputStream = conn.getOutputStream();
		outputStream.write(body.getBytes("UTF-8"));
		outputStream.close();
		*/

		//String response = "";
		//httpExchange.getResponseHeaders().put("content-type", strlist);
		httpExchange.sendResponseHeaders(response.statusCode(), response.body().toString().length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.body().toString().getBytes());
		os.close();
		
		
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	

		
	}
}
