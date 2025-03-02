package codice;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ArchiverCallback implements MqttCallback {
	
	

	ArchiverPub archiverPub = new ArchiverPub(this);
	Thread pb;
	boolean start = false;
	Archiver ar;
	

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		System.out.println("--- CONTROLLO --- Message arrived. Topic: " + topic + "  Message: " + message.toString());
		String arg[] = topic.split("/");
		if(!start)
		{
			start=true;
			ar = Archiver.ReadJson();
			pb = new Thread(archiverPub);
			pb.start();
		}
		
		//{attuatore:r,azione:acc}
		if ((arg[arg.length - 1].equals("A"))) {
			String array[] = message.toString().split(",");
			String arrayAtt[] = array[0].split(":");
			String att = arrayAtt[1]; 
			String arrayAz[] = array[1].split(":");
			String az = arrayAz[1].replace("}", "");
			att = att.replace("\"","");
			az = az.replace("\"","");
			System.out.println("valore di Att:" + att);
			System.out.println("valore di AZ:" + az);
			ar.WriteJson(att, az);
		}
			
	}

}
