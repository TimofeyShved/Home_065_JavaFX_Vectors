package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Вектор"); // заголовок формы

        // наша панель
        Pane root = new Pane();

        // Создаем наш вектор
        myBullet bullet = new myBullet();

        // добавить на панель
        root.getChildren().addAll(bullet);

        // добавление на сцены | на форму
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);  // размер формы и сцена
        primaryStage.show(); // отобразить

        // действие на движение мышки
        scene.setOnMouseMoved(event -> {
            // передаем координаты мышки
            bullet.setVector(event.getSceneX(), event.getSceneY());
        });

        // действие на нажатие мышки
        scene.setOnMouseClicked(event -> {
            bullet.vector = new Point2D(0,0);
        });

        // создаём таймер
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bullet.move(); // заставляем действовать вектор
            }
        };
        timer.start(); // запускаем его
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class myBullet extends Pane{
    public Point2D vector = new Point2D(0,0); // 2Д точка в координатах 0/0

    // конструктор
    public myBullet(){
        // добавляем на панель, новый прямоугольник, ширина 20, длинна 2, цвет красный
        getChildren().add(new Rectangle(20,2, Color.RED));
    }

    // запись навых координат (вызывается каждый раз, при движении мышки)
    public void setVector (double x, double y){
        // получем координаты нашей новой точик (вычитаем старые) и  нармализуем их, а так же умножаем на 5 (скорость 5 пикселей в секунду
        vector = new Point2D(x,y).subtract(getTranslateX(), getTranslateY()).normalize().multiply(5);

        // получаем угол наклона
        double angle = calcAngle(vector.getX(), vector.getY());
        getTransforms().clear(); // очищаем трансоформацию, поворота вращения
        getTransforms().add(new Rotate(angle,0,0)); // добовляем новый угол
    }

    // заставляем объект двигаться
    public void move(){
        // устанавливаем координаты, а так же движение объекта по Х (setTranslateX)
        setTranslateX(getTranslateX()+vector.getX());
        setTranslateY(getTranslateY()+vector.getY());
    }

    // вычисляем угол поворота нашего объекта
    public double calcAngle(double vectX, double vectY){
        // записываем новые точки, текущего положения мышки, а так же вычислется угол, от нашец мышки и вектором
        double angle = new Point2D(vectX,vectY).angle(1,0);
        return vectY>0?angle:-angle;
    }
}