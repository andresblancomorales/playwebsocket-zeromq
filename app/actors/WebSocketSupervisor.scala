// ****************************************************************
// PUREPREDICTIVE CONFIDENTIAL
// -----------------------------
//
// Copyright (c) 2011-2014, All Rights Reserved
// PurePredictive Incorporated
// www.purepredictive.com
//
// NOTICE: All information contained herein is, and remains
// the property of PurePredictive Incorporated. The intellectual
// and technical concepts contained herein are proprietary to
// PurePredictive Incorporated and may be covered by U.S. and
// foreign patents, patents in process, and are protected by trade
// secret, international treaties or copyright law. Unauthorized
// use, duplication, reverse engineering, reproduction, any form
// of redistribution, dissemination or use in part or in whole of
// this material other than by prior, express, printed and signed
// license for use from PurePredictive Incorporated, is strictly
// forbidden and subject to civil and criminal prosecution. If you
// have received this file in error, please notify PurePredictive
// Incorporated immediately and destroy this and any other copies
// as instructed.
// ****************************************************************
package actors

import akka.actor.{ActorRef, Props, Actor}
import akka.zeromq._
import contracts.RegisterListener
import play.libs.Akka

import scala.collection.mutable

object WebSocketSupervisor {
  lazy val supervisor = Akka.system().actorOf(Props[WebSocketSupervisor])
  def apply() = supervisor
}

class WebSocketSupervisor extends Actor {

  val actorContext = ZeroMQExtension(context.system).newSocket(SocketType.Pull, Listener(self), Connect("tcp://127.0.0.1:5555"))

  var subscribers = new mutable.HashMap[String, ActorRef]()
  override def receive: Receive = {
    case RegisterListener(account, subscriber) => {
      subscribers += account -> subscriber
    }
    case queueMessage : ZMQMessage => {
      val msg = queueMessage.frame(0).utf8String
      subscribers.filter(subs => subs._1.equals(msg)).foreach(subs => subs._2 ! msg)
    }
  }
}
