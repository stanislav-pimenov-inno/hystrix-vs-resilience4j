package com.ebsco.ebsconext.sandbox;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

public class ApiStubServer {

  public static void main(String[] args) {

    WireMockServer stubServer = new WireMockServer(
        options()
            .port(8888)
            .asynchronousResponseEnabled(true)
            .asynchronousResponseThreads(300)
            .containerThreads(300));

    stubServer.stubFor(get(urlEqualTo("/successWithDelay"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "text/json; charset=utf-8")
            .withBody("{ \"payload\": \"Endpoint 1 Response\" }")
            .withLogNormalRandomDelay(900, 0.1)
        ));

    stubServer.stubFor(get(urlEqualTo("/badRequest"))
        .willReturn(aResponse()
            .withStatus(400)
            .withHeader("Content-Type", "application/json")
            .withBody("{ \"payload\": \"Bad Request\" }")
        ));

    stubServer.stubFor(get(urlEqualTo("/serverError"))
        .willReturn(serverError()
            .withHeader("Content-Type", "text/json; charset=utf-8")
            .withBody("{ \"payload\": \"Server Error\" }")
        ));

    stubServer.start();
  }
}
