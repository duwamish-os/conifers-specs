package com.specs

/**
  * Created by prayagupd
  * on 2/15/17.
  */

class CustomerOrderFlowSpecs extends FlowSpecs {

  feature("A Customer Order") {

    scenario("Successful customer order checkout") {

      Given("I have a customer order with some items")
      val customerOrder = new CustomerOrder(List(Item("Shirts")))

      When("I checkout the order")
      customerOrder.checkout()

      Then("My order should be ready")
      assert(customerOrder.state == "READY")
    }

  }
}
