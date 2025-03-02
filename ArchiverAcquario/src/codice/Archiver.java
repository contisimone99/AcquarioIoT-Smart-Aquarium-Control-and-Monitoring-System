package codice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Archiver {
	public static JSONArray listaEventi;
	
	private static String FileName = "Archiver.json";
	
	public Archiver(JSONArray ev)
	{
		listaEventi = ev;
		
	}
	


	public Archiver() {
		listaEventi = new JSONArray();
	}

	public static Archiver ReadJson()
	{
		String result = "";
		try {
			//System.out.println(System.getProperty("user.dir"));
			BufferedReader br = new BufferedReader(new FileReader(FileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			result = sb.toString();
			if(result.isEmpty()) {
				return new Archiver();
			}
			//System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		JSONObject ob;
		try {
			ob = new JSONObject(result);
			return new Archiver(ob.getJSONArray("listaEventi"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	} 
	
	public void WriteJson(String Attuatore,String Action)
	{

		try {
			FileWriter file = new FileWriter(FileName);
			BufferedWriter wr = new BufferedWriter(file);
			SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'alle' HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			JSONObject ev = new JSONObject();
			ev.put("attuatore", Attuatore);
			ev.put("azione", Action);
			ev.put("timestamp", formatter.format(date));
			listaEventi.put(ev);
			JSONObject archiver = new JSONObject();
			archiver.put("listaEventi", listaEventi);
			wr.write(archiver.toString(1));
			wr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	



}
