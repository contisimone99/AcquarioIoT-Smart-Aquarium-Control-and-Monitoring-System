import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StopHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		String response = "";

		if (requestMethod.compareTo("GET") == 0 || requestMethod.compareTo("get") == 0) {
			try {
				handlePostRequest(httpExchange);
			} catch (IOException | InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			httpExchange.sendResponseHeaders(501, response.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
}

	private void handlePostRequest(HttpExchange httpExchange) throws IOException, InterruptedException, ExecutionException {
		String httpURL = "http://localhost:3000/stop";
		String body = "{\"domain\":\"gruppo3\"}";
		HttpClient client = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_1_1).build();
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create(httpURL))
				 // .header("Content-Type", "text/plain; charset=UTF-8")
				  .POST(HttpRequest.BodyPublishers.ofString("{\"domain\":\"gruppo3\"}"))
				  .build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		/*InputStream is;
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

		OutputStream outputStream = conn.getOutputStream();
		outputStream.write(body.getBytes("UTF-8"));
		outputStream.close();
*/
		//String response = "";
		//httpExchange.getResponseHeaders().put("content-type", strlist);
		httpExchange.sendResponseHeaders(response.statusCode(), response.body().length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.body().getBytes());
		os.close();

		
	}
}
