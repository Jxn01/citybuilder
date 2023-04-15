package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TaxMenu extends GameMenu {
    private final Rectangle taxMenuArea;
    private final Color taxMenuColor;
    MyButton xBtn;
    MyInputField input;
    MyButton modifyBtn;
    
    public TaxMenu(Game game){
        super(game);
        this.game = game;
        xBtn = new MyButton(1228, 50, 40, 40, "x");
        modifyBtn = new MyButton(960, 130, 120, 40, "modify");
        input = new MyInputField(630,125,325,50);
        taxMenuArea = new Rectangle(268, 50, 1000, 150);
        taxMenuColor = Color.white;
    }
    
    @Override
    public void draw(Graphics2D gr){
        if(!getIsOpen()){
            return;
        }
        paintTaxMenuArea(gr);
       
        xBtn.draw(gr, game.getMousePosition());

        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString("Adó", 750, 80);

        gr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        gr.drawString("Éves adó módosítása ($/fő/év): ", 350, 160);
        
        input.draw(gr, game.getMousePosition());
        modifyBtn.draw(gr, game.getMousePosition());
    }
    
    private void paintTaxMenuArea(Graphics2D gr){
        gr.setColor(taxMenuColor);
        int x = taxMenuArea.x;
        int y = taxMenuArea.y;
        int width = taxMenuArea.width;
        int height = taxMenuArea.height;
        gr.fillRect(x, y, width, height);
    }
    
    @Override
    public void click(Point p){
        if(xBtn.isHovered(p)){
            setIsOpen(false);
        }
        else if(modifyBtn.isHovered(p)){
            System.out.println("modifybutton pressed");
        }
    }
    
    public void keyPressed(KeyEvent e){
        char c = e.getKeyChar();
        if (c == KeyEvent.VK_BACK_SPACE) {
            input.deletelLast();
        } else if (Character.isDigit(c)) {
            input.add(c);
        }
    }
    
    @Override
    public ArrayList<Rectangle> getMenuAreas(){
        ArrayList<Rectangle> areas = new ArrayList<>();
        if(this.getIsOpen()){
            areas.add(taxMenuArea);
        }
        return areas;
    }
}
