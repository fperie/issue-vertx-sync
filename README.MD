## issue-vertx-sync

This project shows an issue to use the vertx sync project in the last version (3.5.4 or 3.6.0-SNAPSHOT).


## A simple test verticle : MyVerticleForTesting.java

You will find a simple test verticle (cf. MyVerticleForTesting class) with just a method to load an content file in using the fibers :

```java
@Suspendable
public String loadTheDormeurDuValPoem()
{
	final String result;
	final boolean exists = Sync.awaitResult(h -> vertx.fileSystem().exists("./le_dormeur_du_val.txt", h));
	
	if (exists)
	{
		final Buffer buf = Sync.awaitResult(h -> vertx.fileSystem().readFile("./le_dormeur_du_val.txt", h));
		result = buf.toString();
	}
	else
	{
		result = "";
	}
	
	return result;
}
```

## The tests
 
The other class is the class to launch the junit 5 tests.
 
When I run the "mvn clean install" command, I get the following error : 
 

```java
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 2.026 s <<< FAILURE! - in fr.issue.vertx.sync.TestVertxSyncInMyVerticleForTesting
[ERROR] testLoadTheDormeurDuValPoem{VertxTestContext, Vertx}  Time elapsed: 0.46 s  <<< ERROR!
io.vertx.core.VertxException: java.lang.IllegalThreadStateException: Method called not from within a fiber
        at fr.issue.vertx.sync.TestVertxSyncInMyVerticleForTesting.testLoadTheDormeurDuValPoem(TestVertxSyncInMyVerticleForTesting.java:76)
Caused by: java.lang.IllegalThreadStateException: Method called not from within a fiber
        at fr.issue.vertx.sync.TestVertxSyncInMyVerticleForTesting.testLoadTheDormeurDuValPoem(TestVertxSyncInMyVerticleForTesting.java:76)
</code>
```