package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@ComponentScan("com.glovoapp.backender")
@EnableAutoConfiguration
class API {
    private final String welcomeMessage;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    
    @Autowired
	private GeneralProperties configuration;
    
    Map<Float, List<Order>> slotMap = new HashMap<>();
	
    @Autowired
    API(@Value("${backender.welcome_message}") String welcomeMessage, OrderRepository orderRepository,
    		CourierRepository courierRepository) {
        this.welcomeMessage = welcomeMessage;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @RequestMapping("/")
    @ResponseBody
    String root() {
        return welcomeMessage;
    }

    @RequestMapping("/orders")
    @ResponseBody
    List<OrderVM> orders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderVM(order.getId(), order.getDescription()))
                .collect(Collectors.toList());
    }
    
    @RequestMapping(value="/orders/{courierId}", method=RequestMethod.GET)
    @ResponseBody
    List<OrderVM> ordersByCourierId(@PathVariable String courierId) {
    	Courier courier = courierRepository.findById(courierId);
    	List<Order> orderList = orderRepository.findAll();
    	if(!configuration.getFilterWords().isEmpty()) {
    		FilterStrategy fs =  StrategyFactory.getStrategy("filterWords");
    		orderList = fs.filter(orderList, courier, configuration);
    	}
    	if(!configuration.getCourierLimitDistance().isNaN()) {
    		FilterStrategy fs =  StrategyFactory.getStrategy("courierLimitDistance");
    		orderList = fs.filter(orderList, courier, configuration);
    	}
    	//Create Slots
    	slotMap = SlotGenerator.creatSlots(orderList, courier, configuration);
    	//Time to sort!
    	slotMap.entrySet().stream().forEach(l -> {
    		l.setValue(this.sortByStrategies(l.getValue()));
    	});
    	orderList = slotMap.values()
    	         .stream()
    	         .flatMap(listContainer -> listContainer.stream())
    	         .collect(Collectors.toList());
    	orderList.clear();
    	generetaFinalList(orderList);
        return orderList
                .stream()
                .map(order -> new OrderVM(order.getId(), order.getDescription()))
                .collect(Collectors.toList());
    }
    
    
    int i = 1;
    private void generetaFinalList(List<Order> orderList) {
    	
    	if(slotMap.get(configuration.getSlotSize()*i) != null) {
			orderList.addAll(slotMap.get(configuration.getSlotSize()*i));
			i++;
			generetaFinalList(orderList);
		}
    	
    }
    
    //This Method should be put into other class, maybe a new Strategy. Similar to FilterStrategy.
    private List<Order> sortByStrategies(List<Order> orderList) {
    	if (!configuration.getPriorities().isEmpty()) {
			List<String> strategiesList = Arrays.asList(configuration.getPriorities().split(","));
			for (String s : strategiesList) {
				if(s.toLowerCase().equals("vip")) {
					Comparator<Order> c = (Order o1, Order o2) -> {
						if (o1.getVip() == o2.getVip()) {
							return 0;
						}
						return o1.getVip() ? -1 : 1;
					};
					orderList.sort(c);
				}else if(s.toLowerCase().equals("food")) {
					Comparator<Order> c = (Order o1, Order o2) -> {
						if (o1.getFood() == o2.getFood()) {
							return 0;
						}
						return o1.getFood() ? -1 : 1;
					};
					orderList.sort(c);
				}else if(s.toLowerCase().equals("distance")) {
					Comparator<Order> c = (Order o1, Order o2) -> {
						double o1Distance = DistanceCalculator.calculateDistance(o1.getPickup(), o1.getDelivery());
						double o2Distance = DistanceCalculator.calculateDistance(o2.getPickup(), o2.getDelivery());
						if (o1Distance == o2Distance) {
							return 0;
						}
						return o1Distance < o2Distance ? -1 : 1;
					};
					orderList.sort(c);
				}
				}
			}
    	return orderList;
    }

    public static void main(String[] args) {
        SpringApplication.run(API.class);
    }
}
