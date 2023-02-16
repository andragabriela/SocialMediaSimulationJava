package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.service.MesajeService;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.RequestEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class MessageController implements Observer<MessageEntityChangeEvent> {
    public TableColumn<UserMessageDTO,String> fromFirstName;
    public TableColumn<UserMessageDTO,String> fromLastName;
    public TableColumn<UserMessageDTO,String> message;
    public TableColumn<UserMessageDTO,String> datacolumn;
    public TableColumn<UserMessageDTO,String> statusCoulmn;
    ServiceGUI serviceGUI;
    MesajeService mesajeService;
    @FXML
    private TableView<UserMessageDTO> tableMessages;
    private Stage stage;
    ObservableList<UserMessageDTO> model = FXCollections.observableArrayList();


    public void setService(MesajeService mesajeService,ServiceGUI serviceGUI)
    {
        this.serviceGUI=serviceGUI;
        this.mesajeService=mesajeService;
        mesajeService.addObserver(this);
        initModel();
    }

    private void initModel() {
        Iterable<Message> messages=mesajeService.getAllMessages();
        Predicate<Message> p= r-> r.getId().getSecond().equals(serviceGUI.getLoggedUser().getId());
        //Predicate<Message> p2= r-> r.getId().getFirst().equals(serviceGUI.getLoggedUser().getId());
        List<Message> allM= StreamSupport.stream(messages.spliterator(), false)
                .filter(p).collect(Collectors.toList());
    List<UserMessageDTO>allUR= allM.stream().map(x->new UserMessageDTO(mesajeService.getWithId(x.getId().getFirst()).getFirstName(),
            mesajeService.getWithId(x.getId().getFirst()).getLastName(),x.getMessage(),x.getData().format(DATE_TIME_FORMATTER),x.getStatus().toString())).collect(Collectors.toList());
       model.setAll(allUR);
    }

    @FXML
    public void initialize(){
       fromFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
       fromLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        datacolumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusCoulmn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableMessages.setItems(model);
    }

    @Override
    public void update(MessageEntityChangeEvent messageEntityChangeEvent) {
    initModel();
    }

    public void handleSeen(ActionEvent actionEvent) {
        UserMessageDTO um=tableMessages.getSelectionModel().getSelectedItem();
        if(!um.getStatus().equals("SENT")){
            MessageAlert.showErrorMessage(null,"Mesajul trebuie sa fie SENT!");
            return;
        }
        Long id1=serviceGUI.getLoggedUser().getId();
        Long id2=mesajeService.getWithName(um.getFirstName(),um.getLastName()).getId();
        Message toUpdate=new Message(id1,id2,um.getMessage(), LocalDateTime.parse(um.getDate(),DATE_TIME_FORMATTER),MessageStatus.SEEN);
        mesajeService.updateMessage(toUpdate);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "SEEN message!");

    }

    public void handleSend(ActionEvent actionEvent) throws IOException {
        Stage userStage= new Stage();
        userStage.setTitle("Send new message");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/addMessageView.fxml"));
        AnchorPane layout= loader.load();

        Scene scene= new Scene(layout);
        userStage.setScene(scene);
        AddMessageController userController= loader.getController();
        userController.setService(serviceGUI,mesajeService);

        userStage.show();

    }

    public void handleSentMessages(ActionEvent actionEvent) throws IOException {
        Stage userStage= new Stage();
        userStage.setTitle("Your messages");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/sentMessages.fxml"));
        AnchorPane layout= loader.load();

        Scene scene= new Scene(layout);
        userStage.setScene(scene);
        SentController userController= loader.getController();
        userController.setService(serviceGUI,mesajeService);

        userStage.show();
    }
}
