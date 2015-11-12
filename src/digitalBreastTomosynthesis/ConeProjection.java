/**
 * 
 */
package digitalBreastTomosynthesis;

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
		
		
	}
}
