import org.eclipse.paho.client.mqttv3.*;
import java.util.Date;

public class Publisher {

    public static final String BROKER_URL = "tcp://smartcity-challenge.edu-al.unipmn.it:1883";

    public static final String TOPIC = "conf";

    private MqttClient client;

    private Date date = new Date();

    public Publisher(String hosturl) {
	String hurl;
        if(hosturl != "") hurl = hosturl;
	else hurl = BROKER_URL;
	System.out.println("connecting to: "+hurl);

        //We have to generate a unique Client id.
        String clientId = "cloudappman-"+Long.toString(date.getTime()) + "-pub";


        try {

            client = new MqttClient(hurl, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName("pissir");
            options.setPassword("pissir2020".toCharArray());

            client.connect(options);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
	}
//         catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    void publish(String topic, String msg) throws MqttException {
        MqttTopic pubTopic = client.getTopic(topic);

        pubTopic.publish(new MqttMessage(msg.getBytes()));

    }
    
    

}
