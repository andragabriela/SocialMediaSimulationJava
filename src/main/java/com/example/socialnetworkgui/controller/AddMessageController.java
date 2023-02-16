package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.MessageStatus;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.MesajeService;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AddMessageController implements Observer<MessageEntityChangeEvent> {


    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public TextField textFieldMessage;

    ServiceGUI serviceGUI;
    MesajeService mesajeService;

    public void setService(ServiceGUI serviceGUI, MesajeService mesajeService) {
        this.serviceGUI=serviceGUI;
        this.mesajeService=mesajeService;
        mesajeService.addObserver(this);
    }
    @FXML
    public void handleSave(ActionEvent actionEvent) {
        String firstName=textFieldFirstName.getText();
        String lastName=textFieldLastName.getText();
        String message=textFieldMessage.getText();
        User u=mesajeService.getWithName(firstName,lastName);
        this.mesajeService.sendMessage(serviceGUI.getLoggedUser().getId(),u.getId(),message);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Message sent!");

    }

    @Override
    public void update(MessageEntityChangeEvent messageEntityChangeEvent) {

    }
}
