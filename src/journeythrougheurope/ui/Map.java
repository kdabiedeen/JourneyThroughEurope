/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class Map extends Canvas {

    boolean rollAgain = false;
    boolean specialCondition = false;
    GraphicsContext gc;
    Cities city;
    JourneyThroughEuropeUI ui;
    int currentRegion = 1;
    String currentCity = "";
    int[] regionCheck = new int[6];
    double[] xCoordStarting = new double[6];
    double[] yCoordStarting = new double[6];
    private double[] flagAnchorX = new double[6];
    private double[] flagAnchorY = new double[6];
    private int[] initialRegion = new int[6];
    private int[] anchorRegion = new int[6];
    String[] cityLocName = new String[6];
    String atCity = "";
    Image[] cards = new Image[300];
    int currentPiece = -1;
    public static int mouseValue = 1;
    boolean gameContinue = true;
    int[] rollDiceAgain = new int[6];
    int[] missTurn = new int[6];
    int[] addPoints = new int[6];
    int[] addPointsTurn = new int[6];
    int[] addMoves = new int[6];
    int[] addMovesTurn = new int[6];
    ArrayList<String> gameHistory = new ArrayList();
    int[] seaportWait = new int[6];
    int cellWidth;
    int cellHeight;

    Image region1 = new Image("file:Artwork/gameplay_AC14.jpg"); //24% resize of original
    Image region2 = new Image("file:Artwork/gameplay_DF14.jpg");
    Image region3 = new Image("file:Artwork/gameplay_AC58.jpg");
    Image region4 = new Image("file:Artwork/gameplay_DF58.jpg");

    Image blackFlag = new Image("file:Artwork/flag_black.png");
    Image blueFlag = new Image("file:Artwork/flag_blue.png");
    Image greenFlag = new Image("file:Artwork/flag_green.png");
    Image redFlag = new Image("file:Artwork/flag_red.png");
    Image whiteFlag = new Image("file:Artwork/flag_white.png");
    Image yellowFlag = new Image("file:Artwork/flag_yellow.png");
    Image blackPiece = new Image("file:Artwork/piece_black.png");
    Image bluePiece = new Image("file:Artwork/piece_blue.png");
    Image greenPiece = new Image("file:Artwork/piece_green.png");
    Image redPiece = new Image("file:Artwork/piece_red.png");
    Image whitePiece = new Image("file:Artwork/piece_white.png");
    Image yellowPiece = new Image("file:Artwork/piece_yellow.png");
    Image blackPiece2 = new Image("file:Artwork/piece_black2.png");
    Image bluePiece2 = new Image("file:Artwork/piece_blue2.png");
    Image greenPiece2 = new Image("file:Artwork/piece_green2.png");
    Image redPiece2 = new Image("file:Artwork/piece_red2.png");
    Image whitePiece2 = new Image("file:Artwork/piece_white2.png");
    Image yellowPiece2 = new Image("file:Artwork/piece_yellow2.png");

    Image flightPlan = new Image("file:Artwork/FlightPlan.JPG");

    Image[] pieces = {blackPiece, yellowPiece, bluePiece, redPiece, greenPiece, whitePiece};
    Image[] flags = {blackFlag, yellowFlag, blueFlag, redFlag, greenFlag, whiteFlag};
    Image[] imagePieces = {blackPiece2, yellowPiece2, bluePiece2, redPiece2, greenPiece2, whitePiece2};
    int currentPlayer = 0;
    public static boolean playCards = true;
    VBox[] playBox = new VBox[6];
    boolean canTravelSea = false;
    

    public Map(JourneyThroughEuropeUI initUI) {
        ui = initUI;
        gc = this.getGraphicsContext2D();
        currentRegion = 1;
        this.setWidth(500);
        this.setHeight(700);
        city = new Cities();
        EventHandlers();
        repaint();
    }

    //Kevlevyck 946 297     

    public void repaint() {
        //implement switch case here 
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        switch (currentRegion) {
            case 1:
                gc.drawImage(region1, 0, 0);
                break;
            case 2:
                gc.drawImage(region2, 0, 0);
                break;
            case 3:
                gc.drawImage(region3, 0, 0);
                break;
            case 4:
                gc.drawImage(region4, 0, 0);
                break;
            case 5:
                gc.drawImage(flightPlan, 0, 0);
                break;
        }

        for (int i = 0; i < flags.length; i++) {
            if (getAnchorRegion()[i] == currentRegion) {
                gc.drawImage(flags[i], getFlagAnchorX()[i] - 30, getFlagAnchorY()[i] - 20);
            }
        }

        for (int i = 0; i < pieces.length; i++) {
            if (regionCheck[i] == currentRegion) {
                gc.drawImage(pieces[i], xCoordStarting[i] - 10, yCoordStarting[i] - 20);
            }
        }

        print();
    }

    public void checkPos(double xCoord, double yCoord) {
        double[] xCoordCheck = city.getxCoord();
        double[] yCoordCheck = city.getyCoord();
        int[] quarterCheck = city.getQuarter();
        String[] nameCheck = city.getName();
        for (int i = 0; i < xCoordCheck.length; i++) {
            if (xCoord > xCoordCheck[i] - 20 && xCoord < xCoordCheck[i] + 20) {
                if (yCoord > yCoordCheck[i] - 20 && yCoord < yCoordCheck[i] + 20) {
                    if (quarterCheck[i] == 1 && currentRegion == 1) {
                        //  System.out.println(nameCheck[i] + " " + xCoordCheck[i] + " " + yCoordCheck[i]);
                        currentCity = nameCheck[i];
                        drawLines();
                        Label temp = new Label(nameCheck[i]);
                        //ui.getLeftGamePane().getChildren().add(temp);
                    }
                    if (quarterCheck[i] == 2 && currentRegion == 2) {
                        //   System.out.println(nameCheck[i] + " " + xCoordCheck[i] + " " + yCoordCheck[i]);
                        currentCity = nameCheck[i];
                        drawLines();
                        Label temp = new Label(nameCheck[i]);
                        //ui.getLeftGamePane().getChildren().add(temp);
                    }
                    if (quarterCheck[i] == 3 && currentRegion == 3) {
                        //  System.out.println(nameCheck[i] + " " + xCoordCheck[i] + " " + yCoordCheck[i]);
                        currentCity = nameCheck[i];
                        drawLines();
                        Label temp = new Label(nameCheck[i]);
                        //ui.getLeftGamePane().getChildren().add(temp);
                    }
                    if (quarterCheck[i] == 4 && currentRegion == 4) {
                        //   System.out.println(nameCheck[i] + " " + xCoordCheck[i] + " " + yCoordCheck[i]);
                        currentCity = nameCheck[i];
                        drawLines();
                        Label temp = new Label(nameCheck[i]);
                        //ui.getLeftGamePane().getChildren().add(temp);
                    }
                }
            }
        }
    }

    public void switchRegion(int region) {
        switch (region) {
            case 1:
                gc.drawImage(region1, 0, 0);
                currentRegion = 1;
                repaint();
                break;
            case 2:
                gc.drawImage(region2, 0, 0);
                currentRegion = 2;
                repaint();
                break;
            case 3:
                gc.drawImage(region3, 0, 0);
                currentRegion = 3;
                repaint();
                break;
            case 4:
                gc.drawImage(region4, 0, 0);
                currentRegion = 4;
                repaint();
                break;
            case 5:
                gc.drawImage(flightPlan, 0, 0);
                currentRegion = 5;
        }
    }

    public void startPoints(int index) {
        double[] coord = new double[2];
        Player[] arr = ui.getPlayersArray();
        String[] dest = arr[index].getDestinations();
        String[] nameArr = city.getName();
        int[] region = city.getQuarter();
        String cityLoc = dest[0];
        int initial = -1;

        for (int i = 0; i < nameArr.length; i++) {
            if (cityLoc.equals(nameArr[i])) {
                initial = i;
            }
        }

        double[] xCoord = city.getxCoord();
        double[] yCoord = city.getyCoord();
        coord[0] = xCoord[initial];
        coord[1] = yCoord[initial];
        xCoordStarting[index] = coord[0];
        yCoordStarting[index] = coord[1];
        getFlagAnchorX()[index] = coord[0];
        getFlagAnchorY()[index] = coord[1];
        getAnchorRegion()[index] = region[initial];
        regionCheck[index] = region[initial];
        cityLocName[index] = cityLoc;

        getInitialRegion()[index] = region[initial];
        //System.out.println(cityLoc + " " + coord[0] + " " + coord[1]);
    }

    public void drawLines() {
        try {
            ArrayList arr = city.getRouteNames();
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).equals(currentCity)) {
                    ArrayList arrLand = city.getLandRoutes();
                    ArrayList arrSea = city.getSeaRoutes();
                    String[] landElements = (String[]) arrLand.get(i);
                    String[] seaElements = (String[]) arrSea.get(i);
                    for (String j : landElements) {
                        extendLine((String) arr.get(i), j);
                    }
                    for (String z : seaElements) {
                        extendLine((String) arr.get(i), z);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR IDENTIFIED");
        }
    }

    //UPDATE METHOD TO ACCOUNT FOR CONNECTION IN DIFFERENT REGIONS 
    public void extendLine(String start, String end) {
        // System.out.println(start + " " + end);
        String[] nameList = city.getName();
        double[] xCoord = city.getxCoord();
        double[] yCoord = city.getyCoord();
        int[] quarter = city.getQuarter();
        int startPos = -1;
        int endPos = -1;
        for (int i = 0; i < nameList.length; i++) {
            if (start.equals(nameList[i])) {
                startPos = i;
            } else if (end.equals(nameList[i])) {
                endPos = i;;
            } else {

            }
        }

        gc.setStroke(Color.RED);
        try {
            if (quarter[startPos] == quarter[endPos]) {
                if (quarter[startPos] == 1 || quarter[startPos] == 3) {
                    gc.strokeLine(xCoord[startPos], yCoord[startPos], xCoord[endPos], yCoord[endPos]);
                } else {
                    gc.strokeLine(xCoord[startPos], yCoord[startPos], xCoord[endPos], yCoord[endPos]);
                }
            }
        } catch (Exception e) {

        }
    }

    public void flyToDestination(String destination) {
        int index = city.getNameArr().indexOf(destination);

        xCoordStarting[currentPlayer] = city.getxCoord()[index];
        yCoordStarting[currentPlayer] = city.getyCoord()[index];
        regionCheck[currentPlayer] = city.getQuarter()[index];
        switchRegion(city.getQuarter()[index]);
        repaint();
    }

    public void canFly(String from, String to, int fromQuarter, int toQuarter) {
        int points = ui.getDiceRollValue();
        if (points >= 2 && fromQuarter == toQuarter) {
            flyToDestination(to);
            ui.getPlayersArray()[currentPlayer].getHistory().add(to);
            ui.setDiceRollValue(ui.getDiceRollValue() - 2);
            ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
            checkDest(to);
            newRound();
        }
        if ((points >= 4 && fromQuarter == 1)) {
            if ((toQuarter == 2 || toQuarter == 4) == true) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
        if ((points >= 4 && fromQuarter == 2)) {
            if ((toQuarter == 1 || toQuarter == 3) == true) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
        if ((points >= 4 && fromQuarter == 5)) {
            if ((toQuarter == 4 || toQuarter == 6) == true) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
        if ((points >= 4 && fromQuarter == 6)) {
            if ((toQuarter == 5 || toQuarter == 3) == true) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
        if (points >= 4 && fromQuarter == 4) {
            if ((toQuarter == 2 || toQuarter == 6) == false) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
        if (points >= 4 && fromQuarter == 3) {
            if ((toQuarter == 1 || toQuarter == 5) == false) {
                flyToDestination(to);
                ui.getPlayersArray()[currentPlayer].getHistory().add(to);
                ui.setDiceRollValue(ui.getDiceRollValue() - 4);
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(to);
                newRound();
            }
        }
    }

    public void EventHandlers() {

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        int size = ui.getPlayersArray()[currentPlayer].getHistory().size();
                        String cityx = ui.getPlayersArray()[currentPlayer].getHistory().get(size - 1);
                        int cityIndex = city.getNameArr().indexOf(cityx);
                        if(checkPos2(mouseEvent.getX(), mouseEvent.getY()) != null){
                            int destIndex = city.getNameArr().indexOf(checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                            if(city.getQuarter()[destIndex] == city.getQuarter()[cityIndex]){
                                int hisSize = ui.getPlayersArray()[currentPlayer].getHistory().size();
                                if(ui.getDiceRollValue() > 1 && isValidCity(ui.getPlayersArray()[currentPlayer].getHistory().get(hisSize - 1), checkPos2(mouseEvent.getX(),mouseEvent.getY()))){
                                    animateMovement(cityx, checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                                    ui.getPlayersArray()[currentPlayer].getHistory().add(checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                                    ui.setDiceRollValue(ui.getDiceRollValue() - 1);
                                    ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                                    checkPort(checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                                    checkDest(checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                                    gameHistory.add("Player " + (currentPlayer + 1) + " moved to " + checkPos2(mouseEvent.getX(), mouseEvent.getY()));
                            }
                       }
                    }
                 }
              }
            }
        });
        
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                checkPos(mouseEvent.getX(), mouseEvent.getY());
                
                System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
                if (currentRegion == 5) {
                    double x = mouseEvent.getX();
                    double y = mouseEvent.getY();
                    double[] xCoordCheck = city.getAirportxCoord();
                    double[] yCoordCheck = city.getAirportyCoord();
                    String targetAirportCity = "";
                    for (int i = 0; i < city.getAirportxCoord().length; i++) {
                        if (x > xCoordCheck[i] - 8 && x < xCoordCheck[i] + 8) {
                            if (y > yCoordCheck[i] - 8 && y < yCoordCheck[i] + 8) {
                                targetAirportCity = city.getAirportCities().get(i);
                                System.out.println(targetAirportCity);
                                int size = ui.getPlayersArray()[currentPlayer].getHistory().size();
                                String cityx = ui.getPlayersArray()[currentPlayer].getHistory().get(size - 1);
                                int indexCityx = city.getAirportCities().indexOf(cityx);
                                int fromQuarter = city.getAirportRegions()[indexCityx];
                                int indexTarget = city.getAirportCities().indexOf(targetAirportCity);
                                int toQuarter = city.getAirportRegions()[indexTarget];
                                canFly(cityx,targetAirportCity, fromQuarter, toQuarter);
                                //flyToDestination(targetAirportCity);
                            }
                        }
                    }
                }

            }
        });

        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            int current = -1;

            @Override
            public void handle(MouseEvent mouseEvent) {
                for (int i = 0; i < pieces.length; i++) {
                    if (mouseEvent.getX() > xCoordStarting[i] - 10 && mouseEvent.getX() < xCoordStarting[i] + 10) {
                        if (mouseEvent.getY() > yCoordStarting[i] - 10 && mouseEvent.getY() < yCoordStarting[i] + 10) {
                            current = i;
                        }
                    }
                }
                currentPiece = current;
                if (currentPiece != -1 && currentPiece == currentPlayer && ui.getDiceRolled()) {
                    if (mouseEvent.getX() > 486.0 && currentRegion == 1) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 2;
                        switchRegion(2);
                        repaint();
                    }
                    if (mouseEvent.getX() < -25.0 && currentRegion == 2) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 1;
                        switchRegion(1);
                        repaint();
                    }
                    if (mouseEvent.getX() > 486.0 && currentRegion == 3) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 4;
                        switchRegion(4);
                        repaint();
                    }
                    if (mouseEvent.getX() < -25.0 && currentRegion == 4) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 3;
                        switchRegion(3);
                        repaint();
                    }
                    if (mouseEvent.getY() > 645.0 && currentRegion == 1) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 3;
                        switchRegion(3);
                        repaint();
                    }
                    if (mouseEvent.getY() > 645.0 && currentRegion == 2) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 4;
                        switchRegion(4);
                        repaint();
                    }
                    if (mouseEvent.getY() < -26.0 && currentRegion == 3) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 1;
                        switchRegion(1);
                        repaint();
                    }
                    if (mouseEvent.getY() < -26.0 && currentRegion == 4) {
                        xCoordStarting[currentPiece] = mouseEvent.getX();
                        yCoordStarting[currentPiece] = mouseEvent.getY();
                        regionCheck[currentPiece] = 2;
                        switchRegion(2);
                        repaint();
                    }
                    gc.drawImage(pieces[currentPiece], mouseEvent.getX() + 5, mouseEvent.getY() + 5);
                    xCoordStarting[currentPiece] = mouseEvent.getX();
                    yCoordStarting[currentPiece] = mouseEvent.getY();
                    // System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
                    repaint();
                }
                mouseValue = 2;
            }
        });

        this.addEventFilter(MouseDragEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//        this.addEventFilter(MouseDragEvent.ANY, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                repaint();
                if (mouseValue != 1 && currentPiece == currentPlayer && ui.getDiceRolled()) {
                    mouseValue = 1;
                    // System.out.println("MOUSE_RELEASED");
                    double releaseX = mouseEvent.getX();
                    double releaseY = mouseEvent.getY();
                    movePiece(currentCity, releaseX, releaseY);
                }
            }
        });
    }

    public void displayCards2(Player player, int index) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new EventHandler<ActionEvent>() {
                            int i = 0;

                            @Override
                            public void handle(ActionEvent actionEvent) {
                                ImageView view = new ImageView(new Image("file:Artwork/cards/" + player.getDestinations()[i] + ".jpg"));
                                ui.getGamePlayPane().getChildren().add(view);
                                view.setX(150.0);
                                view.setY(240.0);

                                ui.getLeftGamePane().getChildren().add(new ImageView(new Image("file:Artwork/tiles/" + player.getDestinations()[i] + ".jpg")));

                                final Duration SEC_2 = Duration.millis(2000);
                                final Duration SEC_3 = Duration.millis(3000);

                                FadeTransition ft = new FadeTransition(SEC_3);
                                ft.setFromValue(1.0f);
                                ft.setToValue(0.0f);
                                ft.setCycleCount(1);
                                ft.setAutoReverse(false);
                                TranslateTransition tt = new TranslateTransition(SEC_2);
                                tt.setFromX(200.0);
                                tt.setToX(-120.0);
                                tt.setCycleCount(1);
                                tt.setAutoReverse(false);
                                ScaleTransition st = new ScaleTransition(SEC_2);
                                st.setByX(0.2f);
                                st.setByY(0.2f);
                                st.setCycleCount(1);
                                st.setAutoReverse(false);
                                i++;

                                ParallelTransition pt = new ParallelTransition(view, ft, tt, st);
                                pt.play();
                            }
                        }
                ),
                new KeyFrame(
                        Duration.millis(3000)
                )
        );
        timeline.setCycleCount(3);

        if (playCards == true) {
            timeline.play();
        }
    }

    public void displaySpecialInstructions(String special) {
        try {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new EventHandler<ActionEvent>() {
                                int i = 0;

                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    ImageView view = new ImageView(new Image("file:Artwork/cards/" + special + "_I.jpg"));
                                    ui.getGamePlayPane().getChildren().add(view);
                                    view.setX(450.0);
                                    view.setY(140.0);

                                    TranslateTransition tt = new TranslateTransition(Duration.millis(10000));
                                    tt.setFromX(200.0);
                                    tt.setToX(-400.0);
                                    tt.setCycleCount(1);
                                    tt.setAutoReverse(false);
                                    PauseTransition pausett = new PauseTransition(Duration.millis(1000));
                                    FadeTransition ft = new FadeTransition(Duration.millis(2000));
                                    ft.setFromValue(1.0f);
                                    ft.setToValue(0.0f);
                                    ft.setCycleCount(1);
                                    ft.setAutoReverse(false);
                                    i++;

                                    SequentialTransition pt = new SequentialTransition(view, tt, pausett, ft);
                                    pt.play();

                                }
                            }
                    ),
                    new KeyFrame(
                            Duration.millis(15000)
                    )
            );
            timeline.setCycleCount(1);

            timeline.play();
        } catch (Exception e) {

        }
    }
    
    public boolean isValidCity(String fromCity, String landedCity){
            int indexList = -1;
            for (int i = 0; i < city.getRouteNames().size(); i++) {
                if (city.getRouteNames().get(i).equals(fromCity)) {
                    indexList = i;
                }
            }
            
            boolean isValidCity = false;

            for (String i : city.getLandRoutes().get(indexList)) {
                if (i.equals(landedCity)) {
                    isValidCity = true;
                }
            }

            boolean seaPort = false;
            for (String i : city.getSeaRoutes().get(indexList)) {
                if (i.equals(landedCity) && canTravelSea == true) {
                    isValidCity = true;
                    seaPort = true;
                }
            }
         
            if(landedCity != null){
                if (landedCity.equals(ui.getPlayersArray()[currentPlayer].getPreviousCity())) {
                    isValidCity = false;
                    }
            }
        return isValidCity;
    }
    
    public void checkPort(String landedCity){
        if (city.getAirportCities().contains(landedCity) == true) {
                    ui.getFlightPlanButton().setDisable(false);
                    //System.out.println("ACCESED ENABLED");
                }
        if (city.getAirportCities().contains(landedCity) == false) {
                    ui.getFlightPlanButton().setDisable(true);
                    // System.out.println("ACCESSED DISABLE");
                }
        if (city.getSeaPorts().contains(landedCity)) {
                    if(seaportWait[currentPlayer] == 0 && ui.getDiceRollValue() != 0 && canTravelSea == true){
                    ui.getEventHandler().respondToSeaPortRequest(ui.getPrimaryStage());
                    }
                    canTravelSea = false;
                }
    }

    public void movePiece(String name, double x, double y) {
        String[] nameList = city.getName();
        double[] xCoord = city.getxCoord();
        double[] yCoord = city.getyCoord();
        int[] quarters = city.getQuarter();
        ArrayList list = city.getRouteNames();
        ArrayList<String[]> land = city.getLandRoutes();
        ArrayList<String[]> sea = city.getSeaRoutes();

        String landedCity = checkPos2(x, y);

        //try {

            int indexList = -1;
            for (int i = 0; i < city.getRouteNames().size(); i++) {
                if (city.getRouteNames().get(i).equals(name)) {
                    indexList = i;
                }
            }
            
            int indexInitial = -1;
            int indexFinal = -1;
            for (int i = 0; i < city.getName().length; i++) {
                if (city.getName()[i] != null) {
                    if (city.getName()[i].equals(name)) {
                        indexInitial = i;
                    }
                    if (city.getName()[i].equals(landedCity)) {
                        indexFinal = i;
                    }
                }
            }

            boolean isValidCity = false;

            for (String i : city.getLandRoutes().get(indexList)) {
                if (i.equals(landedCity)) {
                    isValidCity = true;
                }
            }

            boolean seaPort = false;
            for (String i : city.getSeaRoutes().get(indexList)) {
                if (i.equals(landedCity) && canTravelSea == true) {
                    isValidCity = true;
                    seaPort = true;
                }
            }
         
            if(landedCity != null){
                if (landedCity.equals(ui.getPlayersArray()[currentPlayer].getPreviousCity())) {
                    isValidCity = false;
                    }
            }
            
            if (isValidCity && ui.getDiceRollValue() > 0 && specialCondition == false && landedCity != null) {
                gameHistory.add("Player " + (currentPlayer + 1) + " moved to " + landedCity);
                atCity = landedCity;
                ui.getPlayersArray()[currentPlayer].getHistory().add(landedCity);
                System.out.println(ui.getPlayersArray()[currentPlayer].getHistory());
                if (city.getAirportCities().contains(landedCity) == true) {
                    ui.getFlightPlanButton().setDisable(false);
                    //System.out.println("ACCESED ENABLED");
                }
                if (city.getAirportCities().contains(landedCity) == false) {
                    ui.getFlightPlanButton().setDisable(true);
                    // System.out.println("ACCESSED DISABLE");
                }
                if (city.getSeaPorts().contains(landedCity)) {
                    if(seaportWait[currentPlayer] == 0 && ui.getDiceRollValue() != 0 && canTravelSea == true){
                    ui.getEventHandler().respondToSeaPortRequest(ui.getPrimaryStage());
                    }
                    canTravelSea = false;
                }
                int index;
                if(city.getCityList().contains(landedCity)){
                    index = city.getCityList().indexOf(landedCity);
                  //  System.out.println(city.getInfoList().get(index));
                    Label newLabel = new Label();
                    newLabel.setText(city.getInfoList().get(index));
                    newLabel.setWrapText(true);
                    ui.getScrollPane().setContent(newLabel);
                }
                
                xCoordStarting[currentPiece] = xCoord[indexFinal];
                yCoordStarting[currentPiece] = yCoord[indexFinal];
                getInitialRegion()[currentPiece] = quarters[indexFinal];
                if (city.getSpecialCities().contains(landedCity)) {
                    displaySpecialInstructions(landedCity);
                    //specialConditions(landedCity);
                   // System.out.println("ACCESSED special cities");
                }
                ui.getPlayersArray()[currentPlayer].setPreviousCity(currentCity);
                ui.setDiceRollValue(ui.getDiceRollValue() - 1); //CHANGE
                if (seaPort == true) {
                    ui.setDiceRollValue(0);
                }
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                checkDest(landedCity);
                repaint();
            } else if (!isValidCity) {
               if (getInitialRegion()[currentPiece] != currentRegion) {
                    switchRegion(getInitialRegion()[currentPiece]);
                    regionCheck[currentPiece] = getInitialRegion()[currentPiece];
                    repaint();
                }
                int sizeOfCheck = ui.getPlayersArray()[currentPlayer].getHistory().size();
                String val = ui.getPlayersArray()[currentPlayer].getHistory().get(sizeOfCheck - 1);
                int index = city.getNameArr().indexOf(val);
                xCoordStarting[currentPiece] = city.getxCoord()[index];
                yCoordStarting[currentPiece] = city.getyCoord()[index];
                //xCoordStarting[currentPiece] = xCoord[indexInitial];
                //yCoordStarting[currentPiece] = yCoord[indexInitial];
                repaint();
            }
            newRound();
    }
    
    public void checkDest(String landedCity){
            Player[] temp2 = ui.getPlayersArray();
                String[] playerDest = temp2[currentPlayer].getDestinations();
                for (String zh : playerDest) {
                    if (zh.equals(landedCity) && !landedCity.equals(playerDest[0])) {
                        temp2[currentPlayer].setReachedDest(temp2[currentPlayer].getReachedDest() - 1);
                       // System.out.println("Destination Reached " + zh);
                        String[] arr = new String[temp2[currentPlayer].getReachedDest() + 1];
                        arr[0] = playerDest[0];
                        if (arr.length > 1) {
                          //  System.out.println(arr.length);
                            if (zh.equals(playerDest[1])) {
                                arr[1] = playerDest[2];
                            } else if (zh.equals(playerDest[2])) {
                                arr[1] = playerDest[1];
                            }
                        }
                        ui.getEventHandler().respondToDestinationRequest(ui.getPrimaryStage());
                        ui.getPlayersArray()[currentPlayer].setDestinations(arr);
                        gameHistory.add("Player has reached destination, " + landedCity);
                    }
                }
                if (temp2[currentPlayer].getReachedDest() == 0) {
                    if (landedCity.equals(playerDest[0])) {
                        winner(currentPlayer);
                    }
                }
    }
    
    public void newRound(){
        if (ui.getDiceRollValue() == 0 && gameContinue == true) {
                ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
                int previousPlayer = currentPlayer;
                currentPlayer++;
                currentPlayer = currentPlayer % ui.getNumOfPlayers();
                
                
                if(seaportWait[currentPlayer] == 1){
                    canTravelSea = true;
                    seaportWait[currentPlayer] = 0;
                }
                
                int sizeOfCheck = ui.getPlayersArray()[currentPlayer].getHistory().size();
                if(city.getAirportCities().contains(ui.getPlayersArray()[currentPlayer].getHistory().get(sizeOfCheck - 1)) == true){
                    ui.getFlightPlanButton().setDisable(false);
                }
                if(city.getAirportCities().contains(ui.getPlayersArray()[currentPlayer].getHistory().get(sizeOfCheck - 1)) == false){
                    ui.getFlightPlanButton().setDisable(true);
                }
                
                if (city.getSeaPorts().contains(ui.getPlayersArray()[currentPlayer].getHistory().get(sizeOfCheck - 1)) && canTravelSea == false) {
                    ui.getEventHandler().respondToSeaPortRequest(ui.getPrimaryStage());
                }
                
                int val = city.getNameArr().indexOf(ui.getPlayersArray()[currentPlayer].getHistory().get(sizeOfCheck - 1));
                int regionVal = city.getQuarter()[val];
                switchRegion(regionVal);
                
                if(ui.getInitRollValue() == 6){
                    currentPlayer = previousPlayer;
                }
                
                if (currentPlayer == 0) {
                    playCards = false;
                }
                
                specialCondition = false;

                ui.getDiceRollButton().setDisable(false);
                ui.setDiceRolled(false);
                Player[] temp = ui.getPlayersArray();
                ui.getLeftGamePane().getChildren().clear();
                Label currentPlayerLabel = new Label("\t        Player " + (currentPlayer + 1));
                currentPlayerLabel.setFont(Font.font("Verdana", 16));
                ui.getLeftGamePane().getChildren().add(currentPlayerLabel);
                ui.getLeftGamePane().getChildren().add(new Label("\t\t\t\t\t\t\t\t\t\t\t"));
                Player[] tempPlayer = ui.getPlayersArray();
                String[] cardsBool = tempPlayer[currentPlayer].getDestinations();
                if (playCards == false) {
                    for (int i = 0; i < cardsBool.length; i++) {
                        ui.getLeftGamePane().getChildren().add(new ImageView("file:Artwork/tiles/" + cardsBool[i] + ".jpg"));
                    }
                }
                displayCards2(temp[currentPlayer], currentPlayer);
                ui.getCurrentPlayerLabel().setText("\t        Player " + currentPlayer);
                ui.getPieceImage().setImage(imagePieces[currentPlayer]);
            }
    }
    
    public void winner(int player) {
        gameContinue = false;
        ui.getLeftGamePane().getChildren().clear();
        ui.getLeftGamePane().getChildren().add(new Label("\t\t\t\t\t\t\t\t\t\t\t"));
        Label currentPlayerLabel = new Label("\t        Player " + (player + 1));
        currentPlayerLabel.setFont(Font.font("Verdana", 16));
        ui.getLeftGamePane().getChildren().add(currentPlayerLabel);
        ui.getLeftGamePane().getChildren().add(new ImageView(new Image("file:Artwork/winner.png")));
        ui.getEventHandler().respondToWinnerRequest(ui.getPrimaryStage());
    }

    public void print() {

    }

    public boolean almostEquals(String e1, String e2) {
        char[] e1c = e1.toCharArray();
        char[] e2c = e2.toCharArray();
        int match = 0;
        for (int i = 0; i < e1c.length; i++) {
            for (int j = 0; j < e2c.length; j++) {
                if (e1c[i] == e2c[j]) {
                    match++;
                }
            }
        }

        double percentage = (double) match / (double) e1.length();
        if (percentage > .8) {
            return true;
        }
        return false;
    }

    public String checkPos2(double xCoord, double yCoord) {
        double[] xCoordCheck = city.getxCoord();
        double[] yCoordCheck = city.getyCoord();
        int[] quarterCheck = city.getQuarter();
        String[] nameCheck = city.getName();
        for (int i = 0; i < xCoordCheck.length; i++) {
            if (xCoord > xCoordCheck[i] - 15 && xCoord < xCoordCheck[i] + 15) {
                if (yCoord > yCoordCheck[i] - 15 && yCoord < yCoordCheck[i] + 15) {
                    if (quarterCheck[i] == 1 && currentRegion == 1) {
                        return nameCheck[i];
                    }
                    if (quarterCheck[i] == 2 && currentRegion == 2) {
                        return nameCheck[i];
                    }
                    if (quarterCheck[i] == 3 && currentRegion == 3) {
                        return nameCheck[i];
                    }
                    if (quarterCheck[i] == 4 && currentRegion == 4) {
                        return nameCheck[i];
                    }
                }
            }
        }
        return null;
    }

        public void animateMovement(String city1, String city2) {
        try {
            System.out.println(city1);
            System.out.println(city2);
            int city1Index = -1;
            int city2Index = -1;
            for (int i = 0; i < city.getName().length; i++) {
                if (city.getName()[i].equals(city1)) {
                    city1Index = i;
                }
                if (city.getName()[i].equals(city2)) {
                    city2Index = i;
                }
            }

            double x1 = city.getxCoord()[city1Index];
            double y1 = city.getyCoord()[city1Index];
            double x2 = city.getxCoord()[city2Index];
            double y2 = city.getyCoord()[city2Index];

            double[] temp = new double[50];
            double increase = x1;
            System.out.println(x2 + " FINAL");
            double deltaX = x2 - x1;
            for (int i = 0; i < temp.length; i++) {
                increase += (deltaX / 50.0);
                temp[i] += increase;
            }
            System.out.println(temp[49] + " actual");

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new EventHandler<ActionEvent>() {
                                int i = 0;

                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    xCoordStarting[currentPlayer] = temp[i];
                                    yCoordStarting[currentPlayer] = y1 + ((y2 - y1) / (x2 - x1)) * (temp[i] - x1);
                                    i++;
                                    repaint();
                                }
                            }
                    ),
                    new KeyFrame(
                            Duration.millis(10)
                    )
            );
            timeline.setCycleCount(50);
            timeline.play();
        } catch (Exception e) {
            System.out.println("animMoveE");
        }
        
    }
    
    public void respondToFlightPlanRequest() {
        switchRegion(5);
        repaint();
    }
    
    public void specialConditions(String specialCity) {
        try {
            int i = city.getSpCitiesName().indexOf(specialCity);
            String j = city.getSpCitiesData().get(i);
            if (j.contains("trans")) {
                String temp = j.substring(j.indexOf("<place>") + 7, j.indexOf("</place"));
                int transDest = city.getNameArr().indexOf(temp);
                //System.out.println(transDest + " TRANSDEST");
               // System.out.println("confirm " + city.getName()[transDest]);
                regionCheck[currentPiece] = city.getQuarter()[transDest];
                getInitialRegion()[currentPiece] = city.getQuarter()[transDest];
                xCoordStarting[currentPiece] = city.getxCoord()[transDest];
                yCoordStarting[currentPiece] = city.getyCoord()[transDest];
                System.out.println(city.getQuarter()[transDest] + " " + city.getxCoord()[currentPiece] + " " + city.getyCoord()[currentPiece]);
                switchRegion(city.getQuarter()[transDest]);
                //System.out.println("ACCESSED TRANS ");
                repaint();
            }
            if (j.contains("dice")) {
                if (j.contains("this")) {
                    rollDiceAgain[currentPlayer] = 1;
                }
                if (j.contains("next")) {
                    rollDiceAgain[currentPlayer] = 2;
                }
            }
            if (j.contains("score")) {
                String tempScore = j.substring(j.indexOf("<points>") + 9, j.indexOf("</points"));
                String operation = j.substring(j.indexOf("<points>") + 8, j.indexOf("</points>") - 1);
                if (operation.equals("-")) {
                    addPoints[currentPlayer] = 0 - Integer.parseInt(tempScore);
                }
                if (operation.equals("+")) {
                    addPoints[currentPlayer] = Integer.parseInt(tempScore);
                }
                if (j.contains("this")) {
                    addPointsTurn[currentPlayer] = 1;
                }
                if (j.contains("next")) {
                    addPointsTurn[currentPlayer] = 2;
                }
            //ui.setDiceRollValue(addPoints[currentPlayer] + ui.getDiceRollValue());
                //ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
            }
            if (j.contains("miss")) {
                missTurn[currentPlayer] = 1;
            }
            if (j.contains("move")) {
                String tempMove = j.substring(j.indexOf("<cities>") + 7, j.indexOf("</cities>"));
                int moves = Integer.parseInt(tempMove);
                addMoves[currentPlayer] = moves;

                if (j.contains("this")) {
                    addMovesTurn[currentPlayer] = 1;
                }
                if (j.contains("next")) {
                    addMovesTurn[currentPlayer] = 2;
                }
            //ui.setDiceRollValue(addMoves[currentPlayer] + ui.getDiceRollValue());
                //ui.getDiceLabel().setText("     Points: " + ui.getDiceRollValue());
            }
            ui.setDiceRollValue(1);
            specialCondition = true;
        } catch (Exception e) {
            System.out.println("CHECK2");
        }
    }
    
    public double[] getxCoordStarting() {
        return xCoordStarting;
    }

    public double[] getyCoordStarting() {
        return yCoordStarting;
    }

    public void setxCoordStarting(double[] val) {
        xCoordStarting = val;
    }

    public void setyCoordStarting(double[] val) {
        yCoordStarting = val;
    }

    public void setRegionCheck(int[] val) {
        regionCheck = val;
    }

    public int[] getRegionCheck() {
        return regionCheck;
    }

    /**
     * @return the flagAnchorX
     */
    public double[] getFlagAnchorX() {
        return flagAnchorX;
    }

    /**
     * @param flagAnchorX the flagAnchorX to set
     */
    public void setFlagAnchorX(double[] flagAnchorX) {
        this.flagAnchorX = flagAnchorX;
    }

    /**
     * @return the flagAnchorY
     */
    public double[] getFlagAnchorY() {
        return flagAnchorY;
    }

    /**
     * @param flagAnchorY the flagAnchorY to set
     */
    public void setFlagAnchorY(double[] flagAnchorY) {
        this.flagAnchorY = flagAnchorY;
    }

    /**
     * @return the initialRegion
     */
    public int[] getInitialRegion() {
        return initialRegion;
    }

    /**
     * @param initialRegion the initialRegion to set
     */
    public void setInitialRegion(int[] initialRegion) {
        this.initialRegion = initialRegion;
    }

    /**
     * @return the anchorRegion
     */
    public int[] getAnchorRegion() {
        return anchorRegion;
    }

    /**
     * @param anchorRegion the anchorRegion to set
     */
    public void setAnchorRegion(int[] anchorRegion) {
        this.anchorRegion = anchorRegion;
    }
    
    public ArrayList<String> getGameHistory(){
        return gameHistory;
    }
    
    public void setPlayCards(boolean val){
        playCards = false;
    }
    
    public void setCurrentCity(String val){
        currentCity = val;
    }
    
    public int[] getDiceRollAgain(){
        return rollDiceAgain;
    }
    
    public int[] getSeaportWait(){
        return seaportWait;
    }
    
    public int getCurrentPlayer(){
        return currentPlayer;
    }
    
    public Cities getCity(){
        return city;
    }
    
    public void setCurrentPlayer(int val){
        currentPlayer = val;
    }
    
    public void setGameHistory(ArrayList<String> val){
        gameHistory = val;
    }
}
