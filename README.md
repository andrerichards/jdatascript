# jdatascript

Java wrapper for [DataScript](https://github.com/tonsky/datascript)

## Usage
    
[Maven](http://maven.apache.org/) dependency information:
```xml
    <dependency>
      <groupId>defng</groupId>
      <artifactId>jdatascript</artifactId>
      <version>0.4.0</version>
    </dependency>
    
    <repository>
      <id>clojars.org</id>
      <url>http://clojars.org/repo</url>
    </repository>
```

For example code, see the [tests](https://github.com/andrerichards/jdatascript/blob/master/test/defng/jdatascript/JDataScriptTest.java).

## Rationale
[DataScript](https://github.com/tonsky/datascript) has good support for JavaScript, and is usually 'advertised' as useful for 'client-side applications that need to track a lot of state', e.g. as the single store in the 'redux model'.

However, DataScript also runs on the JVM, and is generally useful for working with deeply nested data structures. 
* The datalog query engine (`q`) and `pull` API provide powerful tools to pull specific data out
* The `dbWith` API provides a convenient way of updating immutable data structures in a local scope
* The `transact` API provides a powerful way to update immutable data structures that are shared, even across multiple threads

DataScript use is convenient on the JVM from Clojure, but not so easy to use from Java, in a way that is familiar to Java programmers. JDataScript tries to fill this gap - because a tool this useful should be available to all Java programmers!

## Project status
Alpha, but there is very little code - it really just wraps the most useful DataScript functions in Java static methods.

The full DataScript API is not yet supported, but the following is provided:
* emptyDB()
* emptyDB(Map schema)
* createConn()
* createConn(Map schema)
* connFromDB(DB db)
* db(Atom conn)
* dbWith(DB db, List txData)
* transact(Atom conn, List txData)
* q(Object query, java.lang.Object... inputs)
* pull(DB db, String pattern, Object entityId)

Things that make it 'a bit more Java':
* enum: Operation.ADD, Operation.RETRACT
* tx(Operation operation, Object entityID, String key, Object value) : convenience method to create a transaction 


For example code, see the [tests](https://github.com/andrerichards/jdatascript/blob/master/test/defng/jdatascript/JDataScriptTest.java).

## License

Copyright © 2017 Andre Richards

Licensed under Eclipse Public License (see [LICENSE](LICENSE)).
