/**
 * 
 */
package digitalBreastTomosynthesis;
import edu.stanford.rsl.conrad.data.numeric.Grid2D;
/**
 * @author ga99jypo
 *
 */
public class GaussPhantom2D extends Grid2D {

	/**
	 * 
	 */
	
	
	public GaussPhantom2D(int x, int y) {
		// TODO Auto-generated constructor stub
		super(x,y);
		float sigma = y/3.5f;
		float xx,yy;
		for (int i=0;i<x;i++){
			xx = i;// - x/2.f;
			for (int j=0;j<y;j++){
				yy = j - y/2.f;
				double gauss = (7.5/Math.sqrt(2.0*Math.PI)*sigma) *
						Math.exp(-(Math.pow(yy, 2)/(2.f * Math.pow(sigma, 2))));
				if(xx <= gauss){
					super.addAtIndex(i, j, (float) gauss);
				}
				else{
					super.addAtIndex(i, j, 0.f);
				}
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GaussPhantom2D gp = new GaussPhantom2D(400,200);
		gp.show();

	}

}
