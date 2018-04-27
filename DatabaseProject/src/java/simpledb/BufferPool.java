package simpledb;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool checks that the transaction has the appropriate
 * locks to read/write the page.
 * 
 * @Threadsafe, all fields are final
 */
public class BufferPool {
    /** Bytes per page, including header. */
    public static final int PAGE_SIZE = 4096;

    private static int pageSize = PAGE_SIZE;
    
    /** Default number of pages passed to the constructor. This is used by
    other classes. BufferPool should use the numPages argument to the
    constructor instead. */
    public static final int DEFAULT_PAGES = 50;
    private static int pageNum=DEFAULT_PAGES;
    private static Map<Page,Vector<TranPerm>> pages= new ConcurrentHashMap<Page,Vector<TranPerm>>();
    private static Map<PageId,Page> pidMaps= new ConcurrentHashMap<PageId,Page>(); 
    private static Map<Page,Long> pageAccessMap= new ConcurrentHashMap<Page,Long>();
    
    private static class TranPerm{
    	
    	public TransactionId tid;
    	public Permissions perm;
    	
    	public TranPerm(TransactionId tid,Permissions perm){
    		this.tid=tid;
    		this.perm=perm;
    	}

    	public int hashCode(){
    		int result=1;
    		final int prime=31;
    		result=prime*result+(this.tid==null?0:this.tid.hashCode());
    		result=prime*result+(this.perm==null?0:this.perm.hashCode());
    		
    		return result;
    		
    		
    		
    	}
    	
    	
    	public boolean equals(Object obj){
    		if(obj==this)
    			return true;
    		if(obj==null)
    			return false;
    		if(!obj.getClass().isInstance(this))
    			return false;
    		
    		TranPerm o=(TranPerm)obj;
    		if(o.tid==null){
    			if(this.tid!=null)
    				return false;
    		}else if(!o.tid.equals(this.tid))
    			return false;
    		
    		if(o.perm==null){
    			if(this.perm!=null)
    				return false;
    		}else if(!o.perm.equals(this.perm))
    			return false;
    		
    		return true;
    		
    	}
    	
    	public String toString(){
    		
    		return tid.toString()+"\t"+perm.toString();
    		
    	}
    	
    }
    
    

    /**
     * Creates a BufferPool that caches up to numPages pages.
     *
     * @param numPages maximum number of pages in this buffer pool.
     */
    public BufferPool(int numPages) {
        pageNum=numPages;
    }
    
    public static int getPageSize() {
      return pageSize;
    }
    
    // THIS FUNCTION SHOULD ONLY BE USED FOR TESTING!!
    public static void setPageSize(int pageSize) {
    	BufferPool.pageSize = pageSize;
    }

    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, an page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public Page getPage(TransactionId tid, PageId pid, Permissions perm)
        throws TransactionAbortedException, DbException {
    	
    	int tableId=pid.getTableId();
		HeapFile file=(HeapFile)Database.getCatalog().getDatabaseFile(tableId);
		Page page=null;;
		synchronized(Boolean.class){
			int pNo=file.numPages();
			if(perm.equals(Permissions.READ_ONLY)&&pid.pageNumber()>=pNo)
				return null;
			//System.out.println("pageNo:"+pNo+",pid="+pid.pageNumber());
			if(pid.pageNumber()>pNo&&pNo>0){
				pid=new HeapPageId(pid.getTableId(),pNo-1);
			}
			
			page=pidMaps.get(pid);
			if(pages.size()>=pageNum){
				try{
					evictPage();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//read page from disk
			if(page==null){
				page=file.readPage(pid);
			}
			
			if(page==null){
				if(perm.equals(Permissions.READ_ONLY))
					return null;
				//if no such page on disk and try to write to the page create initial page
				try{
					page= new HeapPage((HeapPageId)pid, new byte[pageSize]);
					file.pageAdd();
				}catch(Exception e){
					e.printStackTrace();
						return null;
				}
			}

			Vector<TranPerm> l = pages.get(page);
			if(l==null)
				l= new Vector<TranPerm>();
			TranPerm lockTran=null;
			if (perm.equals(Permissions.READ_WRITE)) {
				for (TranPerm tp : l) {
					if (tp.perm.equals(Permissions.READ_WRITE)) {
						if (!tp.tid.equals(tid)){
							lockTran=tp;
							break;
						}
					}
				}
				
				final int wakeUp = 10,restTime=2000;
				int sleepCount = 0;
				while (l.contains(lockTran)) {
					try {
						Thread.sleep(restTime);
						sleepCount++;
					} catch (Exception e) {
						System.out.println("I cannot sleep, what a nightmare");
						e.printStackTrace();
					}
					if (sleepCount > wakeUp) {
						throw new DbException(
								"Cannot get the lock this time, as " + lockTran);
					}
				}
				
				
			}
			TranPerm tp=new TranPerm(tid,perm);
			if(l.contains(tp))
				System.out.println("--same transaction id try to lock the same page");
			else
				l.add(tp);
			Long time=System.currentTimeMillis();
			pageAccessMap.put(page, time);
			pidMaps.put(pid, page);
			pages.put(page,l);
			return page;
			
		}
		
    	
        
    }

    //get the oldest clean page
    private Page getOldestPage(){
    	
    	Page p=null;
    	long min=Long.MAX_VALUE;
    	for(Page page:pageAccessMap.keySet()){
    		long time=pageAccessMap.get(page);
    		if(page==null)
    			continue;
    		if(time<min){
    			min=time;
    			p=page;
    		}
    	}
    	
    	return p;
    }
    
    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param pid the ID of the page to unlock
     */
    public  void releasePage(TransactionId tid, PageId pid) {
    	
    	Page page=pidMaps.get(pid);
    	if(page==null)
    		return;
    	Vector<TranPerm> l=pages.get(page);
		Vector<TranPerm> tmp= new Vector<TranPerm>();
		for(TranPerm tp:l){
			if(tp.tid.equals(tid))
				tmp.add(tp);
		}
		for(TranPerm tp:tmp){
			synchronized(Integer.class){
				l.remove(tp);
			}
			
		}
		
		return;
    }

    /**
     * Release all locks associated with a given transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     */
    public void transactionComplete(TransactionId tid) throws IOException {
    	
    	transactionComplete(tid,true);
    	
       
       
       
    }

    /** Return true if the specified transaction has a lock on the specified page */
    public boolean holdsLock(TransactionId tid, PageId p) {
    	Page page=pidMaps.get(p);
    	Vector<TranPerm> list= pages.get(page);
		   for(TranPerm tp:list){
			   if(tp.tid.equals(tid))
				   return true;
		   } 
		   
		   return false;
  
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    
    public void transactionComplete(TransactionId tid, boolean commit)
        throws IOException {
    	
    	Map<Page,Vector<TranPerm>> tmpPages= new HashMap<Page,Vector<TranPerm>>();
    	
        for(Page p:pages.keySet()){
     	   Vector<TranPerm> l=pages.get(p);
     	   Vector<TranPerm> tmpl= new Vector<TranPerm>();
     	   for(TranPerm tp:l){
     		   if(tp.tid.equals(tid))
     			   tmpl.add(tp);
     	   }
     	   if(!tmpl.isEmpty())
     		   tmpPages.put(p,tmpl);
        }
        //if true, all dirty pages should be written to disk
        //else dirty pages associated with the transaction id should be reread from disk
        
        
        if(commit){
        	//flush all the dirty pages into disk
        	for(Page p:tmpPages.keySet()){
        		TransactionId dirtier=p.isDirty();
        		if(dirtier!=null){
        			flushPage(p.getId());
        		}
        		p.setBeforeImage();//no matter it is dirty or not, it needs to be set, LogTest.PatchTest
        	} 
        	//System.out.println(Thread.currentThread().getName()+" flush everything to disk");
        }else{
        	//read dirty pages from disk
        	Map<Page,Vector<TranPerm>> map= new HashMap<Page,Vector<TranPerm>>();
        	for(Page p:tmpPages.keySet()){
        		if(p.isDirty()!=null){
        			pages.remove(p);
        			try{
        				p=getPage(tid,p.getId(),Permissions.READ_ONLY);
        				TranPerm tp= new TranPerm(tid,Permissions.READ_ONLY);
        				Vector<TranPerm> tmpl=map.get(p);
        				if(tmpl==null){
        					tmpl= new Vector<TranPerm>();
        				}
        				tmpl.add(tp);
        				map.put(p,tmpl);
        			}catch(Exception e){
        				e.printStackTrace();
        			}
        		}
        	}
        	
        	for(Page p:map.keySet()){
        		if(p==null)
        			continue;
        		tmpPages.get(p).addAll(map.get(p));
        	}
        	
        }
        
      //common thing release the locks on the pages,update the lastAccess time
        for(Page p:tmpPages.keySet()){
        	Vector<TranPerm> tmpList=tmpPages.get(p);
        	Vector<TranPerm> l= pages.get(p);
        	//for aborted transaction, dirty pages are removed from pages
        	if(l==null)
        		continue;
        	for(TranPerm tp:tmpList)
        	synchronized(Integer.class){
        		l.remove(tp);
        	}	
        }
        
        
    }

    /**
     * Add a tuple to the specified table on behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to and any other 
     * pages that are updated (Lock acquisition is not needed for lab2). 
     * May block if the lock(s) cannot be acquired.
     * 
     * Marks any pages that were dirtied by the operation as dirty by calling
     * their markDirty bit, and updates cached versions of any pages that have 
     * been dirtied so that future requests see up-to-date pages. 
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public void insertTuple(TransactionId tid, int tableId, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
    	System.out.println("+++++++++++++recordId is "+t.getRecordId());
        PageId pid=t.getRecordId().getPageId();
        System.out.println(Thread.currentThread().getName()+"++++++pageNO:"+t.getRecordId().getPageId().pageNumber()+",tableId:"+t.getRecordId().getPageId().getTableId()+",tupleNo:"+t.getRecordId().tupleno()+",Tuple:"+t);
        HeapPage page=(HeapPage)getPage(tid,pid,Permissions.READ_WRITE);
        if(page==null)
        	System.out.println("I am null");
        page.insertTuple(t);
        page.markDirty(true, tid);
        
        
       
        
        
    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is removed from and any
     * other pages that are updated. May block if the lock(s) cannot be acquired.
     *
     * Marks any pages that were dirtied by the operation as dirty by calling
     * their markDirty bit, and updates cached versions of any pages that have 
     * been dirtied so that future requests see up-to-date pages. 
     *
     * @param tid the transaction deleting the tuple.
     * @param t the tuple to delete
     */
    public  void deleteTuple(TransactionId tid, Tuple t)
        throws DbException, IOException, TransactionAbortedException {
    	System.out.println(Thread.currentThread().getName()+"------pageNO:"+t.getRecordId().getPageId().pageNumber()+",tableId:"+t.getRecordId().getPageId().getTableId()+",tupleNo:"+t.getRecordId().tupleno()+",Tuple:"+t);
        PageId pid=t.getRecordId().getPageId();
        HeapPage page=(HeapPage)getPage(tid,pid,Permissions.READ_WRITE);
        page.deleteTuple(t);
        page.markDirty(true, tid);
    }

    /**
     * Flush all dirty pages to disk.
     * NB: Be careful using this routine -- it writes dirty data to disk so will
     *     break simpledb if running in NO STEAL mode.
     */
    public synchronized void flushAllPages() throws IOException {
  
    		 for(Page p:pages.keySet()){
    	        	if(p==null||p.isDirty()==null)
    	        		continue;
    	        	flushPage(p.getId());
    	        }
    	
       

    }

    /** Remove the specific page id from the buffer pool.
        Needed by the recovery manager to ensure that the
        buffer pool doesn't keep a rolled back page in its
        cache.
    */
    public synchronized void discardPage(PageId pid) {
            Page p=pidMaps.get(pid);
            if(p==null)
            	return;
            try{
            	//if(p.isDirty()!=null)
            		//flushPage(pid);
        		pages.remove(p);
        		pageAccessMap.remove(p);
        		pidMaps.remove(pid);
            }catch(Exception e){
            	e.printStackTrace();
            }

    }

    /**
     * Flushes a certain page to disk
     * @param pid an ID indicating the page to flush
     */
    private  synchronized void flushPage(PageId pid) throws IOException {

    		int tableId=pid.getTableId();
    		HeapFile hf=(HeapFile)Database.getCatalog().getDatabaseFile(tableId);
    		Page p=pidMaps.get(pid);
    		if(p==null)
				return;
    		TransactionId dirtier = p.isDirty();
			if(dirtier!=null){
				Database.getLogFile().logWrite(dirtier, p.getBeforeImage(), p);
		        Database.getLogFile().force();
				hf.writePage(p);
				p.markDirty(false, null);
				//as long as the page is written to the disk, it is not dirty as more in the memory
				//System.out.println(Thread.currentThread().getName()+" flush tableId:"+pid.getTableId()+","+pid.pageNumber()+" to disk");
			}
    	
    	
    }

    /** Write all pages of the specified transaction to disk.
     */
    public   void flushPages(TransactionId tid) throws IOException {
    		for(Page p:pages.keySet()){
    			if(p==null||p.isDirty()==null)
    				continue;
    			Vector<TranPerm> l=pages.get(p);
    			synchronized(Integer.class){
    				for(TranPerm tp:l){
                		if(tp.tid.equals(tid))
                			flushPage(p.getId());
                	}
    			}
            		
            }
    }

    /**
     * Discards a page from the buffer pool.
     * Flushes the page to disk to ensure dirty pages are updated on disk.
     */
	private void evictPage() throws DbException {
		synchronized (Boolean.class) {
			Page p = getOldestPage();
		try{
			if (p.isDirty() != null)
				flushPage(p.getId());
				//throw new DbException("Expected scan to run out of available buffer pages");
			// synchronized(Boolean.class){
			// System.out.println("p is");
			pages.remove(p);
			pageAccessMap.remove(p);
			pidMaps.remove(p.getId());
			// }
		}catch(Exception e){
			
		}
			

		}

	}

}
