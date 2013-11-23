package edu.cmu.graphchidb.storage

import java.nio.ByteBuffer
import java.io.File

/**
 * DataBlock is a low level storage object, that stores key-value pairs.
 * @author Aapo Kyrola
 */

trait ByteConverter[T] {
  def fromBytes(bb: ByteBuffer) : T
  def toBytes(v: T, out: ByteBuffer) : Unit
  def sizeOf: Int
}

object ByteConverters {
  implicit object IntByteConverter extends ByteConverter[Int] {
    override def fromBytes(bb: ByteBuffer) : Int = {
      bb.getInt
    }
    override def toBytes(v: Int, bb: ByteBuffer) : Unit = {
      bb.putInt(v)
    }
    override def sizeOf = 4
  }

  implicit  object ByteByteConverter extends ByteConverter[Byte] {
    override def fromBytes(bb: ByteBuffer) : Byte = {
      bb.get
    }
    override def toBytes(v: Byte, bb: ByteBuffer) : Unit = {
      bb.put(v)
    }
    override def sizeOf = 1
  }
}

trait DataBlock[T] extends IndexedByteStorageBlock {

  def get(idx: Int)(implicit converter: ByteConverter[T]) : Option[T] = {
    val byteBuffer = ByteBuffer.wrap(new Array[Byte](valueLength))
    byteBuffer.rewind()
    if (readIntoBuffer(idx, byteBuffer))
      Some(converter.fromBytes(byteBuffer))
    else
      None
  }

  def set(idx: Int, value: T)(implicit converter: ByteConverter[T]) : Unit = {
    val bb = ByteBuffer.allocate(converter.sizeOf)  // TODO reuse
    converter.toBytes(value, bb)
    writeFromBuffer(idx, bb)
  }

}

/*
 * Internal low-level
 */
trait IndexedByteStorageBlock  {

  def valueLength: Int
  def readIntoBuffer(idx: Int, out: ByteBuffer) : Boolean
  def writeFromBuffer(idx: Int, in: ByteBuffer) : Unit

}