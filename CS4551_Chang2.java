import java.util.Scanner;

public class CS4551_Chang2 {
    public static void main(String[] args) {
    	if(args.length != 1)
	    {
	      usage();
	      System.exit(1);
	    }
	    Scanner reader = new Scanner(System.in);
	    System.out.println("--Welcome to Multimedia Software System--");
	    
    	double[][] luminance= new double[][] {
    			{4,4,4,8,8,16,16,32},
    			{4,4,4,8,8,16,16,32},
    			{4,4,8,8,16,16,32,32},
    			{8,8,8,16,16,32,32,32},
    			{8,8,16,16,32,32,32,32},
    			{16,16,16,32,32,32,32,32},
    			{16,16,32,32,32,32,32,32},
    			{32,32,32,32,32,32,32,32}};
    	double[][] chrominance= new double[][] {
				{8,8,8,16,32,32,32,32},
				{8,8,8,16,32,32,32,32},
				{8,8,16,32,32,32,32,32},
				{16,16,32,32,32,32,32,32},
				{32,32,32,32,32,32,32,32},
				{32,32,32,32,32,32,32,32},
				{32,32,32,32,32,32,32,32},
				{32,32,32,32,32,32,32,32}};
    	

    	while(true) {

		    System.out.println();
		    System.out.println("1. VQ (Vector Quantization)");
		    System.out.println("2. DCT-based Coding");
		    System.out.println("3. Quit");
		    System.out.println();
		    System.out.println("Please enter the task number [1-3]:");
		    int n = reader.nextInt();
		    if(n==3)break;
		    
		    if(n==1) {
		    	MImage img = new MImage(args[0]);
		    	int vqW = img.getW() + (img.getW()%2);
		    	int vqH = img.getH() + (img.getH()%2);
		    	MImage vqImg = new MImage(vqW,vqH);
		    	MImage vqQuantizedImg = new MImage(vqW/2,vqH/2);
		    	double[][] vector = new double[vqW*vqH/4][13];
		    	double[][] kmeans = new double[256][12];
		    	double[][] newKmeans = new double[256][12];
		    	
		    	resizeInputImage(img,vqImg);
		    	storeAllVectors(vqImg,vector);
		    	System.out.println("vector:");
		    	initialKmeans(kmeans);
		    	int times = 0;
		    	while(true) {
		    		times++;
		        	clustering(kmeans,vector);
		        	updateKCentroids(kmeans,vector,newKmeans);
		        	if(kmeans==newKmeans||times==20)break;
		        	else{
		        		for(int i=0;i<256;i++) {
		            		for(int j=0;j<12;j++) {
		                		kmeans[i][j] = newKmeans[i][j];
		            		}
		            	}
		        	}
		    	}
				System.out.println("Code Book:");
		    	for(int i=0;i<kmeans.length;i++) {
		        	System.out.print(i+": ");
		    		for(int j=0;j<12;j++) {
		    			System.out.printf("%8.1f",kmeans[i][j]);
		    		}
					System.out.println();
		    	}
		    	System.out.println("times: "+times);
		    	vqQuantizedImage(vector,vqQuantizedImg);
		    	vqOutputImage(kmeans,vector,vqImg);
		    }
		    if(n==2) {
		    	MImage img = new MImage(args[0]);
		    	int newW = img.getW() + (8-img.getW()%8);
		    	int newH = img.getH() + (8-img.getH()%8);
		    	MImage newImg = new MImage(newW,newH);
		    	double[][][] YCbCr = new double[newW][newH][3];
		    	double[][] newImgY = new double[newW][newH];
		    	double[][] newImgCb = new double[newW/2+ (8-newW/2%8)][newH/2+ (8-newW/2%8)];
		    	double[][] newImgCr = new double[newW/2+ (8-newW/2%8)][newH/2+ (8-newW/2%8)];
		    	
		    	System.out.println("Input Compress Ratio:");
		    	int compressRatio = reader.nextInt();
		    
		    	resizeInputImage(img,newImg);
		    	RGB2YCbCr(newImg,YCbCr);
		    	subSampling(YCbCr,newImgY,newImgCb,newImgCr);
		    	
		    	eightByEight(newImgY,true);
		    	eightByEight(newImgCb,true);
		    	eightByEight(newImgCr,true);
		
		    	eightByEightQuantization(newImgY,luminance,compressRatio);
		    	eightByEightQuantization(newImgCb,chrominance,compressRatio);
		    	eightByEightQuantization(newImgCr,chrominance,compressRatio);
		    	
		    	eightByEightDequantization(newImgY,luminance,compressRatio);
		    	eightByEightDequantization(newImgCb,chrominance,compressRatio);
		    	eightByEightDequantization(newImgCr,chrominance,compressRatio);
		    		
		    	eightByEight(newImgY,false);
		    	eightByEight(newImgCb,false);
		    	eightByEight(newImgCr,false);
		    	
		    	superSampling(YCbCr,newImgY,newImgCb,newImgCr);
		    	YCbCr2RGB(YCbCr,newImg);
		    	cropInputImage(newImg,img);
		    	
		    	img.write2PPM("DCT_based.ppm");
		    }
    	}
	    
    }
    public static void usage()
	  {
	    System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	  }  
    //task 1
    static void vqQuantizedImage(double[][] vector,MImage img) {
    	int num=0;
    	int[] rgb = new int[3];
    	for(int i=0;i<img.getW();i++) {
    		for(int j=0;j<img.getH();j++) {
    			rgb[0]=(int)vector[num][12];
    			rgb[1]=(int)vector[num][12];
    			rgb[2]=(int)vector[num][12];
    			img.setPixel(i, j, rgb);
    			num++;
    		}
    	}
		img.write2PPM("VQ-Encode-GrayScale.ppm");
    }
    static void vqOutputImage(double[][] kmeans, double[][] vector,MImage img) {
		int num = 0;
		int[] rgb = new int[3];
		for(int x=0;x<img.getW()/2;x++) {
			for(int y=0;y<img.getH()/2;y++) {
				int index =(int)vector[num][12];
				for(int i=0;i<2;i++) {
					for(int j=0;j<2;j++) {
				    	if(i==0&&j==0) {
				    		rgb[0]=(int)kmeans[index][0];
				    		rgb[1]=(int)kmeans[index][1];
				    		rgb[2]=(int)kmeans[index][2];
				    	}else if(i==1&&j==0) {
				    		rgb[0]=(int)kmeans[index][3];
				    		rgb[1]=(int)kmeans[index][4];
				    		rgb[2]=(int)kmeans[index][5];
				    	}else if(i==0&&j==1) {
				    		rgb[0]=(int)kmeans[index][6];
				    		rgb[1]=(int)kmeans[index][7];
				    		rgb[2]=(int)kmeans[index][8];
				    	}else if(i==1&&j==1) {
				    		rgb[0]=(int)kmeans[index][9];
				    		rgb[1]=(int)kmeans[index][10];
				    		rgb[2]=(int)kmeans[index][11];
				    	}
						img.setPixel(x*2+i, y*2+j, rgb);
					}
				}
				num++;
			}
		}
		img.write2PPM("VQ-Decode.ppm");
    }
    static void storeAllVectors(MImage img,double[][] vector) {
    	int[] rgb = new int[3];
    	int num = 0;
    	for(int x=0;x<img.getW()/2;x++) {
			for(int y=0;y<img.getH()/2;y++) {
				for(int i=0;i<2;i++) {
					for(int j=0;j<2;j++) {
				    	img.getPixel(x*2+i, y*2+j, rgb);
				    	if(i==0&&j==0) {
				    		vector[num][0]=rgb[0];
				    		vector[num][1]=rgb[1];
				    		vector[num][2]=rgb[2];
				    	}else if(i==1&&j==0) {
				    		vector[num][3]=rgb[0];
				    		vector[num][4]=rgb[1];
				    		vector[num][5]=rgb[2];
				    	}else if(i==0&&j==1) {
				    		vector[num][6]=rgb[0];
				    		vector[num][7]=rgb[1];
				    		vector[num][8]=rgb[2];
				    	}else if(i==1&&j==1) {
				    		vector[num][9]=rgb[0];
				    		vector[num][10]=rgb[1];
				    		vector[num][11]=rgb[2];
				    	}
					}
				}
		    	num++;
			}
		}
    }
    static void initialKmeans(double[][] kmeans) {
    	for(int i=0;i<256;i++) {
    		for(int j=0;j<12;j++) {
    			kmeans[i][j] = Math.round(Math.random() * 256);
    		}
    	}
    }
    static void clustering(double[][] kmeans, double[][] vector) {
    	for(int i=0;i<vector.length;i++) {
    		double smallest = 0;
    		int index = 0;
    		for(int j=0;j<256;j++) {
    			double dist = 0;
    			for(int k=0;k<12;k++) {
    				dist += Math.pow((vector[i][k] - kmeans[j][k]), 2);
    			}
    			dist = Math.sqrt(dist);
    			if(dist<smallest) {
    				smallest=dist;
    				index = j;
    			}else if(smallest==0) {
    				smallest=dist;
    				index = j;
    			}
    		}
    		vector[i][12]=index;
    	}
    }
    static void updateKCentroids(double[][] kmeans, double[][] vector, double[][] newKmeans) {
    	double[][] sumK = new double[256][13];
    	for(int i=0;i<256;i++) {
    		for(int j=0;j<13;j++) {
    			sumK[i][j]=0;
    		}
    	}
    	for(int i=0;i<vector.length;i++) {
			int index = (int)vector[i][12];
    		for(int j=0;j<12;j++) {
    			sumK[index][j] += vector[i][j];
    		}
			sumK[index][12] += 1;
    	}
    	for(int i=0;i<256;i++) {
    		int sum=0;
    		for(int j=0;j<12;j++) {
    			newKmeans[i][j] = Math.round(sumK[i][j]/sumK[i][12]);
    			sum += sumK[i][j];
    		}
    		if(sum==0) {
    			for(int j=0;j<12;j++) {
    				newKmeans[i][j] = Math.round(Math.random() * 256);
        		}
    		}
    	}
    }
    //task 2
    static void setPixels(double[][][] YCbCr, MImage img) {
    	int[] rgb = new int[3];
    	for(int x=0;x<YCbCr.length;x++) {
			for(int y=0;y<YCbCr[1].length;y++) {
				rgb[0]=(int)YCbCr[x][y][0];
				rgb[1]=(int)YCbCr[x][y][1];
				rgb[2]=(int)YCbCr[x][y][2];
				img.setPixel(x, y, rgb);
			}
		}
    }
    static void setImgPixels(MImage img,double[][][] YCbCr) {
    	int[] rgb = new int[3];
    	for(int x=0;x<img.getW();x++) {
			for(int y=0;y<img.getH();y++) {
				img.getPixel(x, y, rgb);
				YCbCr[x][y][0]=rgb[0];
				YCbCr[x][y][1]=rgb[1];
				YCbCr[x][y][2]=rgb[2];
			}
		}
    }
    static void eightByEightQuantization(double[][] channel,double[][] quantizationTable, int ratio) {
    	for(int x=0;x<channel.length/8;x++) {
			for(int y=0;y<channel[1].length/8;y++) {
				for(int i=0;i<8;i++) {
					for(int j=0;j<8;j++) {
						channel[8*x+i][8*y+j]
								=Math.round(channel[8*x+i][8*y+j]/(quantizationTable[i][j]*Math.pow(2, ratio)));
					}
				}
			}
		}
    }
    static void eightByEightDequantization(double[][] channel,double[][] quantizationTable, int ratio) {
    	for(int x=0;x<channel.length/8;x++) {
			for(int y=0;y<channel[1].length/8;y++) {
				for(int i=0;i<8;i++) {
					for(int j=0;j<8;j++) {
						channel[8*x+i][8*y+j]=channel[8*x+i][8*y+j]*(quantizationTable[i][j]*Math.pow(2, ratio));
					}
				}
			}
		}
    }
    static void eightByEight(double[][] channel,boolean DCT) {
    	double[][] temp = new double[8][8];
    	for(int x=0;x<channel.length/8;x++) {
			for(int y=0;y<channel[1].length/8;y++) {
				for(int i=0;i<8;i++) {
					for(int j=0;j<8;j++) {
						if(DCT) {
							temp[i][j]=discrete_cosine_transform_2D(i,j,channel,8*x,8*y);
						}
						else
							temp[i][j]=inverse_discrete_cosine_transform_2D(i,j,channel,8*x,8*y);
					}
				}
				for(int i=0;i<8;i++) {
					for(int j=0;j<8;j++) {
						channel[8*x+i][8*y+j]=temp[i][j];
					}
				}
			}
		}
    }
    static void subSampling(double[][][] YCbCr, double[][] newImgY, double[][] newImgCb, double[][] newImgCr) {
    	double[] sumRGB = new double[3];
    	for(int x=0;x<YCbCr.length/2;x++) {
			for(int y=0;y<YCbCr[1].length/2;y++) {
				sumRGB[1]=0;
				sumRGB[2]=0;
				for(int i=0;i<2;i++) {
					for(int j=0;j<2;j++) {
				    	newImgY[x*2+i][y*2+j]=YCbCr[x*2+i][y*2+j][0];
						sumRGB[1] += YCbCr[x*2+i][y*2+j][1];
						sumRGB[2] += YCbCr[x*2+i][y*2+j][2];
					}
				}
				newImgCb[x][y] = sumRGB[1]/4;
				newImgCr[x][y] = sumRGB[2]/4;
			}
		}
    }
    static void superSampling(double[][][] YCbCr, double[][] newImgY, double[][] newImgCb, double[][] newImgCr) {
    	for(int x=0;x<YCbCr.length/2;x++) {
			for(int y=0;y<YCbCr[0].length/2;y++) {
				for(int i=0;i<2;i++) {
					for(int j=0;j<2;j++) {
						YCbCr[x*2+i][y*2+j][0]=newImgY[x*2+i][y*2+j];
						YCbCr[x*2+i][y*2+j][1]=newImgCb[x][y];
						YCbCr[x*2+i][y*2+j][2]=newImgCr[x][y];
					}
				}
			}
		}
    }
    static void RGB2YCbCr(MImage img, double[][][] YCbCr) {
    	int[] rgb = new int[3];
    	for(int x=0;x<img.getW();x++) {
			for(int y=0;y<img.getH();y++) {
		    	img.getPixel(x, y, rgb);
		    	YCbCr[x][y][0]= (0.2990*rgb[0] + 0.5870*rgb[1] + 0.1140*rgb[2]-128);
		    	YCbCr[x][y][1]= (-0.1687*rgb[0] -0.3313*rgb[1] + 0.5000*rgb[2]-0.5);
		    	YCbCr[x][y][2]= (0.5000*rgb[0] -0.4187*rgb[1] -0.0813*rgb[2]-0.5);
		    	for(int i=0;i<3;i++) {
			    	if(YCbCr[x][y][i]<-128)YCbCr[x][y][i]=-128;
			    	if(YCbCr[x][y][i]>127)YCbCr[x][y][i]=127;
		    	}
			}
		}
    }

    static void YCbCr2RGB(double[][][] YCbCr,MImage img) {
    	int[] rgb = new int[3];
    	for(int x=0;x<img.getW();x++) {
			for(int y=0;y<img.getH();y++) {
		    	double Y = YCbCr[x][y][0]+128;
		    	double Cb = YCbCr[x][y][1]+0.5;
		    	double Cr = YCbCr[x][y][2]+0.5;
		    	rgb[0]= (int) (1.0*Y +  0*Cb      + 1.402*Cr);
		    	rgb[1]= (int) (1.0*Y -  0.3441*Cb -0.7141*Cr);
		    	rgb[2]= (int) (1.0*Y +  1.7720*Cb +0*Cr);
		    	for(int i=0;i<3;i++) {
			    	if(rgb[i]<0)rgb[i]=0;
			    	if(rgb[i]>255)rgb[i]=255;
		    	}
		    	img.setPixel(x, y, rgb);
			}
		}
    }
    static void resizeInputImage(MImage img,MImage newImg) {
    	int[] rgb = new int[3];
    	
    	for(int x=0;x<newImg.getW();x++) {
			for(int y=0;y<newImg.getH();y++) {
				if(x>img.getW()||y>img.getH()) {
					rgb[0]=0;
					rgb[1]=0;
					rgb[2]=0;
					newImg.setPixel(x, y, rgb);
				}else {
					img.getPixel(x, y, rgb);
					newImg.setPixel(x, y, rgb);
				}
			}
		}
    }
    static void cropInputImage(MImage img,MImage newImg) {
    	int[] rgb = new int[3];
    	
    	for(int x=0;x<img.getW();x++) {
			for(int y=0;y<img.getH();y++) {
				img.getPixel(x, y, rgb);
				newImg.setPixel(x, y, rgb);
			}
		}
    }
    static double discrete_cosine_transform_2D(int u, int v, double[][] f,int blockX,int blockY) {
    	double sum = 0;
    	for(int i=0;i<8;i++) {
    		for(int j=0;j<8;j++) {
        		sum += Math.cos(((2*i+1)*u*Math.PI)/(16))* Math.cos(((2*j+1)*v*Math.PI)/(16)) *f[blockX+i][blockY+j];
    		}
    	}
    	double fu = c(u) * c(v) /4 * sum;
    	return fu;
    }
    static double inverse_discrete_cosine_transform_2D(int i, int j,double[][] f,int blockX,int blockY) {
    	double sum = 0;
    	for(int u=0;u<8;u++) {
    		for(int v=0;v<8;v++) {
    			sum += c(u)*c(v)*Math.cos((2*i+1)*u*Math.PI/(16))*Math.cos((2*j+1)*v*Math.PI/(16))*f[blockX+u][blockY+v];
    		}
    	}
    	sum = sum /4;
    	return sum;
    }  
    static double c(double u) {
    	double cu;
    	if(u==0)cu=Math.sqrt(2)/2;
    	else cu=1;
    	return cu;
    }

}