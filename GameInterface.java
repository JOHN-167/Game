import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class GameInterface extends JFrame {
    int space = 200;
    int frameHeight;
    Random rnd = new Random();
    int startX = 0;
    List<TopCollar> collars = new LinkedList<>();
    // boolean gamePlayed = true;

    public GameInterface (int frameHeight) {
        this.frameHeight = frameHeight;
        setSize(1000,frameHeight);
        setMinimumSize(new Dimension(500,frameHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,frameHeight));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 11; i++) 
            collars.add(new TopCollar(i*(50+space)+500,rnd.nextInt(frameHeight*4/6)));
        Canvas canvas = new Canvas();
        canvas.setVisible(true);
        add(canvas);
        Timer collarTimer = new Timer (15, new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                for (TopCollar collar : collars)
                    collar.step();
                canvas.revalidate();
                canvas.repaint();
            }
        });
        collarTimer.start();
    }
    public static void main (String[] args) {
        GameInterface theFrame = new GameInterface(600);
        theFrame.setVisible(true);
    }
    class Canvas extends JPanel {    
        public Canvas () {
            setBackground(Color.BLUE);
        }
        @Override
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            for (TopCollar collar : collars) {
                collar.paintComponent(g);
                int bHeight = frameHeight*5/6 - collar.getHeight();
                BottomCollar bCollar = new BottomCollar(collar.getX(),bHeight);
                bCollar.paintComponent(g);
            }
        }
    }
    class TopCollar extends JComponent {
        int X, height;
        public TopCollar (int X, int height) {
            this.X = X;
            this.height = height;
        }
        @Override
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(X, 0, 50, height);
        }
        public void step () {
            X -= 1;
            if (X < -50) {
                X = 11*(space+50);
                height = rnd.nextInt(frameHeight*4/6);
            }
            repaint();
        }
        public int getHeight() {
            return height;
        }
        public int getX() {
            return X;
        }
    }
    class BottomCollar extends JComponent {
        int X, height;
        public BottomCollar (int X, int height) {
            this.X = X;
            this.height = height;
        }
        @Override
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(X, 600-height, 50, 600);
        }
    }
    class Bird extends JComponent {
        int X,Y,rad;
        public Bird () {
            X = 200;
        }
    }
}