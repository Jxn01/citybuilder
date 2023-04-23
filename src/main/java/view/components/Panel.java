package view.components;

import controller.GameManager;
import view.enums.MenuState;
import view.gui.Game;
import view.gui.NewGame;
import view.gui.mainmenu.*;

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

    private final GameGUI gameGUI;
    private Timer timer;
    private MenuState state;
    
    private final Intro intro;
    private final MainMenu mainMenu;
    private final Tutorial tutorial;
    private final NewGame newGame;
    private final LoadGame loadGame;
    private final Game game;

    private final GameManager gm;
    
    private int width;
    private int height;

    /**
     * The constructor of the Panel class
     * @param gameGUI is the main window, where the panel is placed on
     */
    public Panel(GameGUI gameGUI) {
        this.gm = new GameManager();
        setPreferredSize(gameGUI.getContentPane().getSize());
        setSize(gameGUI.getContentPane().getSize());
        setFocusable(true);
        this.gameGUI = gameGUI;
   
        Timer responsivityTimer = new Timer(50, (ActionEvent e) -> {
            Dimension panelSize = getSize();
            width = panelSize.width;
            height = panelSize.height;
        });
        responsivityTimer.start();
        
        state = MenuState.INTRO;

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
        addMouseWheelListener(game::mouseWheelRotated);
    }

    /**
     * The panel draws the menu based on the current menu state
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
            case EXIT -> gameGUI.dispatchEvent(new WindowEvent(gameGUI, WindowEvent.WINDOW_CLOSING));
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
     * Setter for the menu state
     * @param s is the new menu state
     */
    public void setState(MenuState s) {
        this.state = s;
    }
    
    /**
     * Allows the gui submenus to call the Frame's exit method
     */
    public void exit(){
        gameGUI.exit();
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

    /**
     * Getter for the game manager
     * @return game manager
     */
    public GameManager getGameManager() {
        return gm;
    }
}
