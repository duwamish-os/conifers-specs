package com.specs.config

import java.util.Properties

/**
  * Created by prayagupd
  * on 4/4/17.
  */
class ConfigLoader {
  var appConfig: Properties = new Properties() {
    load(ConfigLoader.this.getClass.getClassLoader.getResourceAsStream(config))
  }

  def environment: String = {
    val applicationEnvironment: String = System.getenv("APPLICATION_ENVIRONMENT")
    println(s"application environment is set to $applicationEnvironment")
    applicationEnvironment
  }

  def config: String = {
    if (environment != null && configExists(environment)) return "application-" + environment + ".properties"
    "application.properties"
  }

  private def configExists(env: String): Boolean =
    classOf[ConfigLoader].getClassLoader.getResource("application-" + env + ".properties") != null
}

