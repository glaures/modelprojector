# ModelProjector

## Purpose

The purpose of this **Java** library is to provide an easy and light-weight approach to **project properties from one model to another**.

This is especially useful if you require a **DTO-like** representation of an object or list of objects that you do not want to expose to clients as is.

## Example

Within a web application you are working with **service** and **view** layers. The service layer work with **entities** retrieved from a data source, the view layer needs to provide **serializable representation** (often called **DTOs**) of those entities which are typically used in **REST-services** or **template engines** to render views.


**Person.java**

```java
public class Person {

    private long id;
    private String login;
    private String password;

    public long getId(){
        return this.id;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
```

**PersonDto.java**

```java
public class PersonDto {

    private long id; 
    private String email;

    public long getId(){
        return this.id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @Projection(propertyName = "login")
    public void setEmail(String email) {
        this.email = email;
    }

}
```

To convert a `Person` p into a `PersonDto` you simply call `Projector.project(p, PersonDto.class)` which will return the projection of the person as a `PersonDto`.

As you might have noticed there is no annotation required if the property names of the source object match those of the projection. In case the projected object is using different names (but the same type) you can specify the name of the source object's property within your projection class (see property `email` in the above example).

And there is much more to discover:
* map nested objects onto their projection classes
* map nested objects onto their ID property
* map lists of nested objects onto a list of references or projection classes

All of that is explained in detail in the Developer's guide below.

## Installation

Either checkout the repository and build it yourself, download it from here or use the Maven component in your pom.xml like this:

**pom.xml**
```xml
        <dependency>
            <groupId>de.sandkastenliga</groupId>
            <artifactId>modelprojector</artifactId>
            <version>1.0.0</version>
        </dependency>
```
# Developer's guide
To get started with ModelProjector you will have to define a projected version (DTO) of an object that you would like to project. Let's get started with the most simplest example.
## Source and Target Classes
When using ModelProjector you always will have the use case to project the properties from one object (source) to another one (target). The nice thing about ModelProjector is that there is not much to do to achieve that - especially not if the names and types of the source and target objects are the same.

In fact you do not have to change anything in the source- nor target-classes if they are the same. You simply call
```java
Projector.project(source, targetClass).
```
This will return an instance of `targetClass`. All properties of the source object that have the same name and type of properties in the target class will be copied into the returned target objects.
This is the simplest type of projection that you can use with ModelProjector. 
To define which projection type to use for certain properties on the target class you can use the annotations provided by the ModelProjector library.
Let's see what options it provides to us:

## Project properties "as is"

To project a property from one object onto another 1:1 you do not have to annotate anything in the target class. It will work out-of-the-box.

#### Source class
**Person.java**
```java
public class Person {
    
    private String email;
    
    public String getEmail(){
        return login;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
}
```
#### Target class
**PersonDto.java**
```java
public class PersonDto {
    
    private String email;
    
    public String getEmail(){
        return email;
    }
    
    @Projection(ProjectionType.asIs)
    public void setEmail(String email){
        this.email = email;
    }
}
```
> The annotation on what projection to use will always be on the **setter method** of the target class.

> This annotation is **optional** as the projection type `@ProjectionType.asIs` is the default value and can therefore be omitted.

> If you want ModelProjector to completely ignore a property on the target object when generating a projection even if there is a corresponding property on the source object  you can use `@ProjectionType.none` on the setter method of the target class.

## Projection on a property with different name

But what if you have different names in source and target object? Well, you simply tell ModelProjector from where to get the property value using the annotation parameter `propertyName`.

In the example above this would look like this:

#### Target class
**PersonDto.java**
```java
 public class PersonDto {
     
     private String login;
     
     public String getLogin(){
         return login;
     }
     
     @Projection(value = ProjectionType.asIs, propertyName = "email")
     public void setLogin(String login){
         this.login = login;
     }
 }
 ```
 
The annotation `@Projection(value = ProjectionType.asIs, propertyName = "email")` tells ModelProjector to use the email property on the source object and project it on the PersonDto class' property `login`.
 
## Projection on a property of a non-simple property like refence ID
 
In some cases you might have more complex data structures with nested or referenced objects as properties. ModelProjector is capable of not just project a complex property onto a complex one in the target object (using `ProjectionType.asIs`, see above) but also to project just a property of a complex property using `ProjectionType.property`.

This is especially useful if you just want to use a referenced object's ID as value in your target object to avoid copying complex object hierarchies entirely.

Let's have a look at a typical example:

#### Source classes
 
**Person.java**
 ```java
public class Person {
    
    private Long id;
    private Person marriedTo;
     
    public Long getId() {
        return id;
    }

    public Person getMarriedTo() {
        return marriedTo;
    }
}
```
 
#### Target class
 
**PersonDto**
```java
public class PersonDto {

    private long husbandOrWifeId;
       
    @Projection(value = ProjectionType.property, propertyName = "marriedTo")
    public void setHusbandOrWifeId(long husbandOrWifeId) {
        this.husbandOrWifeId = husbandOrWifeId;
    }
}
```

In this example a `Person` references its husband or wife in the property `marriedTo`. If we want to project such an object to a DTO representation we might end up with endless loops of projection each of them referencing each other. To avoid this you can use `ProjectionType.property`.

> By default the property named `id` is used as the projected value.

If you want to project a different value from the referenced object you can specify this using an additional paramter like this:

```java 
@Projection(value = ProjectionType.property, propertyName = "marriedTo", referencePropertyName="name")
public void setHusbandOrWifeName(String husbandOrWifeName) {
    this.husbandOrWifeName = husbandOrWifeName;
}
``` 
   
## Projection on a projection
 
Instead of projecting just one specific property of a referenced object you can also project the projection of the property.

 Let's have a look at a typical example:
 
#### Target classes
  
**Person.java**
 ```java
public class Person {
    
    private Addess address;

    public Addess getAddress() {
        return address;
    }
}
```

**PersonDto**
```java
public class PersonDto {
    
    private AddressDto address;
    
    @Projection(value = ProjectionType.projection)
    public void setAddress(AddressDto address){
        this.address = address;
    }
}
```

**AddressDto**
```java
public class AddressDto {
   
   private String city;
   
   public void setCity(String city){
       this.city = city;
   }
}
```

In the example aboove the source object's property `address` is of type `Address`. In some scenarios you might want to use a projected version of an address in the projection. This can by achieved by using `ProjectionType.projection`. If the property names match this is all you need to do. If names differ the same rules apply as mentioned above.

## Projection of collection properties

In case a source object's property is a collection, you might want to apply a projection type onto the entire collection. You can use `ProjectionType.propertyCollection` and `ProjectionType.projectionCollection` to achieve that.

Let's have a look at an example for this:

**Person.java**
```java
public class Person {

    public List<Person> getChildren() {
        return children;
    }

    public List<RealEstate> getHomes() {
        return homes;
    }
}
```

**PersonDto**
```java
public class PersonDto {

    @Projection(value = ProjectionType.propertyCollection, propertyName = "children")
    public void setChildrenIds(List<Long> childrenIds) {
        this.childrenIds = childrenIds;
    }

    @Projection(ProjectionType.projectionCollection)
    public void setHomes(List<RealEstateDto> homes) {
        this.homes = homes;
    }

```

In this example we show that a list of children of type `Person` is projected to a list of those peeople's ```id``` (as we have omitted the `referenceProperyName` here).
Furthermore a list of type `RealEstate` is projected onto a list of type `RealEstateDto`.

## Summary

With ModelProjector you are able to project any bean-like class onto another class with no impact on the source object's class and only a few (if any) annotations to the target class. This even applies to more complex scenarios where nested objects need to be projected onto consumable structures to kind of _flatten_ then for serialization or other purposes.

This allows for a clearer separation of concerns in web application frameworks like Spring MVC where you might want to use different object models for the persistence layer and the presentation(-service) layer. This is especially useful if you do not want to apply the [anti-pattern](https://vladmihalcea.com/the-open-session-in-view-anti-pattern/) of switching on `spring.jpa.open-in-view=true` which very often leads to `LazyInitializationException` as raised as an issue [here](https://github.com/spring-projects/spring-boot/issues/7107).

ModelProjector ensures clear data structures to travers on objects entirely detached from the underlying retrieval or persistence mechanism allowing a better separation of data retrieval in backend layers and data consumption in frontend services (like REST-service) and template engines.

## FAQs

**What is the best way of integrating ModelProjector in Spring MVC / Spring Boot?**

Even though the `Projector`has a default constructor and does not need to be configured in any way can not guarantee that this will stay the same in future versions. Hence, we recommend to create one cenral  `@Bean` instance in one of your `@Configuration` classes and autowire them in components that need it like this:
```java
// in configuration classes
@Bean
public Projector projector(){
    return new Projector();
}

// in client components
@Autowired
private Projector projector;
```

**What is the best way to project a list of objects?**

If you want to project a list of objects in one go in Java 8+ you can make use of the map-collect-pattern like this:
        
`sourceObjectList.stream().map(o -> projector.project(o, TargetClass.class)).collect(Collectors.toList());`