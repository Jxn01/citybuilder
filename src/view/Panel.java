package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

//The canvas of the game, where everything is drawn
public class Panel extends JPanel implements ActionListener{
    
    MenuState state;
    Timer timer;
    Frame frame;
    
    Intro intro;
    MainMenu mainMenu;
    Tutorial tutorial;
    NewGame newGame;
    SavedGame savedGame;
    Game game;

    public Panel(Frame frame) {
        setFocusable(true); // Make the panel focusable
        this.frame = frame;
        timer = new Timer(20, this);
        timer.start();
        state = MenuState.MAINMENU;
        //state = MenuState.INTRO;
        
        intro = new Intro();
        mainMenu = new MainMenu(this);
        tutorial = new Tutorial(this);
        newGame = new NewGame(this);
        savedGame = new SavedGame(this);
        game = new Game(this);
        
        //CLICK
        addMouseListener(new MouseAdapter() { 
          public void mousePressed(MouseEvent me) {
            switch(state) {
                case INTRO:         
                    break;
                case MAINMENU:
                    mainMenu.click(me.getPoint());
                    break;
                case TUTORIAL:
                    tutorial.click(me.getPoint());
                    break;
                case NEWGAME:
                    newGame.click(me.getPoint());
                    break;
                case SAVEDGAMES:
                    savedGame.click(me.getPoint());
                    break;
                case GAME:
                    break;
            }
          } 
        });
        
        //KEYPRESS
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (state) {
                    case NEWGAME:
                        newGame.keyPressed(e);
                        break;
                }
            }
        });
        
        //EGÉR GÖRGŐ EVENT
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                //System.out.println("Rotated Up... " + e.getWheelRotation());
                game.addToZoom(1);
            } else {
                //System.out.println("Rotated Down... " + e.getWheelRotation());
                game.addToZoom(-1);
            }
        });
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        int w = getWidth();
        int h = getHeight();
        Graphics2D gr = (Graphics2D)g;  
        
        switch(state){
            case INTRO:
                intro.draw(this,gr);            
                break;
            case MAINMENU:
                mainMenu.draw(this,gr);
                break;
            case TUTORIAL:
                tutorial.draw(this,gr);
                break;
            case NEWGAME:
                newGame.draw(this,gr);
                break;
            case SAVEDGAMES:
                savedGame.draw(this,gr);
                break;
            case GAME:
                game.draw(this,gr);
                break;
            case EXIT:
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                break;
            
        }
    }
    
    //gets called every 40 milliseconds
    @Override
    public void actionPerformed(ActionEvent ev){
        if(ev.getSource()==timer){
          repaint();
        }
    };
    
    public void setState(MenuState s) {
        this.state = s;
    }
  
}
