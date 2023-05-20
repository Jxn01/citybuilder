package view.gui.game;

import controller.GameManager;
import model.GameData;
import model.Person;
import model.buildings.playerbuilt.*;
import model.field.PlayableField;
import util.Logger;
import view.components.custom.MyButton;
import view.gui.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the statistics menu of the game gui
 */
public class StatsMenu extends GameMenu {
    private final Rectangle statMenuArea;
    private final Color statsMenuColor;
    Game game;
    MyButton xBtn;

    /**
     * Constructor of the statistics menu
     *
     * @param game is the main Game object
     */
    public StatsMenu(Game game) {
        super(game);

        this.game = game;
        xBtn = new MyButton(1420, 50, 40, 40, "x");
        statMenuArea = new Rectangle(460, 50, 1000, 850);
        statsMenuColor = Color.white;
    }

    /**
     * Draw the statistics menu on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    @Override
    public void draw(Graphics2D gr) {
        if (getIsOpen()) {
            paintStatsMenuArea(gr);
            GameData gd = GameManager.getGameData();

            xBtn.draw(gr, game.getMousePosition());

            List<PlayableField> l = Arrays.stream(gd.getFields())
                    .flatMap(Arrays::stream)
                    .filter(f -> f instanceof PlayableField)
                    .map(f -> (PlayableField) f).toList();

            int tax = gd.getYearlyTaxes();
            int retired = (int) gd.getPeople().stream().filter(Person::isRetired).count();
            int working = (int) gd.getPeople().stream().filter(p -> !p.isRetired()).count();
            int homeless = (int) gd.getPeople().stream().filter(p -> p.getHome() == null).count();
            int unemployed = (int) gd.getPeople().stream().filter(p -> p.getWorkplace() == null).count();
            int employed = (int) gd.getPeople().stream().filter(p -> p.getWorkplace() != null).count();
            int total = gd.getPeople().size();
            int founders = (int) gd.getPeople().stream().filter(Person::isFounder).count();
            int nonFounders = total - founders;

            int stadiums = ((int) l.stream().filter(f -> f.getBuilding() instanceof Stadium).count()) / 4;
            int stadiumMaintenance = GameManager.getStadiumMaintenanceCost() * 4;

            int fireDepartments = (int) l.stream()
                    .filter(f -> f.getBuilding() instanceof FireDepartment)
                    .count();
            int fireDepartmentMaintenance = GameManager.getFireStationMaintenanceCost();

            int policeStations = (int) l.stream()
                    .filter(f -> f.getBuilding() instanceof PoliceStation)
                    .count();
            int policeStationMaintenance = GameManager.getPoliceMaintenanceCost();

            int babyForests = (int) l.stream()
                    .map(PlayableField::getBuilding)
                    .filter(building -> building instanceof Forest)
                    .map(Forest.class::cast)
                    .filter(f -> f.getGrowTime() > f.getGrowStage())
                    .count();

            int forestMaintenance = GameManager.getForestMaintenanceCost();

            int grownForests = (int) l.stream()
                    .map(PlayableField::getBuilding)
                    .filter(building -> building instanceof Forest)
                    .map(Forest.class::cast)
                    .filter(f -> f.getGrowTime() == f.getGrowStage())
                    .count();

            int roads = (int) l.stream()
                    .filter(f -> f.getBuilding() instanceof Road)
                    .count();
            int roadMaintenance = GameManager.getRoadMaintenanceCost();
            int averagePension = (int) gd.getPeople()
                    .stream()
                    .filter(Person::isRetired)
                    .mapToInt(p -> (int) p.getPayedTaxes()
                                .stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0) / 2
                    )
                    .average()
                    .orElse(0);

            gr.setColor(Color.black);
            gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            gr.drawString("Statisztikák", 892, 80);

            gr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            gr.drawString("Általános játék statisztikák", 278 + 192, 120);
            gr.drawString("- A játék kezdete: " + gd.getStartDate(), 278 + 192, 160);
            gr.drawString("- A Város alapításának dátuma: " + gd.getInGameStartDate(), 278 + 192, 200);
            gr.drawString("- Város név: " + gd.getCityName(), 790 + 192, 160);
            gr.drawString("- A jelenlegi dátum: " + gd.getInGameCurrentDate(), 790 + 192, 200);

            gr.drawString("A Város statisztikái:", 278 + 192, 320);
            gr.drawString("- Teljes populáció: " + total + " fő", 278 + 192, 360);
            gr.drawString("- Nyugdíjasok száma: " + retired + " fő", 278 + 192, 400);
            gr.drawString("- Munkaképes életkorúak száma: " + working + " fő", 278 + 192, 440);
            gr.drawString("- Foglalkoztatottak száma: " + employed + " fő", 278 + 192, 480);
            gr.drawString("- Hajléktalanok száma: " + homeless + " fő", 790 + 192, 360);
            gr.drawString("- Munkanélküliek száma: " + unemployed + " fő", 790 + 192, 400);
            gr.drawString("- Alapítók száma: " + founders + " fő", 790 + 192, 440);
            gr.drawString("- Bevándoroltak száma: " + nonFounders + " fő", 790 + 192, 480);

            gr.drawString("Éves bevételek és kiadások:", 278 + 192, 560);
            gr.drawString("- Éves adó: " + employed + " fő * +" + tax + "$", 278 + 192, 600);
            gr.drawString("- Éves átlagos nyugdíj: " + retired + " fő * -" + averagePension + "$", 790 + 192, 600);

            gr.drawString("Épület adatok:", 278 + 192, 680);
            gr.drawString("- Stadionok száma: " + stadiums + " (karbantartás: " + stadiums + " * -" + stadiumMaintenance + "$)", 278 + 192, 720);
            gr.drawString("- Tűzoltóságok száma: " + fireDepartments + " (karbantartás: " + fireDepartments + " * -" + fireDepartmentMaintenance + "$)", 278 + 192, 760);
            gr.drawString("- Rendőrségek száma: " + policeStations + " (karbantartás: " + policeStations + " * -" + policeStationMaintenance + "$)", 790 + 192, 720);
            gr.drawString("- Csemete erdők száma: " + babyForests + " (karbantartás: " + babyForests + " * -" + forestMaintenance + "$)", 790 + 192, 760);
            gr.drawString("- Utak száma: " + roads + " (karbantartás: " + roads + " * -" + roadMaintenance + "$)", 278 + 192, 800);
            gr.drawString("- Kifejlett erdők száma: " + grownForests + " (karbantartás: nincs)", 790 + 192, 800);
        }
    }

    /**
     * Draw the statistics menu's background area on the screen
     *
     * @param gr is the graphics context of the main Panel object
     */
    private void paintStatsMenuArea(Graphics2D gr) {
        gr.setColor(statsMenuColor);
        int x = statMenuArea.x;
        int y = statMenuArea.y;
        int width = statMenuArea.width;
        int height = statMenuArea.height;
        gr.fillRect(x, y, width, height);
    }

    /**
     * Click event handler of the statistics menu
     *
     * @param p is the current mouse position
     */
    @Override
    public void click(Point p) {
        if (getIsOpen()) {
            if (xBtn.isHovered(p)) {
                Logger.log("Closed statistics menu");
                this.setIsOpen(false);
            }
        }
    }

    /**
     * Get the statistics menu's area as a rectangle
     * This is important for click event exceptions
     *
     * @return an arraylist of rectangles
     */
    @Override
    public ArrayList<Rectangle> getMenuAreas() {
        ArrayList<Rectangle> areas = new ArrayList<>();
        if (this.getIsOpen()) {
            areas.add(statMenuArea);
        }
        return areas;
    }
}
