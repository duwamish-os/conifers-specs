package com.specs

/**
  * Created by prayagupd
  * on 2/15/17.
  */

class CustomerOrderComponentSpecs extends ComponentSpecs {

  feature("A customer order") {

    scenario("when customer order is put") {
      Given("a order")
      val order = new CustomerOrder(List(Item("Skateboard")))

      When("order is checked out")
      order.checkout()

      Then("should be ready")
      order.state shouldBe "READY"
      //maybe I want to test its state in database as well
      //more granular assertions than FlowSpecs
    }

  }
}