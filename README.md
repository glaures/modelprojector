# ModelProjector

## Purpose

The purpose of this **Java** library is to provide an easy and light-weight approach to **project properties from one model to another**.

This is especially useful if you require a **DTO-like** representation of an object that you do not want to expose to clients.

## Example

Within a web application you are working with **service** and **view** layers. The service layer work with **entities** retrieved from a data source, the view layer needs to provide **serializable representation** (often called **DTOs**) of those entities which are typically used in **REST-services** or **template engines** to render views.


**Person.java**

```java
public class Person {

    private Long id;
    private String name;
    private String login;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```

**PersonDto.java**

```java
public class PersonDto {

    private long id;
    private String email;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

To convert a ``Person`` p into a ``PersonDto`` you simply call ``project(p, PersonDto.class)`` which will return the projection of the person as a ``PersonDto``.

As you might have noticed there is no annotation required if the property names of the source object map those of the projection. In case the projected object is using different names (but the same type) you can specify the name of the source object's property within your projection class (see property ``email`` in the above example).

And there is much more to discover:
* map nested objects onto their projection classes
* map nested objects onto their ID property
* map lists of nested objects onto a list of references or projection classes

All of that is explained in detail in the Developer's guide below.

## Installation

Either checkout the repository and build it yourself, download it from here or use the Maven component in your pom.xml like this:
...

# Developer's guide
