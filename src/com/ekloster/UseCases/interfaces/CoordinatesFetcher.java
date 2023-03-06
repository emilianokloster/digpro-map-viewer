package com.ekloster.UseCases.interfaces;

import java.util.List;

import com.ekloster.entities.Coordinate;

public interface CoordinatesFetcher {
	public List<Coordinate> fetchCoordinates();
}
