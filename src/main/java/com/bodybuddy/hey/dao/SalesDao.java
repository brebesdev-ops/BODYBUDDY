package com.bodybuddy.hey.dao;

import java.util.List;

import com.bodybuddy.hey.bean.Sales;

public interface SalesDao {

	List<Sales> getSalesHistory(String id);

}