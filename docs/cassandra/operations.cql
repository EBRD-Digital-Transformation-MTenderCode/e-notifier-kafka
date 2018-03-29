CREATE KEYSPACE ocds
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 3
};


CREATE TABLE ocds.notifier_kafka_topic (
	platform_id uuid,
	topic_id text,
	PRIMARY KEY (platform_id)
);
