package bank.account.core.config;

import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NullPermission;

@Configuration
public class AxonConfig {

	@Value("${spring.data.mongodb.host:localhost}")
	private String host;

	@Value("${spring.data.mongodb.port:27017}")
	private int port;

	@Value("${spring.data.mongodb.database:bankaccountdb}")
	private String database;

	
	@Bean
	public MongoClient mongoClient() {
		String uri = "mongodb://"+host+":"+port;
		MongoClient mongoClient =  MongoClients.create(uri);
		return mongoClient;
	}
	
	@Bean
	public MongoTemplate axonMongoTemplate() {
		return DefaultMongoTemplate.builder()
				.mongoDatabase(mongoClient(), database)
				.build();
	}
	
	@Bean
    public TokenStore tokenStore(Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(axonMongoTemplate())
                .serializer(serializer)
                .build();
    }
	
	@Bean
	public EventStorageEngine storageEngine(MongoClient client) {
		MongoTemplate template = DefaultMongoTemplate.builder()
				.mongoDatabase(client)
				.build();
		return MongoEventStorageEngine.builder()
				.mongoTemplate(template)
				.snapshotSerializer(JacksonSerializer.defaultSerializer())
				.eventSerializer(JacksonSerializer.defaultSerializer())
				.build();
	}

	@Bean
	public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
		return EmbeddedEventStore.builder()
				.storageEngine(storageEngine)
				.messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
				.build();
	}
	
	@Bean
	public XStream xStream() {
		XStream xstream = new XStream();
		xstream.allowTypesByWildcard(new String[] { 
		        "user.core.**",
		        "user.query.api.**",
		        "user.cmd.api.**"
		        });
		xstream.addPermission(NullPermission.NULL);
		return xstream;
	}
	

}
