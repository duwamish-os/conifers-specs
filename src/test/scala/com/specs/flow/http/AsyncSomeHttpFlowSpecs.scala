package com.specs.flow.http

import akka.http.scaladsl.model.{HttpResponse, StatusCode}
import akka.util.ByteString
import com.specs.http.AsyncHttpFlowSpecs
import org.scalatest.Informing

import scala.concurrent.Future

/**
  * Created by prayagupd
  * on 2/27/17.
  */

class AsyncSomeHttpFlowSpecs extends AsyncHttpFlowSpecs with Informing {

  feature("Getting user comments on my API server") {

    scenario("As a software engineer, I want to receive the user posts when I call the endpoint") {

      When("I send a GET request to the http endpoint")
      val endpoint = "jsonplaceholder.typicode.com"
      val responseFut: Future[HttpResponse] = doHttpGet(endpoint, "/comments")

      Then("I receive 200 response back with the user comments")
      responseFut map { response => {
        response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String) map {
          bodyString => print(bodyString)
        }
        assert(response.status == StatusCode.int2StatusCode(200))
      }
      }
    }
  }
}
