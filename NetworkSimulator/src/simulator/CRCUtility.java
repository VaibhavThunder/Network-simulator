package simulator;

public class CRCUtility {
	
	public static int divisor[]={1,0,1,0};   
	
	public static int[] divide(int old_data[]) {
		int remainder[] , i;
		
		int data[] = new int[old_data.length + divisor.length];
		System.arraycopy(old_data, 0, data, 0, old_data.length);
		// Remainder array stores the remainder
		remainder = new int[divisor.length];
		// Initially, remainder's bits will be set to the data bits
		System.arraycopy(data, 0, remainder, 0, divisor.length);
		
		// Loop runs for same number of times as number of bits of data
		// This loop will continuously exor the bits of the remainder and
		// divisor
		for(i=0 ; i < old_data.length ; i++) {
			if(remainder[0] == 1) {
				// We have to exor the remainder bits with divisor bits
				for(int j=1 ; j < divisor.length ; j++) {
					remainder[j-1] = exor(remainder[j], divisor[j]);
	
				}
			}
			else {
				// We have to exor the remainder bits with 0
				for(int j=1 ; j < divisor.length ; j++) {
					remainder[j-1] = exor(remainder[j], 0);
	
				}
			}
			// The last bit of the remainder will be taken from the data
			// This is the 'carry' taken from the dividend after every step
			// of division
			remainder[divisor.length-1] = data[i+divisor.length];
	
		}
		return remainder;
		
	}
	
	public static String crcDivide(int[] message){
		StringBuffer remainderStr=new StringBuffer();
		int[] remainder=divide(message);
		for(int k:remainder){
			remainderStr.append(k);
		}
		
		return remainderStr.toString();
	}
	
	public static int exor(int a, int b) {
		// This simple function returns the exor of two bits
		if(a == b) {
			return 0;
		}
		return 1;
	}

	public static boolean receive(int data[]) {
		// This is the receiver method
		// It accepts the data and divisor (although the receiver already has
		// the divisor value stored, with no need for the sender to resend it) 
		int remainder[] = divide(data);
		// Division is done
		for(int i=0 ; i < remainder.length ; i++) {
			if(remainder[i] != 0) {
				// If remainder is not zero then there is an error
	
				return false;
			}
		}
		return true;
		//Otherwise there is no error in the received  data

	}
}


