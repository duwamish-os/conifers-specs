package com.specs.http

import java.io.{BufferedReader, InputStreamReader}
import java.util.stream.Collectors

import com.specs.FlowSpecs
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

/**
  * Created by prayagupd
  * on 3/4/17.
  */

trait HttpFlowSpecs extends FlowSpecs {

  def doHttpGet(endpoint: String): CloseableHttpResponse = {
    val httpRequest = new HttpGet(endpoint)
    val response = (new DefaultHttpClient).execute(httpRequest)
    response
  }

  def doHttpPost(endpoint: String, content: String, contentType: String = "application/json"): CloseableHttpResponse = {
    val httpRequest = new HttpPost(endpoint)
    httpRequest.setHeader("Content-type", contentType)
    httpRequest.setEntity(new StringEntity(content))
    val response = (new DefaultHttpClient).execute(httpRequest)
    println("http response :: " + response )
    response
  }

  def responseContent(response: CloseableHttpResponse): String = {
    val responseBody: String = new BufferedReader(new InputStreamReader(response.getEntity.getContent))
      .lines().collect(Collectors.joining("\n"))

    println("Response body = " + responseBody)
    responseBody
  }
}
