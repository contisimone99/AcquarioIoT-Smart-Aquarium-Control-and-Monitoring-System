package sensori;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.dcsquare.paho.client.util.Utils;
import log.Log;

public class Alimentatori implements Runnable {
	
	public String broker;
	private MqttClient client;
	private String Topic = "to/gruppo3/acquario/gpio/OUT2";
	private String Topic_Client = "from/gruppo3/acquario/client/Al";
	final String topic_arch = "to/gruppo3/acquario/archiver/A";
	public String caFilePath = "smartcity-ca/ca_certificate.pem";
	public String clientCrtFilePath= "certificates/client.crt";
	public String clientKeyFilePath= "certificates/client.key";


	public Alimentatori() {
		broker = "ssl://smartcity-challenge.edu-al.unipmn.it:8883";
		String clientId = Utils.getMacAddress() + "con-pub" + System.currentTimeMillis();
		

		try {

			client = new MqttClient(broker, clientId);
			MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("pissir");
            options.setPassword("pissir2020".toCharArray());
            options.setConnectionTimeout(60);
			options.setKeepAliveInterval(60);
			options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
			SSLSocketFactory socketFactory = getSocketFactory(caFilePath, clientCrtFilePath, clientKeyFilePath, "");
			options.setSocketFactory(socketFactory);
			options.setCleanSession(false);
			options.setWill(client.getTopic("home/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		Log ar = Log.ReadJson();
		while(true) {
			try {
				
				pub("{cmd:1}");
				pubClient("{event:1}");
				pubArch("{attuatore:alimentatore,azione:accensione}");
				ar.WriteJson(Topic,"{cmd:1}", "Acceso", "Alimentatore");
				Thread.sleep(5000);
				pub("{cmd:0}");
				pubClient("{event:0}");
				pubArch("{attuatore:alimentatore,azione:spegnimento}");
				ar.WriteJson(Topic,"{cmd:0}", "Spento", "Alimentatore");
				Thread.sleep(28000000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile,
			final String password) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		// load CA certificate
		X509Certificate caCert = null;

		FileInputStream fis = new FileInputStream(caCrtFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		while (bis.available() > 0) {
			caCert = (X509Certificate) cf.generateCertificate(bis);
			// System.out.println(caCert.toString());
		}

		// load client certificate
		bis = new BufferedInputStream(new FileInputStream(crtFile));
		X509Certificate cert = null;
		while (bis.available() > 0) {
			cert = (X509Certificate) cf.generateCertificate(bis);
			// System.out.println(caCert.toString());
		}

		// load client private key
		PEMParser pemParser = new PEMParser(new FileReader(keyFile));
		Object object = pemParser.readObject();
		PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		KeyPair key;
		if (object instanceof PEMEncryptedKeyPair) {
			System.out.println("Encrypted key - we will use provided password");
			key = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
		} else {
			System.out.println("Unencrypted key - no password needed");
			key = converter.getKeyPair((PEMKeyPair) object);
		}
		pemParser.close();

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
				new java.security.cert.Certificate[] { cert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}
	
	private void pub(String controllo) throws MqttPersistenceException, MqttException
	{
		final MqttTopic c = client.getTopic(Topic);
		c.publish(new MqttMessage(controllo.getBytes()));
		 System.out.println("PUBBLICO controllo. Topic: "+c.getName()+ " MESSAGGIO:" +controllo);
	
	}
	
	
	private void pubArch(String controllo) throws MqttPersistenceException, MqttException
	{
		final MqttTopic c = client.getTopic(topic_arch);
		c.publish(new MqttMessage(controllo.getBytes()));
		 System.out.println("PUBBLICO controllo. Topic: "+c.getName()+ " MESSAGGIO:" +controllo);
	
	}
	
	private void pubClient(String controllo) throws MqttPersistenceException, MqttException
	{
		final MqttTopic c = client.getTopic(Topic_Client);
		c.publish(new MqttMessage(controllo.getBytes()));
		 System.out.println("PUBBLICO controllo. Topic: "+c.getName()+ " MESSAGGIO:" +controllo);
	
	}

}
