/**
 * 
 */
package digitalBreastTomosynthesis;

import edu.stanford.rsl.conrad.geometry.shapes.simple.Box;
import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.conrad.geometry.shapes.simple.StraightLine;

import java.util.ArrayList;

import edu.stanford.rsl.conrad.data.numeric.Grid3D;
import edu.stanford.rsl.conrad.data.numeric.InterpolationOperators;
import edu.stanford.rsl.conrad.geometry.transforms.Translation;
import edu.stanford.rsl.conrad.numerics.SimpleVector;

/**
 * @author Emilio Isaias-Camacho
 * The parameters for this function are:
 * @numberOfProjections number of projection
 * @angleRange angle range
 * @sourceIsoDistance source isocentre distance
 * @detIsoDistance detector isocenter distance
 * @detPixels number of pixels {x,y}
 * @detSpacing detector spacing {x,y} 
 * @coneAngle Cone angles in x and y direction.
 * 
 */
public class ConeProjection {
	/**
	 * Data members section
	 */
	int numberOfProjections;		//Total number of projections for the DBT.
	double[] angleRange;			//Angle range from for the DBT.
	float sourceIsoDistance;
	float detIsoDistance;
	float D;						//Distance from the centre of the detector to the x-ray source.
	int[] detPixel;					//Number of pixels in the detector
	float[] detSpacing;				//Spacing or pitch of the detector.
	double[] detectorSize;			//Size of the detector in milimeters.
	double[] coneAngle;				//Cone angles in x and y direction.
	
	// Constructor section
	public ConeProjection(	int nOP,		//Number of projections.
							double[] anglR,	//Angle range.
							float srcID,	//Distance from the x-ray source to the Isocenter.
							float detID,	//Distance from the Isocenter to the detector center.	
							int[] detPix,	//Number of pixels in the detector in x and y direction.
							float[] detSpc	//Detector spacing in x and y direction.
							){
		this.numberOfProjections = nOP;
		this.angleRange = anglR;
		this.sourceIsoDistance = srcID;
		this.detIsoDistance = detID;
		this.D = this.sourceIsoDistance + this.detIsoDistance;
		this.detPixel = detPix;
		this.detSpacing = detSpc;
		double[] aux = {this.detPixel[0] * this.detSpacing[0],this.detPixel[1] * this.detSpacing[1]};
		this.detectorSize = aux;
		double[] aux2 = {Math.atan2((this.detectorSize[0]), this.D*2.0),Math.atan2((this.detectorSize[1]), this.D*2.0)};
		this.coneAngle = aux2;
	}
	
	public ConeProjection(){
		double[] anglRange = {-30,30};		//Angle in radians.
		int[] detPx = {200,200};
		float[] detSpc = {1f,1f};
		this.numberOfProjections = 15;
		this.angleRange = anglRange;
		this.sourceIsoDistance = 250f;
		this.detIsoDistance = 60f;
		this.D = this.sourceIsoDistance + this.detIsoDistance;
		this.detPixel = detPx;
		this.detSpacing = detSpc;
		double[] aux = {this.detPixel[0] * this.detSpacing[0],this.detPixel[1] * this.detSpacing[1]};
		this.detectorSize = aux;
		double[] aux2 = {Math.atan2((this.detectorSize[0]), this.D*2.0),Math.atan2((this.detectorSize[1]), this.D*2.0)};
		this.coneAngle = aux2;
	}
	
	public Grid3D coneProject(	BreastPhantom breastP,	//Breast phantom.
								float fs				//Sampling frequency.
								){
		//Storing the phantom projections into the coneOgram.
		Grid3D sino = new Grid3D((int)detectorSize[0],(int)detectorSize[1],numberOfProjections);
		//Create a bounding box in 3D for finding the intersections of the x-ray beam with the phantom edges.
		//Centered in the origin.
		Translation t = new Translation(-(breastP.getSize()[0]*breastP.getSpacing()[0])/2,
										-(breastP.getSize()[1]*breastP.getSpacing()[1])/2,
										-(breastP.getSize()[2]*breastP.getSpacing()[2])/2);		
		Box auxBox = new Box(	(breastP.getSize()[0]*breastP.getSpacing()[0]),
								(breastP.getSize()[1]*breastP.getSpacing()[1]),
								(breastP.getSize()[2]*breastP.getSpacing()[2]));
		auxBox.applyTransform(t);
		
		double dTheta = (angleRange[1] - angleRange[0])/numberOfProjections;
		for (int i = 0;i<numberOfProjections;++i)
		{			
			double theta = angleRange[0] + i*dTheta;
			//Source point. Moving along the y and z plane and being constant (0) over x axis.
			PointND src = new PointND();			
			double[] aux = {0, this.sourceIsoDistance*Math.cos(theta),
							this.sourceIsoDistance*Math.sin(theta)};
			SimpleVector src2 = new SimpleVector(aux);
			src.setCoordinates(src2);
			for (int a = 0;a <= detPixel[0]; ++a){
				for (int b = 0;b <= detPixel[1];++b){
					aux[0] = a - this.detectorSize[0]/2f;	// X component of the detector element
					aux[1] = -this.detIsoDistance;			// Y component is constant.
					aux[2] = b - this.detectorSize[1]/2f;	// Z component of the detector element.
					double sum = 0.0;
					SimpleVector detVec = new SimpleVector(aux);
					PointND detPT = new PointND();
					detPT.setCoordinates(detVec);
					StraightLine line = new StraightLine(detPT, src);
					line.normalize();
					ArrayList <PointND> cutSites = auxBox.intersect(line);
					if (cutSites.size() == 0){
						continue;
					}
					PointND init = cutSites.get(0);
					PointND end = cutSites.get(1);
					double dist = init.euclideanDistance(end);
					if (dist == 0){
						continue;
					}
					double[] step = {(end.getCoordinates()[0]-init.getCoordinates()[0])/(dist*fs),
							(end.getCoordinates()[1]-init.getCoordinates()[1])/(dist*fs),
							(end.getCoordinates()[2]-init.getCoordinates()[2])/(dist*fs)};
					for (int k = 0; k< dist*fs; k++){
						double x = init.getCoordinates()[0] + step[0]*k;
	        			double y = init.getCoordinates()[1] + step[1]*k;
	        			double z = init.getCoordinates()[2] + step[2]*k;
	    				if(x < 0 || y < 0 || z < 0 ||
	    						x >= breastP.getSize()[0]-1 || y >= breastP.getSize()[1]-1 ||
	    						z >= breastP.getSize()[2]-1){
	    					continue;}
	    				sum += InterpolationOperators.interpolateLinear(breastP, x, y, z);
					}
					sino.setAtIndex(a, b, i, (float) sum);
				}
			}
			//System.out.println(theta);			
		}
		
		return sino;
	}
}
