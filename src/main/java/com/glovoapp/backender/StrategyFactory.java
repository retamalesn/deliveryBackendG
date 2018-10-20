package com.glovoapp.backender;

import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {
 
   public static FilterStrategy getStrategy(String name) {
	   FilterStrategy filterStrategy = null;
	   if(name.equals("filterWords")) {
		   filterStrategy = new FilterByWords();
	   }
	   if(name.equals("courierLimitDistance")) {
		   filterStrategy = new FilterByDistance();
	   }
	   return filterStrategy;
   }
 

   
}

