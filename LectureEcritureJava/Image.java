import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Image {
    private int width;
    private int height;
    
    private int[][][] pixels; //pixels[y][x][0=R,1=G,2=B]
	private byte[][][] pixelsByte;


    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**
     * Constructeur : initialise une image vide.
     */
    public Image(int width, int hauteur) {
        this.width = width;
        this.height = hauteur;
        pixels = new int[height][width][3];
		pixelsByte = new byte[height][width][3];
    }

    /**
     * Définit la couleur d'un pixel à la position (x, y)
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            // Sauvegarde dans le tableau d'entiers
            pixels[y][x][0] = r;
            pixels[y][x][1] = g;
            pixels[y][x][2] = b;

            // Sauvegarde dans le tableau de bytes (conversion explicite)
            pixelsByte[y][x][0] = (byte) r;
            pixelsByte[y][x][1] = (byte) g;
            pixelsByte[y][x][2] = (byte) b;
        }
    }

    /**
     * Sauvegarde l'image au format texte PPM (P3)
     */
    public void save_txt(String filename) throws IOException {

        FileWriter writer = new FileWriter(filename);

        // En-tête PPM
        writer.write("P3\n");
        writer.write(width + " " + height + "\n");
        writer.write("255\n");

        // Écriture des pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.write(
                    pixels[y][x][0] + " " +
                    pixels[y][x][1] + " " +
                    pixels[y][x][2] + " "
                );
            }
            writer.write("\n");
        }

        writer.close();
    }

	
    /**
     * Sauvegarde l'image au format binaire PPM (P6)
     */
    public void save_bin(String filename) throws IOException {
        FileOutputStream out = new FileOutputStream(filename);

		String header = "P6\n" + width + " " + height + "\n255\n";
		out.write(header.getBytes());

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out.write(pixelsByte[y][x][0]); 
				out.write(pixelsByte[y][x][1]); 
				out.write(pixelsByte[y][x][2]); 
			}
		}

        out.close();
    }

}
