package CTRecon;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;


public class myPhantom extends Grid2D{
    
    public myPhantom(int x,int y){
        super(x, y);
        int val1 = 1;
        int val2 = 2;
        int val3 = 3;
        double r1 = 0.45*x;
        double r2 = 0.5*r1;
        
        int xCenter1 = x/2;
        int yCenter1 = y/2;
        
        int xCenter2 = (int) ((0.5*x)-r2);
        int yCenter2 = yCenter1;
        
        int xr1 = xCenter1;
        int xr2 = xr1 + (int) (0.707*r1);
        int yr1 = yCenter1 - (int) (0.5*r1);
        int yr2 = yCenter1 + (int) (0.5*r1);
        
        this.setSpacing(1,1);
        
        
        
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++) {
            	//Outer circle
                if( Math.pow(i - xCenter1, 2)  + Math.pow(j - yCenter1, 2) <= (r1*r1) ){
                    super.setAtIndex(i, j, val1);
                }
                //Inner circle
                if( Math.pow(i - xCenter2, 2)  + Math.pow(j - yCenter2, 2) <= (r2*r2) ){
                    super.setAtIndex(i, j, val2);
                }
                //Square
                if((i > xr1 && i < xr2) && (j > yr1 && j < yr2)){
                    super.setAtIndex(i, j, val3);
                }
            }
        }
    }
    
        public static void main (String [] args)
        {            
            myPhantom phant = new myPhantom(200,200);     
            boolean mode = false;				//true for short scan and false for complete scan.
            //phant.show("MyPhantom");
            Grid2D phantom = new Grid2D(phant);
            int number_of_projections = 200;
            //Sinogram generation 
            //parallelProjRD projector = new parallelProjRD(200,1,400);
            //Grid2D sinogram = projector.projectRayDriven(phant);
            fanProjRD fan_proj = new fanProjRD(number_of_projections,1,400,2000,142);
            Grid2D fanogram = fan_proj.fanProjectRD(phant, mode, 2);
            //sinogram.show("The Sinogram");
            //fanogram.show("The Fanogram");
            //parallelBP_PD backProj = new parallelBP_PD(200,1,400);
    		//(backProj.backProjPD(sinogram, phant.getSize(), phant.getSpacing())).show("Back Projected");
    		//Grid2D sinPhantom = backProj.filteredBP(sinogram, phant.getSize(), phant.getSpacing());
    		//sinPhantom.show("Filtered Back Projected from Sinogram");
    		//(fanBP.filteredBP(fanogram, phant.getSize(), phant.getSpacing())).show("Filtered Back Projected from fan");
            fanogram.show("Full-scan fanogram");
    		fanogram = fan_proj.rebinFanogram(fanogram, mode);   
    		fanogram.show("Rebinned full scan");
    		parallelBP_PD fan = new parallelBP_PD(200,1,400);
    		Grid2D fanPhantomfull = fan.filteredBP(fanogram, phant.getSize(), phant.getSpacing());
    		fanPhantomfull.show("Rebinned sinogram backprojection");    		    	
    		
    		mode = true;
    		double maxAngle = fan_proj.fanAngle + Math.PI;
    		double noProj = Math.floor(maxAngle/fanogram.getSpacing()[1]);
    		fanProjRD fanShortProj = new fanProjRD((int)(noProj),1,400,2000,142);
    		Grid2D fanogramShort = fanShortProj.fanProjectRD(phant,mode,2);
    		fanogramShort.show("Short-scan fanogram");
    		fanogramShort = fanShortProj.rebinFanogram(fanogramShort, mode);    		
    		fan = new parallelBP_PD((int)(noProj),1,400);
    		Grid2D fanPhantom = fan.filteredBP(fanogramShort, phant.getSize(), phant.getSpacing());
    		fanPhantom.show("Rebinned short scan fanogram");
    		
    		phantomDiff sino = new phantomDiff(phantom,fanPhantom);
    		sino.diffBetween().show("Difference image between the original phantom and the reconstructed from the short scan fanogram.");
    		phantomDiff fano = new phantomDiff(fanPhantomfull,fanPhantom);
    		fano.diffBetween().show("Difference image between the fullscan and the shortscan fanogram.");
    		
        }
}