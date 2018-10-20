package com.glovoapp.backender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class FilterByWords implements FilterStrategy{

	@Override
	public List<Order> filter(List<Order> orderList, Courier courier, GeneralProperties configuration) {
		List<Order> result = new ArrayList<>();
		if(!configuration.getFilterWords().isEmpty()) {
			List<String> wordFilters = Arrays.asList(configuration.getFilterWords().split(","));
			for (String s : wordFilters) {
				result.addAll(orderList.stream()
						.filter(o -> (o.getDescription().toLowerCase().contains(s.toLowerCase()) && courier.getBox())
								|| (!o.getDescription().toLowerCase().contains(s.toLowerCase())))
						.collect(Collectors.toList()));
			}
		}
		return result;
	}

}
