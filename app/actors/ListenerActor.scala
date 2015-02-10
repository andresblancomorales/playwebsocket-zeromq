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

import akka.actor.{Props, ActorRef, Actor}
import contracts.RegisterListener

object ListenerActor {
  def props(account : String, out: ActorRef) = Props(new ListenerActor(account, out))
}

class ListenerActor (account : String, out: ActorRef) extends Actor {

  WebSocketSupervisor.supervisor ! RegisterListener(account, self)

  def receive = {
    case msg: String =>
      out ! (msg)
  }
}
