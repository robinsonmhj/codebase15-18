package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }
        
        public int hashCode(){
        	int result=0;
        	result+=fieldType==null?0:fieldType.hashCode();
        	result+=fieldName==null?0:fieldName.hashCode();
        	return result;
        }
        
        public boolean equals(Object obj){
        	if(this==obj)
        		return true;
        	if(obj==null)
        		return false;
        	if(obj.getClass()!=this.getClass())
        		return false;
        	TDItem other=(TDItem)obj;
        	if(other.fieldName==null){
        		if(this.fieldName!=null)
        			return false;
        	}else if(!other.fieldName.equals(fieldName))
        		return false;
        	
        	if(other.fieldType==null){
        		if(this.fieldType!=null)
        			return false;
        	}else if(!this.fieldType.equals(fieldType))
        		return false;
        	
        	return true;
        	
        }

        public String toString() {
            return fieldType + "(" + fieldName + ")";
        }
    }

    private List<TDItem> tdItemList=new ArrayList<TDItem>();;
    
    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        return tdItemList.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
    	
        for(int i=0;i<typeAr.length;i++){
        	TDItem item= new TDItem(typeAr[i],fieldAr[i]);
        	tdItemList.add(item);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
    	this(typeAr,new String[typeAr.length]);
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return tdItemList.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
    	if(i>=0&&i<numFields())
    		return tdItemList.get(i).fieldName;
    	
    	throw new NoSuchElementException();
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
    	if(i>=0&&i<numFields())
    		return tdItemList.get(i).fieldType;
    	
    	throw new NoSuchElementException();
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
    	
      //System.out.println("=====prameter name is "+name);
       for(int i=0;i<numFields();i++){
    	   TDItem item=tdItemList.get(i);
    	   //System.out.println("=====TDItem is "+item);
    	   if(item.fieldName!=null&&item.fieldName.equals(name))
    		   return i;
    	   
       }
       
       int index=name.indexOf(".");
       if(index==-1)
    	   throw new NoSuchElementException();
       
       // the following code is used to fix the following bug
       //bug: filedname contains tablename or alias name, such as tableName.fieldName
       name=name.substring(index+1, name.length());
       for(int i=0;i<numFields();i++){
    	   TDItem item=tdItemList.get(i);
    	   //System.out.println("=====TDItem is "+item);
    	   if(item.fieldName!=null&&item.fieldName.equals(name))
    		   return i;
    	   
       }
       
       throw new NoSuchElementException();
       
       
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
    	int size=0;
        for(TDItem item:tdItemList)
        	size+=item.fieldType.getLen();
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
    	
    	int size1=td1.numFields();
    	int size2=td2.numFields();
    	int size=size1+size2;
    	Type[] typeArray= new Type[size];
    	String[] fileArray= new String[size];
    	int start=0;
    	for(TDItem item:td1.tdItemList){
    		typeArray[start]=item.fieldType;
    		start++;
    	}
    		
    	for(TDItem item:td2.tdItemList){
    		typeArray[start]=item.fieldType;
    		start++;
    	}
    		
    	
    	start=0;
    	for(TDItem item:td1.tdItemList){
    		fileArray[start]=item.fieldName;
    		start++;
    	}
    		
    	for(TDItem item:td2.tdItemList){
    		fileArray[start]=item.fieldName;
    		start++;
    	}
    	
    	return new TupleDesc(typeArray,fileArray);
    	
    	
    }

    @Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TupleDesc other = (TupleDesc) obj;
		if (tdItemList == null) {
			if (other.tdItemList != null)
				return false;
		} else if (!tdItemList.equals(other.tdItemList))
			return false;
		return true;
		
	}

    @Override
	public int hashCode() {
    	
    	/*
		final int prime = 31;
		int result = 1;
		
		
		result = prime * result
				+ ((tdItemList == null) ? 0 : tdItemList.hashCode());
		return result;
		*/
    	
    	int result=0;
    	Iterator<TDItem> iterator=iterator();
    	while(iterator.hasNext()){
    		int r=iterator.next().hashCode();
    		System.out.println(r);
    		result+=r;
    	}
    		
    	return result;
	}

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        StringBuilder sb= new StringBuilder();
        Iterator<TDItem> iterator=iterator();
        while(iterator.hasNext()){
        	TDItem item= iterator.next(); 
        	sb.append(item).append(",");
        }
        
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
        
    }
    
    public static void main(String[] args){
    	
    	
    	TupleDesc singleInt = new TupleDesc(new Type[]{Type.INT_TYPE});
        TupleDesc singleInt2 = new TupleDesc(new Type[]{Type.INT_TYPE});
        System.out.println("singleInt"+singleInt.hashCode());
        System.out.println("singleInt2"+singleInt2.hashCode());
        
        if(singleInt.equals(singleInt2))
        	System.out.println("Yes");
        else
        	System.out.println("No");
        
    }
    
}
