package simpledb;

import java.io.Serializable;

/**
 * A RecordId is a reference to a specific tuple on a specific page of a
 * specific table.
 */
public class RecordId implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private PageId pid;
    private int tupleNo;

    /**
     * Creates a new RecordId referring to the specified PageId and tuple
     * number.
     * 
     * @param pid
     *            the pageid of the page on which the tuple resides
     * @param tupleno
     *            the tuple number within the page.
     */
    public RecordId(PageId pid, int tupleno) {
        this.pid=pid;
        this.tupleNo=tupleno;
    }

    /**
     * @return the tuple number this RecordId references.
     */
    public int tupleno() {
       return tupleNo;
    }

    /**
     * @return the page id this RecordId references.
     */
    public PageId getPageId() {
       return pid;
    }



    /**
     * Two RecordId objects are considered equal if they represent the same
     * tuple.
     * 
     * @return True if this and o represent the same tuple
     */
    
    @Override
    public boolean equals(Object o) {
        if(this==o)
        	return true;
        if(o==null)
        	return false;
        if(!o.getClass().isInstance(this))
        	return false;
        RecordId other=(RecordId)o;
        if(pid==null){
        	if(other.pid!=null)
        		return false;
        }else if(!pid.equals(other.pid))
        	return false;
      
       if(other.tupleNo!=tupleNo)
        	return false;
        
    	return true;
       
    }

    /**
     * You should implement the hashCode() so that two equal RecordId instances
     * (with respect to equals()) have the same hashCode().
     * 
     * @return An int that is the same for equal RecordId objects.
     */
    
    @Override
    public int hashCode() {
        final int prime=31;
        int result=1;
        result=result*prime+(pid==null?0:pid.hashCode());
        result=result*prime+tupleNo;
        return result;

    }
    
    

}
