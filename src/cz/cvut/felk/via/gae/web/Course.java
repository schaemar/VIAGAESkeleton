package cz.cvut.felk.via.gae.web;

import java.io.Serializable;
import java.net.URI;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060235481226039140L;

	@PrimaryKey
	@Persistent
	private Key key;
	@Persistent
	private String code;

	@Persistent
	private String name;

	@Persistent
	private String uri;
	

	public Course(String code, String name) {
		super();
		this.code = code;
		this.name = name;
		this.uri = "/course/" + this.code;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getUri() {
		return this.uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
		
	}

}
