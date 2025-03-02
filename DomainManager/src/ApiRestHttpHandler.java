import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.concurrent.ExecutionException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiRestHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		String response = "";

		if (requestMethod.compareTo("GET") == 0 || requestMethod.compareTo("get") == 0) {
			try {
				handleGetRequest(httpExchange);
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

	private void handleGetRequest(HttpExchange httpExchange) throws IOException, InterruptedException, ExecutionException {
		String httpURL = "http://localhost:3000/apirest";
		HttpClient client = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_1_1).build();
		HttpRequest request = HttpRequest.newBuilder()
				  .version(Version.HTTP_1_1)
				  .uri(URI.create(httpURL))
				 // .header("Content-Type", "text/plain; charset=UTF-8")
				  .GET()
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
