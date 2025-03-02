package controllo;

import java.util.logging.Level;
//import java.util.logging.Level;
import java.util.logging.Logger;


public class Controllo implements Runnable {

	PubControllo pc;
	SubControllo subscriber;
	static String BROKER_URL;
	String topic;
	String broker;
	
	public Controllo() throws InterruptedException
	{
		topic = "from/gruppo3/#";
		broker = "ssl://smartcity-challenge.edu-al.unipmn.it:8883";
		subscriber = new SubControllo();
		
	}


	@Override
	public void run() {
		Thread cl = new Thread(subscriber);
		cl.start();
		try
		{
			cl.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
