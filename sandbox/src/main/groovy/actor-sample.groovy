#!/usr/bin/env groovy

@Grab(group='org.codehaus.gpars', module='gpars', version='0.10-beta-1')

import groovyx.gpars.actor.AbstractPooledActor

class SecuredServer extends AbstractPooledActor {
    def password
    void afterStart() {
        password = new Random().nextInt(10)
        println "password set to ${password}"
    }
    void act() {
        loop {
            react {
                if (it > password) reply 'too large'
                else if (it < password) reply 'too small'
                else { reply 'autorized'; stop(); System.exit 0; }
            }
        }
    }
}

class Client extends AbstractPooledActor {
    def server
    void act() {
        loop {
            def guess = new Random().nextInt(10)
            server.send guess
            react {
                switch(it) {
                    case 'too large': println "$guess was too large"; break
                    case 'too small': println "$guess was too small"; break
                    case 'autorized': println "Access granted with: $guess"; stop(); break
                }
            }
        }
    }
}

def myServer = new SecuredServer().start()
def client = new Client(server: myServer).start()
[myServer, client]*.join()

