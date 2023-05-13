package lk.ijse.messenger.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lk.ijse.messenger.dto.Message;
import lk.ijse.messenger.dto.MessageDTO;
import lk.ijse.messenger.dto.User;
import lk.ijse.messenger.thread.Handler;

import java.io.IOException;

public class MesssageFormController {
    public AnchorPane scrollpane;
    public FlowPane flowPane;
    public VBox vBox;
    public JFXTextField txtMessage;
    public User user;
    public ComboBox cbUsers;


    public void txtMessageOnAction(ActionEvent actionEvent) {
        Text text = new Text("To("+cbUsers.getSelectionModel().getSelectedItem().toString()+") : "+txtMessage.getText());
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_RIGHT);
        TextFlow textFlow = new TextFlow(text);
        if(cbUsers.getSelectionModel().getSelectedItem().equals("All"))
        textFlow.setStyle("-fx-background-color: rgb(248,238,172); "+"fx-background-radius: 80px");
        if(!cbUsers.getSelectionModel().getSelectedItem().equals("All"))
            textFlow.setStyle("-fx-background-color: rgb(252,121,121); "+"fx-background-radius: 80px");
        textFlow.setPadding(new Insets(5,10,5,10));
        hb.getChildren().add(textFlow);
        hb.setPadding(new Insets(5,5,5,10));
        vBox.getChildren().add(hb);
        try {
            user.getHandler().sendMessage(new MessageDTO(txtMessage.getText(),cbUsers.getSelectionModel().getSelectedItem().toString()));
            txtMessage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void txtButtonOnAction(ActionEvent actionEvent) {
    }
    public void setUser(User user) {
        this.user = user;
        Handler handler= new Handler(user.getHandler().getClientSocket());
        handler.valueProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                if(newValue.getMsg()!=null){
                    Text text = new Text(newValue.getMsg());
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-background-color: rgb(152,243,221);"+"fx-background-radius:20px");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    HBox hb = new HBox();
                    hb.setPadding(new Insets(5,5,5,10));
                    hb.setAlignment(Pos.CENTER_LEFT);
                    hb.getChildren().add(textFlow);
                    vBox.getChildren().add(hb);
                }
                if(newValue.getPrivateMsg()!=null){
                    Text text = new Text(newValue.getPrivateMsg().getMsg());
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-background-color: rgb(252,121,121);"+"fx-background-radius:20px;"
                    +"-fx-text-fill: white");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    HBox hb = new HBox();
                    hb.setPadding(new Insets(5,5,5,10));
                    hb.setAlignment(Pos.CENTER_LEFT);
                    hb.getChildren().add(textFlow);
                    vBox.getChildren().add(hb);
                }
                if(newValue.getUsers()!=null){
                    String s;
                    try{
                        s = cbUsers.getSelectionModel().getSelectedItem().toString();
                    }catch (NullPointerException e){
                        s="All";
                    }
                    newValue.getUsers().remove(user.getName());
                    cbUsers.setItems(FXCollections.observableArrayList(newValue.getUsers()));
                    try{
                        cbUsers.getSelectionModel().select(s);
                    }catch (NullPointerException e){
                        cbUsers.getSelectionModel().select(null);
                    }

                }
                //Label lb = new Label(newValue.getMsg());

            }
        });
        Thread t1=new Thread(handler);
        t1.start();
    }
    public void closeConnection(){
        try {
            user.getHandler().getClientSocket().close();
        } catch (IOException e) {
        }
    }
}
