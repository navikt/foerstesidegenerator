module app {

	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.web;
	requires spring.context;

	requires no.nav.dok.tjenester.foerstesidegenerator;

	requires org.apache.logging.log4j;

	requires micrometer.core;

	requires javax.inject;

}