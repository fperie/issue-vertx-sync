package fr.issue.vertx.sync;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
@DisplayName("Test Vertx-Sync API")
public class TestVertxSyncInMyVerticleForTesting
{	
	/** Logger on the class. */
	private static Logger LOGGER = LoggerFactory.getLogger(TestVertxSyncInMyVerticleForTesting.class);
	
	private static final AtomicReference<String> VERTICLE_DEPLOYMENT_ID = new AtomicReference<String>(null);

	private static MyVerticleForTesting VERTICLE;
	
	@BeforeAll
	public static void beforeAll(VertxTestContext context, Vertx vertx) throws Exception
	{
		LOGGER.info("starting on the beforeAll() method...");
		final Checkpoint checkpointBeforeAll = context.checkpoint();
		
		VERTICLE = new MyVerticleForTesting();
		vertx.deployVerticle(VERTICLE, context.succeeding(id -> 
		{
        	// attendre le succès du déploiement
			VERTICLE_DEPLOYMENT_ID.set(id);
			
			LOGGER.info("deploy MyVerticleForTesting verticle    [ OK ]");
			checkpointBeforeAll.flag();
        }));
	}

	@AfterAll
	public static void afterAll(VertxTestContext context, Vertx vertx)
	{
		LOGGER.info("starting on the afterAll() method...");
		String deploymentId = VERTICLE_DEPLOYMENT_ID.get();
		
		if (deploymentId != null && !"".equals(deploymentId.trim()))
		{
			final Checkpoint checkpoint = context.checkpoint();
			vertx.undeploy(deploymentId, new Handler<AsyncResult<Void>>() 
			{
				@Override
				public void handle(AsyncResult<Void> event)
				{
					LOGGER.info("undeploiement MyVerticleForTesting verticle    [ OK ]");
					checkpoint.flag();
				}
			});	
		}
	}
	
	@Test
	public void testLoadTheDormeurDuValPoem(VertxTestContext context, Vertx vertx) throws Exception
	{
		LOGGER.info("unit test 'testLoadTheDormeurDuValPoem' in running...");
		assertThat(VERTICLE.loadTheDormeurDuValPoem()).isNotEmpty().contains("Rimbaud");
		context.completeNow();
	}
}
