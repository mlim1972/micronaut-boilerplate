package com.example.service

import jakarta.inject.Singleton

@Singleton
class HelloService {
    String getHelloMessage(){
        "Hello World"
    }
}
