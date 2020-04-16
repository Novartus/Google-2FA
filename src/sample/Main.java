package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application  {
    Stage window;
    Button generate_button, exit_button;
    Scene scene1, scene2, scene3;

    public static void main(String[] args) {
        launch(args); //It starts here
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       window = primaryStage;

        generate_button = new Button("Generate Code");
        generate_button.setOnAction(e ->window.setScene(scene2));  //LAMBDA EXPRESSIONS ARE AWESOME , CLEAN CODE !! JAVA 8

        Image qrcode = new Image(new FileInputStream("/tmp/2FA-QR-Code.png"));
        ImageView imageView = new ImageView(qrcode);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);


        // Label label1= new Label("Second Window");
        exit_button = new Button("EXIT");
        exit_button.setOnAction(e -> System.exit(0));       //LAMBDA EXPRESSIONS ARE AWESOME , CLEAN CODE !! JAVA 8




        StackPane layout1 = new StackPane();
        layout1.getChildren().add(generate_button);
        scene1 = new Scene(layout1,500,500);

        HBox layout2 = new HBox();
        layout2.setSpacing(10);
        layout2.getChildren().addAll(imageView, exit_button);
        scene2 = new Scene(layout2, 500,500 );

        StackPane layout3 = new StackPane();
        layout3.getChildren().add(exit_button);
        scene3 = new Scene(layout3,500,500);


        window.setScene(scene1);
        window.setTitle("Title: App_FX");
        window.show();

    }

}