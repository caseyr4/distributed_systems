/**
 *  Echo Server Lab
 *  Ross Casey - cf6932cd853ecd5e1c39a62f639f5548cf2b4cbb567075697e9a7339bcbf4ee3
 */

import java.net._
import java.util.concurrent.{ExecutorService, Executors}

object EchoServer {

  var serverSocket: ServerSocket = null
  var threadPool: ExecutorService = null
  var portNumber: Int = -1

  def main(args: Array[String]) {
    //attempt to create a server socket, exit otherwise
    try {
      portNumber = Integer.parseInt(args(0))
      serverSocket = new ServerSocket(portNumber)
      threadPool = Executors.newFixedThreadPool(4)
      println("Listening on port " + portNumber + ": ")
    } catch {
      case e: Exception => {
        println("SERVER ERROR: " + e.getClass + ": " + e.getMessage)
        System.exit(0)
      }
    }


    var exit = false
    while(!exit) {
      try {
        val s = serverSocket.accept()
        println("Connection accepted")
        val serverThread = new ServerThread(s, killServer, portNumber)
        threadPool.execute(serverThread)
      } catch {
        case e: Exception => {
          exit = true
          threadPool.shutdown()
          println("Killing server...")
        }
      }
    }
    System.exit(0)
  }


  /**
   * Callback to allow threads to kill the server
   */
  def killServer(): Unit = {
    serverSocket.close()
  }
}
