package com.example.socialnetworkgui.service;


import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.repo.Repository;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MesajeService implements Observable<MessageEntityChangeEvent> {

    private Repository<Pair<Long,Long>, Message> mesajeRepo;
    private Repository<Long, User> userRepo;
    private Repository<Pair<Long, Long>, Friendship> friendshipRepo;


    private List<Observer<MessageEntityChangeEvent>> observers= new ArrayList<>();

    public MesajeService(Repository<Pair<Long, Long>, Message> mesajeRepo, Repository<Long, User> userRepo, Repository<Pair<Long, Long>, Friendship> friendshipRepo) {
        this.mesajeRepo = mesajeRepo;
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    @Override
    public void addObserver(Observer<MessageEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageEntityChangeEvent> e) {
    }

    @Override
    public void notifyObservers(MessageEntityChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public Iterable<Message> getAllMessages(){return  mesajeRepo.findAll();}

    public void sendMessage(Long id1, Long id2, String message){
        Message m= new Message(id1, id2,message,LocalDateTime.now(), MessageStatus.SENT);
        Message added=mesajeRepo.save(m);
        if(added==null){
            if(userRepo.findOne(id1)==null){
                throw new EntityNotFound("Userul 1 nu exista!");
            }
            if(userRepo.findOne(id2)==null){
                throw new EntityNotFound("Userul 2 nu exista!");
            }
        }
        notifyObservers(new MessageEntityChangeEvent(ChangeEventType.ADD, added));
    }
    public Message getWithMesajOra(String mesaj, String ora) {
        for (Message u : mesajeRepo.findAll()) {
            if (u.getMessage().equals(mesaj) && u.getData().equals(ora)) return u;
        }
        return null;
    }
    public void deleteMessage(Long id1, Long id2){
        //Message m=getWithMesajOra(mesaj, ora);
        Message deleted= mesajeRepo.delete(new Pair<>(id1,id2));
        if(deleted==null){
            throw new EntityNotFound("Mesajul nu exista!");
        }
        else{
            notifyObservers(new MessageEntityChangeEvent(ChangeEventType.DELETE, deleted));
        }
    }

    public void updateMessage(Message m){
        Message ret= mesajeRepo.update(m);
        if(ret==null) throw new EntityNotFound("Message not found!");
        else
            notifyObservers(new MessageEntityChangeEvent(ChangeEventType.UPDATE, ret));
    }
    /**
     * Returns user with given id (id from requests table)
     * @param id
     * @return
     */
    public User getWithId(Long id){
        for(User u: userRepo.findAll()){
            if(u.getId().equals(id)) return u;
        }
        return null;
    }

    public User getWithName(String fName, String lName){
        for (User u: userRepo.findAll()){
            if(u.getFirstName().equals(fName)&&u.getLastName().equals(lName)) return u;
        }
        return null;
    }

}
