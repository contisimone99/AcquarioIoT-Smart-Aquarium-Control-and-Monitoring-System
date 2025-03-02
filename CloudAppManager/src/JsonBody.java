import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.util.*;

class JsonBody {
   public String body;
   private byte[] buffer; 
   private JSONObject jbody;
   public JsonBody(InputStream is) {
	try {
	  buffer = new byte[is.available()];
          is.read(buffer);
	}
	catch (Exception e) {
	  e.printStackTrace();
	}
        body = new String(buffer);
	try {
	  jbody = new JSONObject(body);
	}
	catch (JSONException e) {
	  e.printStackTrace();
	}
   }

   public String getDomain() {
	try { 
	   return jbody.getString("domain");
	}
	catch (JSONException e) {
	   e.printStackTrace();
	}
	return "null";
   }

   public void getHosts(ArrayList<String> host) {
	try {
	   JSONArray services = jbody.getJSONArray("services");
	   for(int i=0; i<services.length(); i++) {
		   JSONObject jobj = services.getJSONObject(i);
		   String hh = jobj.getString("host");
		   if(!host.contains(hh)) host.add(hh);

	   }
	}
	catch (JSONException e) {
	   e.printStackTrace();
	}
   }

   public void print() {
	System.out.println(body);
   }
}
