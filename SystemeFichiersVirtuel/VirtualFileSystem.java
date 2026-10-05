/*
 * VirtualFileSystem.java                                         04/10/2026
 * IUT de Rodez pas de copyright ni de copyleft
 */

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
}