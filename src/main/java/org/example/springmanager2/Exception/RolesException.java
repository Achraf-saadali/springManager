package org.example.springmanager2.Exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class RolesException extends BadCredentialsException
{


          public RolesException(String message)
          {
              super(message);
          }

          @Override
          public String toString()
          {
              return "message : "+super.getMessage() ;
          }
}
