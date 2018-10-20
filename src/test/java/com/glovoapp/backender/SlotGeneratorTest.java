package com.glovoapp.backender;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SlotGeneratorTest {
	 
    @Test
    public void slotTest() {

    	Order order1 = new Order();
    	order1.withId("order-d22b76a4cddc");
    	order1.withPickup(new Location(41.40440729233118, 2.1661962085744095));
    	Order order2 = new Order();
    	order2.withId("order-8eb9a38c63dd");
    	order2.withPickup(new Location(41.38790681247524, 2.1661962085744095));
    	Order order3 = new Order();
    	order3.withId("order-cbcccfc2ed3b");
    	order3.withPickup(new Location(41.40504741338881, 2.1727005988333596));
    	
    	List<Order> orderList = Arrays.asList(order1, order2, order3);
    	
    	Courier courier = new Courier();
    	courier.withLocation(new Location(41.39087699632735, 2.1784116992018028));
    	
    	GeneralProperties configuration = new GeneralProperties();
    	configuration.setSlotSize(0.5F);
    	
    	Map<Float, List<Order>> slotMap = new HashMap<>();
    	List<Order> orderListSlot1 = Arrays.asList(order1, order3);
    	List<Order> orderListSlot2 = Arrays.asList(order2);
    	slotMap.put(2.0F, orderListSlot1);
    	slotMap.put(1.5F, orderListSlot2);
    	
        assertEquals(slotMap, SlotGenerator.creatSlots(orderList, courier, configuration));
    }

}