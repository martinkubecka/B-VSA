## REST

---
**How to create a new web application?**

> New Project -> Java Web (2x Next) -> GlassFish Server (default) & Context Path (change if you want) -> Finish

- We are not going to work with web applications but it will serve us as a web server for our REST services.

**How to create a REST service?**

> Right click on the project -> New -> Other -> Web Services : RESTful Web Services from Patterns -> Simple Root Resource (only one resource)
> Resource Package : rest
> Path ; Class Name ; MIME Type
> Finish

- Path for the REST Services can be found in the `ApplicationConfig.java` :
	- `@javax.ws.rs.ApplicationPath("webresources")` 

**How to test our applications?**

> Inside your project folder right click on the **RESTful Web Services** folder
> Select **Test RESTful Web Services**

**How to create a Java client?**

> New Project -> Java App -> Finish
> Right on the new project -> Web Services : RESTful Java Client
> Package : proxy
> Select the Resource : Browse... -> Choose your web application 

---
Celé znenie úloh [tu](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV8)

---

Use this annotation if you don't want new resource for each request.

`@Singleton` -> import from `javax.inject.Singleton`