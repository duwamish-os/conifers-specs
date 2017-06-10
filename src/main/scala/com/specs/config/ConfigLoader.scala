package com.specs.config

import java.util.Properties

import sys.process._
import scala.language.postfixOps

/**
  * Created by prayagupd
  * on 4/4/17.
  */
class ConfigLoader {

  var appConfig: Properties = new Properties() {
    load(ConfigLoader.this.getClass.getClassLoader.getResourceAsStream(config))
  }

  def environment: String = {

    Option(System.getProperty("CI_APPLICATION_ENVIRONMENT")) match {
      case Some(env) =>
        println("-DCI_APPLICATION_ENVIRONMENT")
        println(s"application CI environment is set to $env")
        env
      case None =>
        println("============ infra environment ============")
        println("env"!!)
        println("============ infra environment ============")
        println(s"application environment is set to ${System.getenv("APPLICATION_ENVIRONMENT")}")
        System.getenv("APPLICATION_ENVIRONMENT")
    }
  }

  def config: String = {
    if (environment != null && configExists(environment)) return "application-" + environment + ".properties"
    "application.properties"
  }

  private def configExists(env: String): Boolean =
    classOf[ConfigLoader].getClassLoader.getResource("application-" + env + ".properties") != null
}
