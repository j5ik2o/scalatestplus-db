package com.github.j5ik2o.scalatest.db

import java.net.{ InetSocketAddress, Socket }
//import java.util.Random

object RandomSocket {
  //private[this] val rng                    = new Random
  private[this] val ephemeralSocketAddress = localSocketOnPort(0)

  def apply(): InetSocketAddress = nextAddress()

  def nextAddress(): InetSocketAddress =
    localSocketOnPort(nextPort())

  private[this] def localSocketOnPort(port: Int) =
    new InetSocketAddress(port)

  def nextPort(): Int = {
    val s = new Socket
    s.setReuseAddress(true)
    try {
      s.bind(ephemeralSocketAddress)
      s.getLocalPort
    } catch {
      case e: Throwable =>
        throw new Exception("Couldn't find an open port: %s".format(e.getMessage))
    } finally {
      s.close()
    }
  }
}
