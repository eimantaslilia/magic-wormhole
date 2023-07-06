### Magic wormhole

getting things from one computer to another

#### How to run:

<i>Requires JDK 17</i>

- build the project  
`.\mvnw clean install`
- run the receiver  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar recv -incomingPath=<directoryPath>`
- run the sender  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar send -p=<filePath>`

