package org.codehaus.groovy.grails.orm.hibernate

class MappedByColumn2Tests extends AbstractGrailsHibernateTests {

	void testWithConfig() {
		def airportClass = ga.getDomainClass("Airport")
		def routeClass = ga.getDomainClass("Route")

		def a = airportClass.newInstance()

		a.save()

		def r = routeClass.newInstance()
		a.addToRoutes(r)

		a.save()

		assertEquals 1, a.routes.size()
		assertEquals a, r.destination

		assertNull r.airport
	}

	void onSetUp() {
		gcl.parseClass '''
class Airport {
	Long id
	Long version
	Set routes

	static mappedBy = [routes:'destination']
	static hasMany = [routes:Route]
}

class Route {
	Long id
	Long version

	Airport airport
	Airport destination

	static constraints = {
		airport nullable:true
	}
}
'''
	}
}
