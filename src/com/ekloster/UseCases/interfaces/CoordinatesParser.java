package com.ekloster.UseCases.interfaces;

import java.util.List;

import com.ekloster.entities.Coordinate;

public interface CoordinatesParser {
	
	public Coordinate parseFrom(String line);
	public List<Coordinate> parseCoordinatesFrom(List<String> lines);

}
