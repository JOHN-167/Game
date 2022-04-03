import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.List;
public class GameInterface  extends JFrame {
    Random rnd = new Random();
    Timer collarTimer;
    int space = 200;
    int frameHeight;
    int birdX = 200;
    int birdRad = 15;
    int score = 0;
    List<TopCollar> collars = new LinkedList<>();
    Color birdColor = Color.YELLOW;
    Color scoreColor = Color.BLACK;
    Color collarColor = Color.WHITE;
    Color canvasColor = Color.PINK;
    boolean gameOver = false; 
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
                setBackground(canvasColor);
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
                g.setColor(scoreColor);
                g.drawString((gameOver == true)? "Game Over" : "Score = " + score, getWidth()/2-50, getHeight()-40);
            }
            @Override
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        bird.flyUp();
                        break;
                }
            }
            @Override
            public void keyReleased (KeyEvent e) {}
            @Override
            public void keyTyped (KeyEvent e) {}
        }
        JPanel startPanel = new JPanel();
        JButton startButton = new JButton("Begin");
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(startPanel);
                for (int i = 0; i < 11; i++) 
                    collars.add(new TopCollar(i*(50+space)+500,rnd.nextInt(frameHeight*4/6)));
                
                Canvas canvas = new Canvas();
                canvas.setVisible(true);
                addKeyListener(canvas);
                add(canvas);
                Timer scoreTimer = new Timer (1000, new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        GameInterface.this.dispose();
                        System.exit(0);
                    }
                });
                Timer birdTimer = new Timer (6, new ActionListener () {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        bird.gravity();
                        canvas.revalidate();
                        canvas.repaint();
                        if (bird.getY()+(double)birdRad*4.5 > frameHeight) {
                            ((Timer)e.getSource()).stop();
                            collarTimer.stop();
                            scoreTimer.start();
                            gameOver = true;
                        }
                    }
                });
                collarTimer = new Timer (15, new ActionListener () {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        int birdY = bird.getY();
                        for (TopCollar collar : collars){
                            collar.step();
                            boolean testX = birdX > collar.getX()-birdRad*2 && 
                                            birdX < collar.getX()+50;
                            boolean testY = birdY < collar.getHeight() || 
                                    birdY+30 > collar.getHeight()+frameHeight/6;
                            if (testX && testY){
                                ((Timer)e.getSource()).stop();
                                birdTimer.stop();
                                gameOver = true;
                                scoreTimer.start();
                            }
                        }
                        canvas.revalidate();
                        canvas.repaint();
                    }
                });
                collarTimer.start();
                birdTimer.start();

            }
        });
        startPanel.add(startButton);
        add(startPanel);
        
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
            g.setColor(collarColor);
            g.fillRect(X, 0, 50, height);
        }
        public void step () {
            X -= 1;
            if (X < -50) {
                X = 11*(space+50);
                height = rnd.nextInt(frameHeight*3/6)+frameHeight/6;
            }
            if (X+50 == birdX)
                score++;
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
            g.setColor(collarColor);
            g.fillRect(X, 600-height, 50, 600);
        }
    }
    class Bird extends JComponent {
        int X,rad;
        double Y;
        public Bird (int X, int rad) {
            this.X = X;
            Y = frameHeight/2;
            // Y = 400; 
            this.rad = rad;
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(birdColor);
            g.fillOval(X, (int) Y, rad*2, rad*2);
        }
        public void gravity () {
            Y += 1.3;
            repaint();
        }
        public void flyUp () {
            if (Y > 40)
            Y -= 40;
            repaint();
        }
        public int getY() {
            return (int) Y;
        }
        public int getHeight() {
            return rad*2;
        }
    }
}