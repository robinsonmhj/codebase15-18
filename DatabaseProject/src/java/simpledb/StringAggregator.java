package simpledb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;
    private int gbfield;
    private Type gbfieldtype;
    private int afield;
    private Op what;
    private Map<Object,List<String>> map= new HashMap<Object,List<String>>();
    private TupleDesc td;
    private List<Tuple> l= new ArrayList<Tuple>();

    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) throws IllegalArgumentException {
        this.gbfield=gbfield;
        this.gbfieldtype=gbfieldtype;
        this.afield=afield;
        this.what=what;
        if(what!=Op.COUNT)
        	throw new IllegalArgumentException("only count is supported");
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void mergeTupleIntoGroup(Tuple tup) {
    	td=tup.getTupleDesc();
    	//System.out.println("merge dpfield type:"+td.getFieldType(gbfield));
    	Object key=null;
    	if(gbfieldtype==Type.INT_TYPE){
    		IntField f1=(IntField)tup.getField(gbfield);
    		key=f1.getValue();
    	}else if(gbfieldtype==Type.STRING_TYPE){
    		StringField f1=(StringField)tup.getField(gbfield);
    		key=f1.getValue();
    	}
    		
    	StringField f2=(StringField)tup.getField(afield);
    	String value=f2.getValue();
    	List<String> sc=map.get(key);
    	if(sc==null)
    		sc= new ArrayList<String>();
    	sc.add(value);
    	map.put(key,sc);
    		
    }

    /**
     * Create a DbIterator over group aggregate results.
     *
     * @return a DbIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public DbIterator iterator(){
    	TupleDesc newTd=null;
    	Type[] types=null;
    	String[] fieldName=null;		
    	if(gbfieldtype==null){
    		types= new Type[]{td.getFieldType(afield)};
    		fieldName= new String[]{td.getFieldName(afield)};
    	}else{
    		types= new Type[]{td.getFieldType(gbfield),td.getFieldType(afield)};
        	fieldName= new String[]{td.getFieldName(gbfield),td.getFieldName(afield)};
    	}
    	newTd= new TupleDesc(types,fieldName);
    	for(Object key:map.keySet()){
        	List<String> sc=map.get(key);
        	Object field1=key;
        	int field2=0;
        	switch(what){
        	case MIN:{
        		/*field2=sc.get(0);
        		for(int i=1;i<sc.size();i++){
        			if(field2.compareTo(sc.get(i))<0)
        				field2=sc.get(i);	
        		}*/
        	};break;
        	case MAX:{
        		/*
        		 * field2=sc.get(0);
        		for(int i=1;i<sc.size();i++){
        			if(field2.compareTo(sc.get(i))>0)
        				field2=sc.get(i);	
        		}*/
        	};break;
        	case SUM:{
        		System.out.println("Doesn't support sum aggression for string");
        	};break;
        	case AVG:{
        		System.out.println("Doesn't support avg aggression for string");
        		
        	};break;
        	case COUNT:{
        		field2=sc.size();
        		
        	};break;
        	case SUM_COUNT:break;
        	case SC_AVG:break;
        	}
        	
        	Tuple t= new Tuple(newTd);
    		if(gbfieldtype==null){
    			t.setField(0, new IntField(field2));
    		}else{
    			if(gbfieldtype==Type.INT_TYPE)
    				t.setField(0, new IntField((int)field1));
    			else
    				t.setField(0, new StringField((String)field1,100));
    			t.setField(1, new IntField(field2));
    		}
    		l.add(t);
        }
        TupleIterator iterator= new TupleIterator(newTd,l);
        return iterator;
    
    }

}
