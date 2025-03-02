package controllo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import sensori.Alimentatori;

public class SubControlloCallback implements MqttCallback {

	ArrayList<Double> valoriPS = new ArrayList<Double>();

	public boolean idempotenza = true;
	public boolean idempotenza_stop = false;
	private boolean start = false;
	private int light = 300;
	Alimentatori al;
	PubControllo pc;
	Persistenza per;
	Thread pubClient;
	Thread monitor;
	Thread publisher;
	Thread alimentatore;
	Thread persistenza;

	@Override
	public void connectionLost(Throwable cause) {
		;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		System.out.println("--- CONTROLLO --- Message arrived. Topic: " + topic + "  Message: " + message.toString());
		String arg[] = topic.split("/");
		if (!start) {
			pc = new PubControllo(this);
			publisher = new Thread(pc);
			publisher.start();
			start = true;
			al = new Alimentatori();
			alimentatore = new Thread(al);
			alimentatore.start();
			per = new Persistenza(this);
			per.setup();
			System.out.println("Sistema On");

		}

		if (arg[arg.length - 1].equals("L1")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {

				String p = array[1];
				p = p.substring(0, p.length() - 1);
				pc.setActuator("{cmd:"+p.trim()+"}", "2");
			}
		}

		if (arg[arg.length - 1].equals("Risc1")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {

				String p = array[1];
				p = p.substring(0, p.length() - 1);
				pc.setActuator("{cmd:"+p.trim()+"}", "1");
			}
		}
		
		if (arg[arg.length - 1].equals("Raff1")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {

				String p = array[1];
				p = p.substring(0, p.length() - 1);
				pc.setActuator("{cmd:"+p.trim()+"}", "0");
			}
		}
		
		if (arg[arg.length - 1].equals("Al1")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {

				String p = array[1];
				p = p.substring(0, p.length() - 1);
				pc.setActuator("{cmd:"+p.trim()+"}","3");
			}
		}

		
		
		
		if (arg[arg.length - 1].equals("TempOtt")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("status/event")) {
				String p = array[1];
				p = p.substring(0, p.length() - 1);
				String[] paz = p.replace("[", "").replace("]", "").replace("\"", "").split(",");
				double[] tval = Arrays.stream(paz).mapToDouble(Double::parseDouble).toArray();
				pc.ST.setRangeTemp(tval);
				per.WriteJson(light, valoriPS, pc.ST.getRangeTemp());
			}
		}

		if ((arg[arg.length - 1].equals("T0")) || (arg[arg.length - 1].equals("T1"))
				|| (arg[arg.length - 1].equals("T2"))) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {
				String p = array[1];
				p = p.substring(0, p.length() - 1);
				double PS = Double.parseDouble(p.replaceAll("\"", ""));
				valoriPS.add(PS);
				per.WriteJson(light, valoriPS, pc.ST.getRangeTemp());
			}

		}

		if (arg[arg.length - 1].equals("AN0")) {
			String array[] = message.toString().split(":");
			String msn = array[0];
			msn = msn.substring(1);
			msn = msn.replace("\"", "");
			if (msn.equals("event")) {

				String p = array[1];
				p = p.substring(0, p.length() - 1);
				light = Integer.parseInt(p.replaceAll("[\\D]", ""));
				per.WriteJson(light, valoriPS, pc.ST.getRangeTemp());

			}
		}
	}

	public ArrayList<Double> getValoriPS() {
		return valoriPS;
	}

	public void resetValPS() {
		valoriPS.clear();
	}

	public int getLight() {
		return light;
	}

	@Override
	public String toString() {
		return "SubControlloCallback [valoriPS=" + valoriPS + ", light=" + light + ", rangeTemp="
				+ Arrays.toString(pc.ST.getRangeTemp()) + ":]";
	}

	public void setValoriPS(ArrayList<Double> valoriPS) {
		this.valoriPS = valoriPS;
	}

	public void setLight(int light) {
		this.light = light;
	}
}
