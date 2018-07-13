package de.sandkastenliga.dtomapper.test;

import de.sandkastenliga.tools.projector.core.ProjectionType;
import de.sandkastenliga.tools.projector.core.Projection;

import java.util.ArrayList;
import java.util.List;

public class PersonDto {

    private long id;
    private String email;
    private String name;
    private AddressDto address;
    private long husbandOrWifeId;
    private List<Long> childrenIds = new ArrayList<Long>();
    private List<RealEstateDto> homes = new ArrayList<RealEstateDto>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @Projection(propertyName = "login")
    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDto getAddress() {
        return address;
    }

    @Projection(ProjectionType.projection)
    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public long getHusbandOrWifeId() {
        return husbandOrWifeId;
    }

    @Projection(value = ProjectionType.property, propertyName = "marriedTo")
    public void setHusbandOrWifeId(long husbandOrWifeId) {
        this.husbandOrWifeId = husbandOrWifeId;
    }

    public List<Long> getChildrenIds() {
        return childrenIds;
    }

    @Projection(value = ProjectionType.propertyCollection, propertyName = "children")
    public void setChildrenIds(List<Long> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public List<RealEstateDto> getHomes() {
        return homes;
    }

    @Projection(ProjectionType.projectionCollection)
    public void setHomes(List<RealEstateDto> homes) {
        this.homes = homes;
    }
}
