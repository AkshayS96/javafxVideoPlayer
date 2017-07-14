/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import javafx.scene.input.KeyEvent;
import java.io.File;
import static java.lang.Math.floor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import static javafx.application.Platform.runLater;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 *
 * @author Akshay Solanki
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private MediaView mv;
    private MediaPlayer mp;
    private Media me;
    public int count = 1;
    @FXML
    private Button playPause;
    @FXML
    private Button reload;
    @FXML
    private Button back;
    @FXML
    private Label time;
    @FXML
    private Slider timeSlider;
    @FXML
    private Pane pane;
    @FXML
    private Label TotalDuration;
    @FXML
    private Label runningTime;
    @FXML
    private Duration duration;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ImageView soundBtn;
    @FXML
    private Button sound;
    @FXML
    private Button forward;
    @FXML
    private Button stop;
    @FXML
    private MenuItem file;
    @FXML 
    private MenuItem quit;
    @FXML
    private MenuItem about;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Menu Playback;
   

//the above media and mediaplayer are not a part of the scene but are necessary to run the media on the mediaview 
    private ImageView pauseimage = new ImageView(new Image(getClass().getResourceAsStream("images/pause.png")));
    private ImageView playImage = new ImageView(new Image(getClass().getResourceAsStream("images/play.png")));
   public String path;
   boolean ctrl,shift;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //now applying the working to intialze the video

        
        file.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                 
              
                 intial();
                   playPause.setGraphic(pauseimage);
        playPause.setStyle("-fx-background-color:transparent; -fx-background-radius: 100%;");
        playPause.setEffect(new DropShadow());
            }
        });
       
        //Adding the pause play event using the space on the mediaplayer
      anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>(){
          @Override
          public void handle(KeyEvent event){
              
              if(event.getCode()==KeyCode.SPACE &&mp.getStatus()==Status.PLAYING){
              mp.pause();
              playPause.setGraphic(playImage);
              }
              else if(event.getCode()==KeyCode.SPACE &&mp.getStatus()==Status.PAUSED)
              {
                  mp.play();
                  playPause.setGraphic(pauseimage);
              }
              else if(event.getCode()==KeyCode.CONTROL)
              {
                  ctrl=true;
                  
              }
              else if(event.getCode()==KeyCode.SHIFT)
              {
                  shift=true;
              }
              
              if(event.getCode()==KeyCode.RIGHT&&ctrl){
                  mp.seek(mp.getCurrentTime().add(Duration.seconds(120)));
              }
              else if(event.getCode()==KeyCode.LEFT&&ctrl)
              {
                  mp.seek(mp.getCurrentTime().subtract(Duration.seconds(120)));
              }
              
              if(event.getCode()==KeyCode.RIGHT&&shift&&!ctrl)
              {
                  mp.seek(mp.getCurrentTime().add(Duration.seconds(60)));
              }
              else if(event.getCode()==KeyCode.LEFT&&shift&&!ctrl)
              {
                  mp.seek(mp.getCurrentTime().subtract(Duration.seconds(60)));
              }
              
              if(event.getCode()==KeyCode.UP&&shift){
                  double volumeNow=(mp.getVolume()*100)+5;
                  System.out.println(mp.getVolume());
                  mp.setVolume(volumeNow/100);
                  volumeSlider.setValue(volumeNow);
                  
                  
                  
              }
              else if(event.getCode()==KeyCode.DOWN&&shift)
              {
                  double volumeNow=(mp.getVolume()*100)-5;
                  System.out.println(mp.getVolume());
                  mp.setVolume(volumeNow/100);
                   volumeSlider.setValue(volumeNow);
                  
                                 }
              event.consume();
          }
      });
      //binding the volume  property  of  the mediaplayer to the volume slider
      
      anchorPane.setOnKeyReleased(new EventHandler<KeyEvent>(){
          @Override
          public void handle(KeyEvent event){
             if(event.getCode()==KeyCode.CONTROL||event.getCode()==KeyCode.SHIFT){
                 ctrl=false;
                 shift=false;             }
           
          }
      });
         
      

        //for adjusting the width and the height of the video on the media view
        //binding the size of the screen with the parent pane 
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.parentProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.parentProperty(), "height"));
        //we have to bind the property of maxwidth with the media size
        //adding the time label
       
        
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    mp.seek(Duration.seconds(timeSlider.getValue()));
                }
            }
        });

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100);
                    if (volumeSlider.getValue() == 0) {
                        sound.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/nosound.png"))));

                    } else {
                        
                        sound.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/sound.png"))));

                    }

                }

            }
        });        
        
//ContexxtMenu and menu bar
ContextMenu contextMenu=new ContextMenu();
        MenuItem item1=new MenuItem("Play");
        item1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.play();
                playPause.setGraphic(pauseimage);
            }
        });
        MenuItem item2=new MenuItem("pause");
        item2.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
             mp.pause();   
             playPause.setGraphic(playImage);
            }
        });
        MenuItem item3= new MenuItem("stop");
        item3.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.stop();
            }
        });
        MenuItem item4=new MenuItem("Quit");
        item4.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
               Platform.exit();
            }
        });
        SeparatorMenuItem separator1=new SeparatorMenuItem();
        SeparatorMenuItem separator2=new SeparatorMenuItem();
        
        Menu playback=new Menu("PlayBack");
        MenuItem item5=new MenuItem("Jump Start");
        item5.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.seek(mp.getStartTime());
            }
        });
        MenuItem item6=new MenuItem("Jump Last");
        item6.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.seek(mp.getStopTime());
            }
        });
        Menu speed= new Menu("Speed");
        MenuItem item7=new MenuItem("Faster");
        MenuItem item8=new MenuItem("Faster(fine)");
        MenuItem item9=new MenuItem("Normal");
        MenuItem item10=new MenuItem("Slower(fine)");
        MenuItem item11=new MenuItem("Slower");
item7.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(2);
}  
});
item8.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(1.5);
}  
});
item9.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(1);
}  
});
item10.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(0.75);
}  
});
item11.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
   mp.setRate(0.5);
}  
});
speed.getItems().addAll(item7,item8,item9,item10,item11);
playback.getItems().addAll(item5,item6,speed);
//for the playback in the menubar
        
        SeparatorMenuItem separator3=new SeparatorMenuItem();
        SeparatorMenuItem separator4=new SeparatorMenuItem();
        
        
        MenuItem item12=new MenuItem("Jump Start");
        item12.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.seek(mp.getStartTime());
            }
        });
        MenuItem item13=new MenuItem("Jump Last");
        item13.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                mp.seek(mp.getStopTime());
            }
        });
        Menu speed1= new Menu("Speed");
        MenuItem item14=new MenuItem("Faster");
        MenuItem item15=new MenuItem("Faster(fine)");
        MenuItem item16=new MenuItem("Normal");
        MenuItem item17=new MenuItem("Slower(fine)");
        MenuItem item18=new MenuItem("Slower");
item14.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(2);
}  
});
item14.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(1.5);
}  
});
item16.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(1);
}  
});
item17.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
    mp.setRate(0.75);
}  
});
item18.setOnAction(new EventHandler<ActionEvent>(){
  @Override
public void handle(ActionEvent event){
   mp.setRate(0.5);
}  
});
speed1.getItems().addAll(item14,item15,item16,item17,item18);
Playback.getItems().addAll(item12,item13,speed1);



contextMenu.getItems().addAll(item1,item2,item3,separator1,playback,separator2,item4);
        mv.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(mv,event.getScreenX(),event.getScreenY());
                
                
            }
        });
        mv.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        if(event.getClickCount()>0)
                        {
                            contextMenu.hide();
                        }
                    }
                });
        
        
quit.setOnAction(new EventHandler <ActionEvent>(){
    @Override
    public void handle(ActionEvent event){
        mp.stop();
        mp.dispose();
        Platform.exit();
    }
});
about.setOnAction(new EventHandler<ActionEvent>(){
    @Override
    public void handle(ActionEvent event){
        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setWidth(500);
        stage.setHeight(400);
        try{
        Parent new1=FXMLLoader.load(getClass().getResource("about.fxml"));
        Scene scene = new Scene(new1,400,200);
        stage.setScene(scene);
        
        stage.show();
    }catch(Exception e){}
    }
});


    }
    
 
    @FXML
    public void playPause(ActionEvent event) {
        
        if(path==null){
            intial();
             playPause.setGraphic(pauseimage);
        playPause.setStyle("-fx-background-color:transparent; -fx-background-radius: 100%;");
        playPause.setEffect(new DropShadow());
        }
        else{
        if (mp.getStatus()==Status.PAUSED) {
            mp.play();
            
            playPause.setGraphic(pauseimage);
            playPause.setStyle("-fx-background-color:transparent;-fx-background-radius: 100%;");

        } else {
            mp.pause();
            
            playImage.setStyle("-fx-background-radius:100%;");
            playPause.setGraphic(playImage);
            playPause.setStyle("-fx-background-color:transparent; -fx-background-radius: 100%;");

        }
    }}

    @FXML
    public void reload(ActionEvent even1t) {
        mp.seek(mp.getStartTime());
        reload.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/reload1.png"))));
        PauseTransition pause = new PauseTransition(
                Duration.seconds(0.5)
        );
        pause.setOnFinished(event -> {
            reload.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/reload.png"))));
        });
        pause.play();

    }

    @FXML
    public void back(ActionEvent event1) {
        mp.seek(mp.getCurrentTime().subtract(Duration.seconds(60)));
        back.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/start1.png"))));
        PauseTransition pause = new PauseTransition(
                Duration.seconds(0.5)
        );
        pause.setOnFinished(event -> {
            back.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/back.png"))));

        });
        pause.play();

    }

    @FXML
    public void forward() {

        mp.seek(mp.getCurrentTime().add(Duration.seconds(60)));
        forward.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/forward1.png"))));
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> {
            forward.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/forward.png"))));
        });
        pause.play();
    }

    @FXML
    public void stop() {
        mp.stop();
        stop.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/stop1.png"))));
        playPause.setGraphic(playImage);
        mp.dispose();
        path=null;
        runningTime.setText("--:--");
        TotalDuration.setText("--:--");
        timeSlider.setValue(0);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> {
            stop.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/stop.png"))));
        });
        pause.play();

    }

    @FXML
    public void sound(ActionEvent event) {
        if (mp.getVolume() != 0) {
            sound.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/nosound.png"))));
            mp.setVolume(0);
        } else {
            sound.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/sound.png"))));
            mp.setVolume(0.2);
        }

    }
  
    
    public static String formatTime(Duration timeElapsed,Duration duration){
        int intElapsed = (int)floor(timeElapsed.toSeconds());
        int elapsedHours = intElapsed/(60*60);
        if(elapsedHours>0)
        {
            intElapsed-=elapsedHours*60*60;
        }
        int elapsedMinutes = intElapsed/60;
        //the below should be taken cate of 
        int elapsedSeconds=intElapsed-elapsedMinutes*60;
        
        if(duration.greaterThan(Duration.ZERO))
        {
            int intDuration =(int)floor(duration.toSeconds());
            int durationHours=intDuration/(60*60);
            if(durationHours>0){
                intDuration-=durationHours*60*60;
            }
            int durationMinutes = intDuration/60;
            int durationSeconds=intDuration-durationHours*60*60-durationMinutes*60;
            if(durationHours>0){
                return String.format("%d:%02d:%02d",elapsedHours,elapsedMinutes,elapsedSeconds);
                
            }else {
return String.format("%02d:%02d",
elapsedMinutes, elapsedSeconds);
}
} else {
if (elapsedHours > 0) {
return String.format("%d:%02d:%02d", elapsedHours,
elapsedMinutes, elapsedSeconds);
} else {
return String.format("%02d:%02d", elapsedMinutes,
elapsedSeconds);
}
}
    }
    
    public void intial(){
        FileChooser fileChooser=new FileChooser();
         File file=fileChooser.showOpenDialog(mv.getScene().getWindow());
                path=file.getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
                 
                 mp=new MediaPlayer(me);
                 
       
        mv.setMediaPlayer(mp);
        mp.setVolume(0.5);
        mp.setAutoPlay(true);
        volumeSlider.setValue(mp.getVolume()*100);
        timeSlider.maxProperty().bind(Bindings.createDoubleBinding(()
                -> mp.getTotalDuration().toSeconds(), mp.totalDurationProperty()));
             mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            timeSlider.setValue(newValue.toSeconds());
            

        });
           mp.currentTimeProperty().addListener((Observable ov)->{
           TotalDuration.setText(formatTime(mp.getTotalDuration(),mp.getTotalDuration()));
           runningTime.setText(formatTime(mp.getCurrentTime(),mp.getTotalDuration()));
         //funtion to update the values of the labels
    });
    }
 

    
}

