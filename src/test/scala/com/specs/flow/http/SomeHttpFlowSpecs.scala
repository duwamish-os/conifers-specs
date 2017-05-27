package com.specs.flow.http

import com.specs.http.HttpFlowSpecs

import scala.util.parsing.json._

/**
  * Created by prayagupd
  * on 2/27/17.
  */

class SomeHttpFlowSpecs extends HttpFlowSpecs {

  feature("Getting user posts on my API server") {

    ignore("As a software engineer, I want to receive the user posts when I call the endpoint") {

      When("I send a GET request to the http endpoint")
      val response = doHttpGet("https://jsonplaceholder.typicode.com/posts/1")

      Then("I receive 200 response back with the user posts")
      assert(response.getStatusLine.getStatusCode == 200)

      val expectedJson =
        """
          |{
          |  "userId": 1,
          |  "id": 1,
          |  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
          |  "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
          |}
        """.stripMargin

      JSON.parseRaw(responseContent(response)) shouldBe JSON.parseRaw(expectedJson)
    }
  }
}
