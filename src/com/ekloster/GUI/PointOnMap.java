package com.ekloster.GUI;

import com.ekloster.entities.Coordinate;

public class PointOnMap extends java.awt.geom.Ellipse2D.Float {
	private Coordinate coordinate;
	
	public PointOnMap(Coordinate coordinate) {
		super(coordinate.x().floatValue(), coordinate.y().floatValue(), 10, 10);
		this.coordinate = coordinate;
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}

}
