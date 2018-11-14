module app {

	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.web;
	requires spring.context;

	requires micrometer.core;

	requires javax.inject;

	requires java.transaction;

	requires java.persistence;
	requires org.hibernate.orm.core;
	requires spring.data.commons;
	requires spring.data.jpa;
	requires spring.tx;
	requires java.sql;
	requires ucp;
//	requires ojdbc8;

	requires spring.beans;

}