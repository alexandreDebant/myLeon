package distribution

import Protocol._
import ProtocolProof._
import FifoNetwork._

import leon.collection._
import leon.proof.check

object Networking {
  
  abstract class ActorId
  abstract class Message
  abstract class State
  abstract class Parameter
  
  
  abstract class Actor {
    val myId: ActorId
    
    def !!(receiver: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
      net send (myId,receiver,m)
    } 
    
    def init()(implicit net: VerifiedNetwork): Unit
    def receive(id: ActorId, m: Message)(implicit net: VerifiedNetwork): Unit
    
    def state(implicit net: VerifiedNetwork) = {
      net.getState(myId)
    }
    
    def update(s: State)(implicit net: VerifiedNetwork) = {
      net.updateState(myId, s)
    }
    
    
  }
  
  
  def runActors(p: Parameter, initial_actor: Actor, schedule: List[(ActorId,ActorId,Message)]): Unit = {
  
    val net = makeNetwork(p)
    
  
    def loop(schedule: List[(ActorId,ActorId,Message)]): Unit = {

      schedule match {
        case Nil() => ()
        case Cons((sender, receiver, m), schedule2) =>
          
          if (validId(net, sender) && validId(net, receiver) && net.applyMessage(sender, receiver, m))
            loop(schedule2)
            
      }
    }
    
  
    initial_actor.init()(net)
    loop(schedule)
  
  } 

  
}
