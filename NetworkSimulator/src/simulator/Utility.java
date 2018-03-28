package simulator;

public class Utility {

	/**
	 * This utility function converts message to its string binary representation
	 * @param message
	 * @return
	 */	
	public static String getBinaryRep(String message) {

		byte[] bytes = message.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			binary.append(' ');
		}
		return binary.toString();
	}
	/**
	 * This utility function converts message to binary bits
	 * @param message
	 * @return
	 */
	public static int[] getBinary(String message) {

		byte[] bytes = message.getBytes();
		int[] binary = new int[bytes.length*8];
		int j=0;
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary[j++]=((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			
		}
		return binary;
	}

	

}
