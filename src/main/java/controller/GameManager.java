package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.graph.MutableGraph;
import controller.catastrophies.Catastrophe;
import controller.catastrophies.Covid;
import controller.catastrophies.FinancialCrisis;
import controller.catastrophies.Firestorm;
import controller.interfaces.SaveManager;
import controller.interfaces.SpeedManager;
import model.Coordinate;
import model.GameData;
import model.Person;
import model.buildings.Building;
import model.buildings.generated.*;
import model.buildings.interfaces.Flammable;
import model.buildings.interfaces.FunctionalBuilding;
import model.buildings.playerbuilt.Forest;
import model.enums.Effect;
import model.enums.Zone;
import model.field.Field;
import model.field.PlayableField;
import util.Date;
import util.GraphDeserializer;
import util.Logger;
import view.enums.Tile;
import view.gui.Game;

import javax.swing.*;
import java.io.File;
import java.util.Timer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private static final int STADIUM_BUILD_COST = 5000; // It will cost 5000 * 4 = 20000, because it contains 4 fields
    private static final int POLICE_BUILD_COST = 10000;
    private static final int ROAD_BUILD_COST = 1000;
    private static final int FIRE_STATION_BUILD_COST = 1000;
    private static final int FOREST_BUILD_COST = 1000;
    private static final int STADIUM_MAINTENANCE_COST = 5000;
    private static final int POLICE_MAINTENANCE_COST = 1000;
    private static final int ROAD_MAINTENANCE_COST = 100;
    private static final int FIRE_STATION_MAINTENANCE_COST = 1000;
    private static final int FOREST_MAINTENANCE_COST = 100;
    private static final int STADIUM_RANGE = 3;
    private static final int POLICE_RANGE = 3;
    private static final int FIRE_STATION_RANGE = 3;
    private static final int FOREST_RANGE = 3;
    private static final int INDUSTRIAL_RANGE = 3;
    private static final int FOREST_GROWTH_TIME = 2;
    private static final int MARK_RESIDENTIAL_COST = 1000;
    private static final int MARK_SERVICE_COST = 1000;
    private static final int MARK_INDUSTRIAL_COST = 1000;
    private static final int LEVEL_ONE_MAX_CAPACITY = 5;
    private static final int LEVEL_TWO_MAX_CAPACITY = 10;
    private static final int LEVEL_THREE_MAX_CAPACITY = 40;
    private static final int LEVEL_TWO_UPGRADE_COST = 10000;
    private static final int LEVEL_THREE_UPGRADE_COST = 100000;
    private static final double REFUND_PERCENT = 0.5;
    private static final int STARTER_MAP_SIZE = 51;
    private static final int STARTER_PEOPLE = 50;
    private static final int STARTER_BUDGET = 10000000;
    private static final int STARTER_TAXES = 1000;
    private static final double FIRE_POSSIBILITY = 0.001;
    private static final int MAX_FIRETRUCKS = 2;
    private static final int PENSION = 1000;
    private static final double CATASTROPHE_CHANCE = 0.0001;
    private static final double HOSPITAL_CHANCE = 0.1;
    private static final int MIN_POPULATION = 10; // for game over
    private static final int MIN_SATISFACTION = -10; // for game over
    private static final int BUILDING_MAX_HP = 10;
    private static final double STARTER_FOREST_PERCENTAGE = 0.1;
    private static final double FIRE_SPREAD_CHANCE = 0.1;
    private static GameData gameData;
    private final List<Catastrophe> catastrophes;
    private final String saveDirectory = System.getProperty("user.home") + File.separator + ".citybuilder" + File.separator + "saves";
    private SimulationSpeed simulationSpeed;
    private List<File> saveFiles;
    private Timer timer;
    private int delay = 1000;
    private int period = 1000;
    private final Game UI;

    /**
     * Constructor for the game manager.
     */
    public GameManager(Game UI) {
        this.UI = UI;
        simulationSpeed = SimulationSpeed.NORMAL;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Logger.log("Game manager created.");
    }

    public GameManager() {
        UI = null;
        simulationSpeed = SimulationSpeed.NORMAL;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Logger.log("Game manager created.");
    }


    /**
     * This method counts the disconnected part-graphs of the main graph.
     * Useful for checking if the demolition of a road would disconnect the graph.
     *
     * @param graph The graph to check.
     * @return The number of disconnected part-graphs.
     */
    public static int countDisconnectedGraphs(MutableGraph<Coordinate> graph) {
        int count = 0;

        Set<Coordinate> visited = new HashSet<>();
        for (Coordinate node : graph.nodes()) {
            if (!visited.contains(node)) {
                count++;
                visited.add(node);
                Queue<Coordinate> queue = new LinkedList<>();
                queue.add(node);
                while (!queue.isEmpty()) {
                    Coordinate current = queue.poll();
                    for (Coordinate neighbor : graph.adjacentNodes(current)) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return count;
    }

    public static MutableGraph<Coordinate> getGraph() {
        return gameData.getGraph();
    }

    /**
     * This method calculates the distance between two coordinates.
     *
     * @param a The first coordinate.
     * @param b The second coordinate.
     * @return The distance between the two coordinates.
     */
    private static int calculateDistance(Coordinate a, Coordinate b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * This method calculates the shortest path between two coordinates in the graph.
     *
     * @param start The start coordinate.
     * @param end   The end coordinate.
     * @return The shortest path between the two coordinates or null if there is no path.
     */
    public static List<Coordinate> findShortestPath(Coordinate start, Coordinate end) {
        Map<Coordinate, Integer> distances = new HashMap<>();
        PriorityQueue<Coordinate> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Map<Coordinate, Coordinate> previous = new HashMap<>();
        MutableGraph<Coordinate> graph = getGraph();
        for (Coordinate vertex : graph.nodes()) {
            if (vertex.equals(start)) {
                distances.put(vertex, 0);
                queue.offer(vertex);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
            }
            previous.put(vertex, null);
        }
        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            if (current.equals(end)) {
                break;
            }
            int currentDistance = distances.get(current);
            if (currentDistance == Integer.MAX_VALUE) {
                break;
            }
            Set<Coordinate> neighbors = graph.adjacentNodes(current);
            for (Coordinate neighbor : neighbors) {
                int newDistance = currentDistance + calculateDistance(current, neighbor);
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        if (previous.get(end) == null) {
            return new ArrayList<>(); // No path exists between start and end
        }
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }

    /**
     * This method calculates the length of the shortest path between two coordinates in the graph.
     *
     * @param c1 The first coordinate.
     * @param c2 The second coordinate.
     * @return The length of the shortest path between the two coordinates or 0 if there is no path.
     */
    public static int findShortestPathLength(Coordinate c1, Coordinate c2) {
        return findShortestPath(c1, c2).size();
    }

    /**
     * This method initializes the game.
     *
     * @param cityName the name of the city
     */
    public void initGame(String cityName) {
        setGameData(new GameData(cityName, STARTER_BUDGET, STARTER_TAXES, STARTER_PEOPLE, STARTER_MAP_SIZE));
        Arrays.stream(gameData.getFields())
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField)f)
                .forEach(f -> {
                    if(STARTER_FOREST_PERCENTAGE >= Math.random()) {
                        Forest forest = new Forest(f.getCoord());
                        forest.setGrowStage(FOREST_GROWTH_TIME);
                        f.setBuilding(forest);
                        f.setTile(Tile.FOREST);
                    }
                });
        Logger.log("Game initialized.");
        startSimulation();
    }

    /**
     * This method collects the taxes and pays the pensions.
     */
    public void doFinancials() {
        for (Person p : gameData.getPeople()) {
            if (p.isRetired()) {
                gameData.subtractFromBudget(PENSION);
            } else {
                gameData.addToBudget(gameData.getYearlyTaxes());
            }
        }

        for (PlayableField field : gameData.getPlayableFieldsWithBuildings()) {
            gameData.subtractFromBudget(field.getBuilding().getMaintenanceCost());
        }
    }

    /**
     * This method is called when the user clicks on the "Start" button.
     * It starts the simulation.
     */
    public void startSimulation() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (gameData.isGameOver()) {
                    stopSimulation();
                }
                gameData.increaseDays();
                gameData.setInGameCurrentDate(Date.nextDay(gameData.getInGameCurrentDate()));

                //daily functions:
                burnBuildings();
                removeWhoDeceasedOrMovedAway();
                houseHomeless();
                employUnemployed();
                effects();
                gameData.calculateAverageSatisfaction();
                isGameOver();
                buildingOnFire();
                fireSpread();
                evokeCatastrophe();

                Logger.log("A day is passed: " + gameData.getDays() + ".day");

                //check if a week has passed
                if (gameData.getInGameCurrentDate().matches("\\d{4}-\\d{2}-01")) {
                    //weekly functions:
                    newPeople();
                    repairBuildings();
                }

                // check if a year has passed
                if (gameData.getInGameCurrentDate().matches("\\d{4}-01-01")) {
                    //yearly functions:
                    growForests();
                    peopleAge();
                    peopleDie();
                    peopleMoveAway();
                    removeWhoDeceasedOrMovedAway();
                    doFinancials();

                    Logger.log("A year passed");
                    UI.log("A year passed");
                }
            }
        }, delay, period);
    }

    private void fireSpread(){
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .map(PlayableField::getBuilding)
                .filter(Building::isOnFire)
                .forEach(b -> b.getNeighbours().forEach(n -> {
                    if(FIRE_SPREAD_CHANCE >= Math.random() && !n.isOnFire()) {
                        n.setOnFire();
                    }
                }));
    }

    private void effects() {
        buildingEffects();
        workplaceDistrEffect();
        negativeBudgetEffect();
        unemployedEffect();
        homelessEffect();
    }

    private void unemployedEffect() {
        gameData.getPeople().forEach(p -> {
            if (p.getWorkplace() == null) {
                p.addEffect(Effect.UNEMPLOYED);
            } else {
                p.removeEffect(Effect.UNEMPLOYED);
            }
        });
    }

    private void homelessEffect() {
        gameData.getPeople().forEach(p -> {
            if (p.getHome() == null) {
                p.addEffect(Effect.HOMELESS);
            } else {
                p.removeEffect(Effect.HOMELESS);
            }
        });
    }

    /**
     * This method checks if the game is over.
     */
    private void isGameOver() {
        if (gameData.getPopulation() <= MIN_POPULATION || gameData.getAverageSatisfaction() <= MIN_SATISFACTION) {
            gameData.setGameOver(true);
            timer.cancel();
            JOptionPane.showMessageDialog(null, "Game Over! You lost!");
        }
    }

    private void repairBuildings() {
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(b -> !b.getBuilding().isOnFire() && b.getBuilding().getHp() != BUILDING_MAX_HP)
                .forEach(b -> b.getBuilding().repair());
    }

    private void burnBuildings() {
        gameData.getPeople().forEach(p -> p.removeEffect(Effect.ON_FIRE));
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof GeneratedBuilding && f.getBuilding().isOnFire())
                .flatMap(f -> ((GeneratedBuilding) f.getBuilding()).getPeople().stream())
                .forEach(p -> p.addEffect(Effect.ON_FIRE));
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() != null && f.getBuilding().isOnFire())
                .forEach(f -> f.getBuilding().burnTick());
    }

    /**
     * This method applies the negative budget effect on people if the budget is negative.
     */
    private void negativeBudgetEffect() {
        if (gameData.getBudget() < 0) {
            gameData.getPeople().forEach(p -> p.addEffect(Effect.BAD_BUDGET));
        } else {
            gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_BUDGET));
        }
    }

    /**
     * This method gets the distribution of service and industrial workplaces.
     *
     * @return the distribution of service and industrial workplaces.
     */
    private double getWorkplaceDistr() { // industrial / service
        double serviceWs = (double) gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ServiceWorkplace)
                .count();

        double industrialWs = (double) gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof IndustrialWorkplace)
                .count();

        if (serviceWs == 0 && industrialWs == 0) {
            return 0.5;
        }

        return serviceWs / (serviceWs + industrialWs);
    }

    /**
     * This method applies the workplace distribution effect on people.
     */
    private void workplaceDistrEffect() {
        double workplaceDistr = getWorkplaceDistr();

        if (workplaceDistr > 0.3 && workplaceDistr < 0.7) {
            gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_WORKPLACE_DIST));
        } else {
            gameData.getPeople().forEach(p -> p.addEffect(Effect.BAD_WORKPLACE_DIST));
        }
    }

    /**
     * This method brings new people to the city.
     */
    private void newPeople() {
        if (gameData.getAverageSatisfaction() >= 0.5) {
            // logarithmically increasing number of new people
            int newPeople = (int) (Math.log(gameData.getPopulation()) * 10);
            IntStream.range(0, newPeople).forEach(i -> gameData.getPeople().add(new Person()));
        }
    }

    /**
     * Employs unemployed people.
     */
    private void employUnemployed() {
        Stack<Person> unemployed = gameData.getPeople()
                .stream()
                .filter(p -> p.getWorkplace() == null && p.getHome() != null && !p.isRetired())
                .collect(Collectors.toCollection(Stack::new));

        List<Workplace> allWorkplaces = gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ServiceWorkplace || f.getBuilding() instanceof IndustrialWorkplace)
                .map(f -> (Workplace) f.getBuilding())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        List<PlayableField> allZonesWithNoBuildings = Arrays.stream(gameData.getFields())
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> (f.getZone() == Zone.SERVICE_ZONE || f.getZone() == Zone.INDUSTRIAL_ZONE) && f.getBuilding() == null)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        while (unemployed.size() != 0) {
            Person p = unemployed.pop();
            allWorkplaces.removeIf(w -> w.getPeople().size() >= w.getMaxCapacity());
            allZonesWithNoBuildings.removeIf(z -> z.getBuilding() != null);
            if (allWorkplaces.isEmpty()) {
                if (allZonesWithNoBuildings.isEmpty()) {
                    break;
                } else {
                    buildWorkplaceBasedOnDistance(allWorkplaces, allZonesWithNoBuildings, unemployed);
                }
            } else {
                chooseWorkplaceBasedOnDistance(allWorkplaces, p);
            }
        }
    }

    /**
     * This method builds a workplace based on the distance between the workplace and the people's homes.
     *
     * @param workplaces The list of workplaces where the new workplace will be added.
     * @param zones      The list of fields with zones in them from which one is chosen to build the workplace upon.
     * @param people     The list of people who will work in the new workplace.
     */
    public void buildWorkplaceBasedOnDistance(List<Workplace> workplaces, List<PlayableField> zones, Stack<Person> people) {
        // sort by the average of the peoples distance to the workplace
        zones.stream().min((z1, z2) -> {
            int d1 = people.stream().mapToInt(p -> findShortestPath(z1.getCoord(), p.getHome().getCoords()).size()).sum() / people.size();
            int d2 = people.stream().mapToInt(p -> findShortestPath(z2.getCoord(), p.getHome().getCoords()).size()).sum() / people.size();
            return d1 - d2;
        }).ifPresent(z -> workplaces.add((Workplace) z.buildBuilding(null)));
    }

    /**
     * This method chooses a workplace for a person based on the distance between the workplace and the person's home.
     *
     * @param ws the list of workplaces
     * @param p  the person
     */
    private void chooseWorkplaceBasedOnDistance(List<Workplace> ws, Person p) {
        ws.stream().min((w1, w2) -> {
            int d1 = findShortestPath(w1.getCoords(), p.getHome().getCoords()).size();
            int d2 = findShortestPath(w2.getCoords(), p.getHome().getCoords()).size();
            return d1 - d2;
        }).filter(w -> w.getPeople().size() < w.getMaxCapacity()).ifPresent(w -> w.addPerson(p));
    }

    /**
     * This method houses the homeless people.
     */
    private void houseHomeless() {
        Stack<Person> homeless = gameData.getPeople()
                .stream()
                .filter(p -> p.getHome() == null)
                .collect(Collectors.toCollection(Stack::new));

        Stack<ResidentialBuilding> residentialBuildings = gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ResidentialBuilding)
                .map(f -> (ResidentialBuilding) f.getBuilding())
                .collect(Collectors.toCollection(Stack::new));

        Stack<PlayableField> residentialZonesWithNoBuildings = Arrays.stream(gameData.getFields())
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> f.getZone() == Zone.RESIDENTIAL_ZONE && f.getBuilding() == null)
                .collect(Collectors.toCollection(Stack::new));

        while (homeless.size() != 0) {
            if (residentialBuildings.size() == 0) {
                if (residentialZonesWithNoBuildings.size() != 0) {
                    PlayableField pf = residentialZonesWithNoBuildings.pop();
                    ResidentialBuilding newRb = (ResidentialBuilding) pf.buildBuilding(null);
                    residentialBuildings.add(newRb);
                } else {
                    break;
                }
            } else {
                ResidentialBuilding rb = residentialBuildings.pop();
                while (rb.getPeople().size() < rb.getMaxCapacity() && homeless.size() != 0) {
                    Person p = homeless.pop();
                    rb.addPerson(p);
                }
            }
        }
    }

    /**
     * This method calls the setOnFire method of the buildings.
     */
    private void buildingOnFire() {
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .map(f -> (Flammable) f.getBuilding())
                .forEach(b -> {
                    if (Math.random() <= ((Building) b).getFirePossibility()) {
                        b.setOnFire();
                    }
                });
    }

    /**
     * This method calls the effect method of the catastrophes.
     */
    private void evokeCatastrophe() {
        if (Math.random() <= CATASTROPHE_CHANCE) {
            int random = (int) (Math.random() * 3);
            switch (random) {
                case 0 -> {
                    catastrophes.get(0).effect(gameData);
                    UI.showCatastrophyIcon("econ"); //econ
                }
                case 1 -> {
                    catastrophes.get(1).effect(gameData);
                    UI.showCatastrophyIcon("covid"); //covid
                }
                case 2 -> {
                    catastrophes.get(2).effect(gameData);
                    UI.showCatastrophyIcon("fire"); //firestorm
                }
            }
        }
    }

    /**
     * This method calls the effect method of the buildings.
     */
    private void buildingEffects() {
        gameData.getPeople().forEach(p -> {
            p.removeEffect(Effect.STADIUM);
            p.removeEffect(Effect.FOREST);
            p.removeEffect(Effect.POLICE_STATION);
        });
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof FunctionalBuilding)
                .map(f -> (FunctionalBuilding) f.getBuilding())
                .forEach(FunctionalBuilding::effect);
    }

    /**
     * This method removes the people who are deceased or moved away.
     */
    private void removeWhoDeceasedOrMovedAway() {
        gameData.getPeople().removeIf(p -> p.getName().equals("Deceased") || p.getName().equals("Moved away"));
    }

    /**
     * People move away.
     */
    private void peopleMoveAway() {
        int people = gameData.getPeople().size();
        gameData.getPeople().forEach(p -> {
            if (p.calculateMoveAwayChance() >= Math.random()) {
                p.moveAway();
            }
        });

        Logger.log("People moved away: " + (people - gameData.getPeople().size()));
        UI.log("People moved away: " + (people - gameData.getPeople().size()));
    }

    /**
     * People die.
     */
    private void peopleDie() {
        int people = gameData.getPeople().size();
        gameData.getPeople().forEach(p -> {
            if (p.calculateDeceaseChance() >= Math.random()) {
                p.decease();
            }
        });

        Logger.log("People died: " + (people - gameData.getPeople().size()));
        UI.log("People died: " + (people - gameData.getPeople().size()));
    }

    /**
     * People age.
     */
    private void peopleAge() {
        gameData.getPeople().forEach(p -> p.setAge(p.getAge() + 1));
    }

    /**
     * Grows the forests.
     */
    private void growForests() {
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(field -> field.getBuilding() instanceof Forest)
                .map(f -> (Forest) f.getBuilding())
                .forEach(Forest::grow);
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Logger.log("Timer stopped");
        }
    }

    @Override
    public void setTimePaused() {
        simulationSpeed = SimulationSpeed.PAUSED;
        stopSimulation();
        Logger.log("SimulationSpeed is PAUSED");
    }

    @Override
    public void setTimeNormal() {
        simulationSpeed = SimulationSpeed.NORMAL;
        stopSimulation();
        setDelay(1000);
        setPeriod(1000);
        startSimulation();
        Logger.log("SimulationSpeed is NORMAL");
    }

    @Override
    public void setTimeFast() {
        simulationSpeed = SimulationSpeed.FAST;
        stopSimulation();
        setDelay(500);
        setPeriod(500);
        startSimulation();
        Logger.log("SimulationSpeed is FAST");
    }

    @Override
    public void setTimeFaster() {
        simulationSpeed = SimulationSpeed.FASTER;
        stopSimulation();
        setDelay(50);
        setPeriod(50);
        startSimulation();
        Logger.log("SimulationSpeed is FASTER");
    }

    public void evokeFinancialCrisis() {
        Logger.log("Financial crisis evoked.");
        UI.log("Financial crisis evoked.");
        catastrophes.get(0).effect(gameData);
    }

    public void evokeCovid() {
        Logger.log("Covid evoked.");
        UI.log("Covid evoked.");
        catastrophes.get(1).effect(gameData);
    }

    public void evokeFirestorm() {
        Logger.log("Firestorm evoked.");
        UI.log("Firestorm evoked.");
        catastrophes.get(2).effect(gameData);
    }

    @Override
    public List<File> readSaveFiles() {
        Logger.log("Reading save files...");
        saveFiles = new ArrayList<>();
        File directory = new File(saveDirectory);
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.getName().endsWith(".json")) {
                    saveFiles.add(file);
                }
            }
        }
        return saveFiles;
    }

    @Override
    public void deleteSaveFile(File file) {
        Logger.log("Deleting save file: " + file.getName());
        if (file.delete()) {
            if (file.delete()) {
                Logger.log("Save file deleted.");
            } else {
                Logger.log("Save file could not be deleted.");
            }
        }
    }

    @Override
    public void loadSaveFile(File file) {
        Logger.log("Loading save file: " + file.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MutableGraph.class, new GraphDeserializer());
        objectMapper.registerModule(module);
        try {
            setGameData(objectMapper.readValue(file, GameData.class));
            List<ResidentialBuilding> rbs = gameData.getPlayableFieldsWithBuildings().stream().filter(f -> f.getBuilding() instanceof ResidentialBuilding).map(f -> (ResidentialBuilding) f.getBuilding()).toList();
            List<Workplace> wps = gameData.getPlayableFieldsWithBuildings().stream().filter(f -> f.getBuilding() instanceof Workplace).map(f -> (Workplace) f.getBuilding()).toList();
            rbs.forEach(rb -> rb.getPeople().forEach(p -> p.setHome(rb)));
            wps.forEach(wp -> wp.getPeople().forEach(p -> p.setWorkplace(wp)));

            Logger.log("Save file loaded.");
        } catch (Exception exc) {
            Logger.log("Save file could not be loaded.");
            exc.printStackTrace();
        }
    }

    @Override
    public void saveGame(GameData gameData) {
        boolean UIExists = UI != null;
        if (gameData.getSaveFile() == null) {
            Logger.log("Game has not been saved yet, creating new save file...");
            if (UIExists)
                UI.log("Game has not been saved yet, creating new save file...");
            File file = new File(saveDirectory + File.separator + gameData.getId() + ".json");
            try {
                if (file.createNewFile()) {
                    Logger.log("Save file created.");
                    if (UIExists)
                        UI.log("Save file created.");
                } else {
                    Logger.log("Save file already exists.");
                    if (UIExists)
                        UI.log("Save file already exists.");
                }
                gameData.setSaveFile(file);
                Logger.log("Save file created.");
                if (UIExists)
                    UI.log("Save file created.");
            } catch (Exception exc) {
                Logger.log("Save file could not be created.");
                if (UIExists)
                    UI.log("Save file could not be created.");
                exc.printStackTrace();
            }
        }
        Logger.log("Saving game...");
        if (UIExists)
            UI.log("Saving game...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(gameData.getSaveFile(), gameData);
            Logger.log("Game saved.");
            if (UIExists)
                UI.log("Game saved.");
        } catch (Exception exc) {
            Logger.log("Game could not be saved.");
            if (UIExists)
                UI.log("Game could not be saved.");
            exc.printStackTrace();
        }
    }

    public static GameData getGameData() {
        return gameData;
    }
    public void setGameData(GameData gameData) {
        GameManager.gameData = gameData;
    }
    public static double getHospitalChance() {
        return HOSPITAL_CHANCE;
    }
    public static int getBudget() {
        return gameData.getBudget();
    }
    public static int getStadiumBuildCost() {
        return STADIUM_BUILD_COST;
    }
    public static int getPoliceBuildCost() {
        return POLICE_BUILD_COST;
    }
    public static int getFireStationBuildCost() {
        return FIRE_STATION_BUILD_COST;
    }
    public static int getForestBuildCost() {
        return FOREST_BUILD_COST;
    }
    public static int getRoadBuildCost() {
        return ROAD_BUILD_COST;
    }
    public static int getStadiumMaintenanceCost() {
        return STADIUM_MAINTENANCE_COST;
    }
    public static int getPoliceMaintenanceCost() {
        return POLICE_MAINTENANCE_COST;
    }
    public static int getFireStationMaintenanceCost() {
        return FIRE_STATION_MAINTENANCE_COST;
    }
    public static int getForestMaintenanceCost() {
        return FOREST_MAINTENANCE_COST;
    }
    public static int getRoadMaintenanceCost() {
        return ROAD_MAINTENANCE_COST;
    }
    public static int getStadiumRange() {
        return STADIUM_RANGE;
    }
    public static int getPoliceRange() {
        return POLICE_RANGE;
    }
    public static int getFireStationRange() {
        return FIRE_STATION_RANGE;
    }
    public static int getForestRange() {
        return FOREST_RANGE;
    }
    public static int getIndustrialRange() {
        return INDUSTRIAL_RANGE;
    }
    public static int getForestGrowthTime() {
        return FOREST_GROWTH_TIME;
    }
    public static int getMarkResidentialCost() {
        return MARK_RESIDENTIAL_COST;
    }
    public static int getMarkServiceCost() {
        return MARK_SERVICE_COST;
    }
    public static int getMarkIndustrialCost() {
        return MARK_INDUSTRIAL_COST;
    }
    public static int getLevelOneMaxCapacity() {
        return LEVEL_ONE_MAX_CAPACITY;
    }
    public static int getLevelTwoMaxCapacity() {
        return LEVEL_TWO_MAX_CAPACITY;
    }
    public static int getLevelThreeMaxCapacity() {
        return LEVEL_THREE_MAX_CAPACITY;
    }
    public static int getLevelTwoUpgradeCost() {
        return LEVEL_TWO_UPGRADE_COST;
    }
    public static int getLevelThreeUpgradeCost() {
        return LEVEL_THREE_UPGRADE_COST;
    }
    public static int getStarterMapSize() {
        return STARTER_MAP_SIZE;
    }
    public static int getPension() {
        return PENSION;
    }
    public static void addToBudget(int amount) {
        gameData.addToBudget(amount);
    }
    public static void subtractFromBudget(int amount) {
        gameData.subtractFromBudget(amount);
    }
    public static Field[][] getFields() {
        return gameData.getFields();
    }
    public static double getFirePossibility() {
        return FIRE_POSSIBILITY;
    }
    public static int getMaxFiretrucks() {
        return MAX_FIRETRUCKS;
    }
    public static int getBuildingMaxHP() {
        return BUILDING_MAX_HP;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    public void setPeriod(int period) {
        this.period = period;
    }
    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }
    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }
}