package com.glovoapp.backender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByDistance  implements FilterStrategy{

	@Override
	public List<Order> filter(List<Order> orderList, Courier courier, GeneralProperties configuration) {
		List<Order> resultFilter = new ArrayList<>();
    	resultFilter.addAll(orderList.stream().filter(
			o -> (DistanceCalculator.calculateDistance(courier.getLocation(), o.getPickup()) > configuration.getCourierLimitDistance()
						&& (courier.getVehicle().equals(Vehicle.MOTORCYCLE) || courier.getVehicle().equals(Vehicle.ELECTRIC_SCOOTER)))
				|| DistanceCalculator.calculateDistance(courier.getLocation(), o.getPickup()) < configuration.getCourierLimitDistance())
				.collect(Collectors.toList()));
    	return resultFilter;
	}

}
