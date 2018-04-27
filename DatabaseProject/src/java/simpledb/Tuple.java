package simpledb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private TupleDesc td;
    private RecordId rid;
    private List<Field> fieldList= new ArrayList<Field>();

    /**
     * Create a new tuple with the specified schema (type).
     * 
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        this.td=td;
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        return td;
        //return null;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
       return rid;
    }

    /**
     * Set the RecordId information for this tuple.
     * 
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        this.rid=rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     * 
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
    	fieldList.add(i,f);
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     * 
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
       return fieldList.get(i);
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     * 
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     * 
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        StringBuilder sb= new StringBuilder();
        for(Field f:fieldList){
        	sb.append(f).append("\t");
        }
        
    	return sb.toString();
    }
    
    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        return fieldList.iterator();
    }
    
    /**
     * reset the TupleDesc of thi tuple
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        this.td=td;
    }
    
    
    public int hashCode(){
    	final int prime=31;
    	int result=1;
    	result=result*prime+(td==null?0:td.hashCode());
    	result=result*prime+(rid==null?0:rid.hashCode());
    	result=result*prime+(fieldList==null?0:fieldList.hashCode());
    	return result;
    
    	
    	
    }
    
    
    public boolean equals(Object o){
    	if(this==o)
    		return true;
    	if(o==null)
    		return false;
    	if(!o.getClass().isInstance(this))
    		return false;
    	Tuple other=(Tuple)o;
    	if(other.td==null){
    		if(this.td!=null)
    			return false;
    	}else if(!other.td.equals(this.td))
    		return false;
    	
    	if(other.rid==null){
    		if(this.rid!=null)
    			return false;
    	}else if(!other.rid.equals(this.rid))
    		return false;
    	
    	if(other.fieldList==null){
    		if(this.fieldList!=null)
    			return false;
    	}else if(!other.fieldList.equals(this.fieldList))
    		return false;
    	
    	return true;
    	
    	
    }
    
    
    
    
}
