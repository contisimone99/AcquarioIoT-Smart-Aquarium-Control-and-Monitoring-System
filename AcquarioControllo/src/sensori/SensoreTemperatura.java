package sensori;

public class SensoreTemperatura {

	private double[] rangeTemp = { 21.5 , 24.5, 26.5, 29 };
	private int[] attuatore = { 0, 0 }; // 0 Raffreddatore 1 Riscaldatore

	/**
	 * 
	 * @param temp
	 * @return 0 se è richiesta l'attivazione del raffreddatore, 1 se è richiesta
	 *         l'attivazione del riscaldatore, 2 se è richiesta la disattivazione
	 *         del raffreddatore, 3 se è richiesta la disattivazione del
	 *         riscaldatore, 4 se sono da disattivare entrambi
	 */
	public int handleTemp(double temp) {
		
		int action = -1;

		if (temp < rangeTemp[0]) {
			attuatore[1] = 1;
			attuatore[0] = 0;
			action=1;
		} else if (temp >= rangeTemp[1] && temp < rangeTemp[2]) {
			attuatore[1] = 0;
			attuatore[0] = 0;
			action=2;
		} else {
			attuatore[0] = 1;
			attuatore[1] = 0;
			action=0;
		}
		return action;
	}
	
	public double[] getRangeTemp() {
		return rangeTemp;
	}

	public void setRangeTemp(double[] rangeTemp) {
		this.rangeTemp = rangeTemp;
	}
	

}
