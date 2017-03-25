
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

[Unit specs](https://en.wikipedia.org/wiki/Unit_testing)
----------

```
a software testing method by which individual units of source code, sets of one or more computer 
program modules together with associated control data, usage procedures, and operating procedures, 
are tested to determine whether they are fit for use.
```

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

[Component Specs](https://en.wikipedia.org/wiki/Integration_testing)
---------------

```
Component testing is testing of specific module or program.
It may be done in isolation from rest of the system depending on the life cycle model selected for 
that particular application.

Stub and driver are used for competent testing. These both are considered as component.
```

```
class CustomerOrderComponentSpecs extends ComponentSpecs {

    //beforeAll start a database server
    //afterAll stop the database server

    scenario("successful customer order") {
      Given("an order")
      val order = new CustomerOrder(List(Item("Skateboard")))

      When("order is checked out")
      order.checkout()

      Then("should be ready to be shipped")
      assert(order.state == "READY TO BE SHIPPED")
      //maybe I want to test its state in database as well
      //more granular assertions than FlowSpecs
    }

}
```

[Flow specs](https://en.wikipedia.org/wiki/System_testing#Types_of_tests_to_include_in_system_testing)
----------

```
System testing is testing conducted on a complete, integrated system to 
evaluate the system's compliance with its specified requirements. 
System testing falls within the scope of black-box testing, and as such, 
should require no knowledge of the inner design of the code or logic.
```

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
            <htmlreporters>${project.build.directory}/surefire-reports/html</htmlreporters>
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
