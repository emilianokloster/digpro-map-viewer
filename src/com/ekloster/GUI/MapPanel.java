package com.ekloster.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import com.ekloster.entities.Coordinate;

public class MapPanel extends JPanel {
	private List<PointOnMap> pointsOnMap;
	private Color pointsColor;
	
	public MapPanel() {
		pointsOnMap = new ArrayList<>();
		pointsColor = new Color(15, 115, 100);
		setPreferredSize(new Dimension(700, 500));
		setToolTipText("");
	}
	
	public void setPointsOnMap(List<Coordinate> coords) {
		pointsOnMap = coords.stream()
			.map(coordinate -> new PointOnMap(coordinate))
			.collect(Collectors.toList());
	}
	
	@Override
	public String getToolTipText(MouseEvent event) {
		for (PointOnMap point : pointsOnMap) {
			if (point.contains(event.getPoint())) {
				return point.getCoordinate().description();
			}
		}
		return null;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D graphic = (Graphics2D) g.create();
		graphic.setColor(pointsColor);
		for (PointOnMap pointOnMap : pointsOnMap) {
			graphic.fill(pointOnMap);
		}
		graphic.dispose();
	}
}
