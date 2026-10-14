package com.saveplate.api.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super("Aucun utilisateur trouvé avec ce emauk: "+email);
    }
}
