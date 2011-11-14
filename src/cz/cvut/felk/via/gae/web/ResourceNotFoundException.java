package cz.cvut.felk.via.gae.web;

import java.net.URI;

public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 7688861337884061522L;
	
	private final URI uri;
	
	public ResourceNotFoundException(final URI uri) {
		super();
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
}
