# octopus-jsr375
Integration between Octopus and Java EE Security API (Java EE8)

# DEPRECATION NOTICE

Proper Java EE Security API (JSR375) support is now available as Octopus module.
See [here](https://bitbucket.org/contribute-group/javaeesecurityfirst/issues/81/support-for-identitystore-of-java-ee)

## modules

# jsr375

Copy of the jsr375 module of [Arjan Tijms proposal](https://github.com/arjantijms/mechanism-to-store-x)

That code is under the GPL / CDDL license of the JSR375 expert group.

# starterEE7

A modified version of the starter EE 7 application of Octopus that uses the *IdentityStore* for authentication of the user.

The code is tested on Payara (Glassfish) server but should run on any compliant Java EE 7 server.

The example uses the *@EmbeddedIdentityStoreDefinition* ( see *be.rubus.octopus.jsr375.demo.AppAuthentication* ) but any other of the IdentityStore definition can be used.

The web application contains an unsecure index.xhtml page and a secured page which required authentication (and shows the other roles/groups the principal has)

This is the flow of the app:

* Click on the _secure page_ link on the *index.xhtml* page.
* access to this page is intercepted as it is a secured page and there is no principal logged in yet. The *login.xhtml* page is shown.
* User name and passwords are validated by the *IdentityStore* which is defined.
* If valid, the original secured page is shown.
* Page indicates also the other roles the principal has (assuming a 1-to-1 mapping between groups and roles)
