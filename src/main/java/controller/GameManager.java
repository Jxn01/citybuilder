package controller;

import java.util.*;

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
import util.GraphDeserializer;
import util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private static GameData gameData;
    private double catastropheChance;
    private double hospitalChance;
    private SimulationSpeed simulationSpeed;

    private final List<Catastrophe> catastrophes;

    private List<File> saveFiles;
    private final String saveDirectory = System.getProperty("user.home") + File.separator + ".citybuilder" + File.separator + "saves";


    public GameManager() {
        catastropheChance = 0.1;
        hospitalChance = 0.1;
        simulationSpeed = SimulationSpeed.PAUSED;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        Logger.log("Game manager created.");

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void initGame(String cityName) {
        setGameData(new GameData(cityName));
        Logger.log("Game initialized.");
        simulate();
    }

    public static GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        GameManager.gameData = gameData;
        Logger.log("Game data set to " + gameData.getId());
    }

    public static MutableGraph<Coordinate> getGraph() {
        return gameData.getGraph();
    }

    public void doFinancials() {
        for (Person p : gameData.getPeople()) {
            if (p.isRetired()) {
                gameData.subtractFromBudget(100);
            } else {
                gameData.addToBudget(100);
            }
        }
    }

    private void simulate() {
        Timer timer = new Timer();
        int delay = 1000; // delay for 1 second
        int period = 1000; // repeat every 1 second

        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            public void run() {
                Logger.log("Timer: " + ++count);
                Logger.log("A day is passed: " + count + ".day");




                if (count == 100) {
                    timer.cancel();
                }
            }
        }, delay, period);
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
     * This method calculates the shortest path between two coordinates.
     *
     * @param start The start coordinate.
     * @param end   The end coordinate.
     * @return The shortest path between the two coordinates.
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
            return null; // No path exists between start and end
        }
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }

    public double getCatastropheChance() {
        return catastropheChance;
    }

    public void setCatastropheChance(double catastropheChance) {
        this.catastropheChance = catastropheChance;
        Logger.log("Catastrophe chance set to " + catastropheChance);
    }

    public void evokeFinancialCrisis() {
        Logger.log("Financial crisis evoked.");
        catastrophes.get(0).effect(gameData);
    }

    public void evokeCovid() {
        Logger.log("Covid evoked.");
        catastrophes.get(1).effect(gameData);
    }

    public void evokeFirestorm() {
        Logger.log("Firestorm evoked.");
        catastrophes.get(2).effect(gameData);
    }

    @Override
    public List<File> readSaveFiles() {
        Logger.log("Reading save files...");
        saveFiles = new ArrayList<>();
        File directory = new File(saveDirectory);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
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
            Logger.log("Save file deleted.");
        } else {
            Logger.log("Save file could not be deleted.");
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
            Logger.log("Save file loaded.");
        } catch (Exception exc) {
            Logger.log("Save file could not be loaded.");
            exc.printStackTrace();
        }
    }

    @Override
    public void saveGame(GameData gameData) {
        if (gameData.getSaveFile() == null) {
            Logger.log("Game has not been saved yet, creating new save file...");
            File file = new File(saveDirectory + File.separator + gameData.getId() + ".json");
            try {
                if (file.createNewFile()) {
                    Logger.log("Save file created.");
                } else {
                    Logger.log("Save file already exists.");
                }
                gameData.setSaveFile(file);
                Logger.log("Save file created.");
            } catch (Exception exc) {
                Logger.log("Save file could not be created.");
                exc.printStackTrace();
            }
        }
        Logger.log("Saving game...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(gameData.getSaveFile(), gameData);
            Logger.log("Game saved.");
        } catch (Exception exc) {
            Logger.log("Game could not be saved.");
            exc.printStackTrace();
        }
    }

    public void setTaxes(int taxes) {
        gameData.setYearlyTaxes(taxes);
        Logger.log("Taxes set to " + taxes);
    }

    public double getWorkplaceDistribution() {
        return 0.0;
    }

    public double getHospitalChance() {
        return hospitalChance;
    }

    public void setHospitalChance(double hospitalChance) {
        this.hospitalChance = hospitalChance;
        Logger.log("Hospital chance set to " + hospitalChance);
    }

    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
        Logger.log("Simulation speed set to " + simulationSpeed);
    }

    public List<File> getSaveFiles() {
        return saveFiles;
    }

    @Override
    public void timeStop  () {
        simulationSpeed = SimulationSpeed.PAUSED;
        Logger.log("Time stopped");
    }

    @Override
    public void setTimeNormal() {
        simulationSpeed = SimulationSpeed.NORMAL;
        Logger.log("Time flows normally");
    }

    @Override
    public void setTimeFast() {
        simulationSpeed = SimulationSpeed.FAST;
        Logger.log("Time flows fast");
    }

    @Override
    public void setTimeFaster() {
        simulationSpeed = SimulationSpeed.FASTER;
        Logger.log("Time flows faster");
    }
}
