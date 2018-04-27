package simpledb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simpledb.TestUtil.SkeletonFile;
import simpledb.systemtest.SystemTestUtil;
import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;

public class HeapFileWriteTest extends TestUtil.CreateHeapFile {
    private TransactionId tid;

    /**
     * Set up initial resources for each unit test.
     */
   // String tableName=SystemTestUtil.getUUID();
    @Before public void setUp() throws Exception {
        super.setUp();
        tid = new TransactionId();
        //File temp = File.createTempFile("table", ".dat");
        //temp.deleteOnExit();
        //ArrayList<ArrayList<Integer>> table = new ArrayList<ArrayList<Integer>>();
        //HeapFileEncoder.convert(table, temp, BufferPool.PAGE_SIZE, 2);
       
        //System.out.println("---tableName to be inserted="+tableName);
       // Database.getCatalog().addTable(new HeapFile(temp, Utility.getTupleDesc(2)), tableName);
        /*
        Iterator<Integer> i=Database.getCatalog().tableIdIterator();
        while(i.hasNext()){
        	int id=i.next();
        	System.out.println("=========tableId "+id);
        	String name=Database.getCatalog().getTableName(id);
        	System.out.println("=========tableName "+name);
        }
        */	
    }

  
    
    @After public void tearDown() throws Exception {
        Database.getBufferPool().transactionComplete(tid);
    }

    /**
     * Unit test for HeapFile.addTuple()
     */
    @Test public void addTuple() throws Exception {
        // we should be able to add 504 tuples on an empty page.
    	int tableId=empty.getId();
        for (int i = 0; i < 504; ++i) {
        	Tuple t=Utility.getHeapTuple(i, 2,tableId,0);
        	//System.out.println("--------------the tuple to be inserted is "+t);
            empty.insertTuple(tid, t);
            assertEquals(1, empty.numPages());
        }

        // the next 512 additions should live on a new page
        for (int i = 0; i < 504; ++i) {
            empty.insertTuple(tid, Utility.getHeapTuple(i, 2,tableId,1));
            assertEquals(2, empty.numPages());
        }

        // and one more, just for fun...
        empty.insertTuple(tid, Utility.getHeapTuple(0, 2,tableId,2));
        assertEquals(3, empty.numPages());
    }

    /**
     * JUnit suite target
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HeapFileWriteTest.class);
    }
}

