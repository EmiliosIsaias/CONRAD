/**
 * 
 */
package digitalBreastTomosynthesis;

import edu.stanford.rsl.conrad.data.numeric.Grid3D;

/**
 * @author Emilio Isaias-Camacho
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
		if ((depth/width) <= 1){
			for (int i = 0;i < width;i++){
				float x = -i;// - (width/2);
				double x_2 = Math.pow(x, 2);
				for (int j = 0;j < height;j++){
					float y = j - (height/2);
					double y_2 = Math.pow(y, 2);
					for (int k = 0;k < depth;k++){
						float z = k - (depth/2);
						double z_2 = Math.pow(z, 2);
						double gauss = (1.0/2.0*Math.PI*1.0) * Math.exp(-(x_2 + y_2 + z_2)/2.0);
						super.addAtIndex(i, j, k, (float) gauss);
					}
				}
			}
		}
		
		// TODO Auto-generated constructor stub
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
