package controllo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import log.Log;

public class Persistenza{
	
	
	public SubControlloCallback subcall;
	public static String FileName = "/home/matt/Desktop/persistenza.json";

	public Persistenza( SubControlloCallback subcall)
	{
		this.subcall=subcall;
		
	}
	
	private String ReadJson()
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
				return null;
			}
			//System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	public void WriteJson(int light, ArrayList<Double> valoriPS, double[] rangeTemp)
	{

		try {
			FileWriter file = new FileWriter(FileName);
			BufferedWriter wr = new BufferedWriter(file);
			JSONObject obj = new JSONObject();
			//JSONArray jps = new JSONArray();
			//jps.put("ValoriPS: "+ subcall.valoriPS)
			obj.put("ValoriPS", valoriPS);
			obj.put("light", light);
			obj.put("rangeTemp", rangeTemp);
			wr.write(obj.toString());
			wr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setup()
	{
		String data = ReadJson();
		if(data==null) return;
		try {
			JSONObject object = new JSONObject(data);
			JSONArray ja = object.getJSONArray("rangeTemp");
			ArrayList<Double> tmp = new ArrayList<Double>();
			for(int i=0;i<ja.length();i++)
			{
				tmp.add(Double.valueOf(ja.get(i).toString()));
			}
			double[] arr = tmp.stream().mapToDouble(d -> d).toArray();
			JSONArray ja1 = object.getJSONArray("ValoriPS");
			ArrayList<Double> tmp1 = new ArrayList<Double>();
			for(int i=0;i<ja1.length();i++)
			{
				tmp1.add(Double.valueOf(ja1.get(i).toString()));
			}
			this.subcall.setValoriPS(tmp1);
			this.subcall.setLight(object.getInt("light"));
			this.subcall.pc.ST.setRangeTemp(arr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
