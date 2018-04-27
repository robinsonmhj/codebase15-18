package simpledb;



/**
 * Inserts tuples read from the child operator into the tableid specified in the
 * constructor
 */
public class Insert extends Operator {

    private static final long serialVersionUID = 1L;
    private TransactionId tid;
    private DbIterator child;
    private int tableid;
   // private Tuple  result= new Tuple(new TupleDesc(new Type[]{Type.INT_TYPE}));
   // private TupleIterator i;

    /**
     * Constructor.
     * 
     * @param t
     *            The transaction running the insert.
     * @param child
     *            The child operator from which to read tuples to be inserted.
     * @param tableid
     *            The table in which to insert tuples.
     * @throws DbException
     *             if TupleDesc of child differs from table into which we are to
     *             insert.
     */
    public Insert(TransactionId tid,DbIterator child, int tableid)
            throws DbException {
        this.tid=tid;
        this.child=child;
        this.tableid=tableid;
    }

    public TupleDesc getTupleDesc() {
        return child.getTupleDesc();
    }

    public void open() throws DbException, TransactionAbortedException {
    	super.open();
        child.open();
    }

    public void close() {
       super.close();
       child.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        close();
        open();
    }

    /**
     * Inserts tuples read from child into the tableid specified by the
     * constructor. It returns a one field tuple containing the number of
     * inserted records. Inserts should be passed through BufferPool. An
     * instances of BufferPool is available via Database.getBufferPool(). Note
     * that insert DOES NOT need check to see if a particular tuple is a
     * duplicate before inserting it.
     * 
     * @return A 1-field tuple containing the number of inserted records, or
     *         null if called more than once.
     * @see Database#getBufferPool
     * @see BufferPool#insertTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
    	
        Tuple  result= null;
        int count=0;
        while(child.hasNext()){
        	Tuple t=child.next();
        	try{
        		Database.getBufferPool().insertTuple(tid, tableid, t);
        		result= new Tuple(new TupleDesc(new Type[]{Type.INT_TYPE}));
        		result.setField(0, new IntField(++count));
        		return result;
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        }
        
        return null;
    }

    @Override
    public DbIterator[] getChildren() {
       return new DbIterator[]{child};
    }

    @Override
    public void setChildren(DbIterator[] children) {
        child=children[0];
    }
}
