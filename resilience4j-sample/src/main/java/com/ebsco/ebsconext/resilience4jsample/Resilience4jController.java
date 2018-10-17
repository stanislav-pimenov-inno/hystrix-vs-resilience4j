package com.ebsco.ebsconext.resilience4jsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Resilience4jController {

  @Autowired
  private Resilience4jService service;

  @GetMapping("/hello/{api}")
  public String getHello(@PathVariable String api) {
    return service.callApi(api);
  }
}
