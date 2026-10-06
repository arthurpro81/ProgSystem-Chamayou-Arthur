/*
 * VirtualFileSystem.java                                         04/10/2026
 * IUT de Rodez pas de copyright ni de copyleft
 */
 
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager =
                new MemoryManager();
    }

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        // Parcourir les inodes de 0 à MAX_INODES - 1.
		for (int i = 0; i < MemoryManager.MAX_INODES; i++) {
			int offsetInode = MemoryManager.INODE_TABLE_OFFSET + i * Inode.INODE_SIZE;
			int numeroInode = Utils.readInt(memory, offsetInode);
			if (numeroInode != i) {
				return i;
			}
        }
        return -1;
    }

    public boolean createFile(
            String directory,
            String filename) {

        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

        Inode variableInode = new Inode(memoryManager, inodeNum);
        long TempActuel = System.currentTimeMillis();

        variableInode.writeToMemory(1,0,TempActuel,TempActuel,
		                    new int[Inode.DIRECT_POINTERS],0,(short) 0644,1);

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
	
	public boolean writeFile(
		int inodeNum,
		byte[] data) {

		int blocksNeeded =
				(data.length
				+ MemoryManager.BLOCK_SIZE - 1)
				/ MemoryManager.BLOCK_SIZE;

		if (blocksNeeded > Inode.DIRECT_POINTERS) {
			return false;
		}

		int[] blockPointers =
				new int[Inode.DIRECT_POINTERS];

		Arrays.fill(blockPointers, -1);

        // Allouer blocksNeeded blocs.
        for (int i = 0; i < blocksNeeded; i++) {
            int numeroDuBloc = memoryManager.allocateBlock();
            if (numeroDuBloc != -1) {
                blockPointers[i] = numeroDuBloc;
            } else {
                return false;
            }
        }

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int bytesRemaining =
				data.length;

		int dataSrcOffset = 0;


		for (int i = 0; i < blocksNeeded; i++) {
            // - calculer la quantité à copier ;
            int departChunk =  i * memoryManager.BLOCK_SIZE;
            int qteOctetFragment = Math.min(memoryManager.BLOCK_SIZE, bytesRemaining);
            // - récupérer le numéro du bloc ;
            int numBloc = blockPointers[i];
            // - calculer son offset physique ;
            int offsetDeMemory = numBloc * memoryManager.BLOCK_SIZE;
            // - copier les données.
            System.arraycopy(data, departChunk, memory, offsetDeMemory, qteOctetFragment);

            bytesRemaining -= qteOctetFragment;
        }

        // Mettre à jour l'inode.
        Inode inode = new Inode(memoryManager, inodeNum);
        inode.writeToMemory(1, data.length, System.currentTimeMillis(), 
                           System.currentTimeMillis(), blockPointers, 0, (short) 0664, 1);

        return true;
	}
	
	public byte[] readFile(int inodeNum) {

		Inode inode =
				new Inode(memoryManager, inodeNum);

		int fileSize =
				inode.getFileSize();

		if (fileSize == 0) {
			return new byte[0];
		}

		byte[] fileData =
				new byte[fileSize];

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int[] blockPointers =
				inode.getDirectPointers();

		// Parcourir les blocs utilisés.
		for (int i = 0; i < blockPointers.length && i * MemoryManager.BLOCK_SIZE < fileSize; i++) {

			int numBloc = blockPointers[i];
			int offsetDeMemory = numBloc * MemoryManager.BLOCK_SIZE;
			int offsetDansFichier = i * MemoryManager.BLOCK_SIZE;
			int qteOctets = Math.min(MemoryManager.BLOCK_SIZE, fileSize - offsetDansFichier);

			// Copier chaque fragment vers fileData.
			System.arraycopy(memory, offsetDeMemory, fileData, offsetDansFichier, qteOctets);
		}
		return fileData;
	}
	
	public boolean deleteFile(int inodeNum) {

		if (inodeNum < 0 || inodeNum >= MemoryManager.MAX_INODES) {
			return false;
		}

		byte[] memory = memoryManager.getFilesystemMemory();

		// 1. Récupérer l'inode
		Inode inode = new Inode(memoryManager, inodeNum);

		// 2. Libérer les blocs utilisés
		int[] pointers = inode.getDirectPointers();

		for (int bloc : pointers) {
			if (bloc != -1) {
				memoryManager.setBlockUsed(bloc, false);
			}
		}

		// 3. Marquer l'inode comme libre
		int inodeOffset = inode.getInodeOffset();
		Utils.writeInt(memory, inodeOffset, -1);

		return true;
	}
	
	public boolean writeExternalFile(String filename) {

		StringBuilder sBuilder = new StringBuilder();
		
		try (FileReader reader = new FileReader(filename)) {
			char[] buffer = new char[1024];
			int compteur;
			while ((compteur = reader.read(buffer)) != -1) {
				sBuilder.append(buffer, 0, compteur);
			}
		} catch (IOException e) {
			return false;
		}
		
		byte[] data = sBuilder.toString().getBytes();
		if (!createFile("/", "texte.txt")) {
			return false;
		}
		return writeFile(0, data);
	}

}