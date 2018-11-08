module app {

	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.web;
	requires spring.context;

	requires micrometer.core;

	requires jaxb2.basics.tools;
	requires jaxb2.basics.runtime;

	requires javax.inject;
	requires java.xml;
	requires java.xml.bind;
	requires java.validation;
	requires java.activation;

	requires metaforce.general.service.tjenestespesifikasjon;


}