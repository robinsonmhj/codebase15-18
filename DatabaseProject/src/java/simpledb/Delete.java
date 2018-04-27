package simpledb;



/**
 * The delete operator. Delete reads tuples from its child operator and removes
 * them from the table they belong to.
 */
public class Delete extends Operator {

    private static final long serialVersionUID = 1L;
    private TransactionId tid;
    private DbIterator child;
    private Tuple  result= new Tuple(new TupleDesc(new Type[]{Type.INT_TYPE}));

    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     * 
     * @param t
     *            The transaction this delete runs in
     * @param child
     *            The child operator from which to read tuples for deletion
     */
    public Delete(TransactionId tid, DbIterator child) {
        this.tid=tid;
        this.child=child;
    }

    public TupleDesc getTupleDesc() {
        return result.getTupleDesc();
    }

    public void open() throws DbException, TransactionAbortedException {
    	super.open();
        child.open();
        int count=0;
        while(child.hasNext()){
        	Tuple t=child.next();
        	try{
        		Database.getBufferPool().deleteTuple(tid, t);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	count++;
        	result.setField(0, new IntField(count));
        }
    }

    public void close() {
        child.close();
        super.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        close();
        open();
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be accessed via the
     * Database.getBufferPool() method.
     * 
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        return result;
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
