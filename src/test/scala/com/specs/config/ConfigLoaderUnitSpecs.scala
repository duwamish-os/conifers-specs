package com.specs.config

import java.util.Properties

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by prayagupd
  * on 6/9/17.
  */

class ConfigLoaderUnitSpecs extends FunSuite with Matchers {

  test("returns environment if theres APPLICATION_ENVIRONMENT set as argLine") {

    val configLoader = new ConfigLoader
    configLoader.appConfig = new Properties()

    configLoader.environment should not be empty

  }
}
