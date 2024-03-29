package view.gui.mainmenu;

import org.jetbrains.annotations.NotNull;
import util.Logger;
import util.ResourceLoader;
import view.components.Panel;
import view.components.custom.MyButton;
import view.enums.MenuState;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements the GUI of the tutorial submenu
 */
public class Tutorial {

    private final view.components.Panel panel;
    private final int MINIMG = 0;
    private final int MAXIMG = 11;
    private final @NotNull ArrayList<TutorialData> allData;
    private final @NotNull ArrayList<String> allText;
    private int currentImg;
    private Image background;
    private MyButton leftBtn;
    private MyButton rightBtn;
    private MyButton backBtn;

    /**
     * The constructor of the tutorial submenu
     *
     * @param panel is the game's main Panel object
     */
    public Tutorial(view.components.Panel panel) {
        this.panel = panel;
        currentImg = 0;
        allText = new ArrayList<>();
        allData = new ArrayList<>();

        //add all TEXTS
        allText.add("A jobb alsó sarokban található az idő menü.");
        allText.add("Középen alul található az építési menü.");
        allText.add("A játék statisztikái itt találjatók (bal alsó sarok).");
        allText.add("Adót kivetni az adó gombbal lehet (bal alsó sarok).");
        allText.add("Katasztrófát előidézni pedig ezzel a gombbal lehet...");
        allText.add("A játék menüje a hamburgermenüben található (jobb felső sarok).");
        allText.add("A város statisztikái a felső sávban láthatók.");
        allText.add("Építés: kiválasztjuk az épületet, majd kattintunk a játéktéren.");
        allText.add("Törölni hasonlóan tudunk az 'X' gomb kiválasztásával.");
        allText.add("Az épület statisztikái és a fejleszési lehetőség kattintás után elérhető.");
        allText.add("Tűz eloltása: az épületre kattintás után az 'Elolt' gombbal lehetséges.");
        allText.add("Sok sikert, Polgármester!");

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

            leftBtn = new MyButton(10, 350, 100, 100, "arrowLeft");
            rightBtn = new MyButton(1436, 350, 100, 100, "arrowRight");
            backBtn = new MyButton(0, 0, 75, 75, "back");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * draw the Tutorial gui on the screen
     *
     * @param panel is the game's main Panel object
     * @param gr    is the graphics context of the main Panel object
     */
    public void draw(@NotNull Panel panel, @NotNull Graphics2D gr) {
        //draw the background
        gr.drawImage(background, 0, 0, panel.width(), panel.height(), null);

        final int btnWidth = 100;
        final int btnHeight = 100;
        final int btnY = panel.height() / 2 - btnHeight / 2;
        final int rightX = panel.width() - btnWidth - 10;

        //draw all buttons
        leftBtn.setY(btnY);
        leftBtn.draw(gr, panel.getMousePosition());
        rightBtn.setX(rightX);
        rightBtn.setY(btnY);
        rightBtn.draw(gr, panel.getMousePosition());
        backBtn.draw(gr, panel.getMousePosition());

        //draw the current tutorial image
        final int imgWidth = panel.width() - (btnWidth * 2 + 100);
        final int imgHeight = panel.height() - 150;
        gr.drawImage(allData.get(currentImg).img, 150, 100, imgWidth, imgHeight, null);

        //write the current image text on the screen
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        gr.drawString((currentImg + 1) + ". lépés: " + allData.get(currentImg).imgText, 150, 75);
    }

    /**
     * the click handler of this class
     *
     * @param p is the Point where the click happened
     */
    public void click(Point p) {
        if (leftBtn.isHovered(p)) {
            Logger.log("left button clicked");
            currentImg--;
            if (currentImg < MINIMG) {
                currentImg = MAXIMG;
            }
        } else if (rightBtn.isHovered(p)) {
            Logger.log("right button clicked");
            currentImg++;
            if (currentImg > MAXIMG) {
                currentImg = MINIMG;
            }
        } else if (backBtn.isHovered(p)) {
            Logger.log("back button clicked");
            panel.setState(MenuState.MAINMENU);
        }
    }

    /**
     * This class holds all data for one step of the tutorial slideshow
     */
    public class TutorialData {

        public String imgText;
        public Image img;
        public int imgNumber;

        /**
         * One step of the tutorial consists of a description, an image and a number
         *
         * @param imgText   description of the step
         * @param img       image of the step
         * @param imgNumber number of the steps
         */
        public TutorialData(String imgText, Image img, int imgNumber) {
            this.imgText = imgText;
            this.img = img;
            this.imgNumber = imgNumber;
        }
    }
}
