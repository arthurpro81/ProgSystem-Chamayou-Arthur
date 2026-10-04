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
}