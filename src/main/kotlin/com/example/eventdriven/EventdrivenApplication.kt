package com.example.eventdriven

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventdrivenApplication

fun main(args: Array<String>) {
	runApplication<EventdrivenApplication>(*args)
}
