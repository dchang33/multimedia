import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CS4551_Chang3 {
    public static void main(String[] args) throws IOException{
    	if(args.length == 0)
	    {
	      usage();
	      System.exit(1);
	    }
	    Scanner reader = new Scanner(System.in);
	    
	    System.out.println("--Welcome to Multimedia Software System--");
	    
	    MImage img = new MImage(args[0]);
    	MImage targetImg = new MImage(args[1]);
    	MImage staticImg = new MImage("Walk_005.ppm");
    	MImage errorBlock = new MImage(targetImg.getW(),targetImg.getH());
    	grayscale(img);
    	grayscale(targetImg);
    	grayscale(staticImg);
	    
	    while(true) {

		    System.out.println();
		    System.out.println("1. Block-Based Motion Compensation");
		    System.out.println("2. Logarithmic Search Motion Compensation");
		    System.out.println("3. Half-Pixel Accuracy Motion Compensation");
		    System.out.println("4. Removing Moving Objects");
		    System.out.println("5. Quit");
		    System.out.println();
		    System.out.println("Please enter the task number [1-5]:");
		    int n = reader.nextInt();
		    if(n==5)break;
		    
		    if(n==1) {
		    	System.out.println("Please enter 8, 16, or 24 for the macro block size:");
			    int block_size = reader.nextInt();
			    if(block_size!=8 && block_size!=16 && block_size!=24) {
			    	System.out.println("Input Error. Please enter 8, 16, or 24");
			    	continue;
			    }
		    	System.out.println("Please enter 2, 4, 8, 12, or 16 for search window:");
		    	int p_search = reader.nextInt();
			    if(p_search!=2 && p_search!=4 && p_search!=8 && p_search!=12 && p_search!=16) {
			    	System.out.println("Input Error. Please enter 2, 4, 8, 12, or 16");
			    	continue;
			    }
		    	
		    	int[][][] motionVector = new int[targetImg.getW()/block_size][targetImg.getH()/block_size][2];
		    	
		    	motionCompensation(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	print_motion_vector(motionVector,args[0],args[1]);
		    	errorBlock.write2PPM("error_img.ppm");
		    	
		    }
		    else if(n==2) {
		    	System.out.println("Please enter 8, 16, or 24 for the macro block size:");
			    int block_size = reader.nextInt();
			    if(block_size!=8 && block_size!=16 && block_size!=24) {
			    	System.out.println("Input Error. Please enter 8, 16, or 24");
			    	continue;
			    }
		    	System.out.println("Please enter 2, 4, 8, 12, or 16 for search window:");
		    	int p_search = reader.nextInt();
			    if(p_search!=2 && p_search!=4 && p_search!=8 && p_search!=12 && p_search!=16) {
			    	System.out.println("Input Error. Please enter 2, 4, 8, 12, or 16");
			    	continue;
			    }
		    	
		    	int[][][] motionVector = new int[targetImg.getW()/block_size][targetImg.getH()/block_size][2];
		    	
		    	logarithmic_search(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	print_motion_vector(motionVector,args[0],args[1]);
		    	errorBlock.write2PPM("error_logarithmic_img.ppm");
		    }else if(n==3) {
		    	System.out.println("Please enter 8, 16, or 24 for the macro block size:");
			    int block_size = reader.nextInt();
			    if(block_size!=8 && block_size!=16 && block_size!=24) {
			    	System.out.println("Input Error. Please enter 8, 16, or 24");
			    	continue;
			    }
		    	System.out.println("Please enter 2, 4, 8, 12, or 16 for search window:");
		    	int p_search = reader.nextInt();
			    if(p_search!=2 && p_search!=4 && p_search!=8 && p_search!=12 && p_search!=16) {
			    	System.out.println("Input Error. Please enter 2, 4, 8, 12, or 16");
			    	continue;
			    }
		    	
		    	double[][][] motionVector = new double[targetImg.getW()/block_size][targetImg.getH()/block_size][2];
		    	
		    	half_pixel_motion_compensation(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	print_motion_vector(motionVector,args[0],args[1]);
		    	errorBlock.write2PPM("error_half_pixel_img.ppm");
		    }
		    else if(n==4) {
		    	System.out.println("Please enter a frame number n between 19 ~ 179:");
			    int index_number = reader.nextInt();
			    String img_index = String.format("%03d", index_number-2);
			    String img_index_target = String.format("%03d", index_number);
		    	System.out.println("Please enter 8, 16, or 24 for the macro block size:");
			    int block_size = reader.nextInt();
			    if(block_size!=8 && block_size!=16 && block_size!=24) {
			    	System.out.println("Input Error. Please enter 8, 16, or 24");
			    	continue;
			    }
		    	System.out.println("Please enter 2, 4, 8, 12, or 16 for search window:");
		    	int p_search = reader.nextInt();
			    if(p_search!=2 && p_search!=4 && p_search!=8 && p_search!=12 && p_search!=16) {
			    	System.out.println("Input Error. Please enter 2, 4, 8, 12, or 16");
			    	continue;
			    }
			    System.out.println();
			    System.out.println("1. Block-based Motion Compensation");
			    System.out.println("2. Logarithmic Search");
			    System.out.println("Please enter a mehtod:");
		    	int motion_method = reader.nextInt();
		    	
		    	int[][][] motionVector = new int[targetImg.getW()/block_size][targetImg.getH()/block_size][2];
			    
		    	img = new MImage("Walk_" + img_index + ".ppm");
		    	targetImg = new MImage("Walk_" + img_index_target + ".ppm");
		    	grayscale(img);
		    	grayscale(targetImg);
		    	
		    	if(motion_method==1) {
			    	motionCompensation(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}else {
		    		logarithmic_search(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}
		    	identify_dynamic_blocks(targetImg,motionVector,block_size);
		    	targetImg.write2PPM("dynamic_img.ppm");
		    	
		    	if(motion_method==1) {
			    	motionCompensation(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}else {
		    		logarithmic_search(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}
		    	remove_moving_object_by_staticblock(staticImg,targetImg,motionVector,block_size);
		    	targetImg.write2PPM("removed_staticblock.ppm");
		    	
		    	if(motion_method==1) {
			    	motionCompensation(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}else {
		    		logarithmic_search(img,targetImg,block_size,p_search,motionVector,errorBlock);
		    	}
		    	remove_moving_object_by_self_staticblock(targetImg,motionVector,block_size);
		    	targetImg.write2PPM("removed_self_staticblock.ppm");
		    }
	    }
    	
    }
    static void print_motion_vector(int [][][] motionVector, String targetImg, String refImage) throws IOException{
    	PrintWriter printWriter = new PrintWriter("Motion_Vector.txt");
    	
    	printWriter.println("# Name: Daniel Chang");
    	printWriter.println("# Target image name: "+targetImg);
    	printWriter.println("# Reference image name: "+refImage);
    	printWriter.println("# Number of target macro blocks: "+ motionVector.length +" x "+motionVector[0].length);
    	printWriter.println();
    	
    	int matches = 0;
    	for(int i=0;i<motionVector.length;i++) {
    		for(int j=0;j<motionVector[0].length;j++) {
    		    printWriter.print("[" + motionVector[i][j][0] + "," + motionVector[i][j][1]+"]");
    		    if(motionVector[i][j][0]!=0 || motionVector[i][j][1]!=0)
    		    	matches++;
    		}
    		printWriter.println();
    	}
    	printWriter.println();
    	printWriter.println("The number of matches: "+matches);
    	printWriter.close();
    }
    static void print_motion_vector(double [][][] motionVector, String targetImg, String refImage) throws IOException{
    	PrintWriter printWriter = new PrintWriter("Motion_Vector.txt");
    	
    	printWriter.println("# Name: Daniel Chang");
    	printWriter.println("# Target image name: "+targetImg);
    	printWriter.println("# Reference image name: "+refImage);
    	printWriter.println("# Number of target macro blocks: "+ motionVector.length +" x "+motionVector[0].length);
    	printWriter.println();
    	
    	int matches = 0;
    	for(int i=0;i<motionVector.length;i++) {
    		for(int j=0;j<motionVector[0].length;j++) {
    		    printWriter.print("[" + motionVector[i][j][0] + "," + motionVector[i][j][1]+"]");
    		    if(motionVector[i][j][0]!=0 || motionVector[i][j][1]!=0)
    		    	matches++;
    		}
    		printWriter.println();
    	}
    	printWriter.println();
    	printWriter.println("The number of matches: "+matches);
    	printWriter.close();
    }   
    static void grayscale(MImage img) {
    	int gray;
    	int[] rgb = new int[3];
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
    }
    static void motionCompensation(MImage img, MImage targetImg,int block_size, int p_search, int[][][] motionVector, MImage errorBlock) {
    	int[] rgb = new int[3];
    	int[] searchRGB = new int[3];
    	int minError=99999, maxError=0;
    	for(int x=0;x<img.getW()/block_size;x++) {
			for(int y=0;y<img.getH()/block_size;y++) {
				double MSD = 99999999;
				for(int searchX=-1*p_search;searchX<=p_search;searchX++) {
					for(int searchY=-1*p_search;searchY<=p_search;searchY++) {
						double sum = 0;
						for(int i=0;i<block_size;i++) {
							for(int j=0;j<block_size;j++) {
								int tempX=x*block_size+i,tempY=y*block_size+j;
								targetImg.getPixel(tempX, tempY, rgb);
								img.getPixel(tempX+searchX, tempY+searchY, searchRGB);
								sum += Math.pow(rgb[0]-searchRGB[0],2);
							}
						}
						sum/=block_size*block_size;
						if(sum<MSD) {
							motionVector[x][y][0]=searchX;
							motionVector[x][y][1]=searchY;
							MSD=sum;
							for(int i=0;i<block_size;i++) {
								for(int j=0;j<block_size;j++) {
									int error=0;
									int tempX=x*block_size+i,tempY=y*block_size+j;
									targetImg.getPixel(tempX, tempY, rgb);
									img.getPixel(tempX+searchX, tempY+searchY, searchRGB);
									error = Math.abs(rgb[0]-searchRGB[0]);
									if(error<minError)minError=error;
									if(error>maxError)maxError=error;
									rgb[0]=error;
									rgb[1]=error;
									rgb[2]=error;
									errorBlock.setPixel(tempX, tempY, rgb);
								}
							}
						}
					}
				}
			}
		}
    	for(int i=0;i<errorBlock.getW();i++) {
			for(int j=0;j<errorBlock.getH();j++) {
				int scaledError=0;
				errorBlock.getPixel(i, j, rgb);
				int sub = maxError-minError;
				if(sub==0)sub=1;
				scaledError = (rgb[0]-minError)*255/sub;
				rgb[0]=scaledError;
				rgb[1]=scaledError;
				rgb[2]=scaledError;
				errorBlock.setPixel(i, j, rgb);
			}
		}
    }
    static void half_pixel_motion_compensation(MImage img, MImage targetImg,int block_size, int p_search, double[][][] motionVector, MImage errorBlock) {
    	int[] rgb = new int[3];
    	int[] searchRGB = new int[3];
    	int minError=99999, maxError=0;
    	for(int x=0;x<img.getW()/block_size;x++) {
			for(int y=0;y<img.getH()/block_size;y++) {
				double MSD = 99999999;
				for(double searchX=-1*p_search;searchX<=p_search;searchX+=0.5) {
					for(double searchY=-1*p_search;searchY<=p_search;searchY+=0.5) {
						double sum = 0;
						for(int i=0;i<block_size;i++) {
							for(int j=0;j<block_size;j++) {
								int tempX=x*block_size+i,tempY=y*block_size+j;
								int half_pixel = 0;
								if(searchX%1==0.5 && searchY%1==0.5) {
									img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)(searchY-0.5), searchRGB);
									half_pixel += searchRGB[0];
									img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)(searchY-0.5), searchRGB);
									half_pixel += searchRGB[0];
									img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)(searchY+0.5), searchRGB);
									half_pixel += searchRGB[0];
									img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)(searchY+0.5), searchRGB);
									half_pixel += searchRGB[0];
									half_pixel /= 4;
								}else if(searchX%1==0.5 && searchY%1==0){
									img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)searchY, searchRGB);
									half_pixel += searchRGB[0];
									img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)searchY, searchRGB);
									half_pixel += searchRGB[0];
									half_pixel /= 2;
								}else if(searchX%1==0 && searchY%1==0.5) {
									img.getPixel(tempX+(int)searchX, tempY+(int)(searchY-0.5), searchRGB);
									half_pixel += searchRGB[0];
									img.getPixel(tempX+(int)searchX, tempY+(int)(searchY+0.5), searchRGB);
									half_pixel += searchRGB[0];
									half_pixel /= 2;
								}else {
									img.getPixel(tempX+(int)searchX, tempY+(int)searchY, searchRGB);
									half_pixel=searchRGB[0];
								}
								targetImg.getPixel(tempX, tempY, rgb);
								sum += Math.pow(rgb[0]-half_pixel,2);
							}
						}
						sum/=block_size*block_size;
						if(sum<MSD) {
							motionVector[x][y][0]=searchX;
							motionVector[x][y][1]=searchY;
							MSD=sum;
							for(int i=0;i<block_size;i++) {
								for(int j=0;j<block_size;j++) {
									int error=0;
									int tempX=x*block_size+i,tempY=y*block_size+j;
									targetImg.getPixel(tempX, tempY, rgb);
									int half_pixel = 0;
									if(searchX%1==0.5 && searchY%1==0.5) {
										img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)(searchY-0.5), searchRGB);
										half_pixel += searchRGB[0];
										img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)(searchY-0.5), searchRGB);
										half_pixel += searchRGB[0];
										img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)(searchY+0.5), searchRGB);
										half_pixel += searchRGB[0];
										img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)(searchY+0.5), searchRGB);
										half_pixel += searchRGB[0];
										half_pixel /= 4;
									}else if(searchX%1==0.5 && searchY%1==0){
										img.getPixel(tempX+(int)(searchX-0.5), tempY+(int)searchY, searchRGB);
										half_pixel += searchRGB[0];
										img.getPixel(tempX+(int)(searchX+0.5), tempY+(int)searchY, searchRGB);
										half_pixel += searchRGB[0];
										half_pixel /= 2;
									}else if(searchX%1==0 && searchY%1==0.5) {
										img.getPixel(tempX+(int)searchX, tempY+(int)(searchY-0.5), searchRGB);
										half_pixel += searchRGB[0];
										img.getPixel(tempX+(int)searchX, tempY+(int)(searchY+0.5), searchRGB);
										half_pixel += searchRGB[0];
										half_pixel /= 2;
									}else {
										img.getPixel(tempX+(int)searchX, tempY+(int)searchY, searchRGB);
										half_pixel = searchRGB[0];
									}
									error = Math.abs(rgb[0]-half_pixel);
									if(error<minError)minError=error;
									if(error>maxError)maxError=error;
									rgb[0]=error;
									rgb[1]=error;
									rgb[2]=error;
									errorBlock.setPixel(tempX, tempY, rgb);
								}
							}
						}
					}
				}
			}
		}
    	for(int i=0;i<errorBlock.getW();i++) {
			for(int j=0;j<errorBlock.getH();j++) {
				int scaledError=0;
				errorBlock.getPixel(i, j, rgb);
				int sub = maxError-minError;
				if(sub==0)sub=1;
				scaledError = (rgb[0]-minError)*255/sub;
				rgb[0]=scaledError;
				rgb[1]=scaledError;
				rgb[2]=scaledError;
				errorBlock.setPixel(i, j, rgb);
			}
		}
    }
    static void identify_dynamic_blocks(MImage img,int[][][] motionVector,int block_size) {
    	int[] rgb = new int[3];
    	for(int i=0;i<motionVector.length;i++) {
    		for(int j=0;j<motionVector[0].length;j++) {
    			if(motionVector[i][j][0]==0&&motionVector[i][j][1]==0)continue;
    			for(int x=0;x<8;x++) {
    				for(int y=0;y<8;y++) {
    					int tempX = i*8+x;
    					int tempY = j*8+y;
    					img.getPixel(tempX, tempY, rgb);
    					rgb[0]=255;
    					img.setPixel(tempX, tempY, rgb);
    				}
    			}
    		}
    	}
    }
    static void remove_moving_object_by_staticblock(MImage img,MImage targetImg,int[][][] motionVector,int block_size) {
    	int[] rgb = new int[3];
    	for(int i=0;i<motionVector.length;i++) {
    		for(int j=0;j<motionVector[0].length;j++) {
    			if(motionVector[i][j][0]==0&&motionVector[i][j][1]==0)continue;
    			for(int x=0;x<8;x++) {
    				for(int y=0;y<8;y++) {
    					int tempX = i*8+x;
    					int tempY = j*8+y;
    					img.getPixel(tempX, tempY, rgb);
    					targetImg.setPixel(tempX, tempY, rgb);
    				}
    			}
    		}
    	}
    }
    static void remove_moving_object_by_self_staticblock(MImage targetImg,int[][][] motionVector,int block_size) {
    	int[] rgb = new int[3];
    	for(int i=0;i<motionVector.length;i++) {
    		for(int j=0;j<motionVector[0].length;j++) {
    			if(motionVector[i][j][0]==0&&motionVector[i][j][1]==0)continue;
    	    	double min=99999999;
    			for(int i2=0;i2<motionVector.length;i2++) {
    	    		for(int j2=0;j2<motionVector[0].length;j2++) {
    	    			if(motionVector[i2][j2][0]==0&&motionVector[i2][j2][1]==0) {
    	    				double dist = Math.sqrt(Math.pow((i-i2), 2)+Math.pow((j-j2), 2));
    	    				if(dist<min) {
    	    					min = dist;
    	    					for(int x=0;x<8;x++) {
    	    	    				for(int y=0;y<8;y++) {
    	    	    					int tempX = i*8+x;
    	    	    					int tempY = j*8+y;
    	    	    					int temp2X = i2*8+x;
    	    	    					int temp2Y = j2*8+y;
    	    	    					targetImg.getPixel(temp2X, temp2Y, rgb);
    	    	    					targetImg.setPixel(tempX, tempY, rgb);
    	    	    				}
    	    	    			}
    	    				}
    	    			}
    	    		}
    			}
    			
    		}
    	}
    }
    static void logarithmic_search(MImage img, MImage targetImg,int block_size, int p_search, int[][][] motionVector, MImage errorBlock) {
    	int[] rgb = new int[3];
    	int[] searchRGB = new int[3];
    	int minError=99999, maxError=0;
    	for(int x=0;x<img.getW()/block_size;x++) {
			for(int y=0;y<img.getH()/block_size;y++) {
				int search = p_search;
				int targetX=x*block_size,targetY=y*block_size;
				int newX=targetX,newY=targetY;
				while(search!=0) {
					int bestX = 0, bestY = 0;
					double MSD = 99999999;
					for(int searchX=-1;searchX<2;searchX++) {
						for(int searchY=-1;searchY<2;searchY++) {
							double sum = 0;
							int tempX=newX+searchX*search,tempY=newY+searchY*search;
							
							for(int i=0;i<block_size;i++) {
								for(int j=0;j<block_size;j++) {
									targetImg.getPixel(targetX+i, targetY+j, rgb);
									img.getPixel(tempX+i, tempY+j, searchRGB);
									sum += Math.pow(rgb[0]-searchRGB[0],2);
								}
							}					
							sum/=block_size*block_size;
							if(sum<MSD) {
								bestX = tempX;
								bestY = tempY;
								MSD=sum;
							}
						}
					}
					newX = bestX;
					newY = bestY;
					search /= 2;
				}
				motionVector[x][y][0]=newX-targetX;
				motionVector[x][y][1]=newY-targetY;
				for(int i=0;i<block_size;i++) {
					for(int j=0;j<block_size;j++) {
						int error=0;
						int tempX=targetX+i,tempY=targetY+j;
						targetImg.getPixel(tempX, tempY, rgb);
						img.getPixel(newX+i, newY+j, searchRGB);
						error = Math.abs(rgb[0]-searchRGB[0]);
						if(error<minError)minError=error;
						if(error>maxError)maxError=error;
						rgb[0]=error;
						rgb[1]=error;
						rgb[2]=error;
						errorBlock.setPixel(tempX, tempY, rgb);
					}
				}
			}
		}
    	for(int i=0;i<errorBlock.getW();i++) {
			for(int j=0;j<errorBlock.getH();j++) {
				int scaledError=0;
				errorBlock.getPixel(i, j, rgb);
				int sub = maxError-minError;
				if(sub==0)sub=1;
				scaledError = (rgb[0]-minError)*255/sub;
				rgb[0]=scaledError;
				rgb[1]=scaledError;
				rgb[2]=scaledError;
				errorBlock.setPixel(i, j, rgb);
			}
		}
    }
    public static void usage()
	  {
	    System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	  }  
}