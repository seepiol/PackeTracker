package Routing.Controller;

import Routing.MainApp;
import Routing.Model.MainModel;
import Routing.Model.Router;
import Routing.View.DragResizeMod;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainController {
    private MainApp mainApp;
    private MainModel mainModel;

    @FXML
    private Group group;

    private void postMainAppInitialize(){
        disegnaRouter();

    }

    private void disegnaRouter(){
        for(Router router : mainModel.getListaRouter()){
            Rectangle routerRect = new Rectangle(50, 50);
            routerRect.setFill(Color.BLACK);
            DragResizeMod.makeResizable(routerRect);
            group.getChildren().add(routerRect);



        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainModel = mainApp.getMainModel();
        postMainAppInitialize();
    }
}
