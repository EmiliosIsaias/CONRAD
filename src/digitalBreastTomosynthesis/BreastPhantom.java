/**
 * 
 */
package digitalBreastTomosynthesis;

/* TODO
 * 1.- Fix the breast phantom. CHECK!
 * 2.- Create detector class derived from the Grid2D class
 * 3.- Create a detector pixel class. Think about this implementation
 * 4.- Create a source point class.
 * 5.- Project with these created classes.  
 */

import ij.ImageJ;
import edu.stanford.rsl.conrad.data.numeric.Grid3D;

/**
 * @author Emilio Isaias-Camacho
 * @version 1.2
 *
 */
public class BreastPhantom extends Grid3D{
	/**
	 * Basic emulation for a breast phantom. Generating a 3D Gaussian.
	 */
	
	public BreastPhantom(int width, int height, int depth) {
		super(width, height, depth);
		new ImageJ();
		//Getting a standard deviation which gives a proper scale to the gauss curve.
		float sigmaY = height/7f;
		float sigmaZ = depth/7f;
		double x,y,z;
		double y_2,z_2;
		if ((depth/width) <= 1){
			for (int i = 0;i < width;i++){
				//Setting the x coordinate to be at the left edge of the image(s)
				x = i;
				for (int j = 0;j < height;j++){
					y = j - (height/2.0);
					y_2 = Math.pow(y, 2);
					for (int k = 0;k < depth;k++){
						z = k - (depth/2);
						z_2 = Math.pow(z, 2);
						//Creating a 2D gauss considering the x-axis to be the amplitude.
						double gauss = (1.0/Math.sqrt(2.0*Math.PI)*sigmaY*sigmaZ) *
								Math.exp(-((y_2/(2.f * Math.pow(sigmaY,2)))+(z_2/(2.f * Math.pow(sigmaZ,2)))));
						if (x <= gauss){super.addAtIndex(i,j,k,(float)gauss);}
						else{super.addAtIndex(i, j, k, 0.f);}
					}
				}
			}
		}
	}
	
	public static void main(String [] args){
		int width,height,depth;
		width = 200;
		height = 200;
		depth = 100;
		BreastPhantom bp = new BreastPhantom(width,height,depth);
		bp.show();
	}
}
