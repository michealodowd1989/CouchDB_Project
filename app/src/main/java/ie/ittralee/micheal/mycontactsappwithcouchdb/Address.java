package ie.ittralee.micheal.mycontactsappwithcouchdb;

/**
 * Created by micheal on 14/10/2017.
 */


public class Address {
    private String addressLineOne;
    private String addressLine2;
    private String town;
    private String county;
    private String postcode;


    public Address(String addressLineOne, String addressLine2, String town, String county, String postcode){
        this.addressLineOne = addressLineOne;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "\n\nAddress 1: " + addressLineOne + "\n\nAddress 2: " + addressLine2 + "\n\nTown: " + town + "\n\nCounty: " + county +
                        "\n\nPostcode: " + postcode;
    }
}
