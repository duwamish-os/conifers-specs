package com.specs

/**
  * Created by prayagupd
  * on 2/15/17.
  */

class SomeUnitSpecs extends UnitSpecs {

  describe("doSomething") {
    it("does process given command") {
      val subject = new Subject()
      val response = subject.doSomething("event")

      response shouldBe "Processing event"
    }
  }
}


class Subject {
  def doSomething(message: String) = "Processing " + message
}