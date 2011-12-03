package cz.cvut.felk.via.gae.web;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Controller
public class PostController {
	private Map<URI, PostalAddress> cache;

	PersistenceManager pm = PMF.get().getPersistenceManager();

	@PostConstruct
	void init() {
		cache = new HashMap<URI, PostalAddress>();
	}

	// POSTS //////////////////

	@RequestMapping(value = "/posts/", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Collection<URI> listPosts() {
		javax.jdo.Query q = pm.newQuery(Post.class);
		q.setResult("uri");
		Collection<URI> res = (Collection<URI>) q.execute();
		return res;

	}

	@RequestMapping(value = "/posts/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Post create(@RequestBody @Valid Post post,
			HttpServletResponse response) {
		
		
		pm.makePersistent(post);
		final URI uri = URI.create("/post/" + (post.getKey()));
		post.setUri(uri.toString());
		response.setHeader("Location", uri.toString()); // tell client where the
														// new resource resides

		return post;
	}
	@RequestMapping(value = "/posts/{from}", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Collection<Post> list(@PathVariable long from) {
		javax.jdo.Query q = pm.newQuery(Post.class);
		q.setOrdering("date descending");
		q.setRange(from, from + 20);
		q.setResultClass(Post.class);

		return (Collection<Post>) q.execute();

	}
	
	@RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Post readPost(@PathVariable long post_id) {
		return (Post) pm.getObjectById(Post.class, post_id);

	}
	@RequestMapping(value="/post/{post_id}", method=RequestMethod.PUT)
	@ResponseBody
	public Post updatePost(@RequestBody @Valid Post post, @PathVariable Long post_id) throws ResourceNotFoundException {
		
		try{
		Post p = (Post)pm.getObjectById(Post.class,post_id);
		pm.deletePersistent(p);
		pm.makePersistent(post);
		final URI uri = URI.create("/post/" + (post.getKey()));
		post.setUri(uri.toString());
		}catch (Exception e) {
		throw new ResourceNotFoundException(URI.create("/post/"+post_id));	
		}
		return post;
	}
	
	@RequestMapping(value="/post/{post_id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePost(@PathVariable Long post_id) throws ResourceNotFoundException {
		try{Post p = (Post)pm.getObjectById(Post.class,post_id);
		pm.deletePersistent(p);
		}catch (Exception e) {
		throw new ResourceNotFoundException(URI.create("/post/"+post_id));	
		}
	}
	
	//COURSES////////////////////
	
	@RequestMapping(value = "/courses/", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Collection<URI> listCourses() {

		javax.jdo.Query q = pm.newQuery(Course.class);
		q.setResult("uri");
		Collection<URI> res = (Collection<URI>) q.execute();
		return res;

	}

	@RequestMapping(value = "/courses/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Course createCourse(@RequestBody @Valid Course course,
			HttpServletResponse response) {
		Key key = KeyFactory.createKey(Course.class.getSimpleName(), course
				.getCode());
		course.setKey(key);
		
		final URI uri = URI.create("/course/" + (course.getCode()));
		course.setUri(uri.toString());
		pm.makePersistent(course);
		response.setHeader("Location", uri.toString()); // tell client where the
														// new resource resides

		return course;
	}

	@RequestMapping(value = "/course/{course_id}", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Course readCourse(@PathVariable String course_id) {
		Key key = KeyFactory.createKey(Course.class.getSimpleName(), course_id);
		return (Course) pm.getObjectById(Course.class, key);

	}
	@RequestMapping(value="/course/{course_id}", method=RequestMethod.PUT)
	@ResponseBody
	public Course updateCourse(@RequestBody @Valid Course course, @PathVariable String course_id) throws ResourceNotFoundException {
		Key key = KeyFactory.createKey(Course.class.getSimpleName(), course_id);
		try {
			Course p = (Course)pm.getObjectById(Course.class,key);
		
		pm.deletePersistent(p);
		pm.makePersistent(course);
		}catch (Exception e) {
			throw new ResourceNotFoundException(URI.create(course_id));
		}
		return course;
	}
	
	@RequestMapping(value="/course/{course_id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCourse(@PathVariable String course_id) throws ResourceNotFoundException {
		Key key = KeyFactory.createKey(Course.class.getSimpleName(), course_id);
		try {
			Course c = (Course)pm.getObjectById(Course.class,key);
			pm.deletePersistent(c);
		}catch (Exception e) {
			throw new ResourceNotFoundException(URI.create(course_id));
		} 
		
		
		
	}



	

	//
	//
	// ////////////////////////////////////////////////////////////////////////////////////////////
	//

	@RequestMapping(value = "/address/", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public URI[] list() {
		return cache.isEmpty() ? new URI[] {} : cache.keySet().toArray(
				new URI[cache.keySet().size()]);
	}

	@RequestMapping(value = "/address/all/", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<URI, PostalAddress> list2() {
		if (cache.isEmpty()) {
			return new HashMap<URI, PostalAddress>();
		}

		else {
			return cache;
		}
	}

	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.GET)
	@ResponseBody
	public PostalAddress read(@PathVariable String addressId)
			throws ResourceNotFoundException {

		final URI id = URI.create("/address/" + addressId);
		if (!cache.containsKey(id)) {
			throw new ResourceNotFoundException(id);
		}

		return (PostalAddress) cache.get(id);
	}

	@RequestMapping(value = "/address/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public PostalAddress create(@RequestBody @Valid PostalAddress address,
			HttpServletResponse response) {

		final URI id = URI.create("/address/" + (new Date()).getTime()); // use
																			// better
																			// id
																			// generator,
																			// this
																			// is
																			// just
																			// demo
																			// ....

		response.setHeader("Location", id.toString()); // tell client where the
														// new resource resides

		cache.put(id, address);

		return address;
	}

	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.PUT)
	@ResponseBody
	public PostalAddress update(@RequestBody @Valid PostalAddress address,
			@PathVariable String addressId) {
		cache.put(URI.create("/address/" + addressId), address);
		return address;
	}

	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String addressId)
			throws ResourceNotFoundException {
		final URI id = URI.create("/address/" + addressId);

		if (cache.containsKey(id)) {
			cache.remove(id);
		} else {
			throw new ResourceNotFoundException(id);
		}
	}

	//

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleNotFoundException(ResourceNotFoundException ex,
			HttpServletResponse response) {
		// ...
	}
}
