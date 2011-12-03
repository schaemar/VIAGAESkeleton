package cz.cvut.felk.via.gae.web;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Post implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6117967124186661938L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String name;

	@Persistent
	private Date date;

	@Persistent
	private Text text;

	@Persistent
	private Course course;

	@Persistent
	private String uri;

	public Post(long key, String name, Date date, Text text, Course course) {
		super();
		this.key = key;
		this.name = name;
		this.date = date;
		this.text = text;
		this.course = course;
		if (uri == null) {
			this.uri = "/post/" + this.key;

		}
	}

	// Accessors for the fields. JDO doesn't use these, but your application
	// does.

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public long getKey() {
		return this.key;
	}

	public String getUri() {
		
		return this.uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}

}