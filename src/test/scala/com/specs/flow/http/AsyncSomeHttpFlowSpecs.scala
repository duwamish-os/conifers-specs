package com.specs.flow.http

import akka.http.scaladsl.model.{HttpResponse, StatusCode}
import akka.util.ByteString
import com.specs.http.AsyncHttpFlowSpecs
import org.scalatest.Informing

import scala.concurrent.Future
import scala.util.parsing.json.JSON

/**
  * Created by prayagupd
  * on 2/27/17.
  */

class AsyncSomeHttpFlowSpecs extends AsyncHttpFlowSpecs with Informing {

  feature("Getting user posts on my API server") {

    scenario("As a software engineer, I want to receive the user posts when I call the endpoint") {

      When("I send a GET request to the http endpoint")
      val endpoint = "jsonplaceholder.typicode.com"
      val responseFuture: Future[HttpResponse] = doHttpGet(endpoint, "/posts/1")

      Then("I receive 200 response back with the user comments")
      responseFuture map { response => {

        response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String) map { bodyString => {
          val json = JSON.parseRaw(bodyString)
          println(s"Before assertion ${json}")
          assert(json == JSON.parseRaw(
            """
              |{
              |  "userId": 1,
              |  "id": 1,
              |  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
              |  "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
              |}
            """.stripMargin)
          )
          println(s"After assertion ${json}")
        }
        }
        assert(response.status == StatusCode.int2StatusCode(200))
      }
      }
    }
  }
}
