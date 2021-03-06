package org.codehaus.groovy.grails.orm.hibernate

class UnidirectionalListMappingTests extends AbstractGrailsHibernateTests {

	void testUniListMapping() {
		def personClass = ga.getDomainClass("UnidirectionalListMappingPerson")
		def emailClass = ga.getDomainClass("UnidirectionalListMappingEmailAddress")

		def p = personClass.newInstance()

		def e = emailClass.newInstance()

		p.firstName = "Fred"
		p.lastName = "Flintstone"

		e.email = "fred@flintstones.com"
		p.addToEmailAddresses(e)

		p.save()

		session.flush()
		session.clear()

		assert p.id
		assert e.id

		def e2 = emailClass.newInstance()
		e2.email = "foo@bar.com"
		e2.save()
		session.flush()

		assert e2.id
	}

	protected void onSetUp() {
		gcl.parseClass '''
class UnidirectionalListMappingEmailAddress {
	Long id
	Long version
	String email
}

class UnidirectionalListMappingPerson {
	Long id
	Long version
	String firstName
	String lastName
	List emailAddresses
	static hasMany = [emailAddresses:UnidirectionalListMappingEmailAddress]
}'''
	}
}
