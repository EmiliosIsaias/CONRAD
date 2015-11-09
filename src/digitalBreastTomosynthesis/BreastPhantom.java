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
			for (int i = 0;i <= width;i++){
				float x = i - (width/2);
				for (int j = 0;j <= height;j++){
					float y = j - (height/2);
					for (int k = 0;k <= depth;k++){
						float z = k - (depth/2);
						double gauss = (1/2*Math.PI*1) * Math.exp(-(x + y + z));
						
					}
				}
			}
		}
		
		// TODO Auto-generated constructor stub
	}

}
