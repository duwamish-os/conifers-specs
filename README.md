
install the specs-lib using maven, 

```
mvn clean install
```

Then use as a dependency

```
<dependency>
  <groupId>com.specs</groupId>
  <artifactId>specs-lib</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Unit specs
----------

Subject to test

```
class Subject {
  public String doSomething(String message) {
    return "Processing " + message;
  }
}
```

Tests

```
class SomeUnitSpecs extends UnitSpecs {

  describe("doSomething") {
    it("does process given command") {
      val subject = new Subject()
      val response = subject.doSomething("event")

      assert(response == "Processing event")
    }
  }
}
```

Component Specs
---------------

```
class CustomerOrderComponentSpecs extends ComponentSpecs {

    scenario("when customer order is put") {
      Given("a order")
      val order = new CustomerOrder(List(Item("Skateboard")))

      When("order is checked out")
      order.checkout()

      Then("should be ready")
      assert(order.state == "READY")
      //maybe I want to test its state in database as well
      //more granular assertions than FlowSpecs
    }

}
```

Flow specs
----------

```
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

```

To run the specs
----------------

```
mvn test
```

Also, to have more control over the tests add the following plugin, (for example the specs are parallel by default, 
which can be turned off etc). Also specific tests can be run, as I'm running only those tests
tagged as `KinesisStream` in following example.

```
    <plugin>
        <groupId>org.scalatest</groupId>
        <artifactId>scalatest-maven-plugin</artifactId>
        <version>1.0</version>
        <configuration>
            <parallel>false</parallel>
            <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
            <junitxml>.</junitxml>
            <filereports>stream_specs.log</filereports>
            <tagsToInclude>KinesisStream</tagsToInclude>
        </configuration>
        <executions>
            <execution>
                <id>test</id>
                <goals>
                    <goal>test</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
```