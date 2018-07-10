package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
	
  @Value("#{environment['namespace'] ?: 'localhost'}")
  private String namespace;

  private CustomerClient customerClient;

  private AddressClient addressClient;

  private Logger logger = LoggerFactory.getLogger(Controller.class);
  
  @Autowired
  private RestTemplate restTemplate;  

  @Autowired
  public Controller(CustomerClient customerClient, AddressClient addressClient) {
    this.customerClient = customerClient;
    this.addressClient = addressClient;
  }

  @GetMapping(path = "customers-with-address/{id}")
  public CustomerAndAddress getCustomerWithAddress(@PathVariable("id") long customerId){
	  logger.info("AWS - Name Space is : " + namespace);
	  logger.info("COLLECTING CUSTOMER AND ADDRESS WITH ID {} FROM UPSTREAM SERVICE", customerId);
	  String upstreamService = "sleuth-upstream-service"+ "."+ namespace;
	  logger.info("AWS Routr53 sleuth-upstream-service name is : " + upstreamService);
	  
     logger.info("Retrieving data for Customer with ID: ", customerId);
     Customer customer = restTemplate.getForObject("http://"+upstreamService+"/customers/"+ customerId, Customer.class);
     Address address = restTemplate.getForObject("http://"+upstreamService+"/addresses/"  +customerId,  Address.class);    
 
//     Customer customer = customerClient.getCustomer(customerId);
//     Address address = addressClient.getAddressForCustomerId(customerId);
    
	  logger.info("Retrieved data for Customer with ID: : ", customerId);
      return new CustomerAndAddress(customer, address);
  }
  
  

//  @GetMapping(path = "customers-with-address/{id}")
//  public Object getCustomerWithAddress(@PathVariable("id") long customerId){
//	  logger.info("AWS - Name Space is : " + namespace);
//	  logger.info("COLLECTING CUSTOMER AND ADDRESS WITH ID {} FROM UPSTREAM SERVICE", customerId);
//	  String upstreamService = "sleuth-upstream-service"+ "."+ namespace;
//	  logger.info("AWS Routr53 sleuth-upstream-service name is : " + upstreamService);
//
//    RestTemplate restTemplate = new RestTemplate();
//    
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    
//   Object pkgEl = restTemplate.getForObject("http://localhost:8085/products//v1/testlog", Object.class, headers);
//   return pkgEl;
////    
////    Address address = restTemplate.getForObject("http://"+ upstreamService +"/addresses/"+customerId,  Address.class);
//    
//    
//    
////    Customer customer = customerClient.getCustomer(customerId);
////    Address address = addressClient.getAddressForCustomerId(customerId);
//    //return new CustomerAndAddress(customer, address);
//  }

  
}
