package distribution


import FifoNetwork._
import Networking._

import leon.lang._
import leon.collection._
import leon.proof._
import leon.annotation._
import leon.lang.synthesis._

import scala.language.postfixOps


// This object contains lemma and auxiliary functions used in the proofs

object ProtocolProof {
  import Protocol._
  
  def validId(net: VerifiedNetwork, id: ActorId) = {
    true
  }
  
  
  def validGetActor(net: VerifiedNetwork, id: ActorId) = {    
    net.getActor.contains(id)
  } holds
  
  
  def makeNetwork(p: Parameter) = {
    
    def states(id: ActorId): Option[State] = id match {
      Some(CommonState())
    }
    
    def getActor(id: ActorId): Option[Actor] = id match {
      case ActorIdSys(x) => if (x == 1) {Some(SystemActor(actor1))}
			else {
			  if (x == 2) then {Some(SystemActor(actor2))}
			  else {
			    Some(SystemActor(actor1))
			  }
			}
      case ActorIdUser(x) => Some(userActor(actor4))
    }


    VerifiedNetwork(NoParam(), 
		MMap(states), 
		MMap(
		  (sender,receiver) =>
			if (sender == actor1 && receiver == actor2) {List(WriteSystem("1", 1))} 
			if (sender == actor4 && receiver == actor2) {List(Read("1"), WriteUser("1", 2), Read("1"))}
			if (sender == actor2 && receiver == actor3) {List(WriteSystem("1", 2))}
			if (sender == actor4 && receiver == actor3) {List(Read("1"), Read("1"))}
			if (sender == actor4 && receiver == actor3) {List(WriteSystem("1", 1))}
			else {List()}
		),
		MMap(getActor))
  }

  
  def validParam(p: Parameter) = true
  
  // This is an invariant of the class VerifiedNetwork
  def networkInvariant(param: Parameter, states: MMap[ActorId, State], messages: MMap[(ActorId,ActorId),List[Message]], getActor: MMap[ActorId,Actor]) = {
    true
  }
  
    
    
  def countingActorReceivePre(receiver: Actor, sender: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
    true
  }
  
  def checkingActorReceivePre(receiver: Actor, sender: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
    true
  }
  
  def receivePre(a: Actor, sender: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
    true
  }
  
  def peekMessageEnsuresReceivePre(n: VerifiedNetwork, sender: ActorId, receiver: ActorId, m: Message) = {
    true
  }
  
  
  def initPre(a: Actor)(implicit net: VerifiedNetwork) = {
    true
  }
  
  
  def min(l: List[Message]): BigInt = {
    0
  }
  
  def max(l: List[Message]): BigInt = {
    0
  }
  

  def isSorted(l: List[Message]): Boolean = {
    true
  }
  
  def areDelivers(l: List[Message]): Boolean = {
    true
  }
  
  @induct 
  def appendDeliver(messages: List[Message], x: BigInt) = {
    true
  } holds
  
  def areIncrements(l: List[Message]): Boolean = {
    true
  }
  
  @induct 
  def appendIncrement(messages: List[Message]) = {
    true
  } holds

  
  @induct
  def appendItself(l: List[Message], j: BigInt) = {
    true
  } holds
  
  @induct
  def appendLarger(l: List[Message], j: BigInt, k: BigInt) = {
    true
  } holds
  
  @induct
  def appendSorted(l: List[Message], j: BigInt) = {
    true
  } holds
  
  
  @induct
  def smallestHead(j: BigInt, l: List[Message]) = {
    true
  } holds
}
