package com.specs.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.specs.AsyncFlowSpecs

import scala.concurrent.Future

/**
  * Created by prayagupd
  * on 3/4/17.
  */

class AsyncHttpFlowSpecs extends AsyncFlowSpecs {
  implicit val system = ActorSystem("http-actor")
  implicit val actorMaterializer = ActorMaterializer()
  override implicit val executionContext = system.dispatcher

  def doHttpGet(endpoint: String, requestUri: String): Future[HttpResponse] = {
    val httpConnection = Http().outgoingConnection(host = endpoint)
    val request = HttpRequest(uri = Uri(requestUri))
    Source.single(request)
      .via(httpConnection)
      .runWith(Sink.head[HttpResponse])
  }
}
