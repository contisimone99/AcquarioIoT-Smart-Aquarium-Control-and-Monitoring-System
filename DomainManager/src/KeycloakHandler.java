import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.io.*;
import java.math.BigInteger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
//import javax.net.ssl.SSLUtilities;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class KeycloakHandler {

	public JSONArray getUsersInfo() {

		try {
			HttpClient client = HttpClient.newHttpClient();
		
			//System.out.println(toHexString(getSHA("funziona")));
			String httpURL = "http://localhost:8080/auth/realms/master/protocol/openid-connect/token";
			String body = "username=gruppo3&password=admin&grant_type=password&client_id=admin-cli";
			
			InputStream is;
			InputStreamReader isr;
			BufferedReader br;
			/*URL myUrl = new URL(httpURL);
			// SSLUtilities.trustAllHttpsCertificates();
			// SSLUtilities.trustAllHostnames();
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

			conn.setReadTimeout(7000);
			conn.setConnectTimeout(7000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			*/
			//System.out.println(HttpRequest.BodyPublishers.ofString(body).toString());
			
			HttpRequest request = HttpRequest.newBuilder()
					  .uri(URI.create(httpURL))
					  .header("Content-Type", "application/x-www-form-urlencoded")
					  .POST(BodyPublishers.ofString(body))
					  
					  .build();
			
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			//HttpResponse<String> response = futureResponse.get();
			
			/*OutputStream outputStream = conn.getOutputStream();
			outputStream.write(body.getBytes("UTF-8"));
			outputStream.close();

			String inputLine;
			is = conn.getInputStream();
			*/
			JSONTokener tokener = new JSONTokener(response.body());
			System.out.println(response.body());
			JSONObject tokenObject = new JSONObject(tokener);
			/*
			 * isr = new InputStreamReader(is); br = new BufferedReader(isr); while
			 * ((inputLine = br.readLine()) != null) { System.out.println(inputLine); }
			 * 
			 * br.close();
			 */

			String token = tokenObject.getString("access_token");
			String httpURL2 = "http://localhost:8080/auth/admin/realms/Acquario-auth/users";
			URL myUrl2 = new URL(httpURL2);
			// SSLUtilities.trustAllHttpsCertificates();
			// SSLUtilities.trustAllHostnames();
			HttpRequest request2 = HttpRequest.newBuilder()
					  .uri(URI.create(httpURL2))
					 // .header("Content-Type", "text/plain; charset=UTF-8")
					  .GET()
					  .header("Authorization", "Bearer " + token)
					  .build();
			
			HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
			//CompletableFuture<HttpResponse<String>> futureResponse2 = client.sendAsync(request2, HttpResponse.BodyHandlers.ofString());

			//HttpResponse<String> response2 = futureResponse2.get();
			/*HttpURLConnection conn2 = (HttpURLConnection) myUrl2.openConnection();
			String token = tokenObject.getString("access_token");
			conn2.setRequestProperty("Authorization", "Bearer " + token);

			conn2.setReadTimeout(7000);
			conn2.setConnectTimeout(7000);
			conn2.setRequestMethod("GET");*/
			// conn2.setDoOutput(true);
			// conn2.setDoInput(true);

			ArrayList<String> ids = new ArrayList<String>();
			ArrayList<String> usernames = new ArrayList<String>();

			//InputStream response = conn2.getInputStream();
			try  {
				//String responseBody = scanner.useDelimiter("\\A").next();
				//System.out.println(responseBody);
				String responseBody = response2.body();
				JSONArray jsonArray = new JSONArray(responseBody);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject user = jsonArray.getJSONObject(i);
					//System.out.println("Username: " + user.getString("username"));
					//System.out.println("ID: " + user.getString("id"));
					ids.add(user.getString("id"));
					usernames.add(user.getString("username"));

				}
			} catch (Exception e) {
				System.out.println("In keycloak: "+e.getMessage());
			}

			JSONArray users = new JSONArray();
			int k = 0;
			for (String id : ids) {
				JSONObject user = new JSONObject();
				user.put("username", usernames.get(k));
				user.put("password", toHexString(getSHA("funziona")));				

				String httpURL3 = "http://localhost:8080/auth/admin/realms/Acquario-auth/users/" + id
						+ "/role-mappings/realm";
				URL myUrl3 = new URL(httpURL3);
				HttpRequest request3 = HttpRequest.newBuilder()
						  .uri(URI.create(httpURL3))
						 // .header("Content-Type", "text/plain; charset=UTF-8")
						  .GET()
						  .header("Authorization", "Bearer " + token)
						  .build();
				HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
				//CompletableFuture<HttpResponse<String>> futureResponse3 = client.sendAsync(request3, HttpResponse.BodyHandlers.ofString());

				//HttpResponse<String> response3 = futureResponse3.get();
				// SSLUtilities.trustAllHttpsCertificates();
				// SSLUtilities.trustAllHostnames();
				/*HttpURLConnection conn3 = (HttpURLConnection) myUrl3.openConnection();
				conn3.setRequestProperty("Authorization", "Bearer " + token);

				conn3.setReadTimeout(7000);
				conn3.setConnectTimeout(7000);
				conn3.setRequestMethod("GET");
				InputStream response2 = conn3.getInputStream();
				*/
				try {
					//String responseBody2 = scanner2.useDelimiter("\\A").next();
					String responseBody2 = response3.body();
					System.out.println("Ruoli utente: " + responseBody2);
					JSONArray jsonArray = new JSONArray(responseBody2);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject userRole = jsonArray.getJSONObject(i);
						if(userRole.getString("name").equals("A") || userRole.getString("name").equals("U")) {
							user.put("role", userRole.getString("name"));
							users.put(user);
						}
					}
				} catch (Exception e) {
					System.out.println("In keycloak: "+e.getMessage());
				}
				k++;
              
			}
			
			for(int i=0;i<users.length();i++) {
				JSONObject u = users.getJSONObject(i);
				//System.out.println("Username: "+ u.getString("username"));
				//System.out.println("Role: "+ u.getString("role"));
				//System.out.println("Password: "+ u.getString("password"));
				
			}	
			return users;

		} catch (Exception e) {
			System.out.println("In keycloak: "+e.getMessage());
			return null;
		}

	}
	
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException  
    {  
        /* MessageDigest instance for hashing using SHA256 */  
        MessageDigest md = MessageDigest.getInstance("SHA-256");  
  
        /* digest() method called to calculate message digest of an input and return array of byte */  
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    }
	
	public static String toHexString(byte[] hash)  
    {  
        /* Convert byte array of hash into digest */  
        BigInteger number = new BigInteger(1, hash);  
  
        /* Convert the digest into hex value */  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        /* Pad with leading zeros */  
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString(); 
    }

}
