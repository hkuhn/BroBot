package Away.Vision.BallDetection;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.Point2D;
import javax.swing.*;

import april.jcam.*;
import april.util.*;
import april.jmat.*;


public class BallDetectionDetector
{
    static final int DEFAULT_GRAY_THRESHOLD = 254;

    JFrame jf;
    JImage jim;
    Point ballCoords;
    
    
    
    
    private class node_t {
        
        int parent;
        int right;
        int down;
        //initialize pixels to -1
        public node_t() {
            parent = -1;
            right = -1;
            down = -1;
        }
        
    }
    
    private class treeStats_t {
		int num_pixels;
		float mean_x;
		float mean_y;
		//float M2_x;
		//float M2_y;
		float sum_x;
        float sum_y;
        float sum2_x;
        float sum2_y;
        float var_x;
		float var_y;
		float cov;
        int min_x;
        int max_x;
        int min_y;
        int max_y;
        
        public treeStats_t() {
            num_pixels = 0;
			mean_x = 0;
			mean_y = 0;
			//M2_x = 0;
			//M2_y = 0;
			cov = 0;
            min_x = -1;
            max_x = -1;
            min_y = -1;
            max_y = -1;
            sum_x = 0;
            sum_y = 0;
            sum2_x = 0;
            sum2_y = 0;
		}
        
        
    }
    
    
    public BallDetectionDetector() {
        ballCoords = new Point(-1, -1);
        
    }
    
    public void addNeighbors(int cur_pixel, int threshold, int height,
                             int width, int data[], node_t nodes[]) {
        // check pixel to the right
        
        if (((cur_pixel + 1) % width) != 0 &&
            data[cur_pixel+1] >= threshold) {
            nodes[cur_pixel].right = cur_pixel + 1;
            nodes[cur_pixel+1].parent = cur_pixel;
        }
        // check pixel below
        if (cur_pixel + width < (height*width) &&
            data[cur_pixel+width] >= threshold) {
            
            nodes[cur_pixel].down = cur_pixel + width;
            nodes[cur_pixel+width].parent = cur_pixel;
            
        }
        
    }
    
    
    public int findRoot (int cur_pixel, node_t nodes[]) {
        if (nodes[cur_pixel].parent != -1) {
            int examine = nodes[cur_pixel].parent;
            // while not at the parent of the tree
            while (nodes[examine].parent != examine) {
                examine = nodes[examine].parent;
                
            }
            // set cur_pixel's parent to the root of the tree
            nodes[cur_pixel].parent = examine;
            
        }
        // new tree, cur_pixel is the root
        else {
            nodes[cur_pixel].parent = cur_pixel;
            
        }
        
        return nodes[cur_pixel].parent;
        
    }
    
	public void updateStats(HashMap<Integer, treeStats_t> treeHash, int cur_pixel,
                            int root, int height, int width) {
        
		treeStats_t stats = treeHash.get(root);
        int x = cur_pixel % width;
        int y = cur_pixel / width;
        
        //System.out.printf("x is %d, y is %d\n", x, y);
		stats.num_pixels++;
        
        if (x > stats.max_x) {
            stats.max_x = x;
        }
        
        else if (stats.min_x == -1 ||x < stats.min_x) {
            stats.min_x = x;
            
        }
        
        if (y > stats.max_y) {
            stats.max_y = y;
        }
        
        else if (stats.min_y == -1 ||y < stats.min_y) {
            stats.min_y = y;
        }
		stats.sum_x += x;
		stats.sum_y += y;
		treeHash.put(root, stats);
        
	}
	public void calcVars(HashMap <Integer, treeStats_t> treeHash, node_t[] nodes, int width) {
		for (Integer root : treeHash.keySet()) {
			treeStats_t stats = treeHash.get(root);
            if (stats.num_pixels > 100) {
                stats.mean_x = stats.sum_x / stats.num_pixels;
                stats.mean_y = stats.sum_y / stats.num_pixels;
                int examine_x = root;
                int examine_y = root;
                while (nodes[examine_y].down != -1) {
                    while (nodes[examine_x].right != -1) {
                        int x = examine_x % width;
                        int y = examine_x / width;
                        
                        stats.sum2_x += (x - stats.mean_x)*(x - stats.mean_x);
                        stats.sum2_y += (y - stats.mean_y)*(y - stats.mean_y);
                        float a = x - stats.mean_x;
                        float b = y - stats.mean_y;
                        stats.cov += (a * b) / stats.num_pixels;
                        examine_x = nodes[examine_x].right;
                    }
                    examine_y = nodes[examine_y].down;
                    examine_x = examine_y;
                }
                stats.var_x = stats.sum2_x / (stats.num_pixels - 1);
                stats.var_y = stats.sum2_y / (stats.num_pixels - 1);
                
            }
            
        }
	}
    
    public treeStats_t getBallParam(HashMap <Integer, treeStats_t> treeHash) {
        
        treeStats_t ballStats = new treeStats_t();
        treeStats_t prev = new treeStats_t();
        prev.var_x = 100;
        prev.var_y = 0;
		for (Integer root : treeHash.keySet()) {
			treeStats_t stats = treeHash.get(root);
            System.out.printf("var_x: %f, var_y: %f, cov: %f\n", stats.var_x, stats.var_y, stats.cov);
            
            if (stats.num_pixels > 70 && stats.num_pixels < 2800) {
                if (Math.abs(stats.max_x - stats.min_x - (stats.max_y - stats.min_y)) <= 25) {
                    if ((Math.abs(stats.var_x - stats.var_y) < 45) ||
                        ( Math.abs(stats.cov) < 5)) {
                        if (Math.abs(stats.var_x - stats.var_y) < Math.abs(prev.var_x - prev.var_y)) {
                            ballStats = stats;
                            prev = stats;
                        }
                        System.out.printf("FOUND BALL var_x: %f, var_y: %f, cov: %f\n", stats.var_x, stats.var_y, stats.cov);
                    }
                }
            }
        }
        return ballStats;
        
    }
    
    
    // Returns the bounds of the pixel coordinates of the led: {min_x, min_y, max_x, max_y}
    public int[] findBall(BufferedImage img, int thresh)
    {
        int mythresh = thresh;
        
        img = ImageUtil.conformImageToInt(img);
        
        int data[] = ((DataBufferInt) (img.getRaster().getDataBuffer())).getData();
        
        int height = img.getHeight();
        int width = img.getWidth();
        
        node_t[] nodes = new node_t[height*width];
        for (int i=0; i < nodes.length; i++) {
            nodes[i] = new node_t();
            
        }
        
        
        HashMap <Integer, treeStats_t> treeHash = new HashMap<Integer, treeStats_t>();
        
        int threshold = (mythresh << 16) | (mythresh << 8) | (mythresh);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                int cur_pixel = width*y + x;
                
                if (data[cur_pixel] >= threshold) {
                    
                    data[cur_pixel] = 0x0000FF;
                    addNeighbors (cur_pixel, threshold, height, width, data, nodes);
                    int root = findRoot (cur_pixel, nodes);
                    
                    
                    // create hash table entry for pixel's tree if it doesn't exist
                    if (!treeHash.containsKey(root)) {
                        treeStats_t stats = new treeStats_t();
                        treeHash.put(root, stats);
                        
                    }
					updateStats(treeHash, cur_pixel, root, height, width);
                    
                }
                
            }
        }
        
		calcVars(treeHash, nodes, width);
        treeStats_t ballStats = new treeStats_t();
        ballStats = getBallParam (treeHash);
        if (ballStats.min_x == -1 || ballStats.min_y == -1 ||
            ballStats.max_x == -1 || ballStats.max_y == -1) {
            
            return new int[]{0, 0, 0, 0};
        }
        System.out.printf("min x: %d min y: %d max x: %d max y: %d\n",
                          ballStats.min_x, ballStats.min_y, ballStats.max_x, ballStats.max_y);
        
        return new int[]{ballStats.min_x, ballStats.min_y, ballStats.max_x, ballStats.max_y};
    }
    
    int frame = 0;
    public int[] runDetector(BufferedImage im, int thresh)
    {
        
        int bounds[] = findBall(im, thresh);
        
        
        /*
         // Display the detection, by drawing on the image
         if (true) {
         // draw the horizontal lines
         for (int y = bounds[1]; y <= bounds[3]; y++) {
         for (int x = bounds[0]; x <=bounds[2]; x++) {
         im.setRGB(x,y, 0xff0000ff); //Go Blue!
         }
         }
         
         int center_x = (bounds[2] + bounds[0])/2;
         int center_y = (bounds[3] + bounds[1])/2;
         for (int y = -2; y < 3; y++) {
         for (int x = -2; x < 3; x++) {
         if (bounds[0] > 0)
         im.setRGB(center_x + x, center_y + y, 0xff0000);
         }
         }
         }
         */
        
        return bounds;
        
    }
    
}
