package org.codehaus.groovy.grails.orm.hibernate

import org.hibernate.Hibernate

/**
 * @author Graeme Rocher
 * @since 1.0
 *
 * Created: Oct 27, 2008
 */
class FetchMappingTests extends AbstractGrailsHibernateTests {

	protected void onSetUp() {
		gcl.parseClass '''
import grails.persistence.*

@Entity
class FetchMappingBook {
	String title
}

@Entity
class FetchMappingAuthor {
	String name
	static hasMany = [books:FetchMappingBook]
}

@Entity
class FetchMappingPublisher {
	String name
	static hasMany = [books:FetchMappingBook]

	static mapping = {
		books fetch:'join'
	}
}
'''
	}

	void testFetchMapping() {
		def authorClass = ga.getDomainClass("FetchMappingAuthor").clazz
		def publisherClass = ga.getDomainClass("FetchMappingPublisher").clazz

		def author = authorClass.newInstance(name:"Stephen King")
		                        .addToBooks(title:"The Shining")
		                        .addToBooks(title:"The Stand")
		                        .save(flush:true)

		def publisher = publisherClass.newInstance(name:"Apress")
		                              .addToBooks(title:"DGG")
		                              .addToBooks(title:"BGG")
		                              .save(flush:true)

		assertNotNull author
		assertNotNull publisher

		session.clear()

		author = authorClass.get(1)
		assertFalse "books association is lazy by default and shouldn't be initialized",Hibernate.isInitialized(author.books)

		publisher = publisherClass.get(1)
		assertTrue "books association mapped with join query and should be initialized",Hibernate.isInitialized(publisher.books)
	}
}
