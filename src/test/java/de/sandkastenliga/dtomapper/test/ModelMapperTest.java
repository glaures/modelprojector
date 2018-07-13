package de.sandkastenliga.dtomapper.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sandkastenliga.tools.projector.core.Projector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class ModelMapperTest {

    private Person husband;
    private Person person;
    private Address address;
    private Projector projector = new Projector();
    private GsonBuilder gb = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gb.create();

    @Before
    public void setupTestData(){
        husband = new Person();
        husband.setId(34L);
        person = new Person();
        person.setId(1L);
        person.setLogin("test@test.de");
        person.setPassword("test1234");
        person.setMarriedTo(husband);
        person.setName("Test Person");
        address = new Address();
        address.setPerson(person);
        address.setCity("Testcity");
        address.setStreet("teststreet");
        address.setZip("012345");
        address.setHousenumber("123");
        person.setAddress(address);
        Person child = new Person();
        child.setId(2131L);
        child.setName("Child 1");
        person.getChildren().add(child);
        Person child2 = new Person();
        child2.setId(2931L);
        child2.setName("Child 2");
        person.getChildren().add(child2);
        RealEstate re = new RealEstate();
        Address a = new Address();
        a.setCity("Leipzig");
        re.setAddress(a);
        re.setName("Home 1");
        person.getHomes().add(re);
    }

    @Test
    public void testProjections() {
        PersonDto aDto = projector.project(person, PersonDto.class);
        Assert.assertEquals(person.getName(), aDto.getName());
        Assert.assertEquals(person.getLogin(), aDto.getEmail());
        Assert.assertEquals((long) person.getMarriedTo().getId(), aDto.getHusbandOrWifeId());
        Assert.assertEquals(aDto.getAddress().getClass(), AddressDto.class);
        Assert.assertEquals(person.getAddress().getCity(), aDto.getAddress().getCity());
        System.out.println(gson.toJson(aDto));
    }

}
