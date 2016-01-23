package utils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
//import org.frontendtest.components.ImageComparison;
import org.im4java.process.StandardStream;
import org.testng.Reporter;

import pages.WebDriverController;


/**
 * I am gonna pass two images and I am gonna report only the differences
 * trying to catch if there a different object or not into the scene
 *
 * @author maikon
 */
public class CheckingDifferentImages {
    private static Logger log = Logger.getLogger(CheckingDifferentImages.class);

    
    public static boolean compareImages (String pathToTheNewOne, String pathToTheSample, String nameDifference, int diffInt) {
    	
      	String pathWithScreenshots1 = "screenshots"+ File.separator +pathToTheNewOne;
      	log.info(pathWithScreenshots1);
        File screenShot1 = new File(pathWithScreenshots1);
        String pathWithScreenshots2 = "screenshots"+ File.separator + pathToTheSample;
        log.info(screenShot1.getAbsolutePath());
        File screenShot2 = new File(pathWithScreenshots2);
        log.info(screenShot2.getAbsolutePath());
        String pathWithScreenshots3 = "screenshots"+ File.separator + nameDifference;
        File screenDifference = new File(pathWithScreenshots3);
        log.info(screenDifference.getAbsolutePath());
		if (screenShot1.exists() && screenShot2.exists()){

	    	  // This instance wraps the compare command
	    	  CompareCmd compare = new CompareCmd();
	    	 
	    	  // For metric-output
	    	  compare.setErrorConsumer(StandardStream.STDERR);
	    	  IMOperation cmpOp = new IMOperation();
	    	  // Set the compare metric
	    	  cmpOp.metric("mae");
	    	 
	    	  // Add the expected image
	    	  cmpOp.addImage(screenShot2.getAbsolutePath());
	    	 
	    	  // Add the current image
	    	  cmpOp.addImage(screenShot1.getAbsolutePath());
	    	 
	    	  // This stores the difference
	    	  cmpOp.addImage(screenDifference.getAbsolutePath());
	    	 
	    	  try {
	    	    // Do the compare
	    	    compare.run(cmpOp);
	    	  }
	    	  catch (Exception ex) {
	    	    return false;
	    	  }
		}else
        	log.error(pathWithScreenshots1 + " or " + pathWithScreenshots2 + " does not exist");
			return false;
    	}
    /*
    public static boolean compareImagesFront (String pathToTheNewOne, String pathToTheSampler, String nameDifference, int diffInt) {
    	
      	String pathWithScreenshots1 = "screenshots"+ File.separator +pathToTheNewOne;
      	log.info(pathWithScreenshots1);
        File screenShot1 = new File(pathWithScreenshots1);
        String pathWithScreenshots2 = "screenshots"+ File.separator + pathToTheSampler;
        log.info(screenShot1.getAbsolutePath());
        File screenShot2 = new File(pathWithScreenshots2);
        log.info(screenShot2.getAbsolutePath());
        String pathWithScreenshots3 = "screenshots"+ File.separator + nameDifference;
        File screenDifference = new File(pathWithScreenshots3);
        log.info(screenDifference.getAbsolutePath());
        screenDifference.getParentFile().mkdirs();
		if (screenShot1.exists() && screenShot2.exists()){

			ImageComparison imageComparison = new ImageComparison(8,8,0.01);
			
			try {
				if(imageComparison.fuzzyEqual(screenShot1.getAbsolutePath(),screenShot2.getAbsolutePath(),screenDifference.getAbsolutePath()))
					System.out.println("Images are fuzzy-equal.");
				else
				System.out.println("Images are not fuzzy-equal.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
        	log.error(pathWithScreenshots1 + " or " + pathWithScreenshots2 + " does not exist");
			return false;
    	}*/
    
    
   
    class MyOwnException extends Exception {
 	   public MyOwnException(String msg){
 	      super(msg);
 	   }
 }
    
   

}