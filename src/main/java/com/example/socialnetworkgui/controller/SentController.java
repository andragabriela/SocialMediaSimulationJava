package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserMessageDTO;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.service.MesajeService;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class SentController implements Observer<MessageEntityChangeEvent> {

    ServiceGUI serviceGUI;
    MesajeService mesajeService;
    public TableView<UserMessageDTO> tableMessages;
    public TableColumn<UserMessageDTO,String> fromFirstName;
    public TableColumn<UserMessageDTO,String> fromLastName;
    public TableColumn<UserMessageDTO,String> message;
    public TableColumn<UserMessageDTO,String> datacolumn;
    public TableColumn<UserMessageDTO,String> statusCoulmn;
    ObservableList<UserMessageDTO> model = FXCollections.observableArrayList();



    public void setService(ServiceGUI serviceGUI, MesajeService mesajeService) {
        this.serviceGUI=serviceGUI;
        this.mesajeService=mesajeService;
        mesajeService.addObserver(this);
        initModel();
    }

    private void initModel() {
        Iterable<Message> messages=mesajeService.getAllMessages();
        Predicate<Message> p= r-> r.getId().getFirst().equals(serviceGUI.getLoggedUser().getId());
        //Predicate<Message> p2= r-> r.getId().getFirst().equals(serviceGUI.getLoggedUser().getId());
        List<Message> allM= StreamSupport.stream(messages.spliterator(), false)
                .filter(p).collect(Collectors.toList());
        List<UserMessageDTO>allUR= allM.stream().map(x->new UserMessageDTO(mesajeService.getWithId(x.getId().getSecond()).getFirstName(),
                mesajeService.getWithId(x.getId().getSecond()).getLastName(),x.getMessage(),x.getData().format(DATE_TIME_FORMATTER),x.getStatus().toString())).collect(Collectors.toList());
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

    public void handleDelete(ActionEvent actionEvent) {
        try{
            Long id1= serviceGUI.getLoggedUser().getId();
            UserMessageDTO selected= tableMessages.getSelectionModel().getSelectedItem();
            String fn= selected.getFirstName();
            String ln= selected.getLastName();
            User u=mesajeService.getWithName(fn,ln);
            String mesaj=selected.getMessage();
            String ora=selected.getDate();
            Long id2=u.getId();
            mesajeService.deleteMessage(id1,id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Deleted message!");
        }catch (EntityNotFound | NullPointerException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
