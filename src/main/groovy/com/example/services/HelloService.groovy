package com.example.services

import jakarta.inject.Singleton

@Singleton
class HelloService {
    String getHelloMessage(){
        "Hello World"
    }
}
