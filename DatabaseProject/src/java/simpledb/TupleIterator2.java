package simpledb;
import java.util.*;

/**
 * DbFileIterator is the iterator interface that all SimpleDB Dbfile should
 * implement.
 */
public class TupleIterator2 implements DbFileIterator{
	
	private List<Tuple> list;
	private Iterator<Tuple> i;
	private int index=0;
	private boolean open;
	
	public TupleIterator2(List<Tuple> list){
		this.list=list;
		
	}
	
	
    /**
     * Opens the iterator
     * @throws DbException when there are problems opening/accessing the database.
     */
    public void open() throws DbException, TransactionAbortedException{
    	//open=true;
    	i=list.iterator();
    	
    }
        

    /** @return true if there are more tuples available. */
    public boolean hasNext() throws DbException, TransactionAbortedException{
    	
    	/*if(!open)
    		return false;
    	
    	if(index<list.size())
    		return true;
    	
    	return false;*/
    	
    	return i.hasNext();
    	
    }
       

    /**
     * Gets the next tuple from the operator (typically implementing by reading
     * from a child operator or an access method).
     *
     * @return The next tuple in the iterator.
     * @throws NoSuchElementException if there are no more tuples
     */
    public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException{
    	/*if(!open)
    		throw new NoSuchElementException();
    	Tuple t=list.get(index);
    	index++;
    	return t;*/
    	return i.next();
    }
    
       

    /**
     * Resets the iterator to the start.
     * @throws DbException When rewind is unsupported.
     */
    public void rewind() throws DbException, TransactionAbortedException{
    	close();
    	open();
    }

    /**
     * Closes the iterator.
     */
    public void close(){
    	i=null;
    }
}
