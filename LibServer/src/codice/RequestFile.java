package codice;

import java.io.InputStream;

public class RequestFile {
	public byte buffer[];
	
	public RequestFile(InputStream is) {
		try {
			  buffer = new byte[is.available()];
		          is.read(buffer);
			}
			catch (Exception e) {
			  e.printStackTrace();
			}
		
		
	}
}
