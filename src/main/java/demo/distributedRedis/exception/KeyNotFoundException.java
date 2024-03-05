package demo.distributedRedis.exception;

public class KeyNotFoundException extends Exception {

    public KeyNotFoundException(){
        super("Key Not Found");
    }

}
