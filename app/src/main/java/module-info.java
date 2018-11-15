open module app {

	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.beans;
	requires spring.context;
	requires spring.data.commons;
	requires spring.data.jpa;
	requires spring.tx;
	requires spring.web;

	requires micrometer.core;

	requires java.naming;
	requires java.persistence;
	requires java.transaction;
	requires java.sql;
	requires java.xml.bind;

	requires javax.inject;

	requires net.bytebuddy;

	requires org.hibernate.orm.core;

	requires ojdbc8;
}