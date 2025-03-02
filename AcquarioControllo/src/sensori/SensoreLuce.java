package sensori;

public class SensoreLuce {
	
	private int lightStatus = 0;
	private int dayLight = 300; //TODO Controllare Valori Arduino

	public void handleLight (int light)
	{
		if((lightStatus==0) && (light<dayLight))
		{
			System.out.println("Accendo per:" + light);
			lightStatus=1;
			
		}
		else if((lightStatus==1) && (light>=dayLight ))
		{
			System.out.println("Spengo per:" + light);
			lightStatus=0;
		}
	}
	
	public int GetlightStatus() {
		return lightStatus;
	}
	
}
