package com.example.solace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SolaceApplication

fun main(args: Array<String>) {
	runApplication<SolaceApplication>(*args)
}
