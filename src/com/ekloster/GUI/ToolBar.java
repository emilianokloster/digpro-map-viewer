package com.ekloster.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {
	
	public ToolBar() {
		super(JToolBar.HORIZONTAL);
		setFloatable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
		setBackground(Color.white);
		setPreferredSize(new Dimension(600, 30));
	}
	
}
class ToolBarButton extends JButton {
	public ToolBarButton(String text, String toolTip, ActionListener listener) {
		super();
		setVerticalTextPosition(BOTTOM);
		setHorizontalTextPosition(CENTER);
		setFont(new Font("sansserif",Font.PLAIN,10));
		setText(text);
		setToolTipText(toolTip);
		setPreferredSize(new Dimension(110, 20));
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		setFocusPainted(false);
		addActionListener(listener);
	}
}