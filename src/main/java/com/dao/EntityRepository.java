package com.dao;

public interface EntityRepository<T> {
	
	T find(long id);
	
	T persist(T entity);
	
	T update(T entity);
	
}
