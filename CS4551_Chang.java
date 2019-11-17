import java.util.Scanner;
public class CS4551_Chang {
	public static void main(String[] args){
		// the program expects one commandline argument
		// if there is no commandline argument, exit the program
	    if(args.length != 1)
	    {
	      usage();
	      System.exit(1);
	    }
	    Scanner reader = new Scanner(System.in);
	    System.out.println("--Welcome to Multimedia Software System--");
	    
	    while(true) {

		    System.out.println();
		    System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
		    System.out.println("2. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)");
		    System.out.println("3. Quit");
		    System.out.println();
		    System.out.println("Please enter the task number [1-3]:");
		    int n = reader.nextInt();
		    if(n==3)break;
	
		    MImage img = new MImage(args[0]);
			int[] rgb = new int[3];
		    if(n==1) {
				// 24 bits to 8 bits gray scale
			    img = new MImage(args[0]);
				int gray;
				for(int x=0;x<img.getW();x++) {
					for(int y=0;y<img.getH();y++) {
						img.getPixel(x, y, rgb);
						gray = (int) Math.round(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
						rgb[0]=gray;
						rgb[1]=gray;
						rgb[2]=gray;
						img.setPixel(x, y, rgb);
					}
				}
				img.write2PPM(args[0]+"_gray.ppm");
		    }
			if(n==2) {
				//Generate and display the 8-bit color Look Up Table (LUT)
				int[][] lut = new int[3][256];
				System.out.println("LUT by UCQ");
				System.out.println("Index    R    G    B");
				
				for(int x=0;x<256;x++) {
					lut[0][x]=32*(x/32)+16;
					lut[1][x]=32*(x%32/4)+16;
					lut[2][x]=64*(x%4)+32;
					System.out.printf("%5d",x);
					System.out.printf("%5d",lut[0][x]);
					System.out.printf("%5d",lut[1][x]);
					System.out.printf("%5d",lut[2][x]);
					System.out.println();
				}
				
				// Convert the input (24-bit pixels) to 8-bit indexed values and save index values in PPM format
				img = new MImage(args[0]);
				int[] index_value = new int[img.getH()*img.getW()];
				int index = 0;
				for(int x=0;x<img.getW();x++) {
					for(int y=0;y<img.getH();y++) {
						img.getPixel(x, y, rgb);
						index_value[index]=rgb[0]/32*(int)Math.pow(2, 5) + rgb[1]/32*(int)Math.pow(2,2) + rgb[2]/64;
						rgb[0]=index_value[index];
						rgb[1]=index_value[index];
						rgb[2]=index_value[index];
						img.setPixel(x, y, rgb);
						index++;
					}
				}
				img.write2PPM(args[0]+"-index.ppm");
				
				//Save the quantized RGB pixels in PPM format
				img = new MImage(args[0]);
				index=0;
				for(int x=0;x<img.getW();x++) {
					for(int y=0;y<img.getH();y++) {
						rgb[0]=lut[0][index_value[index]];
						rgb[1]=lut[1][index_value[index]];
						rgb[2]=lut[2][index_value[index]];
						img.setPixel(x, y, rgb);
						index++;
					}
				}
				img.write2PPM(args[0]+"-QT8.ppm");
			}
	    }

	    System.out.println("--Good Bye--");
	  }

	  public static void usage()
	  {
	    System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	  }  
}
