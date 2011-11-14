package cz.cvut.felk.via.gae.web;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class PostalAddress implements Serializable {

	private static final long serialVersionUID = 1728167041538065348L;

	/** street and number */
	@NotBlank
	private String street;
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String country;
	
	@NotBlank
	private String zipCode;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
