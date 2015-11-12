/**
 * 
 */
package digitalBreastTomosynthesis;

import edu.stanford.rsl.conrad.data.numeric.Grid3D;

/**
 * @author Emilio Isaias-Camacho
 *
 */
public class ConeProjection {
	/**
	 * Data members section
	 */
	int numberOfProjections;
	int[] angleRange;
	float sourceIsoDistance;
	float detIsoDistance;
	float D;
	float[] detectorSize;
	// Constructor section
	public ConeProjection(	int nOP,		//Number of projections.
							int[] anglR,	//Angle range.
							float srcID,	//Distance from the x-ray source to the Isocenter.
							float detID		//Distance from the Isocenter to the detector center.							
							){
		numberOfProjections = nOP;
		angleRange = anglR;
		sourceIsoDistance = srcID;
		detIsoDistance = detID;
		D = sourceIsoDistance + detIsoDistance;	
	}
	
	public Grid3D cone_project(){
		Grid3D sino = new Grid3D((int)detectorSize[0],(int)detectorSize[1],numberOfProjections);
		return sino;
	}
}
