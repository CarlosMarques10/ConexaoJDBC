package db;

public class DbIntegrityExcpetion extends RuntimeException{
    
    public DbIntegrityExcpetion(String msg){
        super(msg);
    }
    
}
