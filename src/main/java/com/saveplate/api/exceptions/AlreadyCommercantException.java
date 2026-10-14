package com.saveplate.api.exceptions;

public class AlreadyCommercantException extends RuntimeException{
public AlreadyCommercantException(String email){
    super("L'utilisateur " + email + " n'est pas un simple client, impossible de le promouvoir en commerçant");
}
}
