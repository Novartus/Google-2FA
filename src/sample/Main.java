package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application  {
    Stage window;
    Button button1, exit_button;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args); //It starts here
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       window = primaryStage;

       Label label1= new Label("Second Window");
        exit_button = new Button("EXIT");

        exit_button.setOnAction(e -> {
         //  System.out.println("Now switching to other scene :)");
           System.exit(0);
       });  //LAMBDA EXPRESSIONS ARE AWESOME , CLEAN CODE !! JAVA 8


        //LAYOUT Vertical Column
        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(exit_button);
        scene2 = new Scene(layout2,250,250);

        button1 = new Button("Next Window");
        button1.setOnAction(e ->window.setScene(scene2));  //LAMBDA EXPRESSIONS ARE AWESOME , CLEAN CODE !! JAVA 8

        //  exit_button.setOnAction(e -> System.exit(0));

        StackPane layout1 = new StackPane();
        layout1.getChildren().add(button1);
        scene1 = new Scene(layout1,300,300);

         window.setScene(scene1);
         window.setTitle("Title: App_FX");
         window.show();

    }

}