package lk.ijse.messenger.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import lk.ijse.messenger.dto.Message;
import lk.ijse.messenger.dto.User;
import lk.ijse.messenger.thread.Handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;

public class LoginFormController {
    public PasswordField txtPassword;
    public TextField txtName;
    public AnchorPane loginFormContext;
    private Socket clientSocket;
    private Thread t1;
    public void initialize() {
        connectWithServer();
        setDataReceiver();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(txtName.getText() + '\n');
            txtName.setEditable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connectWithServer(){
        while (true){
            try {
                String hostName = InetAddress.getLocalHost().getHostAddress();
                String replace = hostName.replace(".", " ");
                String[] split = replace.split(" ");
                System.out.println(hostName);
                System.out.println(Arrays.toString(split));
                String temp=split[0]+"."+split[1]+"."+split[2]+".";
                for(int i=0;i<255;i++) {
                    try {
                        clientSocket = new Socket();
                        InetSocketAddress isa=new InetSocketAddress(temp + i, 9001);
                        clientSocket.connect(isa,5);
                        new Alert(Alert.AlertType.INFORMATION, "Connect With "+isa.getHostName()).showAndWait();
                        return true;
                    } catch (Exception e) {
                        System.out.println("Failed : " + temp + i);
                    }
                }
                new Alert(Alert.AlertType.ERROR, "Connect With Server Error").showAndWait();
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setDataReceiver(){
        Handler handler = new Handler(clientSocket);
        handler.valueProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                System.out.println(newValue.getMsg());
                System.out.println(newValue.getUsers());
                if(newValue.getMsg()==null)return;
                String[] s = newValue.getMsg().split(" ");
                switch (s[0]){
                    case "Success": onSuccess(handler);handler.valueProperty().removeListener(this);break;
                    case "Duplicate": new Alert(Alert.AlertType.ERROR,newValue.getMsg()).show();
                    txtName.setEditable(true);break;
                }
            }
        });
        t1 =new Thread(handler);
        t1.start();
    }
    public void onSuccess(Handler handler){
        t1.stop();
        handler.cancel();
        new Alert(Alert.AlertType.INFORMATION,"Accoount Create Success").show();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/MassageForm.fxml"));
        try {
            Parent load = loader.load();
            MesssageFormController controller = loader.getController();
            controller.setUser(new User(handler,txtName.getText()));
            Stage window = (Stage) loginFormContext.getScene().getWindow();
            window.setTitle(txtName.getText());
            window.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    controller.closeConnection();
                }
            });
            window.setScene(new Scene(load));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
