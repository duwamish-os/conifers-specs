package com.specs.database

import java.sql._
import java.util.Properties

import com.specs.ComponentSpecs
import org.json.JSONObject

/**
  * Created by prayagupd
  * on 3/20/17.
  */

class DatabaseComponentSpecs extends ComponentSpecs {

  val config = new Properties(){{
    load(this.getClass.getClassLoader.getResourceAsStream("application.properties"))
  }}

  val url: String = config.getProperty("state.url")
  val database: String = config.getProperty("state.database.name")
  val user: String = config.getProperty("state.username")
  val password: String = config.getProperty("state.password")

  def queryState(query: String, numberOfColumns: Int): java.util.List[JSONObject] = {
    var statement: Statement = null
    var resultSet: ResultSet = null
    val states: java.util.List[JSONObject] = new java.util.ArrayList[JSONObject]
    try {
      val connection: Connection = getEstablishedDatabaseConnection
      connection.setAutoCommit(false)
      statement = connection.createStatement
      val start: Long = System.currentTimeMillis
      resultSet = statement.executeQuery(query)
      val columns: ResultSetMetaData = resultSet.getMetaData
      while (resultSet.next) {
        val jsonObject: JSONObject = new JSONObject
        Range(1, numberOfColumns).foreach(col => {
          try {
            jsonObject.put(columns.getColumnName(col), resultSet.getString(col));
          } catch {
            case e: SQLException => e.printStackTrace()
          }
        })
        states.add(jsonObject)
      }
      System.out.println("read :: timeTaken to read = " + (System.currentTimeMillis - start) + "ms")
      resultSet.close()
      statement.close()
      connection.commit()
      connection.close()
      return states

    } catch {
      case ex: SQLException => {
        System.err.println("SQLException information")
        while (ex != null) {
          System.err.println("Error msg: " + ex.getMessage)
          val ex1 = ex.getNextException
        }
      }
    }
    states
  }

  // End main
  def readState(databaseName: String, tableName: String): java.util.List[String] = {
    var statement: Statement = null
    var resultSet: ResultSet = null
    val states: java.util.List[String] = new java.util.ArrayList[String]
    try {
      val connection: Connection = getEstablishedDatabaseConnection
      connection.setAutoCommit(false)
      statement = connection.createStatement
      val start: Long = System.currentTimeMillis
      resultSet = statement.executeQuery("SELECT * FROM " + databaseName + "." + tableName)
      System.out.println("read :: timeTaken to read = " + (System.currentTimeMillis - start) + "ms")
      while (resultSet.next) {
        Range(1, 16).foreach(x => {
          try {
            System.out.print(resultSet.getString(x) + ",");
          } catch {
            case e: SQLException => e.printStackTrace()
          }
        })
        System.out.println("\n")
        states.add(resultSet.getString(1))
      }
      resultSet.close()
      statement.close()
      connection.commit()
      connection.close()

    } catch {
      case ex: SQLException => {
        System.err.println("SQLException information")
        while (ex != null) {
          System.err.println("Error msg: " + ex.getMessage)
          val ex1 = ex.getNextException
        }
      }
    }
    states
  }

  // End main
  def dropRecords(table: String) {
    var statement: Statement = null
    try {
      val connection: Connection = getEstablishedDatabaseConnection
      connection.setAutoCommit(true)
      val compiledQuery: String = "DELETE FROM " + table + " where 1=1"
      statement = connection.createStatement
      val start: Long = System.currentTimeMillis
      statement.executeUpdate(compiledQuery)
      val end: Long = System.currentTimeMillis
      System.out.println("drop :: total time taken = " + (end - start) + " ms")
      statement.close()
      connection.close()

    } catch {
      case ex: SQLException => {
        System.err.println("SQLException information")
        while (ex != null) {
          System.err.println("Error msg: " + ex.getMessage)
          val ex1 = ex.getNextException
          System.exit(0)
        }
      }
    }
  }

  @throws[SQLException]
  protected def getEstablishedDatabaseConnection: Connection = {
    println(s"initialising connection for ${this.getClass.getSimpleName}")
    val driver = DriverManager.getDriver(config.getProperty("state.url"))
    DriverManager.registerDriver(driver)
    val connection = DriverManager.getConnection(url, user, password)
    connection
  }
}
