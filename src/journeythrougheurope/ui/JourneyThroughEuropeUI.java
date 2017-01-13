/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class JourneyThroughEuropeUI {

    JourneyEventHandler eventHandler;

    //splashScreen
    private StackPane splashScreenPane;
    private ImageView splashScreenImageView;
    private Label splashScreenImageLabel;
    private VBox splashScreenButtons;

    //SplashScreenButtons
    private Button startButton;
    private Button loadButton;
    private Button aboutButton;
    private Button quitButton;

    //AboutScreen
    private BorderPane aboutJTEScreen;

    private ScrollPane pane;
    private AnchorPane gamePlayPane;
    //gamePlayScreen
    private Map mapView;
    private HBox gamePlayScreen;
    private VBox rightGamePaneButtons;
    private VBox leftGamePane;
    private Button flightPlanButton;
    private Button gameHistoryButton;
    private Button aboutJTEButton;
    private Button saveGameButton;
    private Label currentPlayerLabel;
    private ImageView pieceImage;
    private Button diceRollButton;
    private Label diceLabel;
    private int initRollValue;

    //gameSetupScreen
    private Label numOfPlayerLabel;
    private BorderPane gameSetupContainer;
    private GridPane gameSetupScreen;
    private Button goButton;
    private ChoiceBox numPlayerChoiceBox;

    //historyScreen
    private BorderPane historyScreen;
    private int aboutScreenVal = 3;

    private BorderPane mainPane;
    private Stage primaryStage;

    private Insets marginlessInsets;

    private Player[] playersArray;
    private String[] playerNames = new String[7];
    boolean[] isComputerArray = new boolean[7];
    private int numOfPlayers;
    int diceRollValue;
    boolean diceRolled = false;
    // mainPane weight && height
    private int paneWidth;
    private int paneHeigth;

    public JourneyThroughEuropeUI() {
        eventHandler = new JourneyEventHandler(this);
        initMainPane();
        initSplashScreen();
        initGamePlayScreen();
        initGameSetupScreen();
        initAboutScreen();
        initHistoryScreen();

    }

    public void initMainPane() {
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();

        paneWidth = 1500;
        paneHeigth = 1500;
        mainPane.setPadding(marginlessInsets);
        mainPane.setPrefWidth(paneWidth);
        mainPane.setPrefHeight(paneHeigth);
    }

    public void initSplashScreen() {

        splashScreenPane = new StackPane();

        Image splashScreenImage = new Image("file:Artwork/Game.JPG");
        splashScreenImageView = new ImageView(splashScreenImage);
        splashScreenImageView.setOpacity(0.90);

        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);
        // move the label position to fix the pane
        splashScreenImageLabel.setLayoutX(-45);
        splashScreenPane.getChildren().add(splashScreenImageLabel);

        startButton = new Button();
        startButton.setGraphic(new ImageView(new Image("file:Artwork/startButton.PNG")));
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToNewGameRequest(primaryStage);
            }
        });

        loadButton = new Button();
        ImageView loadButtonView = new ImageView(new Image("file:Artwork/loadButton.PNG"));
        loadButton.setGraphic(loadButtonView);
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String data = "";

                    FileInputStream fstream = new FileInputStream("SaveGameState.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                    String strLine;
                    ArrayList<String> file = new ArrayList();

                    while ((strLine = br.readLine()) != null) {
                        data = data.concat(strLine);
                        file.add(strLine);
                    }
                    int numSavePlayers = Integer.parseInt(file.get(1));
                    int loadPoints = Integer.parseInt(file.get(2));
                    int loadCurrPlayer = Integer.parseInt(file.get(3));
                    String gameHistory = file.get(4);
                    String[] gameHis = gameHistory.split(",");
                    ArrayList<String> replaceHis = new ArrayList();
                    for(String i : gameHis){
                        replaceHis.add(i);
                    }
                    mapView.setGameHistory(replaceHis);
                    historyLabelMake();
                    numOfPlayers = numSavePlayers;
                    Player[] players = new Player[numSavePlayers];
                    for(int i = 0; i < players.length; i++){
                            players[i] = new Player("blank", false);
                        }
                    ArrayList<String[]> tempDest = new ArrayList();
                    for(int i = 5; i < file.size(); i++){
                        String temp = file.get(i);
                        String[] tempArr = temp.split(",");
                        tempDest.add(tempArr);
                    }
                    for(int i = 0; i < numSavePlayers; i++){
                        players[i].setDestinations(tempDest.get(i));
                        players[i].getHistory().add(tempDest.get(i)[0]);
                    }
                    playersArray = players;
                    for (int i = 0; i < players[0].getDestinations().length; i++) {
                        getLeftGamePane().getChildren().add(new ImageView("file:Artwork/tiles/" + players[0].getDestinations()[i] + ".jpg"));
                    }
                    loadGameData(data);
                    mapView.setCurrentPlayer(loadCurrPlayer - 1);
                    mapView.newRound();
                    if(loadPoints == 0){
                        diceRollButton.setDisable(false);
                    } else {
                        diceRollButton.setDisable(true);
                        diceRollValue = loadPoints;
                        diceRolled = true;
                        diceLabel.setText("     Points: " + diceRollValue);
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        aboutButton = new Button();
        aboutButton.setGraphic(new ImageView(new Image("file:Artwork/aboutButton.PNG")));
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToAboutJTERequest(primaryStage);
                aboutScreenVal = 2;
            }
        });

        quitButton = new Button();
        quitButton.setGraphic(new ImageView(new Image("file:Artwork/quitButton.PNG")));
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToExitRequest(primaryStage);
            }
        });

        splashScreenButtons = new VBox();
        splashScreenButtons.getChildren().add(startButton);
        splashScreenButtons.getChildren().add(loadButton);
        splashScreenButtons.getChildren().add(aboutButton);
        splashScreenButtons.getChildren().add(quitButton);

        splashScreenPane.getChildren().add(splashScreenButtons);
        splashScreenButtons.setAlignment(Pos.CENTER);
        mainPane.setCenter(splashScreenPane);
    }

    public void loadGameData(String data) {
        String[] temp = data.split(",");
        for (String i : temp) {
          //  System.out.println(i);
        }
        double xCoordTemp[] = new double[6];
        double yCoordTemp[] = new double[6];
        int regionTemp[] = new int[6];
        int anchorRegion[] = new int[6];
        double flagAnchorX[] = new double[6];
        double flagAnchorY[] = new double[6];
        int initialRegion[] = new int[6];
        ArrayList<String> dest = new ArrayList();
        for (int i = 0; i < temp.length; i++) {
            if (i < 6) {
                xCoordTemp[i] = Double.parseDouble(temp[i]);
            }
            if (i >= 6 && i < 12) {
                yCoordTemp[i % 6] = Double.parseDouble(temp[i]);
            }
            if (i >= 12 && i < 18) {
                regionTemp[i % 6] = Integer.parseInt(temp[i]);
            }
            if (i >= 18 && i < 24) {
                anchorRegion[i % 6] = Integer.parseInt(temp[i]);
            }
            if (i >= 24 && i < 30) {
                flagAnchorX[i % 6] = Double.parseDouble(temp[i]);
            }
            if (i >= 30 && i < 36) {
                flagAnchorY[i % 6] = Double.parseDouble(temp[i]);
               // System.out.println(flagAnchorY[i % 6]);
            }
        }
        mapView.setxCoordStarting(xCoordTemp);
        mapView.setyCoordStarting(yCoordTemp);
        mapView.setRegionCheck(regionTemp);
        mapView.setFlagAnchorX(flagAnchorX);
        mapView.setFlagAnchorY(flagAnchorY);
        mapView.setInitialRegion(initialRegion);
        mapView.setAnchorRegion(anchorRegion);
        mapView.setPlayCards(false);
        mapView.repaint();

        eventHandler.respondToGamePlayScreenRequest(primaryStage);
    }

    public void initGamePlayScreen() {
        leftGamePane = new VBox();
        gamePlayScreen = new HBox();
        VBox rightGamePane = new VBox();

        currentPlayerLabel = new Label("\t        Player 1");
        getCurrentPlayerLabel().setFont(Font.font("Verdana", 16));
        leftGamePane.getChildren().add(getCurrentPlayerLabel());
        getLeftGamePane().getChildren().add(new Label("\t\t\t\t\t\t\t\t\t\t\t"));

        flightPlanButton = new Button();
        flightPlanButton.setGraphic(new ImageView(new Image("file:Artwork/flightPlanButton.PNG")));
        flightPlanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapView.respondToFlightPlanRequest();
            }
        });
        flightPlanButton.setDisable(true);

        saveGameButton = new Button();
        saveGameButton.setGraphic(new ImageView(new Image("file:Artwork/saveGameButton.PNG")));
        saveGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToSaveGameRequest(primaryStage);
                BufferedWriter writer = null;
                try {
                    //create a temporary file
                    String timeLog = "SaveGameState";
                    File logFile = new File(timeLog);

                    // This will output the full path where the file will be written to...
                    System.out.println(logFile.getCanonicalPath());

                    writer = new BufferedWriter(new FileWriter(logFile + ".txt"));
                    for (int i = 0; i < mapView.getxCoordStarting().length; i++) {
                        writer.write(mapView.getxCoordStarting()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getyCoordStarting().length; i++) {
                        writer.write(mapView.getyCoordStarting()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getRegionCheck().length; i++) {
                        writer.write(mapView.getRegionCheck()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getAnchorRegion().length; i++) {
                        writer.write(mapView.getAnchorRegion()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getFlagAnchorX().length; i++) {
                        writer.write(mapView.getFlagAnchorX()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getFlagAnchorY().length; i++) {
                        writer.write(mapView.getFlagAnchorY()[i] + ",");
                    }
                    for (int i = 0; i < mapView.getInitialRegion().length; i++) {
                        writer.write(mapView.getFlagAnchorX()[i] + ",");
                    }
                    writer.newLine();
                    String num = "" + numOfPlayers;
                    writer.write(num);
                    writer.newLine();
                    String point = "" + diceRollValue;
                    writer.write(point);
                    writer.newLine();
                    String currPlay = "" + mapView.getCurrentPlayer();
                    writer.write(currPlay);
                    String history = "";
                    for(String i : mapView.getGameHistory()){
                        history = history.concat(i);
                        history = history.concat(",");
                    }
                    writer.newLine();
                    writer.write(history);
                    for(Player i : playersArray){
                        writer.newLine();
                        for(String j : i.getDestinations()){
                            writer.write(j + ",");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        );

        aboutJTEButton = new Button();
        aboutJTEButton.setGraphic(new ImageView(new Image("file:Artwork/aboutJTEButton.PNG")));
        aboutJTEButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToAboutJTERequest(primaryStage);
                aboutScreenVal = 1;
            }
        });

        gameHistoryButton = new Button();
        gameHistoryButton.setGraphic(new ImageView(new Image("file:Artwork/gameHistoryButton.PNG")));
        gameHistoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToHistoryScreenRequest(primaryStage);
                historyLabelMake();
            }
        });

        GridPane selector = new GridPane();

        Button topLeft = new Button();
        topLeft.setGraphic(new ImageView(new Image("file:Artwork/topLeft.PNG")));
        topLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapView.switchRegion(1);
            }
        });

        Button topRight = new Button();
        topRight.setGraphic(new ImageView(new Image("file:Artwork/topRight.PNG")));
        topRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapView.switchRegion(3);
            }
        });

        Button bottomLeft = new Button();
        bottomLeft.setGraphic(new ImageView(new Image("file:Artwork/bottomLeft.PNG")));
        bottomLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapView.switchRegion(2);
            }
        });

        Button bottomRight = new Button();
        bottomRight.setGraphic(new ImageView(new Image("file:Artwork/bottomRight.PNG")));
        bottomRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mapView.switchRegion(4);
            }
        });

        ImageView oneFour = new ImageView(new Image("file:Artwork/1-4.PNG"));
        ImageView fiveEight = new ImageView(new Image("file:Artwork/5-8.PNG"));
        ImageView AC = new ImageView(new Image("file:Artwork/A-C.PNG"));
        ImageView DF = new ImageView(new Image("file:Artwork/D-F.PNG"));

        selector.add(new Label(""), 1, 1);
        selector.add(oneFour, 1, 2);
        selector.add(fiveEight, 1, 3);
        selector.add(AC, 2, 1);
        selector.add(topLeft, 2, 2);
        selector.add(topRight, 2, 3);
        selector.add(DF, 3, 1);
        selector.add(bottomLeft, 3, 2);
        selector.add(bottomRight, 3, 3);
        pieceImage = new ImageView(new Image("file:Artwork/piece_black2.png"));

        rightGamePaneButtons = new VBox();
        rightGamePaneButtons.getChildren().add(flightPlanButton);
        rightGamePaneButtons.getChildren().add(saveGameButton);
        rightGamePaneButtons.getChildren().add(aboutJTEButton);
        rightGamePaneButtons.getChildren().add(gameHistoryButton);

        VBox diceContainer = new VBox();
        diceLabel = new Label("     Points: ");
        diceLabel.setFont(Font.font("Verdana", 20));
        diceRollButton = new Button();
        int randomDice = (int) (Math.random() * 6) + 1;
        diceRollButton.setGraphic(new ImageView(new Image("file:Artwork/die_" + randomDice + ".jpg")));
        //ImageView diceView = new ImageView(new Image("file:Artwork/die_" + randomDice + ".jpg"));
        diceContainer.setAlignment(Pos.CENTER);
        diceRollButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diceRollButton.setDisable(true);

                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.ZERO,
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        diceRolled = true;
                                        int randomDicePress = (int) (Math.random() * 6) + 1;
                                        diceRollButton.setGraphic(new ImageView(new Image("file:Artwork/die_" + randomDicePress + ".jpg")));
                                        diceRollValue = randomDicePress;
                                        initRollValue = diceRollValue;
                                        diceLabel.setText("     Points: " + diceRollValue);
                                    }
                                }
                        ),
                        new KeyFrame(
                                Duration.millis(500)
                        )
                );
                timeline.setCycleCount(6);
                timeline.play();
            }
        });
        diceContainer.getChildren().add(diceRollButton);

        rightGamePane.getChildren().add(diceLabel);
        rightGamePane.getChildren().add(diceContainer);
        rightGamePane.getChildren().add(selector);
        rightGamePane.getChildren().add(rightGamePaneButtons);
        rightGamePane.getChildren().add(getPieceImage());
        pane = new ScrollPane();
        pane.setMaxWidth(200);
        pane.setMaxHeight(200);
        rightGamePane.getChildren().add(pane);
        
        
        mapView = new Map(this);
        mapView.repaint();
        gamePlayScreen.getChildren().add(getLeftGamePane());
        gamePlayScreen.getChildren().add(mapView);
        gamePlayScreen.getChildren().add(rightGamePane);
        gamePlayPane = new AnchorPane();
        gamePlayPane.getChildren().add(gamePlayScreen);
    }

    public BorderPane GetMainPane() {
        return this.mainPane;
    }

    public void SetStage(Stage stage) {
        primaryStage = stage;
    }
   

    private void initGameSetupScreen() {
        gameSetupContainer = new BorderPane();
        gameSetupScreen = new GridPane();

        VBox componentMaxHolder = new VBox();
        HBox component1Holder = new HBox();
        HBox component2Holder = new HBox();
        HBox[] fullComponentArray = new HBox[6];
        fullComponentArray[0] = fullComponent("file:Artwork/flag1.PNG", 1);
        fullComponentArray[1] = fullComponent("file:Artwork/flag2.PNG", 2);
        fullComponentArray[2] = fullComponent("file:Artwork/flag3.PNG", 3);
        fullComponentArray[3] = fullComponent("file:Artwork/flag4.PNG", 4);
        fullComponentArray[4] = fullComponent("file:Artwork/flag5.PNG", 5);
        fullComponentArray[5] = fullComponent("file:Artwork/flag6.PNG", 6);
        component1Holder.getChildren().add(fullComponentArray[0]);
        component1Holder.getChildren().add(fullComponentArray[1]);
        component1Holder.getChildren().add(fullComponentArray[2]);
        component2Holder.getChildren().add(fullComponentArray[3]);
        component2Holder.getChildren().add(fullComponentArray[4]);
        component2Holder.getChildren().add(fullComponentArray[5]);
        fullComponentArray[0].setVisible(false);
        fullComponentArray[1].setVisible(false);
        fullComponentArray[2].setVisible(false);
        fullComponentArray[3].setVisible(false);
        fullComponentArray[4].setVisible(false);
        fullComponentArray[5].setVisible(false);
        Label blankSpace = new Label("                                        ");
        blankSpace.setFont(Font.font("Verdana", 12));
        Label blankSpace2 = new Label("                              ");
        blankSpace2.setWrapText(true);
        blankSpace2.setFont(Font.font("Verdana", 100));

        componentMaxHolder.getChildren().add(blankSpace);
        componentMaxHolder.getChildren().add(component1Holder);
        componentMaxHolder.getChildren().add(blankSpace2);
        componentMaxHolder.getChildren().add(component2Holder);
        component2Holder.setAlignment(Pos.CENTER);
        gameSetupScreen.getChildren().add(componentMaxHolder);

        //Insets sets = new Insets(5,5,3,2);
        goButton = new Button("GO!");
        goButton.setDisable(true);
        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToGamePlayScreenRequest(primaryStage);
                playersArray = new Player[numOfPlayers];

                for (int i = 0; i < numOfPlayers; i++) {
                    playersArray[i] = new Player(getPlayerNames()[i + 1], isComputerArray[i + 1]);
                }

                int increment = 0;
                int colorCode = 0;
                for (Player i : playersArray) {
                    for (int j = 0; j < 3; j++) {
                        i.getColorValues().add((Integer) colorCode % 3);
                        colorCode++;
                    }
                    colorCode = 0;
                    increment += 1;
                    colorCode = +increment;
                }

                for (Player i : playersArray) {
                   // System.out.print(i.getColorValues());
                }

                int sppointer = 0;
                for (Player i : playersArray) {
                    i.initializeColors();
                    i.intializeCards();
                    i.print();;
                    if (sppointer < numOfPlayers) {
                        mapView.startPoints(sppointer);
                        sppointer++;
                    }
                    i.getHistory().add(i.getDestinations()[0]);
                }

                mapView.displayCards2(playersArray[0], 0);
                mapView.setCurrentCity(playersArray[0].getDestinations()[0]);
                
                if(mapView.getCity().getSeaPorts().contains(playersArray[0].getDestinations()[0])){
                    eventHandler.respondToSeaPortRequest(primaryStage);
                }
                int regionIndex = mapView.getCity().getNameArr().indexOf(playersArray[0].getDestinations()[0]);
                int region = mapView.getCity().getQuarter()[regionIndex];
                mapView.switchRegion(region);

                //mapView.print();
                mapView.repaint();
            }

        });

        HBox selector = new HBox();
        numPlayerChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6));
        numPlayerChoiceBox.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number t, Number t1) {
                numOfPlayers = (int) t1;
                for (int i = 0; i < (int) t1; i++) {
                    fullComponentArray[i].setVisible(true);
                }
                for (int i = (int) t1; i < 6; i++) {
                    fullComponentArray[i].setVisible(false);
                }

                if ((int) t1 > 1) {
                    goButton.setDisable(false);
                } else if ((int) t1 == 0 || (int) t1 == 1) {
                    goButton.setDisable(true);
                }
            }
        });

        Label spaceLabel = new Label("  ");
        numOfPlayerLabel = new Label("Number of Players ");
        numOfPlayerLabel.setFont(Font.font("Verdana", 16));
        selector.getChildren().add(numOfPlayerLabel);
        selector.getChildren().add(numPlayerChoiceBox);
        selector.getChildren().add(spaceLabel);
        selector.getChildren().add(goButton);

        getGameSetupContainer().setTop(selector);
        getGameSetupContainer().setCenter(gameSetupScreen);

    }
    
    public void initAboutScreen() {
        aboutJTEScreen = new BorderPane();
        Label aboutTitle = new Label("               About Journey Through Europe");
        aboutTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        Label about = new Label("Journey through Europe is a family board game published by Ravensburger. The board is a map of Europe with various major cities marked, for example, Athens, Amsterdam and London. The players are given a home city from which they will begin and are then dealt a number of cards with various other cities on them. They must plan a route between each of the cities in their hand of cards. On each turn they throw a die and move between the cities. The winner is the first player to visit each of their cities and then return to their home base.");
        about.setFont(Font.font("Verdana", 28));
        about.setWrapText(true);
        Button closeAboutButton = new Button();
        closeAboutButton.setGraphic(new ImageView(new Image("file:Artwork/closeButton.PNG")));
        closeAboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(aboutScreenVal == 1){
                eventHandler.respondToGamePlayScreenRequest(primaryStage);
                aboutScreenVal = 3;
                }
                else if(aboutScreenVal == 2){
                eventHandler.respondToSplashScreenRequest(primaryStage);
                aboutScreenVal = 3;
                }
            }
        });
        aboutJTEScreen.setTop(aboutTitle);
        aboutJTEScreen.setCenter(about);
        aboutJTEScreen.setBottom(closeAboutButton);
    }
    
    public void historyLabelMake(){
        String history = "";
        for(String i : mapView.getGameHistory()){
            history = history.concat(i) + "\n";
         }
        Label label = new Label();
        label.setText(history);
        ScrollPane pane = new ScrollPane();
        pane.setContent(label);
        historyScreen.setCenter(pane);
    }

    public void initHistoryScreen() {
        historyScreen = new BorderPane();
        HBox topNode = new HBox();
        Label historyTitle = new Label("                               Game History");
        historyTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        
        Button closeHistoryButton = new Button();
        closeHistoryButton.setGraphic(new ImageView(new Image("file:Artwork/closeButton.PNG")));
        closeHistoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToGamePlayScreenRequest(primaryStage);
            }
        });
        topNode.getChildren().add(historyTitle);
        topNode.getChildren().add(closeHistoryButton);
        historyScreen.setTop(topNode);
        historyScreen.setBottom(closeHistoryButton);
    }

    public HBox fullComponent(String filePath, int playerValue) {
        ToggleGroup group = new ToggleGroup();
        HBox component1 = new HBox();
        RadioButton player = new RadioButton("Player");
        player.setSelected(true);
        player.requestFocus();
        player.setToggleGroup(group);
        Label space = new Label("          ");
        Label name = new Label("Name");
        component1.getChildren().add(player);
        component1.getChildren().add(space);
        component1.getChildren().add(name);

        HBox component2 = new HBox();
        RadioButton computer = new RadioButton("Computer");
        computer.setToggleGroup(group);
        Label nameSpace = new Label("   ");
        TextField playerField = new TextField("Player ");
        playerField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerNames[playerValue] = playerField.getText();
            }
        });
        component2.getChildren().add(computer);
        component2.getChildren().add(nameSpace);
        component2.getChildren().add(playerField);

        VBox componentHolder = new VBox();
        componentHolder.getChildren().add(component1);
        componentHolder.getChildren().add(component2);

        HBox fullComponent = new HBox();
        ImageView flag = new ImageView(new Image(filePath));
        fullComponent.getChildren().add(flag);
        fullComponent.getChildren().add(componentHolder);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    String raw = (group.getSelectedToggle().toString());
                    String type = raw.substring(raw.indexOf("'") + 1, raw.lastIndexOf("'"));
                    // System.out.println(type);
                    if (type.equals("Computer")) {
                        isComputerArray[playerValue] = true;
                    }
                    if (type.equals("Player")) {
                        isComputerArray[playerValue] = false;
                    }
                }
            }
        });

        return fullComponent;
    }

    /**
     * @return the gameSetupContainer
     */
    public BorderPane getGameSetupContainer() {
        return gameSetupContainer;
    }

    /**
     * @return the gamePlayScreen
     */
    public HBox getGamePlayScreen() {
        return gamePlayScreen;
    }

    /**
     * @return the aboutJTEScreen
     */
    public BorderPane getAboutJTEScreen() {
        return aboutJTEScreen;
    }

    /**
     * @return the historyScreen
     */
    public BorderPane getHistoryScreen() {
        return historyScreen;
    }

    /**
     * @return the leftGamePane
     */
    public VBox getLeftGamePane() {
        return leftGamePane;
    }

    /**
     * @return the playerNames
     */
    public String[] getPlayerNames() {
        return playerNames;
    }

    /**
     * @return the playersArray
     */
    public Player[] getPlayersArray() {
        return playersArray;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getDiceRollValue() {
        return diceRollValue;
    }

    public void setDiceRollValue(int diceRollValue) {
        this.diceRollValue = diceRollValue;
    }

    public boolean getDiceRolled() {
        return diceRolled;
    }

    public void setDiceRolled(boolean diceRolled) {
        this.diceRolled = diceRolled;
    }

    /**
     * @return the currentPlayerLabel
     */
    public Label getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }

    /**
     * @return the pieceImage
     */
    public ImageView getPieceImage() {
        return pieceImage;
    }

    public Button getDiceRollButton() {
        return diceRollButton;
    }

    public Label getDiceLabel() {
        return diceLabel;
    }

    public AnchorPane getGamePlayPane() {
        return gamePlayPane;
    }

    public StackPane getSplashScreenPane(){
        return splashScreenPane;
    }
    
    public Button getFlightPlanButton() {
        return flightPlanButton;
    }
    
    public Stage getPrimaryStage(){
        return primaryStage;
    }
    
    public JourneyEventHandler getEventHandler(){
        return eventHandler;
    }

    public ScrollPane getScrollPane(){
        return pane;
    }
    
    public int getInitRollValue(){
        return initRollValue;
    }
    
    public Map getMapView(){
        return mapView;
    }
}
