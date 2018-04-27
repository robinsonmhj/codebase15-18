package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {
	
	
	
	private File file;
	private TupleDesc td;
	private int pageCount=0;

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
    public HeapFile(File f, TupleDesc td) {
        this.file=f;
        this.td=td;
        this.pageCount=pageNoInFile();
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
       return file.getAbsolutePath().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        return td;
    }

    // see DbFile.java for javadocs
    public  Page readPage(PageId pid) {
    	BufferedInputStream reader=null;
    	int len=BufferPool.getPageSize();
    	byte[] b= new byte[len];
    	try{
    		int offset=0,index=0,read=0;
    		Database.getBufferPool();
    		
    		int pageNo=pid.pageNumber();
    		
    		reader= new BufferedInputStream(new FileInputStream(file));
    		//System.out.println("====there are "+index+" pages");
    		while((read=reader.read(b, 0, len))!=-1){
    			if(index==pageNo)
    				return new HeapPage((HeapPageId)pid,b);
    			offset+=read;
    			index++;
    			//System.out.println("****read page***********I read "+read+" bytes");
    			//System.out.println("====there are "+index+" pages");
    		}
    		//pageAdd();
    		//return new HeapPage((HeapPageId)pid,new byte[len]);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		try{
    			reader.close();
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    		
    	}
       
    	return null;
       
    	
    }

    // see DbFile.java for javadocs
    public synchronized void writePage(Page page) throws IOException {
    	
    	PageId pid=page.getId();
    	BufferedInputStream reader=null;
    	BufferedOutputStream writer=null;
    	int random=(int)Math.random()*1000000;
		String tmpFileName="tmp"+random;
		String dir=file.getParent();
		if(dir==null){
			String fileName=file.getAbsolutePath();
			System.out.println("====db file path is "+fileName);
			dir=fileName.substring(0, fileName.lastIndexOf("\\"));
			
		}
			
		File tmpFile= new File(dir+"/"+tmpFileName);
    	try{
    		int offset=0,index=0,read=0;
    		Database.getBufferPool();
			int len=BufferPool.getPageSize();
    		byte[] b= new byte[len];
    		int pageNo=pid.pageNumber();
    		writer= new BufferedOutputStream(new FileOutputStream(tmpFile));
    		reader= new BufferedInputStream(new FileInputStream(file));
    		boolean empty=true;
    		while((read=reader.read(b, 0, len))!=-1){
    			//System.out.println("=======bytes"+b.toString());
    			if(index==pageNo){
    				writer.write(page.getPageData());
    			}else{
    				writer.write(b);
    			}
    			//System.out.println("****************I read "+read+" bytes");
    			empty=false;
    			offset+=read;
    			index++;
    		}
    		//the following 2 lines is used to write new page which doesn't exists in the file
    		if(empty||index<pageNo){//fix the bug that if the file is empty and pageNo is 0 robin systemtest.LogTest.TestFlushAll
    			writer.write(page.getPageData());
    			//System.out.println("I am before update pageCount:"+pageCount);
    			pageCount+=1;
    		}
    			
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		try{
    			writer.close();
    			reader.close();
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    	}
    	/*
    	String fname=file.getAbsolutePath();
    	long m=1000000;
    	System.out.println("====file name is "+fname);
    	System.out.println("file size after write the page,"+file.getTotalSpace()/m);
    	*/
    	file.delete();
    	tmpFile.renameTo(file);
    	//System.out.println("file size after write the page,"+file.getTotalSpace()/m);
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    
    private int pageNoInFile(){
    	
    	int num=0;
    	BufferedInputStream reader=null;
    	Database.getBufferPool();
		int len=BufferPool.getPageSize();
    	try{
    		reader= new BufferedInputStream(new FileInputStream(file));
    		byte[] bytePage=new byte[len];
    		while(reader.read(bytePage)!=-1){
    			num++;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		
    		try{
    			reader.close();
    			
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    	}
    	
    	return num;
    }
    public int numPages() {
    	
    	return pageCount;
        
    }
    
    public void pageAdd(){
    	pageCount++;
    }
    
    
    public  void pageMinus(){
    	pageCount--;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
    		//System.out.println(System.currentTimeMillis()+"I am in insertTuple");
    		ArrayList<Page> pageList= new ArrayList<Page>();
    		PageId pid=t.getRecordId().getPageId();
    		HeapPage p=(HeapPage)Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
    		//System.out.print("I am after p  ");
    		while(p.getNumEmptySlots()<1){
    			Database.getBufferPool().releasePage(tid, pid);
    			pid=new HeapPageId(pid.getTableId(),pid.pageNumber()+1);
    			p=(HeapPage)Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
    			//System.out.println("I am trying to get page");
    		}
    		//System.out.println("I get the page");
    		p.insertTuple(t);
    		//writePage(p);//
    		p.markDirty(true, tid);
    		pageList.add(p);
    		//System.out.println("insert 1 from heapFile insertTuple");
    		//System.out.println(System.currentTimeMillis()+"I am out of insertTuple");
    		return pageList;
    	
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
    	
    	ArrayList<Page> pageList= new ArrayList<Page>();
		PageId pid=t.getRecordId().getPageId();
		HeapPage p=(HeapPage)Database.getBufferPool().getPage(tid, pid, Permissions.READ_WRITE);
		Iterator<Tuple> i=p.iterator();
		while(i.hasNext()){
			Tuple tuple=i.next();
			if(tuple.equals(t)){
				pageList.add(p);
				i.remove();
				p.markDirty(true, tid);
			}		
		}
		
		return pageList;	
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
    	List<Tuple> l= new ArrayList<Tuple>();
    	BufferedInputStream reader=null;
    	Database.getBufferPool();
		int len=BufferPool.getPageSize();
		int tableId=getId();
		//int count=0;
    	try{
    		//reader= new BufferedInputStream(new FileInputStream(file));
    		//byte[] bytePage=new byte[len];
    		int pageNo=0;
    		while(pageNo<pageCount){
    			HeapPageId id= new HeapPageId(tableId,pageNo);
    			HeapPage p=(HeapPage)Database.getBufferPool().getPage(tid, id, Permissions.READ_ONLY);
    			if(p==null)
    				break;
    			Iterator<Tuple> iterator=p.iterator();
        		while(iterator.hasNext()){
        			//old implementation
        			Tuple t=iterator.next();
        			System.out.println(Thread.currentThread().getName()+"======pageNO:"+t.getRecordId().getPageId().pageNumber()+",tableId:"+t.getRecordId().getPageId().getTableId()+",tupleNo:"+t.getRecordId().tupleno()+",Tuple:"+t);
        			l.add(t);
        			//count++;
        		}
        		pageNo++;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		try{
    			//reader.close();
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    	}
    	
    	//new implementation, fixed for systemtest TransactionTest validate(2)
    	//Tuple t= new Tuple(new TupleDesc(new Type[]{Type.INT_TYPE}));
    	//t.setField(0, new IntField(count));
    	//l.add(t);
    	return new TupleIterator2(l);
   
    	
    	
    	
    }

}

