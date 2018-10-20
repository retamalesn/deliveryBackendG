package com.glovoapp.backender;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("configuration")
public class GeneralProperties {
	
	private String filterWords;
	private Double courierLimitDistance;
	private String priorities;
	private Float slotSize;
	
	public String getFilterWords() {
		return filterWords;
	}
	public void setFilterWords(String filterWords) {
		this.filterWords = filterWords;
	}
	public Double getCourierLimitDistance() {
		return courierLimitDistance;
	}
	public void setCourierLimitDistance(Double courierLimitDistance) {
		this.courierLimitDistance = courierLimitDistance;
	}
	public String getPriorities() {
		return priorities;
	}
	public void setPriorities(String priorities) {
		this.priorities = priorities;
	}
	public Float getSlotSize() {
		return slotSize;
	}
	public void setSlotSize(Float slotSize) {
		this.slotSize = slotSize;
	}
	
	
}
