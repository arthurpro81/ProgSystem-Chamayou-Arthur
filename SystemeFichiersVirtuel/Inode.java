/*
 * Inode.java                                          04/10/2026
 * IUT de Rodez pas de copyright ni de copyleft
 */

public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
        // Calculer l'offset exact de l'inode.
        return 1024+inodeNumber*128;
    }

    public int getFileType() {
        // Lire le type à offset + 4.
		byte[] memory = memoryManager.getFilesystemMemory();
        int offset = getInodeOffset() + 4;
		
        return Utils.readInt(memory, offset);
    }

    public int getFileSize() {
        // Lire la taille à offset + 8.
		byte[] memory = memoryManager.getFilesystemMemory();
        int offset = getInodeOffset() + 8;
		
        return Utils.readInt(memory, offset);
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];
		
		int debutListe = getInodeOffset() + 28;
		
		// Lire les 10 pointeurs directs.
		for (int i = 0; i < DIRECT_POINTERS; i++) {
            pointers[i] = Utils.readInt(memory, debutListe + i * 4);
        }
        return pointers;
    }
	
	public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int offset = getInodeOffset();


		// 1. Numéro d'inode
		offset += Utils.writeInt(memory,offset ,inodeNumber);
		
		// 2. Type
		offset += Utils.writeInt(memory,offset ,fileType);
		
		// 3. Taille
		offset += Utils.writeInt(memory,offset ,fileSize);
		
		// 4. Création
		offset += Utils.writeLong(memory,offset ,creationTime);
		
		// 5. Modification
		offset += Utils.writeLong(memory,offset ,modificationTime);
		
		// 6. 10 pointeurs directs
		for (int i = 0; i < DIRECT_POINTERS; i++) {
            offset += Utils.writeInt(memory, offset, directPointers[i]);
        }
		
		// 7. Pointeur indirect
		offset += Utils.writeInt(memory, offset, indirectPointer);
		
		// 8. Permissions
		offset += Utils.writeShort(memory, offset, permissions);
		
		// 9. Nombre de liens
		offset += Utils.writeInt(memory, offset, linkCount);
        
        
	}
}