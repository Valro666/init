package video;

import java.util.ArrayList;

import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;

/**
 * A DataSource to read from a list of JPEG image files and turn that into a
 * stream of JMF buffers. The DataSource is not seekable or positionable.
 */
class ImageSource extends PullBufferDataSource {

    Frame streams[];

    ImageSource (int width, int height, int frameRate, ArrayList<String> images) {
        streams = new Frame[1];
        streams[0] = new Frame(width, height, frameRate, images);
    }

    public void setLocator(MediaLocator source) {}

    public MediaLocator getLocator() {
        return null;
    }
    
    /**
     * Return the ImageSourceStreams.
     */
    public PullBufferStream[] getStreams() {
        return streams;
    }

    /**
     * We could have derived the duration from the number of frames and
     * frame rate. But for the purpose of this program, it's not necessary.
     */
    public Time getDuration() {
        return DURATION_UNKNOWN;
    }

    /**
     * Content type is of RAW since we are sending buffers of video frames
     * without a container format.
     */
    public String getContentType() {
        return ContentDescriptor.RAW;
    }

    //-----------Methods we don't need------------------
    public void connect() {}

    public void disconnect() {}

    public void start() {}

    public void stop() {}

    public Object[] getControls() {return new Object[0];}

    public Object getControl(String type) {return null;}
}
