package view.gui.game;

import controller.GameManager;
import model.GameData;
import util.Logger;
import view.components.custom.MyButton;
import view.gui.Game;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import util.ResourceLoader;

/**
 * This class implements the top bar of the game gui
 */
public class TopBar extends GameMenu {
    private final Rectangle topBarArea;
    private final Color topBarColor;
    MyButton hamburgerBtn;
    HamburgerMenu hamburgerMenu;
    private Image fireStorm,covid,financialCrisis;
    private catastrophyTimer econTimer,fireTimer,covidTimer;

    public static class catastrophyTimer {
        public long startTime;
        public long elapsedTime;
        
        public catastrophyTimer() {
            startTime = 0;
            elapsedTime = 0;
        }
    }
    
    /**
     * Constructor of the top bar
     *
     * @param game is the main game object
     */
    public TopBar(Game game) {
        super(game);

        hamburgerMenu = new HamburgerMenu(game);
        hamburgerBtn = new MyButton(0, 0, 40, 40, "hamburgerMenu");
        topBarArea = new Rectangle(0, 0, 1536, 40);
        topBarColor = Color.white;
        
        econTimer = new catastrophyTimer();
        fireTimer = new catastrophyTimer();
        covidTimer = new catastrophyTimer();
        
        try {
            fireStorm = ResourceLoader.loadImage("firestorm.png");
            covid = ResourceLoader.loadImage("covid.png");
            financialCrisis = ResourceLoader.loadImage("financial_crisis.png");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Draw the bottom top on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr) {
        GameData gd = GameManager.getGameData();

        paintTopBarArea(gr);
        hamburgerMenu.draw(gr);
        hamburgerBtn.draw(gr, game.getMousePosition());
        gr.setColor(Color.black);
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        gr.drawString("Költségvetés: " + gd.getBudget() + "$", 40, 30);
        gr.drawString("Elégedettség: " + gd.getAverageSatisfaction() + "%", 400, 30);
        gr.drawString("Populáció: " + gd.getPopulation(), 700, 30);
        gr.drawString("Dátum: " + gd.getInGameCurrentDate(), 1000, 30);
        gr.drawString("Éves adók: " + gd.getYearlyTaxes() + "$", 1300, 30);
        gr.drawString("Város: " + gd.getCityName(), 1600, 30);
        
        drawCatastrophyIcons(gr);
    }

    /**
     * Draw the top bar's background area on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintTopBarArea(Graphics2D gr) {
        gr.setColor(topBarColor);
        int x = topBarArea.x;
        int y = topBarArea.y;
        int width = game.width();
        int height = topBarArea.height;
        gr.fillRect(x, y, width, height);
    }

    /**
     * Upon a click event, roll the click event further into a submenu
     *
     * @param p is the current cursor location
     */
    @Override
    public void click(Point p) {
        hamburgerMenu.click(p);
        if (hamburgerBtn.isHovered(p)) {
            Logger.log("Hamburger menu opened");
            boolean negated = !hamburgerMenu.getIsOpen();
            hamburgerMenu.setIsOpen(negated);
        }
    }

    /**
     * Get the top bar's and all it's submenu areas as rectangles
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        areas.add(topBarArea);
        areas.addAll(hamburgerMenu.getMenuAreas());
        return areas;
    }

    public void setWidth(int width) {
        topBarArea.width = width;
    }
    
    /**
     * In case of a catastrophy happening, draw the catastropy icon in
     * the top right corner
     * @param gr is the graphics context of the main Panel object
     */
    public void drawCatastrophyIcons(Graphics2D gr) {
        //fireStorm,covid,financialCrisis
        fireTimer.elapsedTime = System.currentTimeMillis() - fireTimer.startTime;
        covidTimer.elapsedTime = System.currentTimeMillis() - covidTimer.startTime;
        econTimer.elapsedTime = System.currentTimeMillis() - econTimer.startTime;
        
        if(fireTimer.elapsedTime < 10000) {
            gr.drawImage(fireStorm,game.width()-40,40,40,40, null);
        }
        if(covidTimer.elapsedTime < 10000) {
            gr.drawImage(covid,game.width()-80,40,40,40, null);
        }
        if(econTimer.elapsedTime < 10000) {
            gr.drawImage(financialCrisis,game.width()-120,40,40,40, null);
        }
    }
    
    public void showCatastrophyIcon(String name) {
        switch(name) {
            case "fire" -> fireTimer.startTime = System.currentTimeMillis();
            case "econ" -> econTimer.startTime = System.currentTimeMillis();
            case "covid" -> covidTimer.startTime = System.currentTimeMillis();
        }
    }
}
