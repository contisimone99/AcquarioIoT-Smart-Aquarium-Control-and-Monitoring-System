package controllo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
//import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.dcsquare.paho.client.util.Utils;
import log.Log;
import sensori.SensoreLuce;
import sensori.SensoreTemperatura;

public class PubControllo implements Runnable {

	public String topic;
	public String broker;
	SubControlloCallback ccb;
	private MqttClient client;
	String utente;
	final String topic_risc = "to/gruppo3/acquario/gpio/OUT5";
	final String topic_raff = "to/gruppo3/acquario/gpio/OUT4";
	final String topic_light = "to/gruppo3/acquario/gpio/OUT3";
	final String topic_al = "to/gruppo3/acquario/gpio/OUT2";
	final String topic_media = "from/gruppo3/acquario/client/T";
	final String topic_risc_client = "from/gruppo3/acquario/client/Risc";
	final String topic_raff_client = "from/gruppo3/acquario/client/Raff";
	final String topic_light_client = "from/gruppo3/acquario/client/L";
	final String topic_al_client = "from/gruppo3/acquario/client/Al";
	final String topic_arch = "to/gruppo3/acquario/archiver/A";
	public String caFilePath = "smartcity-ca/ca_certificate.pem";
	public String clientCrtFilePath = "certificates/client.crt";
	public String clientKeyFilePath = "certificates/client.key";

	int mediaPS;
	double mediaFC;
	ArrayList<Integer> mediaRip = new ArrayList<Integer>();
	public boolean flag = true;
	SensoreTemperatura ST;
	SensoreLuce SL;

	public PubControllo(SubControlloCallback controllCB) {
		ccb = controllCB;
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

	public void pub(String controllo) {
		try {
			Thread.sleep(500);
			publishControllo(controllo, "test");
			Thread.sleep(500);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void publishControllo(String controllo, String Topic) throws MqttPersistenceException, MqttException {

		final MqttTopic c = client.getTopic(Topic);
		c.publish(new MqttMessage(controllo.getBytes()));
		System.out.println("PUBBLICO controllo. Topic: " + c.getName() + " MESSAGGIO:" + controllo);
	}

	@Override
	public void run() {
		try {
			startGestione();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startGestione() throws MqttSecurityException, MqttException, InterruptedException {
		flag = true;
		ST = new SensoreTemperatura();
		SL = new SensoreLuce();
		Log ar = Log.ReadJson();
		while (flag) {
			Thread.sleep(10000);

			double val = calcola_mediaPS();
			if (val != -1) {
				publishControllo("{event:" + val + "}", topic_media);
				int action = ST.handleTemp(val); // TODO: Ricordarsi di farlo anche per la pagina web
				switch (action) {
				case 0:
					publishControllo("{cmd:1}", topic_raff);
					publishControllo("{cmd:0}", topic_risc);
					publishControllo("{event:1}", topic_raff_client);
					publishControllo("{event:0}", topic_risc_client);
					publishControllo("{attuatore:raffreddatore,azione:accensione}", topic_arch);
					ar.WriteJson(topic_raff, "{cmd:1}", "Acceso", "Raffreddatore");
					ar.WriteJson(topic_risc, "{cmd:0}", "Spento", "Riscaldatore");
					break;
				case 1:
					publishControllo("{cmd:0}", topic_raff);
					publishControllo("{cmd:1}", topic_risc);
					publishControllo("{event:0}", topic_raff_client);
					publishControllo("{event:1}", topic_risc_client);
					publishControllo("{attuatore:riscaldatore,azione:accensione}", topic_arch);
					ar.WriteJson(topic_raff, "{cmd:0}", "Spento", "Raffreddatore");
					ar.WriteJson(topic_risc, "{cmd:1}", "Acceso", "Riscaldatore");
					break;
				case 2:
					publishControllo("{cmd:0}", topic_raff);
					publishControllo("{cmd:0}", topic_risc);
					publishControllo("{event:0}", topic_raff_client);
					publishControllo("{event:0}", topic_risc_client);
					publishControllo("{attuatore:raffreddatore,azione:spento}", topic_arch);
					publishControllo("{attuatore:riscaldatore,azione:spento}", topic_arch);
					ar.WriteJson(topic_raff, "{cmd:0}", "Spento", "Raffreddatore");
					ar.WriteJson(topic_risc, "{cmd:0}", "Spento", "Riscaldatore");
					break;
				case -1:
					break;
				}
			}
			SL.handleLight(ccb.getLight()); // TODO: Ricordarsi di farlo anche per la pagina web
			int status = SL.GetlightStatus();
			publishControllo("{cmd:" + status + "}", topic_light);
			publishControllo("{event:" + status + "}", topic_light_client);
			publishControllo("{attuatore:luci,azione:"+(status==1?"Accensione":"Spegnimento")+"}", topic_arch);
			if (status == 1) {
				ar.WriteJson(topic_light, "{cmd:1}", "Acceso", "Luci");
			}

			else {
				ar.WriteJson(topic_light, "{cmd:0}", "Acceso", "Luci");
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

	public void setActuator(String controllo, String att) throws MqttPersistenceException, MqttException {
		switch (att) {
		case "0":
			publishControllo(controllo, topic_raff);
			publishControllo((controllo.equals("{cmd:0}")?"{attuatore:raffreddatore,azione:spento}":"{attuatore:raffreddatore,azione:accensione}"), topic_arch);
			break;
		case "1":
			publishControllo(controllo, topic_risc);
			publishControllo((controllo.equals("{cmd:0}")?"{attuatore:riscaldatore,azione:spento}":"{attuatore:riscaldatore,azione:accensione}"), topic_arch);
			break;
		case "2":
			publishControllo(controllo, topic_light);
			publishControllo((controllo.equals("{cmd:0}")?"{attuatore:luci,azione:spento}":"{attuatore:luci,azione:accensione}"), topic_arch);
			break;
		case "3":
			publishControllo(controllo, topic_al);
			publishControllo((controllo.equals("{cmd:0}")?"{attuatore:alimentatore,azione:spento}":"{attuatore:alimentatore,azione:accensione}"), topic_arch);
			break;
		}
	}

	private double calcola_mediaPS() {
		double sommaPS = 0;
		double res = 0;
		if (ccb.getValoriPS().size() != 3)
			return -1.0;
		for (double val : ccb.getValoriPS()) {
			sommaPS += val;
		}
		res = sommaPS / 3;
		ccb.resetValPS();
		return res;
	}
}
