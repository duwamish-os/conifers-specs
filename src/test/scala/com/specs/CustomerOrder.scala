package com.specs

/**
  * Created by prayagupd
  * on 2/15/17.
  */

class CustomerOrder(items: List[Item]) {
  var state = ""

  def checkout(): Unit = {
    state = "READY"
  }
}

case class Item(name: String)
