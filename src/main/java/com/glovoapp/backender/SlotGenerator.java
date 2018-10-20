package com.glovoapp.backender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotGenerator {


	private static Map<Float, List<Order>> slotMap = new HashMap<>();
	
	public static Map<Float, List<Order>> creatSlots(List<Order> orderList, Courier courier, GeneralProperties configuration) {
		    	
    	orderList.stream().forEach(o -> {    		
    		creatSlots(configuration.getSlotSize(), o, courier, configuration);
    	});
	
    	return slotMap;
    }
	
	private static void creatSlots(Float currentSlot, Order o, Courier c, GeneralProperties configuration) {
    	Float slotSize = configuration.getSlotSize();
    
    	if(currentSlot - DistanceCalculator.calculateDistance(c.getLocation(), o.getPickup()) < 0) {
    		creatSlots(currentSlot + slotSize, o, c, configuration);
    		return;
    	}
    	if(slotMap.get(currentSlot) != null){
    		if(!slotMap.get(currentSlot).contains(o)) {
    			slotMap.get(currentSlot).add(o);
    		}
    	}else {
    		List<Order> slotList = new ArrayList<Order>();
    		slotList.add(o);
    		slotMap.put(currentSlot, slotList);
    	}    	
    }
}
