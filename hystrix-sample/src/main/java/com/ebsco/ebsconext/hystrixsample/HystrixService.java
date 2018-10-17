package com.ebsco.ebsconext.hystrixsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HystrixService {

  @Autowired
  private RestTemplate restTemplate;

  public String callApi(String api) {
    return new ApiHystrixCommand(restTemplate, api).execute();
  }
}
