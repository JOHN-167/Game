import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.List;
public class GameInterface extends JFrame {
    int space = 200;
    int frameHeight;
    Random rnd = new Random();
    int startX = 0;
    int birdX = 200;
    int birdRad = 15;
    List<TopCollar> collars = new LinkedList<>();
    final int ogCounter = 800;
    final int SPACE = 6;
    int counter = ogCounter;
    public GameInterface (int frameHeight) {
        this.frameHeight = frameHeight;
        setSize(1000,frameHeight);
        setMinimumSize(new Dimension(500,frameHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,frameHeight));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        Bird bird = new Bird(birdX,birdRad);
        class Canvas extends JPanel implements KeyListener {    
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
                bird.paintComponent(g);
            }
            @Override
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        counter = ogCounter;
                        bird.flyUp();
                        break;
                }
            }
            @Override
            public void keyReleased (KeyEvent e) {}
            @Override
            public void keyTyped (KeyEvent e) {}
        }
        JButton startButton = new JButton("Begin");
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                remove((JButton) e.getSource());
                for (int i = 0; i < 11; i++) 
                    collars.add(new TopCollar(i*(50+space)+500,rnd.nextInt(frameHeight*4/6)));
                
                Canvas canvas = new Canvas();
                canvas.setVisible(true);
                addKeyListener(canvas);
                add(canvas);
                Timer birdTimer = new Timer (counter/(ogCounter/SPACE), new ActionListener () {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        bird.gravity();
                        canvas.revalidate();
                        canvas.repaint();
                        counter -= 3;
                        System.out.println("timer = " + ((Timer)(e.getSource())).getDelay());
                    }
                });
                Timer collarTimer = new Timer (15, new ActionListener () {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        int birdY = bird.getY();
                        for (TopCollar collar : collars){
                            collar.step();
                            boolean testX = birdX > collar.getX()-birdRad*2 && 
                                            birdX < collar.getX()+50;
                            boolean testY = birdY < collar.getHeight() || 
                                    birdY+30 > collar.getHeight()+frameHeight/6;
                            // if (testX && testY){
                            //     ((Timer)e.getSource()).stop();
                            //     birdTimer.stop();
                            // }
                        }
                        canvas.revalidate();
                        canvas.repaint();
                    }
                });
                collarTimer.start();
                birdTimer.setDelay(counter/(ogCounter/SPACE));
                birdTimer.start();

            }
        });
        add(startButton);
        
    }
    public static void main (String[] args) {
        GameInterface theFrame = new GameInterface(600);
        theFrame.setVisible(true);
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
        public Bird (int X, int rad) {
            this.X = X;
            Y = frameHeight*4/6;
            // Y = 400; 
            this.rad = rad;
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.YELLOW);
            g.fillOval(X, Y, rad*2, rad*2);
        }
        public void gravity () {
            Y += 1;
            repaint();
        }
        public void flyUp () {
            Y -= 40;
            repaint();
        }
        public int getY() {
            return Y;
        }
    }
}