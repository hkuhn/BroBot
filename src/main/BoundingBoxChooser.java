package main;


import april.util.JImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BoundingBoxChooser extends JFrame {

    private JImage imageHolder;
    private JButton selectButton;
    private java.util.List<Point2D> points;
    private Rectangle rectangle;
    private BroBotApplicationController applicationController;

    public BoundingBoxChooser(BufferedImage image, BroBotApplicationController applicationController) {
        super("Choose Box");

        this.setLayout(new BorderLayout());

        this.applicationController = applicationController;

        imageHolder = new JImage();
        this.add(imageHolder, BorderLayout.CENTER);

        selectButton = new JButton("Calc");
        this.add(selectButton, BorderLayout.SOUTH);
        this.selectButton.setEnabled(false);

        this.setSize(1024, 600);
        this.setVisible(true);

        points = new ArrayList<Point2D>();

        this.imageHolder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                didClickMouse(me);
            }
        });

        this.selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Rectangle rectangle = new Rectangle();
                for (Point2D point : points) {
                    rectangle.add(point);
                }
                BoundingBoxChooser.this.rectangle = rectangle;
                BoundingBoxChooser.this.applicationController.boundingBoxChooserDidChoose(BoundingBoxChooser.this);
            }
        });

    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    protected void didClickMouse(MouseEvent me) {
        // toggle click action
        // retrieve pixel coordinate point
        Point input = me.getPoint();

        final Point2D guiPoint = new Point2D.Double(input.x, input.y);
        // System.out.println("Clicked at " + guiPoint + " w.r.t GUI.");
        AffineTransform imageTransform = null;
        try {
            imageTransform = this.imageHolder.getAffine().createInverse();
        } catch ( Exception e ) {
            System.out.println("Fuck.");
            e.printStackTrace();
            return;
        }
        Point2D imagePoint = imageTransform.transform(guiPoint, null);

        // test bounds
        if ( imagePoint.getX() < 0 || imagePoint.getX() > 1296 ||
                imagePoint.getY() < 0 || imagePoint.getY() > 964 ) {
            System.out.println("Error. Click Went beyond bounds");
            return;
        }

        points.add(imagePoint);

        if ( points.size() > 4 ) {
            this.selectButton.setEnabled(true);
        }
    }



}
