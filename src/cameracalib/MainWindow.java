package cameracalib;

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
public class MainWindow extends JFrame {
	
	private static final String SelectLeftImageButtonText = "Select Left Image";
	private static final String SelectRightImageButtonText = "Select Right Image";
	private static final String SelectLeftImagePointsButtonText = "Select Left Points";
	private static final String SelectRightImagePointsButtonText = "Select Right Points";
	
	private static final long serialVersionUID = 1L;
	private JImage leftImageView;
	private JImage rightImageView;
	private JButton selectLeftImageButton;
	private JButton selectRightImageButton;
	private JButton selectLeftImagePointsButton;
	private JButton selectRightImagePointsButton;

	public MainWindow() {
		super("Template Image Matcher");
		this.setLayout(new BorderLayout());
		
		// make the image views
		leftImageView = new JImage();
		rightImageView = new JImage();
		selectLeftImageButton = new JButton(SelectLeftImageButtonText);
		selectRightImageButton = new JButton(SelectRightImageButtonText);
		selectLeftImagePointsButton = new JButton(SelectLeftImagePointsButtonText);
		selectRightImagePointsButton = new JButton(SelectRightImagePointsButtonText);
		
		// add center image
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 2, 10, 10));
		imagePanel.add(leftImageView);
		imagePanel.add(rightImageView);
		this.add(imagePanel, BorderLayout.CENTER);
		
		JPanel bottomButtonPanel = new JPanel(new FlowLayout());
		bottomButtonPanel.add(selectLeftImageButton);
		bottomButtonPanel.add(selectRightImageButton);
		bottomButtonPanel.add(selectLeftImagePointsButton);
		bottomButtonPanel.add(selectRightImagePointsButton);
		this.add(bottomButtonPanel, BorderLayout.SOUTH);
	}

	public JImage getLeftImageView() {
		return leftImageView;
	}

	public JImage getRightImageView() {
		return rightImageView;
	}

	public JButton getSelectLeftImageButton() {
		return selectLeftImageButton;
	}

	public JButton getSelectRightImageButton() {
		return selectRightImageButton;
	}

	public JButton getSelectLeftImagePointsButton() {
		return selectLeftImagePointsButton;
	}

	public JButton getSelectRightImagePointsButton() {
		return selectRightImagePointsButton;
	}

}
