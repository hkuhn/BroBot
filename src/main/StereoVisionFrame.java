package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.*;

import april.util.ParameterGUI;
import april.util.JImage;

/**
 * @author slessans
 *
 */
public class StereoVisionFrame extends JFrame {

    private static final String SelectLeftImageSourceButtonText = "Select Left Image Source";
    private static final String SelectRightImageSourceButtonText = "Select Right Image Source";

    private static final long serialVersionUID = 1L;
    private JImage leftImageView;
    private JImage rightImageView;

    public StereoVisionFrame() {
        super("Template Image Matcher");
        this.setLayout(new BorderLayout());

        // make the image views
        leftImageView = new JImage();
        rightImageView = new JImage();

        // add center image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(1, 2, 10, 10));
        imagePanel.add(leftImageView);
        imagePanel.add(rightImageView);
        this.add(imagePanel, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout());
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
    }


    public JImage getLeftImageView() {
        return leftImageView;
    }

    public JImage getRightImageView() {
        return rightImageView;
    }

}

