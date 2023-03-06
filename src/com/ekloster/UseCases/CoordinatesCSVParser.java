package com.ekloster.UseCases;

import java.util.ArrayList;
import java.util.List;

import com.ekloster.UseCases.interfaces.CoordinatesParser;
import com.ekloster.entities.Coordinate;

public class CoordinatesCSVParser implements CoordinatesParser {

	@Override
	public Coordinate parseFrom(String line) {
		final String[] columns = line.split(",");
		final Integer x_coordinate = Integer.parseInt(columns[0].trim());
		final Integer y_coordinate = Integer.parseInt(columns[1].trim());
		final String description = columns[2].trim();
		return new Coordinate(x_coordinate, y_coordinate, description);
	}

	@Override
	public List<Coordinate> parseCoordinatesFrom(List<String> lines) {
		final List<Coordinate> coordinates = new ArrayList<>();
		for (String line : lines) {
			coordinates.add(parseFrom(line));
		}
		return coordinates;
	}

}
