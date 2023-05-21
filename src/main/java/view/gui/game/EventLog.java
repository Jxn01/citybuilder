package view.gui.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import view.gui.Game;

/**
 * This class implements the event log submenu in the game
 */
public class EventLog extends GameMenu {
    private final @NotNull Rectangle eventLogArea;
    private final Color eventLogAreaColor;
    private final int MAXEVENTS = 9;
    private ArrayList<String> events;
    private long startTime;
    private long elapsedTime;

    /**
     * Constructor of the Event Log area
     *
     * @param game is the main game object
     */
    public EventLog(@NotNull Game game) {
        super(game);
        int width = 350;
        int height = 210;
        eventLogArea = new Rectangle(game.width() - width, game.height() - 40 - height, width, height);
        events = new ArrayList<>();
        eventLogAreaColor = Color.red;
        startTime = 0;
        elapsedTime = 0;
    }
    
    /**
     * Draw the event log on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(@NotNull Graphics2D gr) {
        /*
        gr.setColor(eventLogAreaColor);
        int x = game.width() - eventLogArea.width;
        int y = game.height() - 40 - eventLogArea.height;
        int width = eventLogArea.width;
        int height = eventLogArea.height;
        gr.fillRect(x, y, width, height);
        */
        
        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        for(int i=0;i<events.size();++i){
            gr.drawString(events.get(i), game.width() - eventLogArea.width + 10, game.height() - 40 - 20 * (i+1) );
        }
        
        elapsedTime = System.currentTimeMillis() - startTime;
        if(elapsedTime > 5000) {
            events.removeAll(events);
            
        }
    }
    
    /**
     * Upon a click event, do nothing
     *
     * @param p is the current cursor location
     */
    @Override
    public void click(Point p) {}
    
    /**
     * Get the event log's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles
     */
    @Override
    public @NotNull ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(eventLogArea);
        return areas;
    }
    
    public void log(String s){
        events.add(s);
        if(events.size() > MAXEVENTS){
            events.remove(0);
        }
        
        startTime = System.currentTimeMillis();
        
    }
}
