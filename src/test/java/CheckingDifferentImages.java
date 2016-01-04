import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * I am gonna pass two images and I am gonna report only the differences
 * trying to catch if there a different object or not into the scene
 *
 * @author maikon
 */
public class CheckingDifferentImages {
    private static Logger log = Logger.getLogger(CheckingDifferentImages.class);

    /**
     * Check difference.
     *
     * @param pathToTheFirstScreen the path to the first screen
     * @param pathToTheSecond the path to the second
     * @param nameDifference the name difference
     * @param accuracy the accuracy   1pixels
     * @throws MyOwnException 
     */
    public static void checkDifference(String pathToTheFirstScreen, String pathToTheSecond, String nameDifference, int accuracy){
        BufferedImage im1 = null;
        BufferedImage im2 = null;
        //loading the two pictures
        //read and load the image
      	String pathWithScreenshots1 = "screenshots"+ File.separator +pathToTheFirstScreen;
      	log.info(pathWithScreenshots1);
        File screenShot1 = new File(pathWithScreenshots1);
        String pathWithScreenshots2 = "screenshots"+ File.separator + pathToTheSecond;
		log.info(pathWithScreenshots2);
        File screenShot2 = new File(pathWithScreenshots2);
		if (screenShot1.exists() && screenShot2.exists()){
	        try {
	        	BufferedImage input = ImageIO.read(screenShot1);
	            //build an image with the same dimension of the file read
	            im1 =
	                    new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
	            //object create to draw into the bufferedImage
	            Graphics2D g2d = im1.createGraphics();
	            //draw input into im
	            g2d.drawImage(input, 0, 0, null);
	            //making all again for the second image
	
	        	BufferedImage input2 = ImageIO.read(screenShot2);
	            //build an image with the same dimension of the file read
	            im2 =
	                    new BufferedImage(input2.getWidth(), input2.getHeight(), BufferedImage.TYPE_INT_ARGB);
	            //object create to draw into the bufferedImage
	            Graphics2D g2d2 = im2.createGraphics();
	            //draw input into im
	            g2d2.drawImage(input2, 0, 0, null);
	        } catch (Exception ex) {
	       ex.printStackTrace();
	        }
	
	        showDifference(im1, im2, nameDifference, accuracy);
        }else
        	log.error(pathWithScreenshots1 + " or " + pathWithScreenshots2 + " does not exist");

    }

    class MyOwnException extends Exception {
 	   public MyOwnException(String msg){
 	      super(msg);
 	   }
 }
    
    /**
     * Show difference.
     *
     * @param im1 the im 1
     * @param im2 the im 2
     * @param nameDifference the name difference
     * @param accuracy the accuracy
     */
    public static void showDifference(BufferedImage im1, BufferedImage im2, String nameDifference, int accuracy) {
       try{
        BufferedImage resultImage = new BufferedImage(im1.getWidth(), im2.getHeight(), BufferedImage.TYPE_INT_ARGB);
        double THR = 50;
        int area = 0;
        for (int h = 0; h < im1.getHeight(); h++) {
            for (int w = 0; w < im1.getWidth(); w++) {
            	
            	try{
	                int red1 = 0xff & (im1.getRGB(w, h) >> 16);
	                int green1 = 0xff & (im1.getRGB(w, h) >> 8);
	                int blue1 = 0xff & im1.getRGB(w, h);
	
	
	                int red2 = 0xff & (im2.getRGB(w, h) >> 16);
	                int green2 = 0xff & (im2.getRGB(w, h) >> 8);
	                int blue2 = 0xff & im2.getRGB(w, h);
	            	
	
	                //euclidian distance to estimate the simil.
	                double dist = 0;
	                dist = Math.sqrt(Math.pow((double) (red1 - red2), 2.0)
	                        + Math.pow((double) (green1 - green2), 2.0)
	                        + Math.pow((double) (blue1 - blue2), 2.0));
	                if (dist > THR) {
	                    resultImage.setRGB(w, h, im2.getRGB(w, h));
	                    area++;
	                } else {
	                    resultImage.setRGB(w, h, 0);
	                }
	                //2nd option
	           /*     if (dist > THR) {
	                    resultImage.setRGB(w, h,255);
	                    area++;
	                } else {
	                    resultImage.setRGB(w, h, im1.getRGB(w, h));
	                }*/
            	}catch(ArrayIndexOutOfBoundsException e){
            		log.info(im1.getRGB(w, h));
            		throw(e);
            		
            	}
            } //w
        } //h
            if (accuracy < area) {
                log.info("Difference is more than "+accuracy+" pixels and it is "+area);
                File fileScreenshot = new File("target"+ File.separator +"failure_screenshots"+ File.separator +nameDifference);
                fileScreenshot.getParentFile().mkdirs();
                ImageIO.write(resultImage, "PNG", fileScreenshot);
                log.info("Diff screen was saved in "+"target"+ File.separator +"failure_screenshots"+ File.separator + nameDifference);
                WebDriverController.failedTests.add("Diff screen was saved in "+"target"+ File.separator +"failure_screenshots"+ File.separator + nameDifference);
               } else {
              log.info("Everything is ok!");
            }
        } catch (Exception ex) {
      ex.printStackTrace();
        }
    }


}