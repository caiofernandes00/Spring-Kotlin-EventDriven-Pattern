package com.example.eventdriven.domain.port.out

import org.apache.avro.specific.SpecificRecordBase
import java.io.Serializable

interface MessageProducerOut<K : Serializable, V : SpecificRecordBase> {
    fun send(topic: String, key: K, value: V)
}