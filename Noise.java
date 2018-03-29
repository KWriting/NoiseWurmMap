import java.io.File;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.Math;
import java.awt.Color;
import java.awt.*;
import java.awt.image.ColorModel;

public class Noise {
	/*static int height, width;
	static double[][] noiseMap;
	static double[] noiseArray;	//pLen / 3
	static int pixelData[];
	static int dWidth, pLen;
	static BufferedImage img;
	
	/*public static void main(String args[]) throws IOException {
		height = width = 16;
		dWidth = width * 3;
		noiseMap = new double[height][width];
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
		pixelData = new int[width * height * 3];
		System.out.println("Data array is of length: " + pixelData.length);
		pLen = pixelData.length;
		noiseArray = generateNoiseArray(width * height);
		generateNoiseMap(width, height);
		double r, g, b;
		/*for (int i=0; i < pLen; i+=3) {
			pixelData[i+0] = 200;
			pixelData[i+1] = 200;
			pixelData[i+2] = 200;
		}*/
		/*
		//setColorSquareUL(4096, 0, 0, Color.gray);
		generateGreyNoise();
		//genGreyNoise(noiseMap);
		
		img.getRaster().setPixels(0, 0, width, height, pixelData);
		File file = new File(System.getProperty("user.dir") + "\\image.png");
		ImageIO.write(img, "png", file);
		
		int size = 16;
		exportHeightMap(generateNoiseArray(size * size), "graymaptest2");
		
	}*/
	
	
	static int mapSize = 1024;
	static double[][] heightArray;
	public static void main(String args[]) {
		//heightArray = generateNoiseMap(mapSize);
		heightArray = genTestMap1(mapSize);
		exportHeightImage();
	}
	
	/**Wurm's:**/
	static void exportHeightImage() {
		File imageFile = new File(System.getProperty("user.dir") + "\\imagepls.png");
		BufferedImage bufferedImage = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_USHORT_GRAY);
		WritableRaster wr = (WritableRaster) bufferedImage.getRaster();

		double[] array = new double[mapSize * mapSize];
		for (int x = 0; x < mapSize; x++) {
			for (int y = 0; y < mapSize; y++) {
				array[x + y * mapSize] = (getHeight(x,y) * 65535);
			}
		}

		wr.setPixels(0, 0, mapSize, mapSize, array);

		bufferedImage.setData(wr);
		try {
			if (!imageFile.exists())
				imageFile.mkdirs();
			ImageIO.write(bufferedImage, "png", imageFile);
		} catch (IOException e) {
			System.out.println("error");
			return;
		}
	}
	
	static double getHeight(int x, int y) {
		return heightArray[x][y];
	}
	/*****Wurm's ^ ******/
	
	static double[][] genTestMap1(int size) {
		double[][] newMap = new double[size][size];
		double nv = 0.0;	// New Value
		newMap[0][0] = 0.0001;
		// For each column (x), take the value of the previous column (x-1) and add to it a random value b/w 0.01 and 0.03.
		// Then, set the nv to be this value.
		for(int x = 1; x < size; x++) {
			nv = newMap[0][x-1] + ((Math.random() * 0.02) + 0.01);
			if(nv >= 0.3)
				nv = 0.0001;
			System.out.println(nv);
			for(int y = 0; y < size; y++) {
				newMap[y][x] = nv;
			}
		}
		return newMap;
	}
	
	static double[][] generateNoiseMap(int size) {
		double[][] newMap = new double[size][size];
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				newMap[y][x] = genRand();
			}
		}
		return newMap;
	}
	
	
	
	
	
	
	/** ***Edit***
	* export a 16-bit grayscale image given an array of height values, sized mapSize^2.
	*/
	static void exportHeightMap(double[] heightArray, String fileName) throws IOException {
		int size = heightArray.length / heightArray.length;	// width of the map
		
		//File imageFile = new File("./maps/" + fileName);
		// 
		double[] pixelValues = new double[heightArray.length];
		int pos;
		for(int i = 0; i < heightArray.length; i++) {
			pixelValues[i] = (heightArray[i] * 65535);	// Creates pixel grayscale value to be printed on the image.
			System.out.println("i: " + i + ", " + (heightArray[i] * 65535));	//d
		}
		
		// Initialize objects for image and file creation:
		File file = new File(".\\" + fileName);
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_USHORT_GRAY);
		img.getRaster().setPixels(0, 0, size, size, pixelValues);
		//img.setData(wr);
		ImageIO.write(img, "png", file);
	}
	
	
	/*
	
	//returns array of length equal to height and width of map, containing random double noise
	static double[] generateNoiseArray(int length) {
		double[] arr = new double[length];
		for (int i=0; i < length; i++) {
			arr[i] = genRand();
			System.out.print("arr["+i+"]: " + arr[i] + ", ");	//d
		}
		return arr;
	}
	
	//Try using setRGB()
	static void genGreyNoise(double[][] noiseMap) {
		int color;
		int len = noiseMap.length;
		for(int y = 0; y < len; y++) {
			for(int x = 0; x < len; x++) {
				color = (int)(255 * noiseMap[y][x]);
				img.setRGB(x, y, color);
				System.out.print(color + " ");
			}
		}
	}
	
	//Wurm way... using setPixel()
	static void generateGreyNoise() {
		int color;
		for (int i = 0, j = 0; i < pLen; i+=3, j++) {
			color = (int)(noiseArray[j] * 255);
			pixelData[i] = pixelData[i+1] = pixelData[i+2] = color;
			//System.out.print(color + " ");
		}
	}
	
	//Takes pixel's coordinates on image, returns starting location in pixelData array.
	static int getIndex(int x, int y) {
		//To find the correct location in the pixelData array, take the width (one row of pixels, length equal to width of image) and multiply it by the
		//number of rows down (the y coordinate) and 3.  Then, add the number of columns over (the x coordinate) mult. by 3 to represent how far across
		//the width of the image the pixel is.
		return (y * dWidth) + (x * 3);	//This is where the three RGB indices will start.
	}
	
	//Takes x and y coordinates of the pixel to be changed, finds the correct spot in the data index, and sets the RGB values to the ones supplied.
	static void setColor(int index, Color color) {
		pixelData[index] = color.getRed();
		pixelData[index + 1] = color.getGreen();
		pixelData[index + 2] = color.getBlue();
		//System.out.println("Color at location [" + index + "] set to [" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "].");
	}
	static void setColor(int x, int y, Color color) {
		setColor(getIndex(x, y), color);
	}
	
	//Sets a square of pixels to the supplied color.
	/*	size : size of square to change pixels of
		x : x coord of pixel at middle of square
		y : y coord of pixel at middle of square
		color : color to be set
	*//*
	static void setColorSquareM(int size, int x, int y, Color color) {
		if (size % 2 == 0) {
			System.out.println("Error: Square's size must be an odd number.");
			return;
		}
		int pLen = pixelData.length;
		//To get upper left:
		int upLeftY = y - ((size/2));
		int upLeftX = x - ((size/2));
		System.out.println("Upper left pixel of size " + size + " square with the center located at [" + x + "," + y + "]" +
						   " is at [" + upLeftX + "," + upLeftY + "].");
		int upLeftIndex = getIndex(upLeftX, upLeftY);
		for(int j = 0; j < size; j++) {		//down a column
			int index = upLeftIndex + (dWidth * j);	//add one width 
			//System.out.println("j: " + j + ", index: " + index);	(d)
			for(int i = 0; i < size; i++, index += 3) {	//across a row
				if (index < pLen && index >= 0) {
					pixelData[index] = color.getRed();
					pixelData[index + 1] = color.getGreen();
					pixelData[index + 2] = color.getBlue();
				}
			}
		}
	}
	
	//Same as above, but takes upper left x, y as arg instead of middle.
	static void setColorSquareUL(int size, int upLeftX, int upLeftY, Color color) {
		int upLeftIndex = getIndex(upLeftX, upLeftY);
		for(int j = 0; j < size; j++) {		//down a column
			int index = upLeftIndex + (width * j * 3);	//add one width 
			//System.out.println("j: " + j + ", index: " + index);	(d)
			for(int i = 0; i < size; i++, index += 3) {	//across a row
				if (index < pLen && index >= 0) {
					pixelData[index] = color.getRed();
					pixelData[index + 1] = color.getGreen();
					pixelData[index + 2] = color.getBlue();
				}
			}
		}
	}
	
	/*
	(pixels - data is x3)
	  0 1 2 3 4 5 6 7 8
	0 . . . . . . . . .
	1 . . . . . . . . .
	2 . . . . . . . . .
	3 . . . o o o . . .
	4 . . . o o o . . .
	5 . . . o o o . . .
	6 . . . . . . . . .
	7 . . . . . . . . .
	8 . . . . . . . . .
	
	*/
	
	
	
	static double genRand () {
		return Math.random();
	}
}