package model;

import java.util.Objects;

/**
 *
 * @author
 */
public class Invoice {

    private Integer id;
    private Boolean paid;
    //Save forename+lastname - if it does not exist: company name;
    private String namecompanyname;
    private String street;
    private Integer areacode;
    private String city;
    private String county;
    private String country;
    private Integer sum;
    private String currencyiso;
    private String note;

    public Invoice(Integer id, Boolean paid, String namecompanyname, String street, Integer areacode, String city, String county, String country, Integer sum, String currencyiso, String note) {
        this.id = id;
        this.paid = paid;
        this.namecompanyname = namecompanyname;
        this.street = street;
        this.areacode = areacode;
        this.city = city;
        this.county = county;
        this.country = country;
        this.sum = sum;
        this.currencyiso = currencyiso;
        this.note = note;
    }

    public Invoice() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getNamecompanyname() {
        return namecompanyname;
    }

    public void setNamecompanyname(String namecompanyname) {
        this.namecompanyname = namecompanyname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getAreacode() {
        return areacode;
    }

    public void setAreacode(Integer areacode) {
        this.areacode = areacode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getCurrencyiso() {
        return currencyiso;
    }

    public void setCurrencyiso(String currencyiso) {
        this.currencyiso = currencyiso;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.paid);
        hash = 23 * hash + Objects.hashCode(this.namecompanyname);
        hash = 23 * hash + Objects.hashCode(this.street);
        hash = 23 * hash + Objects.hashCode(this.areacode);
        hash = 23 * hash + Objects.hashCode(this.city);
        hash = 23 * hash + Objects.hashCode(this.county);
        hash = 23 * hash + Objects.hashCode(this.country);
        hash = 23 * hash + Objects.hashCode(this.sum);
        hash = 23 * hash + Objects.hashCode(this.currencyiso);
        hash = 23 * hash + Objects.hashCode(this.note);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Invoice other = (Invoice) obj;
        if (!Objects.equals(this.namecompanyname, other.namecompanyname)) {
            return false;
        }
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.county, other.county)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.currencyiso, other.currencyiso)) {
            return false;
        }
        if (!Objects.equals(this.note, other.note)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.paid, other.paid)) {
            return false;
        }
        if (!Objects.equals(this.areacode, other.areacode)) {
            return false;
        }
        if (!Objects.equals(this.sum, other.sum)) {
            return false;
        }
        return true;
    }

}
