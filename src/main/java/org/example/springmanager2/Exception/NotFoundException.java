package org.example.springmanager2.Exception;

public class NotFoundException  extends Exception
{



    public NotFoundException(String message)
    {
        super(message);


    }
    @Override
    public String toString()
    {
        return "message : "+super.getMessage() ;
    }
}
