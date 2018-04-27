/**
 * 
 */
package com.multiplemedia.dao;

import java.util.List;


/**
 * @author Haojie Ma
 * @date Dec 11, 2015
 */

//reference
//http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
public interface GenericDao<E,K> {
	
	public void add(E entity) ;
    public void saveOrUpdate(E entity) ;
    public void update(E entity) ;
    public void remove(E entity);
    public E find(K key);
    public List<E> getAll() ;
}


