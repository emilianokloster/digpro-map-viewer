package com.ekloster.entities;

import java.util.Objects;

// Could be used to represent any domain entity on a map, as an attribute of that object
public record Coordinate (Integer x, Integer y, String description) {
	public Coordinate {
		Objects.requireNonNull(x);
		Objects.requireNonNull(y);
		Objects.requireNonNull(description);
	}
}