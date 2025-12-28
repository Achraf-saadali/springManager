package org.example.springmanager2.Exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends BadCredentialsException
{



    public InvalidTokenException(String message)
    {
        super(message);


    }
    @Override
    public String toString()
    {
        return "message : "+super.getMessage() ;
    }
}
