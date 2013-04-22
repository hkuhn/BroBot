package main;


import java.awt.image.BufferedImage;

public interface BroBotAppControllerDelegate {

    /**
     * Will be called on vision thread.
     * @param leftImage
     * @param rightImage
     */
    public BufferedImage[] processImages(BufferedImage leftImage, BufferedImage rightImage);

    public void setAppController(BroBotApplicationController controller);
    public void setBotController(BroBotController controller);
    
}