package com.example.socialnetworkgui.repo.db;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.MessageStatus;
import com.example.socialnetworkgui.domain.Pair;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repo.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class MessageDbRepository implements Repository<Pair<Long,Long>, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Message save(Message entity) {
        //if(entity.getId()==null) throw new IllegalArgumentException("ID cannot be null");
        //Message found=findOne(entity.getId());
        String sql="INSERT INTO mesaje(id1,id2,message,data,status) values (?,?,?,?,?) ";
        validator.validate(entity);
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement ps=connection.prepareStatement(sql)) {

               ps.setLong(1,entity.getId().getFirst());
               ps.setLong(2,entity.getId().getSecond());
               ps.setString(3,entity.getMessage());
               ps.setString(4,entity.getData().format(DATE_TIME_FORMATTER));
               ps.setString(5,entity.getStatus().toString());

                ps.executeUpdate();
        } catch (SQLException e) {
               e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Message delete(Pair<Long, Long> longLongPair) {
        if(longLongPair==null) throw new IllegalArgumentException("ID cannot be null!");
        Message found=findOne(longLongPair);
        if(found!=null){
            Long id1=found.getId().getFirst();
            Long id2=found.getId().getSecond();

            String sql="DELETE FROM mesaje where id1='"+id1.toString()+"'"+"AND id2='"+id2.toString()+"'";
            try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement ps=connection.prepareStatement(sql)){
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return found;

    }

    @Override
    public Message findOne(Pair<Long, Long> longLongPair) {
        for(Message m:findAll()){
            if(m.getId().equals(longLongPair)){
                return m;
            }
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> allM=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM mesaje");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Long id1 = rs.getLong("id1");
                Long id2 = rs.getLong("id2");
                String message=rs.getString("message");
                String dateString=rs.getString("data");
                LocalDateTime date=LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
                String status=rs.getString("status");
                MessageStatus st=null;
                if(status.equals("SENT")){
                    st=MessageStatus.SENT;
                }
                if(status.equals("SEEN")){
                    st=MessageStatus.SEEN;
                }
                if(status.equals("REPLIED")){
                    st=MessageStatus.REPLIED;
                }
                Message m=new Message(id1,id2,message,date,st);
                allM.add(m);
            }
            Set<Message> allM2 = new HashSet<>();
            allM2.addAll(allM);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allM;
    }
        @Override
    public Message update(Message entity) {
            if (entity.getId() == null) throw new IllegalArgumentException("ID cannot be null!");

            Message found=findOne(entity.getId());
            if(found!=null){
                validator.validate(entity);
                String Sql= "UPDATE mesaje SET status='"+entity.getStatus().toString()+"'"+
                        "WHERE id1='"+found.getId().getFirst().toString()+"'"+
                        "AND id2='"+found.getId().getSecond().toString()+"'";

                try(Connection connection=DriverManager.getConnection(url, username, password);
                    PreparedStatement ps= connection.prepareStatement(Sql)){
                    ps.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            return found;
        }

}
