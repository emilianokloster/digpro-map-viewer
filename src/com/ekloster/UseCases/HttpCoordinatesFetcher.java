package com.ekloster.UseCases;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import com.ekloster.UseCases.interfaces.CoordinatesFetcher;
import com.ekloster.UseCases.interfaces.CoordinatesParser;
import com.ekloster.entities.Coordinate;

public class HttpCoordinatesFetcher implements CoordinatesFetcher {
	private HttpRequest request;
	private HttpResponse<String> response;
	private CoordinatesParser coordinatesParser;

	@Override
	public List<Coordinate> fetchCoordinates() {
		List<String> responseLines = getResponse().body()
				.lines()
				.filter(line -> !line.startsWith("#") && line.length() > 0)
				.collect(Collectors.toList());
		return coordinatesParser.parseCoordinatesFrom(responseLines);
	}
	
	public HttpCoordinatesFetcher(String url, CoordinatesParser parser) {
		// Parser's implementation will depend on the source's format 
		coordinatesParser = parser;
		try {
			this.request = HttpRequest.newBuilder()
					.uri(new URI(url))
					.GET()
					.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private HttpResponse<String> getResponse() {
		try {
			this.response = HttpClient.newBuilder()
					.build()
					.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return this.response;
	}
}
