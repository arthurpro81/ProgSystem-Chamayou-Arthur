/*
 * Utils.java                                          16/09/2026
 * IUT de Rodez pas de copyright ni de copyleft
 */

public class Utils {
	
	/*
	 * cette classe permet de convertir des valeurs (int, short,long et String) 
	 * en une suite d'octets stockée dans un tableau,
	 * et de reconstituer ces valeurs à partir du tableau.
	 * int, short et long sont implémenter et fini
	 */
	
    public static int writeInt(byte[] memory, int offset, int value) {
        
		memory[offset] = (byte) (value >> 24);
		memory[offset + 1] = (byte) (value >> 16);
		memory[offset + 2] = (byte) (value >> 8);
		memory[offset + 3] = (byte) (value);
		
		return 4;
    }

    public static int readInt(byte[] memory, int offset) {
		// reconstitution de l'int
		int b0 = (memory[offset] & 0xFF) << 24;
		int b1 = (memory[offset+1] & 0xFF) << 16;
		int b2 = (memory[offset+2] & 0xFF) << 8;
		int b3 = (memory[offset+3] & 0xFF);
		// fusion avec le "ou" qu'on a vue en cours
		return b0|b1|b2|b3;
	}

    public static int writeShort(byte[] memory, int offset, short value) {
		memory[offset] = (byte) (value >> 8);
		memory[offset + 1] = (byte) (value);
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
		int b0 = (memory[offset] & 0xFF) << 8;
		int b1 = (memory[offset + 1] & 0xFF);
		// fusion des 2 morceaux avec ou
		return (short) (b0|b1);
    }
		
	public static int writeLong(byte[] memory, int offset, long value) {
		memory[offset] = (byte) (value >> 56);
		memory[offset+1] = (byte) (value >> 48);
		memory[offset+2] = (byte) (value >> 40);
		memory[offset+3] = (byte) (value >> 32);
		memory[offset+4] = (byte) (value >> 24);
		memory[offset+5] = (byte) (value >> 16);
		memory[offset+6] = (byte) (value >> 8);
		memory[offset+7] = (byte) (value);
		
		return 8;
    }

    public static long readLong(byte[] memory, int offset) {
		// reconstitution de l'int
		long b0 = (memory[offset] & 0xFFL) << 56;
		long b1 = ((long) memory[offset+1] & 0xFFL) << 48;
		long b2 = ((long) memory[offset+2] & 0xFFL) << 40;
		long b3 = ((long) memory[offset+3] & 0xFFL) << 32;
		long b4 = ((long) memory[offset+4] & 0xFFL) << 24;
		long b5 = ((long) memory[offset+5] & 0xFFL) << 16;
		long b6 = ((long) memory[offset+6] & 0xFFL) << 8;
		long b7 = ((long) memory[offset+7] & 0xFFL);

		// fusion des 8 morceaux avec ou
		return b0|b1|b2|b3|b4|b5|b6|b7;
	}
	
	public static int writeString(byte[] memory, int offset, String str, int maxLength) {
		// 1. Convertir la chaîne en octets.
		
		byte[] bytes = str.getBytes();
		int length = Math.min(bytes.length, maxLength);

		// 2. Copier les octets sans dépasser maxLength.
		for (int i = 0; i < length; i++) {
			memory[offset + i] = bytes[i];
		}

		// 3. Nettoyer le reste de la zone avec des zéros.
		for (int i = length; i < maxLength; i++) {
			memory[offset + i] = 0;
		}

		return maxLength;

	}

	public static String readString(byte[] memory, int offset, int maxLength) {
		// code ne marche pas encore 
		String resultat = "";
		for (int i = 0;i < maxLength && memory[offset+i] != 0; i++) {
			resultat = resultat + memory[offset+i];
		}

		return resultat;
	}
}