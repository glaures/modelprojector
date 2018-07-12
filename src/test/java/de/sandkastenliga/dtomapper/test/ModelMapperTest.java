package de.sandkastenliga.dtomapper.test;

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
    }

    @Test
    public void testSimpleProjectionsWithoutAnnotations() {
        PersonDto aDto = projector.project(person, PersonDto.class);
        Assert.assertEquals(person.getName(), aDto.getName());
    }

    @Test
    public void testSimpleProjectionsFromDifferentName() {
        PersonDto aDto = projector.project(person, PersonDto.class);
        Assert.assertEquals(person.getLogin(), aDto.getEmail());
    }

    @Test
    public void testReferenceProjection() {
        PersonDto aDto = projector.project(person, PersonDto.class);
        Assert.assertEquals((long) person.getMarriedTo().getId(), aDto.getHusbandOrWifeId());
    }

    @Test
    public void testProjectionProjection() {
        PersonDto aDto = projector.project(person, PersonDto.class);
        Assert.assertEquals(aDto.getAddress().getClass(), AddressDto.class);
        Assert.assertEquals(person.getAddress().getCity(), aDto.getAddress().getCity());
    }

}
