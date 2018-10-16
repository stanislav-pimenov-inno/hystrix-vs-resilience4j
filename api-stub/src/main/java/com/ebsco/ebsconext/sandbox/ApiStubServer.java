package com.ebsco.ebsconext.sandbox;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

public class ApiStubServer {

  public static void main(String[] args) {

    WireMockServer wireMockServer3 = new WireMockServer(
        options().port(8888)); //No-args constructor will start on port 8080, no HTTPS

    wireMockServer3.stubFor(get(urlEqualTo("/successWithDelay"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "text/json; charset=utf-8")
            .withBody("{ \"payload\": \"Endpoint 1 Response\" }")
            .withLogNormalRandomDelay(900, 0.1)
        ));

    wireMockServer3.stubFor(get(urlEqualTo("/badRequest"))
        .willReturn(aResponse()
            .withStatus(400)
            .withHeader("Content-Type", "application/json")
            .withBody("{ \"payload\": \"Bad Request\" }")
        ));

    wireMockServer3.stubFor(get(urlEqualTo("/serverError"))
        .willReturn(serverError()
            .withHeader("Content-Type", "text/json; charset=utf-8")
            .withBody("{ \"payload\": \"Server Error\" }")
        ));

    wireMockServer3.start();
  }
}
