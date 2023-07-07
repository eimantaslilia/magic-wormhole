### Magic wormhole

getting things from one computer to another

#### How to run:

<i>Requires JDK 17</i>

- build the project  
`.\mvnw clean install`
- run the registry, which will
  - register the receiver's `host` and `port`
  - supply receivers `host` and `port` to sender  
    `java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar`
- run the receiver  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar recv -incomingPath=<directoryPath>`
- run the sender  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar send -p=<filePath>`

Additional CLI arguments can be viewed by running  
`java -jar <jar> <send/recv>`
