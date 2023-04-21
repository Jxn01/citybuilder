package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * The 'canvas' of the game, where all the submenus are drawn
 */
public class Panel extends JPanel implements ActionListener {

    private Frame frame;
    private Timer timer;
    private MenuState state;
    
    private Intro intro;
    private MainMenu mainMenu;
    private Tutorial tutorial;
    private NewGame newGame;
    private LoadGame loadGame;
    private Game game;
    
    private int width;
    private int height;

    /**
     * The constructor of the Panel class
     * @param frame is the main window, where the panel is placed on
     */
    public Panel(Frame frame) {
        setPreferredSize(frame.getContentPane().getSize());
        setSize(frame.getContentPane().getSize());
        setFocusable(true);
        this.frame = frame;
   
        Timer timer = new Timer(50, (ActionEvent e) -> {
            Dimension panelSize = getSize();
            width = panelSize.width;
            height = panelSize.height;
        });
        timer.start();
        
        state = MenuState.GAME;

        intro = new Intro(this);
        mainMenu = new MainMenu(this);
        tutorial = new Tutorial(this);
        newGame = new NewGame(this);
        loadGame = new LoadGame(this);
        game = new Game(this);

        //CLICK EVENT
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                switch (state) {
                    case MAINMENU -> mainMenu.click(me.getPoint());
                    case TUTORIAL -> tutorial.click(me.getPoint());
                    case NEWGAME -> newGame.click(me.getPoint());
                    case LOADGAME -> loadGame.click(me.getPoint());
                    case GAME -> game.click(me.getPoint());
                }
            }
        });
     
        //KEYPRESS EVENT
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (state) {
                    case NEWGAME -> newGame.keyPressed(e);
                    case GAME -> game.keyPressed(e);
                }
            }
        });

        //MOUSE WHEEL EVENT
        addMouseWheelListener(e -> {
            game.mouseWheelRotated(e);
        });
    }

    /**
     * The panel draws the menu based on the current menustate
     * @param g is the graphics context of the Panel object
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        switch (state) {
            case INTRO -> intro.draw(this, gr);
            case MAINMENU -> mainMenu.draw(this, gr);
            case TUTORIAL -> tutorial.draw(this, gr);
            case NEWGAME -> newGame.draw(this, gr);
            case LOADGAME -> loadGame.draw(this, gr);
            case GAME -> game.draw(this, gr);
            case EXIT -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        }
    }

    /**
     * The main game loop that listens to the timer
     * @param ev is the event happening on the panel 
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer) {
            repaint();
        }
    }

    /**
     * Setter for the menustate
     * @param s is the new menustate
     */
    public void setState(MenuState s) {
        this.state = s;
    }
    
    /**
     * Allows the gui submenus to call the Frame's exit method
     */
    public void exit(){
        frame.exit();
    }

    /**
     * Getter for width
     * @return width
     */
    public int width() {
        return width;
    }

    /**
     * Getter for height
     * @return height
     */
    public int height() {
        return height;
    }
    
}
