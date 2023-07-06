### Magic wormhole

getting things from one computer to another

#### How to run:

- build the project  
`.\mvnw clean install`
- run the server (receiver)  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar`
- run the sender  
`java -jar ./target/magic.wormhole-0.0.1-SNAPSHOT.jar send -p=<filePath>`

