#!/bin/bash

javac *.java
rmic ChatServer
rmic ChatClient
rmiregistry 5000
