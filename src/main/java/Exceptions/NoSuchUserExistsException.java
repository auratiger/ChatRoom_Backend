package Exceptions;

public class NoSuchUserExistsException extends RuntimeException{
    public NoSuchUserExistsException(){
        super();
    }

    public NoSuchUserExistsException(String e){
        super(e);
    }


}
