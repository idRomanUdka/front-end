package utils;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;


public class WebDriverListener implements IInvokedMethodListener {
    protected static Logger log4j = Logger.getLogger(WebDriverListener.class);

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }


    private static ConcurrentSkipListSet<Integer> invocateds = new ConcurrentSkipListSet<Integer>();

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (invocateds.add(System.identityHashCode(testResult))) {
            if (!testResult.isSuccess() && !method.getTestMethod().toString().contains(
                    "CreateTests") && method.isTestMethod()) {
            	 String FILENAME = makeScreenshot(testResult.getName());
                     String pathScreenShoot = System.getProperty("user.dir") + "\\target" + File.separator + "failure_screenshots" + File.separator + FILENAME;

                     Reporter.log(String.format("SCREENSHOT JAVAROBOT: <a href='%s'><img border='0' src='%s' width='80' height ='60'/></a>",
                             pathScreenShoot + "_javarobot.jpg", pathScreenShoot + "_javarobot.jpg"));

                 log4j.error(
                         "Test FAILED! Method:" + testResult.getName() + ". StackTrace is " + Throwables.getStackTraceAsString(
                                 testResult.getThrowable()));
            }

        }
    }

    public String makeScreenshot(String methodName) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        BufferedImage capture;
        String filename = "";
        try {
            capture = new Robot().createScreenCapture(screenRect);
            filename = methodName + "_" + formater.format(calendar.getTime());

            File fileScreenshot = new File("target" + File.separator + "failure_screenshots" +
                    File.separator + methodName + "_" + formater.format(calendar.getTime()) + "_javarobot.jpg");
            fileScreenshot.getParentFile()
                    .mkdirs();
            ImageIO.write(capture, "jpg", fileScreenshot);
            return filename;
          /*  return System.getProperty("user.dir") + File.separator + "target" + File.separator + "failure_screenshots" +
                    File.separator + methodName + "_" + formater.format(calendar.getTime()); */
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
    }

}
