package com.glovoapp.backender;

import java.util.List;

public interface FilterStrategy {
	
	List<Order> filter(List<Order> orderList, Courier courier, GeneralProperties configuration);

}
