package distribution


import FifoNetwork._
import Networking._
import sun.font.TrueTypeFont

import scala.language.postfixOps


object Protocol {
  import ProtocolProof._

  case class WriteUser(s: String, i: BigInt) extends Message
  case class WriteSystem(s: String, i: BigInt) extends Message
  case class Read(s: String) extends Message
  case class Value(v: Option[BigInt]) extends Message
  case class Deliver(j: BigInt) extends Message

  case class State() extends State
  case class BadState() extends State


  case class ActorIdSys(i: BigInt) extends ActorId
  case class ActorIdUser(i: BigInt) extends ActorId

  // this protocol does not need a parameter
  case class NoParam() extends Parameter

  val actor1: ActorIdSys = ActorIdSys(1)
  val actor2: ActorIdSys = ActorIdSys(2)
  val actor3: ActorIdSys = ActorIdSys(3)
  val actor4: ActorIdUser = ActorIdUser(1)

  case class SystemActor(myId: ActorId) extends Actor {
    require(myId == ActorIdSys(1) || myId == ActorIdSys(2) || myId == ActorIdSys(3))

    var memory: MMap[String, BigInt] = MMap((x: String) => None[BigInt]())

    def init()(implicit net: VerifiedNetwork) = {
      require(true)
    } ensuring(true)


    def receive(sender: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
      require(true)

      (sender, m, state) match {
        case (id, WriteUser(s,i), State()) =>
          memory = memory.updated(s,i)
          if(myId != actor1){
            !! (actor1, WriteSystem(s,i), State())
          }
          if(myId != actor2){
            !! (actor2, WriteSystem(s,i), State())
          }
          if(myId != actor3){
            !! (actor3, WriteSystem(s,i), State())
          }

        case (id, WriteSystem(s,i), State()) =>
          memory = memory.updated(s,i)

        case (id,Read(s), State()) =>
          !! (id, memory.getOrElse(s,None[BigInt]), State())

        case _ => update(BadState())
      }
    } ensuring(true)

  }



  case class UserActor(myId: ActorId) extends Actor {
    require(myId == ActorIdUser(1))

    var x: Option[BigInt] = None[BigInt]

    def init()(implicit net: VerifiedNetwork) = ()

    def receive(sender: ActorId, m: Message)(implicit net: VerifiedNetwork) = {
      require(true)
      check(sender == ActorIdSys(1) || sender == ActorIdSys(2) || sender == ActorIdSys(3))

      (sender, m, state) match {
        case (sender, Value(v), State()) =>
          x = v
          v match {
            case Some(i) => PrettyPrinting.messageToString(Deliver(i))
            case None => PrettyPrinting.messageToString(Deliver(50))
          }
        case _ => update(BadState())
      }
    }  ensuring(true)


  }

  @extern
  def printing(s: String) = {
    println(s)
  }

  @ignore
  def main(args: Array[String]) = {
    val a1 = SystemActor(actor1)
    val a2 = SystemActor(actor2)
    val a3 = SystemActor(actor3)
    val a4 = UserActor(actor4)

    runActors(NoParam(), List(actor1, actor2, actor3, actor4), List(
      (actor4, actor1, WriteUser("1", 1)),
      (actor1, actor2, WriteSystem("1", 1)),
      (actor4, actor2, Read("1")),

      (actor4, actor2, WriteUser("1", 2)),
      (actor4,actor2, Read("1")),
      (actor2, actor3, WriteSystem("1", 2)),
      (actor4, actor3, Read("1")),

      (actor1, actor3, WriteSystem("1", 1)),
      (actor4, actor3, Read("1"))
    ))
  }


}


