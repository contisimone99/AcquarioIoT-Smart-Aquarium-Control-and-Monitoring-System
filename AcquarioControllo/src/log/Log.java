package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Log {
	private ArrayList<String> logTopic = new ArrayList<String>();
	private ArrayList<String> logAction = new ArrayList<String>();
	private static String FileName="/home/matt/Desktop/log.json";

	public Log()
	{
		logTopic = new ArrayList<String>();
		logAction = new ArrayList<String>();
	}
	
	
	public Log(ArrayList<String> Topic, ArrayList<String> Action)
	{
		this.logTopic=Topic!=null?Topic:new ArrayList<String>();
		this.logAction=Action!=null?Action:new ArrayList<String>();
	}
	
	
	public static Log ReadJson()
	{
		String result = "";
		try {
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
				return new Log();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		Log ar = gson.fromJson(result,Log.class);
		return ar;
		
	} 
	
	
	public void WriteJson(String Topic, String Messaggio, String Action, String Attuatore)
	{

		try {
			FileWriter file = new FileWriter(FileName);
			BufferedWriter wr = new BufferedWriter(file);
			SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'alle' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());
			this.logTopic.add(formatter.format(date) + " - Scritto messaggio: " + Messaggio + " sul topic " + Topic + "\n");
			this.logAction.add(formatter.format(date) + " - Modificato stato dell'attuatore: " + Attuatore + " in: " + Action + "\n");
			Gson gson = new GsonBuilder().create();
			String result = gson.toJson(this);
			wr.write(result);
			wr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
