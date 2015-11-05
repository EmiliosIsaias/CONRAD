package CTRecon;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class phantomDiff{
	Grid2D img1;
	Grid2D img2;
	
	public phantomDiff(Grid2D image1, Grid2D image2){
		double min = minimum(image2);
		double max = maximum(image2);
		double min2 = minimum(image1);
		double max2 = maximum(image1);
		for(int i=0;i<image1.getSize()[0];i++){
			for(int j=0;j<image1.getSize()[1];j++){
				image2.subAtIndex(i, j, (float) min);
				image2.divideAtIndex(i, j, (float) max);
				image1.subAtIndex(i, j, (float) min2);
				image1.divideAtIndex(i, j, (float) max2);
			}
		}
		this.img1 = image1;
		this.img2 = image2;
	}
	
	public Grid2D diffBetween(){
		Grid2D res = img1;
		int[] x_y = this.img1.getSize();
		int[] aux = this.img2.getSize();
		if(x_y[0] == aux[0] && x_y[1] == aux[1]){
			for(int i=0;i<x_y[0];i++){
				for(int j=0;j<x_y[1];j++){
					res.setAtIndex(i, j, img1.getAtIndex(i, j) - img2.getAtIndex(i, j));
				}
			}
		}
		return res;
	}
	
	public double minimum(Grid2D img){
		double min = img.getAtIndex(0, 0);
		for(int i=0;i<img.getSize()[0];i++){
			for(int j=0;j<img.getSize()[1];j++){
				if(min > img.getAtIndex(i, j)){
					min = img.getAtIndex(i, j);
				}
			}
		}
		return min;
	}
	
	public double maximum(Grid2D img){
		double max = img.getAtIndex(0, 0);
		for(int i=0;i<img.getSize()[0];i++){
			for(int j=0;j<img.getSize()[1];j++){
				if(max < img.getAtIndex(i, j)){
					max = img.getAtIndex(i, j);
				}
			}
		}
		return max;
	}
}