/**
 * 
 */
package com.multiplemedia.service;

import java.util.List;


/**
 * @author Haojie Ma
 * @date Dec 14, 2015
 */

public interface GenericService<E,K> {
	
	public void add(E entity) ;
    public void saveOrUpdate(E entity) ;
    public void update(E entity) ;
    public void remove(E entity);
    public E get(K key);
    public List<E> getAll() ;
}


