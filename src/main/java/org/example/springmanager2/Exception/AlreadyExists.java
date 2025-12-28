package org.example.springmanager2.Exception;

public class AlreadyExists extends Exception {
    public AlreadyExists(String message) {
        super(message);
    }
    @Override
    public String toString(){
        return "message : "+getMessage();
    }
}
