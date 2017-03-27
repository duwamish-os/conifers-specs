package com.specs.database

import java.sql._
import java.util.Properties

import com.specs.ComponentSpecs
import org.apache.commons.dbcp.BasicDataSource
import org.json.JSONObject

/**
  * Created by prayagupd
  * on 3/20/17.
  */

trait DatabaseComponentSpecs extends ComponentSpecs {

  val appConfig = new Properties(){{
    load(this.getClass.getClassLoader.getResourceAsStream("application.properties"))
  }}

  val url: String = appConfig.getProperty("state.url")
  val database: String = appConfig.getProperty("state.database.name")
  val user: String = appConfig.getProperty("state.username")
  val password: String = appConfig.getProperty("state.password")

  val establishedConnections = connectionPool()

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

  def connectionPool() : BasicDataSource = {
    println(s"setting up connection pool for $url")
    val driver: String = appConfig.getProperty("state.database.driver")
    val autoCommit: Boolean = appConfig.getOrDefault("state.database.auto.commit", "true").toString.toBoolean

    val establishedConnections = new BasicDataSource()
    establishedConnections.setUrl(url)
    establishedConnections.setUsername(user)
    establishedConnections.setPassword(password)
    establishedConnections.setDriverClassName(driver)

    establishedConnections.setMinIdle(5)
    establishedConnections.setMaxIdle(10)
    establishedConnections.setMaxOpenPreparedStatements(50)
    establishedConnections.setInitialSize(5)
    establishedConnections.setPoolPreparedStatements(true)
    establishedConnections.setDefaultAutoCommit(autoCommit); //adds ~30ms, on connection creation delay
    establishedConnections.getConnection.close()

    establishedConnections
  }

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

      println("readState :: closing connection")
      connection.close()

    } catch {
      case ex: SQLException => {
        System.err.println("readState :: SQLException error")
        while (ex != null) {
          System.err.println("Error msg: " + ex.getMessage)
          val ex1 = ex.getNextException
        }
      }
    }
    states
  }

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

      println("dropRecords :: closing connection")
      connection.close()

    } catch {
      case ex: SQLException => {
        System.err.println("dropRecords :: SQLException information")
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
    establishedConnections.getConnection
  }
}
