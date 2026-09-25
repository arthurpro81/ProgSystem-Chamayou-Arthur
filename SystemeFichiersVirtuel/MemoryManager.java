/*
 * MemoryManager.java                                          25/09/2026
 * IUT de Rodez pas de copyright ni de copyleft
 */

import java.io.*;

public class MemoryManager {

    public static final int BLOCK_SIZE = 512;
    public static final int TOTAL_MEMORY = 1024 * 1024;
    public static final int NUM_BLOCKS = TOTAL_MEMORY / BLOCK_SIZE;

    public static final int SUPERBLOCK_OFFSET = 0;
    public static final int BITMAP_OFFSET = BLOCK_SIZE;
    public static final int INODE_TABLE_OFFSET = 2 * BLOCK_SIZE;
    public static final int DATA_OFFSET =129 * BLOCK_SIZE;

    public static final int INODE_SIZE = 128;

    public static final int INODE_TABLE_SIZE =DATA_OFFSET - INODE_TABLE_OFFSET;

    public static final int MAX_INODES =INODE_TABLE_SIZE / INODE_SIZE;

    private byte[] memory;

    public MemoryManager() {
        this.memory = new byte[TOTAL_MEMORY];
        initializeFilesystem();
    }

    private void initializeFilesystem() {
        writeSuperblock();

        // Réserver les blocs système 0 à 128.
		for (int bloc=0; bloc<=128; bloc++){
			
			// on cherche l'octet puis le bit qui corespond a l'octet
			int byteIndex = bloc / 8;
			int bitIndex = bloc % 8;
			
			// note : "|" permet de mettre bit a 1
			memory[BITMAP_OFFSET + byteIndex] |= (1 << bitIndex);
			
		}
    }

    private void writeSuperblock() {

        Utils.writeString(
                memory,
                SUPERBLOCK_OFFSET,
                "MYFS1.0",
                16);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 16,
                BLOCK_SIZE);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 20,
                TOTAL_MEMORY);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 24,
                NUM_BLOCKS);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 28,
                MAX_INODES);
    }

    public byte[] getFilesystemMemory() {
        return memory;
    }
}