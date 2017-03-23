package video;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.datasink.*;

/**
* This program takes a list of JPEG image files and convert them into a
* QuickTime movie.
* We just have to use the static method fromImagesToVideo to convert.
*/
public class ConvertVideo implements ControllerListener, DataSinkListener {
	
	private Object waitSync = new Object();
	private boolean stateTransitionOK = true;
    Object waitFileSync = new Object();
    boolean fileDone = false;
    boolean fileSuccess = true;
		
	/**
     * Convert images of a directory into a video. All images must be the same size.
     * @param src_dir Te directory where all images are
     * @param dest The name of the video
     */
    public static void fromImagesToVideo(String src_dir, String dest){
 	   ConvertVideo converter = new ConvertVideo();
 	   
 	   //We get the name of images
 	   ArrayList<String> names = converter.getImageFilesPathsVector(src_dir);
 	   
 	   //src_dir="file:///"+src_dir;
 	   
 	   //We get the size of images 
 	   BufferedImage firstImageInFolder = converter.getFirstImageInFolder(src_dir);
        int width = firstImageInFolder.getWidth();
        int height = firstImageInFolder.getHeight();
        
        //We create the medialocator
        MediaLocator oml = converter.createMediaLocator(dest);
        
        converter.doIt(width, height, 0, names, oml);
    }

   private ArrayList<String> getImageFilesPathsVector(String imagesFolderPath) {
	   
       File imagesFolder = new File(imagesFolderPath);
       String[] imageFilesArray = imagesFolder.list();
       
       for (int i = 0; i < imageFilesArray.length; i++) {
    	   System.out.println(imageFilesArray[i]);
       }
       
       ArrayList<String> imageFilesPathsVector = new ArrayList<String>();
       for (String imageFileName : imageFilesArray) {
               if (!imageFileName.toLowerCase().endsWith("jpg"))
                       continue;
               imageFilesPathsVector.add(imagesFolder.getAbsolutePath()+ File.separator + imageFileName);
       }
       return imageFilesPathsVector;
       
   }

   private boolean doIt(int width, int height, int frameRate, ArrayList<String> inFiles, MediaLocator outML) {
       ImageSource ids = new ImageSource(width, height, frameRate, inFiles);

       Processor p;

       try {
               System.out.println("- create processor for the image datasource ...");
               p = Manager.createProcessor(ids);
       } catch (Exception e) {
               System.err.println("Cannot create a processor from the data source.");
               return false;
       }

       p.addControllerListener(this);

       // Put the Processor into configured state so we can set
       // some processing options on the processor.
       p.configure();
       if (!waitForState(p, Processor.Configured)) {
               System.err.println("Failed to configure the processor.");
               return false;
       }

       // Set the output content descriptor to QuickTime.
       p.setContentDescriptor(new ContentDescriptor(
                       FileTypeDescriptor.QUICKTIME));// FileTypeDescriptor.MSVIDEO

       // Query for the processor for supported formats.
       // Then set it on the processor.
       TrackControl tcs[] = p.getTrackControls();
       Format f[] = tcs[0].getSupportedFormats();
       if (f == null || f.length <= 0) {
               System.err.println("The mux does not support the input format: " + tcs[0].getFormat());
               return false;
       }

       tcs[0].setFormat(f[0]);

       System.out.println("Setting the track format to: " + f[0]);

       // We are done with programming the processor. Let's just
       // realize it.
       p.realize();
       if (!waitForState(p, Controller.Realized)) {
               System.err.println("Failed to realize the processor.");
               return false;
       }

       // Now, we'll need to create a DataSink.
       DataSink dsink;
       if ((dsink = createDataSink(p, outML)) == null) {
               System.err.println("Failed to create a DataSink for the given output MediaLocator: "
                                               + outML);
               return false;
       }

       dsink.addDataSinkListener(this);
       fileDone = false;

       System.out.println("start processing...");

       // OK, we can now start the actual transcoding.
       try {
               p.start();
               dsink.start();
       } catch (IOException e) {
               System.err.println("IO error during processing");
               return false;
       }

       // Wait for EndOfStream event.
       waitForFileDone();

       // Cleanup.
       try {
               dsink.close();
       } catch (Exception e) {
       }
       p.removeControllerListener(this);

       System.out.println("...done processing.");

       return true;
   }

   /**
    * Create the DataSink.
    */
   private DataSink createDataSink(Processor p, MediaLocator outML) {

       DataSource ds;

       if ((ds = p.getDataOutput()) == null) {
               System.err.println("Something is really wrong: the processor does not have an output DataSource");
               return null;
       }

       DataSink dsink;

       try {
               System.out.println("- create DataSink for: " + outML);
               dsink = Manager.createDataSink(ds, outML);
               dsink.open();
       } catch (Exception e) {
               System.err.println("Cannot create the DataSink: " + e);
               return null;
       }

       return dsink;
   }

   /**
    * Block until the processor has transitioned to the given state. Return
    * false if the transition failed.
    */
   private boolean waitForState(Processor p, int state) {
       synchronized (waitSync) {
               try {
                       while (p.getState() < state && stateTransitionOK)
                               waitSync.wait();
               } catch (Exception e) {
               }
       }
       return stateTransitionOK;
   }

   /**
    * Controller Listener.
    */
   public void controllerUpdate(ControllerEvent evt) {

       if (evt instanceof ConfigureCompleteEvent|| evt instanceof RealizeCompleteEvent|| evt instanceof PrefetchCompleteEvent) {
           synchronized (waitSync) {
                   stateTransitionOK = true;
                   waitSync.notifyAll();
           }
       } else if (evt instanceof ResourceUnavailableEvent) {
           synchronized (waitSync) {
                   stateTransitionOK = false;
                   waitSync.notifyAll();
           }
       } else if (evt instanceof EndOfMediaEvent) {
           evt.getSourceController().stop();
           evt.getSourceController().close();
       }
   }

   /**
    * Block until file writing is done.
    */
   private boolean waitForFileDone() {
	   synchronized (waitFileSync) {
	           try {
	                   while (!fileDone)
	                           waitFileSync.wait();
	           } catch (Exception e) {
	           }
	   }
	   return fileSuccess;
   }

   /**
    * Event handler for the file writer.
    */
   public void dataSinkUpdate(DataSinkEvent evt) {

       if (evt instanceof EndOfStreamEvent) {
               synchronized (waitFileSync) {
                       fileDone = true;
                       waitFileSync.notifyAll();
               }
       } else if (evt instanceof DataSinkErrorEvent) {
               synchronized (waitFileSync) {
                       fileDone = true;
                       fileSuccess = false;
                       waitFileSync.notifyAll();
               }
       }
   }

   private BufferedImage getFirstImageInFolder(String rootDir) {
       File rootFile = new File(rootDir);
       String[] list = (rootFile).list();
       BufferedImage bufferedImage = null;
       for (String filePath : list) {
           if (!filePath.toLowerCase().endsWith(".jpeg")&& !filePath.toLowerCase().endsWith(".jpg")) {
                   continue;
           }
           try {
                   bufferedImage = ImageIO.read(new File(rootFile.getAbsoluteFile() + File.separator + filePath));
                   break;
           } catch (IOException e) {
                   e.printStackTrace();
           }
       }
       return bufferedImage;
   }

   /**
    * Create a media locator from the given string.
    */
   private MediaLocator createMediaLocator(String url) {

       MediaLocator ml;

       if (url.indexOf(":")>0)
    	   ml = new MediaLocator(url);

       if (url.startsWith(File.separator)) {
    	   ml = new MediaLocator("file:" + url);
       } else {
               String file = "file:" + File.separator + url;
               ml = new MediaLocator(file);
       }

       return ml;
   }

}
