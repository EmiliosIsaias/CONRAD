/**
 * 
 */
package digitalBreastTomosynthesis;

import ij.ImageJ;
import edu.stanford.rsl.conrad.data.numeric.Grid3D;

/**
 * @author Emilio Isaias-Camacho
 * @version 1.1
 *
 */
public class BreastPhantom extends Grid3D{
	
	/**
	 * Basic emulation for a breast phantom. Generating a 3D Gaussian.
	 */
/*	int resolution;							//3D Resolution for the phantom.
	float compression;						//Compression rate for the breast.
	int[] phantomSize = {200,200,100};		//Size of the phantom in pixels.
	*/

	public BreastPhantom(int width, int height, int depth) {		
		super(width, height, depth);
		new ImageJ();
		float sigma = 50.0f;
		float ampl = Math.max(width, height);
		
		double x,y,z;
		double x_2,y_2,z_2;
		
		if ((depth/width) <= 1){
			
			for (int i = 0;i < width;i++){
				x = i - (width/2);
				x_2 = Math.pow(x, 2);
				
				for (int j = 0;j < height;j++){
					y = j - (height/2);
					y_2 = Math.pow(y, 2);
					
					for (int k = 0;k < depth;k++){
						z = k;// - (depth/2);
						z_2 = Math.pow(z, 2);
						
						double gaussX = (ampl/Math.sqrt(2.0*Math.PI*sigma)) * Math.exp(-(x_2)/(2.0*Math.pow(sigma, 2)));
						double gaussY = (ampl/Math.sqrt(2.0*Math.PI*sigma)) * Math.exp(-(y_2)/(2.0*Math.pow(sigma, 2)));
						double gaussZ = (ampl/Math.sqrt(2.0*Math.PI*sigma)) * Math.exp(-(z_2)/(2.0*Math.pow(sigma, 2)));
						if (z<=gaussY*gaussZ){
							super.addAtIndex(i, j, k, 50.0f);
						}
							
//						double gaussZ = (ampl/Math.sqrt(2.0*Math.PI*sigma)) * Math.exp(-(z_2)/(2.0*Math.pow(sigma, 2)));
//						double gauss = (2.0/(2.0*Math.PI*sigma)) * Math.exp(-(y_2 + z_2)/(2.0*Math.pow(sigma, 2)));
//						double gauss = gaussX*gaussY*gaussZ;
//						System.out.println((int)Math.round(gauss));
					}
				}
			}
		}
		
		/* TODO
		 * 1.- Fix the breast phantom.
		 * 2.- Create detector class derived from the Grid2D class
		 * 3.- Create a detector pixel class. Think about this implementation
		 * 4.- Create a source point class.
		 * 5.- Project with these created classes.  
		 */
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
