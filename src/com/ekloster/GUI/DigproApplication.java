package com.ekloster.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ekloster.UseCases.CoordinatesCSVParser;
import com.ekloster.UseCases.HttpCoordinatesFetcher;
import com.ekloster.entities.Coordinate;

public class DigproApplication extends JFrame {
	
	private final String COORDINATES_SOURCE = 
			"http://daily.digpro.se/bios/servlet/bios.servlets.web.RecruitmentTestServlet";
	
	private HttpCoordinatesFetcher fetcher;
	private MapPanel map;
	private JPanel mainContainer;
	private Timer timer;
	private ToolBarButton reloadButton;
	private JLabel foot;
	
	public static void main(String[] args) {
		new DigproApplication();
	}
	
	public DigproApplication() {
        super("Digpro Map Viewer");
		fetcher = new HttpCoordinatesFetcher(COORDINATES_SOURCE, new CoordinatesCSVParser());
		EventQueue.invokeLater(() -> runApp());
	}

	private void runApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        map = new MapPanel();
        mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(getToolBar(), BorderLayout.NORTH);
        mainContainer.add(map, BorderLayout.CENTER);
        mainContainer.add(foot(), BorderLayout.SOUTH);

        timer = new Timer(30000, (event) -> updateMap());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainContainer);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        timer.setInitialDelay(0);
        timer.start();
	}
	
	private JToolBar getToolBar() {
        ToolBar topToolBar = new ToolBar();
        
        topToolBar.add(disableAutoReloadCheckBox());
        
        reloadButton = new ToolBarButton("Reload", "Refresh map with new data", 
        		e -> updateMap());
        
        topToolBar.add(reloadButton);
        topToolBar.add(new ToolBarButton("About", "Display info about dev team", 
        		e -> about(this)));
        
        topToolBar.add(new ToolBarButton("Exit", "Close the application", 
        		e -> System.exit(0)));
        
		return topToolBar;
	}
	
	private JCheckBox disableAutoReloadCheckBox() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setFont(new Font("sansserif",Font.PLAIN,9));
		checkBox.setText("Disable auto-reload");
		checkBox.setPreferredSize(new Dimension(130, 20));
		checkBox.setFocusPainted(false);
		checkBox.addActionListener(e -> {
			if (checkBox.isSelected()) {
				timer.stop();
			} else {
				timer.restart();
			}
		});
		return checkBox;
	}
	
	private JLabel foot() {
        foot = new JLabel("");
        foot.setOpaque(true);
        foot.setBackground(new Color(60, 195, 165));
        foot.setForeground(Color.DARK_GRAY);
        foot.setPreferredSize(new Dimension(600, 20));
        foot.setHorizontalAlignment(SwingConstants.CENTER);
        return foot;
	}
	
	private JDialog about(JFrame ownerFrame) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Digpro Map Viewer demo"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(new JLabel("This demo has been developed by Emiliano Kloster"));
		panel.add(link("linkedin", "https://www.linkedin.com/in/emilianokloster/"));
		panel.add(link("github", "https://github.com/emilianokloster"));
		
		JDialog about = new JDialog(ownerFrame);
		about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		about.setTitle("About");
		about.add(panel);
		about.setSize(450, 150);
		about.setLocationRelativeTo(ownerFrame);
		about.setVisible(true);
		
		return about;
	}
	
	private void updateMap() {
		reloadButton.setEnabled(false);
		foot.setText("Retrieving data...");
		(new MapUpdater()).execute();
	}

	// Assigns the http request to another thread to prevent UI from freezing
	private class MapUpdater extends SwingWorker<List<Coordinate>, Object> {
		@Override
		// Worker Thread
		protected List<Coordinate> doInBackground() {
			return fetcher.fetchCoordinates();
		}
		
		@Override
		// Event Dispatch Thread
		protected void done() {
    		try {
				map.setPointsOnMap(get());
				foot.setText("OK");
				
			} catch (InterruptedException | ExecutionException e) {
				map.setPointsOnMap(Arrays.asList());
				foot.setText("Couldn't get new coordinates");
				
			} finally {
				reloadButton.setEnabled(true);
				mainContainer.repaint();
			}
		}
	}
	
	private JLabel link(String text, String url) {
		JLabel link = new JLabel(text);
		link.setForeground(Color.BLUE.darker());
		link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		link.addMouseListener(linkMouseListener(url));
		return link;
	}
	
	private MouseListener linkMouseListener(String url) {
		return new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
		};
	}
}
