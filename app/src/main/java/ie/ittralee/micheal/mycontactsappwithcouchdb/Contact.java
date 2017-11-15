package ie.ittralee.micheal.mycontactsappwithcouchdb;

/**
 * Created by micheal on 14/10/2017.
 */

public class Contact {
    private String moblePhoneNum;
    private Address address;

    public Contact(String moblePhoneNum) {
        this.moblePhoneNum = moblePhoneNum;
    }

    public Contact( String moblePhoneNum, Address address) {
        this.moblePhoneNum = moblePhoneNum;
        this.address = address;
    }

    public String getMoblePhoneNum() {
        return moblePhoneNum;
    }

    public void setMoblePhoneNum(String moblePhoneNum) {
        this.moblePhoneNum = moblePhoneNum;
    }

    @Override
    public String toString() {
        if(this.address == null)
        {
            return "\n\nMobile: " + moblePhoneNum;
        }
        return "\n\nMobile: " + moblePhoneNum + this.address.toString();
    }
}
