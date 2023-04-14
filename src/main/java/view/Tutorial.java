package view;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import util.ResourceLoader;

/**
 * This class implements the GUI of the tutorial submenu
 */
public class Tutorial {

    /**
     * This class holds all data for one step of the tutorial slideshow
     */
    public class TutorialData {
        
        public String imgText;
        public Image img;
        public int imgNumber;
        
        /**
         * One step of the tutorial consists of a description, an image and a number
         * @param imgText description of the step
         * @param img image of the step
         * @param imgNumber number of the step
         */
        public TutorialData(String imgText, Image img, int imgNumber) {
            this.imgText = imgText;
            this.img = img;
            this.imgNumber = imgNumber;
        }
    }

    private Panel panel;
    private final int MINIMG = 0;
    private final int MAXIMG = 2;
    private int currentImg;
    private Image background;
    private MyButton leftBtn;
    private MyButton rightBtn;
    private MyButton backBtn;
    private ArrayList<TutorialData> allData;
    private ArrayList<String> allText;

    /**
     * The constructor of the tutorial submenu
     * @param panel is the game's main Panel object
     */
    public Tutorial(Panel panel) {
        this.panel = panel;
        currentImg = 0;
        allText = new ArrayList<>();
        allData = new ArrayList<>();

        //add all TEXTS
        allText.add("első lépés dummy text");
        allText.add("második lépés dummy text");
        allText.add("harmadik lépés dummy text");

        try {
            background = ResourceLoader.loadImage("tutbg.png");

            //add all IMAGES
            for (int i = MINIMG; i <= MAXIMG; ++i) {
                String imgText = allText.get(i);
                //images are labeled with a number
                Image img = ResourceLoader.loadImage("tutorialImg" + i + ".png");
                //finally, add the image NUMBER
                TutorialData newData = new TutorialData(imgText, img, i);
                allData.add(newData);
            }

            leftBtn = new MyButton(0, 350, 100, 100, "arrowLeft");
            rightBtn = new MyButton(1436, 350, 100, 100, "arrowRight");
            backBtn = new MyButton(0, 0, 75, 75, "back");
        } catch (IOException ex) {}
    }

    /**
     * draw the Tutorial gui on the screen
     * @param panel is the game's main Panel object
     * @param gr is the graphics context of the main Panel object
     */
    public void draw(Panel panel, Graphics2D gr) {
        //draw the background
        gr.drawImage(background, 0, 0, 1536 + 15, 793, null);
        
        //draw all buttons
        leftBtn.draw(gr, panel.getMousePosition());
        rightBtn.draw(gr, panel.getMousePosition());
        backBtn.draw(gr, panel.getMousePosition());

        //draw the current tutorial image
        gr.drawImage(allData.get(currentImg).img, 143, 100, 1250, 675, null);
        
        //write the current image text on the screen
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString((currentImg + 1) + ". Lépés: " + allData.get(currentImg).imgText, 143, 75);
    }

    /**
     * the click handler of this class
     * @param p is the Point where the click happened 
     */
    public void click(Point p) {
        //the left button was clicked
        if (leftBtn.isHovered(p)) {
            currentImg--;
            if (currentImg < MINIMG) {
                currentImg = MAXIMG;
            }
        //the right button was clicked
        } else if (rightBtn.isHovered(p)) {
            currentImg++;
            if (currentImg > MAXIMG) {
                currentImg = MINIMG;
            }
        //the back button was clicked
        } else if (backBtn.isHovered(p)) {
            panel.setState(MenuState.MAINMENU);
        }
    }
}
