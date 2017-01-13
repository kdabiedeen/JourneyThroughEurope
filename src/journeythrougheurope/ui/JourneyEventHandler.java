/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class JourneyEventHandler {

    private JourneyThroughEuropeUI ui;

    public void respondToSaveGameRequest(Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane savePane = new BorderPane();
        Label saveLabel = new Label("Game Has Been Saved!");
        saveLabel.setFont(Font.font("Verdana", 20));
        savePane.setCenter(saveLabel);
        Scene scene = new Scene(savePane, 300, 100);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    public void respondToDestinationRequest(Stage primaryStage){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane savePane = new BorderPane();
        Label saveLabel = new Label("Destination Reached!");
        saveLabel.setFont(Font.font("Verdana", 20));
        savePane.setCenter(saveLabel);
        Scene scene = new Scene(savePane, 300, 100);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    public void respondToSeaPortRequest(Stage primaryStage) {
        Stage dialogStage = new Stage();
        String options[] = new String[]{"Wait", "Dont Wait"};
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane exitPane = new BorderPane();
        HBox optionPane = new HBox();
        Button yesButton = new Button(options[0]);
        Button noButton = new Button(options[1]);
        optionPane.setSpacing(10.0);
        optionPane.getChildren().addAll(yesButton, noButton);
        Label exitLabel = new Label("Sea Port! Make A Choice");
        exitLabel.setFont(Font.font("Verdana", 20));
        exitPane.setCenter(exitLabel);
        exitPane.setBottom(optionPane);
        Scene scene = new Scene(exitPane, 300, 100);
        dialogStage.setScene(scene);
        dialogStage.show();
        yesButton.setOnAction(e -> {
            ui.getMapView().getSeaportWait()[ui.getMapView().getCurrentPlayer()] = 1;
            ui.setDiceRollValue(0);
            ui.getMapView().newRound();
            dialogStage.close();
        });
        noButton.setOnAction(e -> {
            dialogStage.close();
        });

    }

    public void respondToWinnerRequest(Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane exitPane = new BorderPane();
        HBox optionPane = new HBox();
        Button yesButton = new Button("EXIT");
        optionPane.setSpacing(10.0);
        optionPane.getChildren().addAll(yesButton);
        Label exitLabel = new Label("You Win!");
        exitLabel.setFont(Font.font("Verdana", 20));
        exitPane.setCenter(exitLabel);
        exitPane.setBottom(optionPane);
        Scene scene = new Scene(exitPane, 50, 100);
        dialogStage.setScene(scene);
        dialogStage.show();
        yesButton.setOnAction(e -> {
            System.exit(0);
        });

    }

    public JourneyEventHandler(JourneyThroughEuropeUI initUI) {
        ui = initUI;
    }
    
    void respondToHistoryScreenRequest(Stage primaryStage) {
       ui.GetMainPane().setCenter(ui.getHistoryScreen());
    }
    
    public void respondToNewGameRequest(Stage primaryStage){
        ui.GetMainPane().setCenter(ui.getGameSetupContainer());
    }
    
    public void respondToGamePlayScreenRequest(Stage primaryStage){
        ui.GetMainPane().setCenter(ui.getGamePlayPane());
    }
    
    public void respondToAboutJTERequest(Stage primaryStage){
        ui.GetMainPane().setCenter(ui.getAboutJTEScreen());
    }
    
    public void respondToSplashScreenRequest(Stage primaryStage){
        ui.GetMainPane().setCenter(ui.getSplashScreenPane());
    }
    
    public void respondToExitRequest(Stage primaryStage) {
        System.exit(0);

    }
    
    
}
