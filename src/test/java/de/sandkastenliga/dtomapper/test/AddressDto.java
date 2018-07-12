package de.sandkastenliga.dtomapper.test;

import de.sandkastenliga.tools.projector.core.NoProjection;

public class AddressDto {

    private String streetAndNumber;
    private String city;
    private String zip;

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    @NoProjection
    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
