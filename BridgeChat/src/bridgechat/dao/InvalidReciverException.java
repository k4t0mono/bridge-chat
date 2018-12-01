package bridgechat.dao;

public class InvalidReciverException extends DaoException {

    public InvalidReciverException(String recived, String expected) {
        super("Expected `" + expected + "` but recived `" + recived + "`.");
    }
    
}
