package com.in28minutes.microservices.currencyconversionservice.resources;

import java.math.BigDecimal;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.microservices.currencyconversionservice.resources.model.CurrencyConversionBean;
import com.in28minutes.microservices.currencyconversionservice.CurrencyExchangeServiceProxy;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	@GetMapping
	public CurrencyConversionBean getAmountAfterConversion(@RequestParam String from, @RequestParam String to, @RequestParam int quantity) {		
//		CurrencyConversionBean bean = new CurrencyConversionBean(1,"USD","INR",new BigDecimal(65.00D), 50000, new BigDecimal(1000000), 8100);
		String link = new String("http://localhost:8000/currency-exchange-rate?from="+from+"&to="+to);
		CurrencyConversionBean bean = new RestTemplate().getForObject(link, CurrencyConversionBean.class);
		bean.setTotalCalculatedAmount(bean.getConversionMultiple().multiply(new BigDecimal(quantity)));
		bean.setQuantity(quantity);
		return bean;
	}
	
	@GetMapping("/v2")
	public CurrencyConversionBean getAmountAfterConversionV2(@RequestParam String from, @RequestParam String to, @RequestParam int quantity) {
		CurrencyConversionBean bean = proxy.retrieveExchangeValue(from, to);
		bean.setQuantity(quantity);
		bean.setTotalCalculatedAmount(bean.getConversionMultiple().multiply(new BigDecimal(quantity)));		
		return bean;
	}
}
