package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class StatsMenu extends GameMenu{
    private final Rectangle statsMenuArea;
    private final Color statsMenuColor;
    Game game;
    MyButton xBtn;
    
    public StatsMenu(Game game){
        super(game);
        this.game = game;
        xBtn = new MyButton(1228, 50, 40, 40, "x");
        statsMenuArea = new Rectangle(268, 50, 1000, 690);
        statsMenuColor = Color.white;
    }
    
    @Override
    public void draw(Graphics2D gr){
        if(!getIsOpen()){
            return;
        }
                
        paintStatsMenuArea(gr);
      
        xBtn.draw(gr, game.getMousePosition());

        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString("Statisztikák", 700, 80);

        gr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        gr.drawString("Bevételek", 278, 120);
        gr.drawString("- Bevétel1: ababababababababababababab", 278, 160);
        gr.drawString("- Bevétel2: ababababababababababababab", 278, 200);
        gr.drawString("- Bevétel3: ababababababababababababab", 278, 240);
        gr.drawString("- Bevétel4: ababababababababababababab", 800, 160);
        gr.drawString("- Bevétel5: ababababababababababababab", 800, 200);
        gr.drawString("- Bevétel6: ababababababababababababab", 800, 240);

        gr.drawString("Kiadások", 278, 320);
        gr.drawString("- Kiadás1: ababababababababababababab", 278, 360);
        gr.drawString("- Kiadás2: ababababababababababababab", 278, 400);
        gr.drawString("- Kiadás3: ababababababababababababab", 278, 440);
        gr.drawString("- Kiadás4: ababababababababababababab", 800, 360);
        gr.drawString("- Kiadás5: ababababababababababababab", 800, 400);
        gr.drawString("- Kiadás6: ababababababababababababab", 800, 440);

        gr.drawString("Építési adatok", 278, 520);
        gr.drawString("- Építési adat1: ababababababababababababab", 278, 560);
        gr.drawString("- Építési adat2: ababababababababababababab", 278, 600);
        gr.drawString("- Építési adat3: ababababababababababababab", 278, 640);
        gr.drawString("- Építési adat4: ababababababababababababab", 800, 560);
        gr.drawString("- Építési adat5: ababababababababababababab", 800, 600);
        gr.drawString("- Építési adat6: ababababababababababababab", 800, 640);
    }
    
    private void paintStatsMenuArea(Graphics2D gr){
        gr.setColor(statsMenuColor);
        int x = statsMenuArea.x;
        int y = statsMenuArea.y;
        int width = statsMenuArea.width;
        int height = statsMenuArea.height;
        gr.fillRect(x, y, width, height);
    }
    
    @Override
    public void click(Point p){
        if(xBtn.isHovered(p)){
            this.setIsOpen(false);
        }
    }
    
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        if(this.getIsOpen()){
            areas.add(statsMenuArea);
        }
        return areas;
    }
}
