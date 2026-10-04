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
		for (int i = 0; i < MemoryManager.MAX_INODES-1; i++) {
		
		// TODO:
		// Identifier le premier inode libre (if).
			// Retourner son numéro.
            
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

        // TODO:
        // Construire l'inode.
        // L'initialiser comme fichier vide.

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
}