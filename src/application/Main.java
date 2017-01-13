/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import journeythrougheurope.ui.JourneyThroughEuropeUI;

/**
 *
 * @author user
 */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try{   
        String title = "Journey Through Europe";
        primaryStage.setTitle(title);
        
        
        JourneyThroughEuropeUI root = new JourneyThroughEuropeUI();
        BorderPane mainPane = root.GetMainPane();
        root.SetStage(primaryStage);
        
        Scene scene = new Scene(mainPane, 1000, 630);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
