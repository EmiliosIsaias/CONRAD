/**
 * 
 */
package digitalBreastTomosynthesis;

import ij.ImageJ;
import edu.stanford.rsl.conrad.data.numeric.Grid3D;

/* TODO
 * 1.- Fix the breast phantom. CHECK!
 * 2.- Create detector class derived from the Grid2D class
 * 3.- Create a detector pixel class. Think about this implementation
 * 4.- Create a source point class.
 * 5.- Project with these created classes.  
 */

/**
 * @author Emilio Isaias-Camacho
 * @version 1.2
 *
 */
public class BreastPhantom extends Grid3D{
	/**
	 * Basic emulation for a breast phantom. Generating a 3D Gaussian.
	 */
	double[] dimensions;
	
	public BreastPhantom(int width, int height, int depth) {
		super(width, height, depth);
		this.setSpacing(1,1,1);
		this.setOrigin(width/2f,height/2f,depth/2f);
		/*
		dimensions[0] = width * getSpacing()[0];
		dimensions[1] = height * getSpacing()[1];
		dimensions[2] = depth * getSpacing()[2];
		*/
		double[] dims = {width*getSpacing()[0],height*getSpacing()[1],depth*getSpacing()[2]};
		this.dimensions = dims;
		//Getting a standard deviation which gives a proper scale to the gauss curve.
		double SS = Math.sqrt(width);
		float sigmaY = (float) ((1*height)/SS);
		float sigmaZ = (float) ((2*depth)/SS);
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
								Math.exp(-((y_2/(2f * Math.pow(sigmaY,2)))+(z_2/(2f * Math.pow(sigmaZ,2)))));
						//Add some little balls in the phantom.
						if (x <= gauss){
							super.addAtIndex(i,j,k,(float)gauss);
						}
						else{
							super.addAtIndex(i, j, k, 0.f);
						}
					}
				}
			}
		}
	}
	
	public static void main(String [] args){
		int width,height,depth;
		new ImageJ();
		width = 152;
		height = 140;
		depth = 162;
		double[] angleR = {(float) -Math.PI/6f,(float) Math.PI/6f};
		int[] nP = {200,200};
		float[] sp = {1,1};
		BreastPhantom bp = new BreastPhantom(width,height,depth);
		for (int count = 0;count < bp.getOrigin().length; count++){
			System.out.println(bp.getOrigin()[count]);
		}
		bp.show();
		ConeProjection cp = new ConeProjection(15,angleR,150f,60f,nP,sp);
		Grid3D sino = cp.coneProject(bp, 1);
		sino.show();
	}
}