package com.example.socialnetworkgui.domain.validators;


import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message>{
    public void validate(Message entity) throws ValidationException {
        String errors="";
        if(entity.getMessage() == null || entity.getMessage().equals(""))
            errors+="Invalid Message";
        if(errors!="")
            throw new ValidationException(errors);
    }
}

