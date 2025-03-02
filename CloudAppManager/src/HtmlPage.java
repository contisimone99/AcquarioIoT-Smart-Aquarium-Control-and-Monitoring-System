import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpPrincipal;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URLDecoder;

public class HtmlPage implements HttpHandler {
  
  private  String confdir;

  public HtmlPage(String conf) {
      confdir = conf;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
      URI requestURI = exchange.getRequestURI();
      String response;
      //printRequestInfo(exchange);
      List<String> strlist = new ArrayList<String>();

      String requestMethod = exchange.getRequestMethod();
      String infopage = confdir+"/apirest.json";
      strlist.add("text/html");

      if(requestMethod.compareTo("GET")==0||requestMethod.compareTo("get")==0) {
        response = getLocalPage(infopage);
        exchange.getResponseHeaders().put("content-type",strlist);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
	os.close();
     }
     else {
        System.out.println("Operation not supported!");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.close();
     }
  }
 
  private static String getLocalPage(String pageid) {
       String line;

       String answer = "";
       String response;
       try {
          FileReader fileReader = new FileReader(pageid);

          BufferedReader bufferedReader = new BufferedReader(fileReader);

          while((line = bufferedReader.readLine()) != null) {
                answer += line;
          }
          bufferedReader.close();
       }
       catch(FileNotFoundException ex) {
          System.out.println( "Unable to open file '" + pageid + "'");
          return "fail";
       }
       catch(IOException ex) {
          System.out.println( "Error reading file '" + pageid + "'");
          return "fail";
       }
       return answer;
  }

  private static String getExtension(String file) {
     int i=file.length()-1;
     while(i>0 && file.charAt(i) != '.' && file.charAt(i) != '/') i--;
     if(file.charAt(i) == '.') return file.substring(i+1);
     else return "";
  }

}
