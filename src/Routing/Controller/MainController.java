package Routing.Controller;

import Routing.MainApp;
import Routing.Model.Collegamento;
import Routing.Model.MainModel;
import Routing.Model.Router;
import Routing.View.DragResizeMod;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class MainController {
    private MainApp mainApp;
    private MainModel mainModel;

    @FXML
    private Group group;

    public MainController(){
        DragResizeMod.setMainController(this);
    }

    private void postMainAppInitialize(){
        disegnaRouter();
        disegnaCollegamenti();
    }

    public void eliminaCollegamenti(){
        for(int i=0; i<group.getChildren().size(); i++){
            if(group.getChildren().get(i) instanceof Line){
                group.getChildren().remove(i);
            }
        }
    }

    private void disegnaCollegamenti(){
        System.out.println("COLL");
        eliminaCollegamenti();
        for(Collegamento collegamento : mainModel.getListaCollegamenti()){
            Canvas canvasRouter1 = collegamento.getNodo1().getRouter().getCanvas();
            Canvas canvasRouter2 = collegamento.getNodo2().getRouter().getCanvas();
            Line line = new Line(canvasRouter1.getLayoutX()+50, canvasRouter1.getLayoutY()+50, canvasRouter2.getLayoutX()+50, canvasRouter2.getLayoutY()+50);
            line.setStrokeWidth(4);
            group.getChildren().add(line);
        }
    }

    private void disegnaRouter(){
        int spawnPosX = 0;
        int spawnPosY = 0;
        for(Router router : mainModel.getListaRouter()){
            Canvas canvas = new Canvas(100, 100);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.setLineWidth(5);
            graphicsContext.setStroke(Color.BLACK);
            Font font = Font.font(15);
            graphicsContext.setFont(font);
            graphicsContext.fillText(router.toString(), 12, 90);
            graphicsContext.strokeOval(30, 30, 40, 40);
            //graphicsContext.strokeRect(0, 0, 100, 100);
            DragResizeMod.makeResizable(canvas);
            group.getChildren().add(canvas);
            canvas.setLayoutX(spawnPosX);
            canvas.setLayoutY(spawnPosY);
            spawnPosX+=100;
            spawnPosY+=100;
            router.setCanvas(canvas);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainModel = mainApp.getMainModel();
        postMainAppInitialize();
    }
}
