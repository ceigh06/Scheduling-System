package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import dao.BuildingDAO;
import dao.EnrolledCoursesDAO;
import view.common.ConfirmPanel;
import view.components.RoundedPanel;
import model.Building;
import model.Course;

public class SearchRooms1 extends JPanel {
	JPanel buildingContainer, selectionWrapper, form, comboPanel, moreFilter, clicked, btnPanel, timeInPanel,
			timeOutPanel;
	JLabel select, timeIn, timeOut, timeInLbl, timeOutLbl, selectCourseLbl, clickFilter, floorLbl, capLbl;
	JScrollPane selectBuilding;
	JCheckBox check;
	JSpinner hrs, mins, cap;
	JComboBox<Course> courseCombo;
	private ConfirmPanel confirmArea;
	JComboBox<String> input;
	boolean toggle = false;

	public SearchRooms1() {

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		selectionWrapper = new JPanel(new BorderLayout());
		selectionWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		selectionWrapper.setMaximumSize(new Dimension(450, 50));
		// HEADER
		select = new JLabel("SELECT BUILDING");
		select.setFont(new Font("Arial", Font.BOLD, 15));
		select.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		selectionWrapper.add(select, BorderLayout.NORTH);

		// ROUNDED PANEL CONTAINER
		RoundedPanel buildings = new RoundedPanel(30, 5, Color.BLACK, new BorderLayout());
		buildings.setPreferredSize(new Dimension(380, 100));
		buildings.setMaximumSize(new Dimension(380, 150));

		// container for building choices
		buildingContainer = new JPanel();
		buildingContainer.setLayout(new GridLayout(0, 1, 5, 5));
		buildingContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		buildingContainer.setOpaque(false);

		// scrollpane for buildings
		selectBuilding = new JScrollPane(buildingContainer);
		selectBuilding.setOpaque(false);
		selectBuilding.getViewport().setOpaque(false);
		selectBuilding.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		selectBuilding.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 20));

		buildings.add(selectBuilding, BorderLayout.CENTER);

		selectionWrapper.add(buildings, BorderLayout.CENTER);
		// adds it to the upper part ng panel para madali ma layout yung rest ng
		// components
		add(selectionWrapper, BorderLayout.NORTH);

		// form panel
		form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));

		// time in
		timeInLbl = new JLabel("TIME IN");
		timeInLbl.setFont(new Font("Arial", Font.BOLD, 16));
		timeInLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(timeInLbl);
		timeInPanel = timePanel();
		form.add(timeInPanel);
		form.add(Box.createVerticalStrut(10));// add space/padding

		// timeout
		timeOutLbl = new JLabel("TIME OUT");
		timeOutLbl.setFont(new Font("Arial", Font.BOLD, 16));
		timeOutLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(timeOutLbl);
		timeOutPanel = timePanel();
		form.add(timeOutPanel);
		form.add(Box.createVerticalStrut(10));

		// select course
		selectCourseLbl = new JLabel("SELECT COURSE");
		selectCourseLbl.setFont(new Font("Arial", Font.BOLD, 16));
		selectCourseLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(selectCourseLbl);
		// wrapper panel for combo box
		comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		comboPanel.setAlignmentX(LEFT_ALIGNMENT);

		courseCombo = new JComboBox<>();
		courseCombo.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 20));
		courseCombo.setPreferredSize(new Dimension(372, 25));

		comboPanel.add(courseCombo);

		// more filters clickable label
		clickFilter = new JLabel("More Filters");
		clickFilter.setFont(new Font("Arial", Font.PLAIN, 13));
		clickFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// get current font
		Font font = clickFilter.getFont();
		// copy attributes
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		// add underline
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		// apply new font
		clickFilter.setFont(font.deriveFont(attributes));

		form.add(comboPanel);
		// i added the click filter here
		form.add(clickFilter);
		// add it to the center of the main panel
		add(form, BorderLayout.CENTER);
		// end of default look--------------------------------------------------

		// more filters panel wiwth buttons
		moreFilter = new JPanel(new BorderLayout());
		moreFilter.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		moreFilter.setPreferredSize(new Dimension(450, 190));

		// container for the components
		clicked = new JPanel();
		clicked.setLayout(new BoxLayout(clicked, BoxLayout.Y_AXIS));
		clicked.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		clicked.setAlignmentX(LEFT_ALIGNMENT);
		clicked.setVisible(false);

		// FLOOR LEVEL
		floorLbl = new JLabel("FLOOR LEVEL");
		floorLbl.setFont(new Font("Arial", Font.BOLD, 16));
		floorLbl.setAlignmentX(LEFT_ALIGNMENT);

		input = new JComboBox<>();
		input.addItem("1st Floor");
		input.addItem("2nd Floor");
		input.addItem("3rd Floor");
		input.addItem("4th Floor");
		input.setMaximumSize(new Dimension(372, 25));
		input.setPreferredSize(new Dimension(372, 25));
		input.setAlignmentX(LEFT_ALIGNMENT);

		// CAPACITY
		capLbl = new JLabel("CAPACITY");
		capLbl.setFont(new Font("Arial", Font.BOLD, 16));
		capLbl.setAlignmentX(LEFT_ALIGNMENT);

		cap = new JSpinner(new SpinnerNumberModel(60, 1, 200, 1));
		cap.setMaximumSize(new Dimension(100, 30));
		cap.setAlignmentX(LEFT_ALIGNMENT);

		// ADD COMPONENTS
		clicked.add(floorLbl);
		clicked.add(Box.createVerticalStrut(5));
		clicked.add(input);

		clicked.add(Box.createVerticalStrut(5));// PADDING

		clicked.add(capLbl);
		clicked.add(Box.createVerticalStrut(5));
		clicked.add(cap);

		moreFilter.add(clicked, BorderLayout.NORTH);

		// buttons below
		btnPanel = new JPanel();
		confirmArea = new ConfirmPanel(MainFrame.getFrame(), "Clear All", "Search");
		btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		btnPanel.add(confirmArea.getConfirmPanel(), BorderLayout.CENTER);

		moreFilter.add(btnPanel, BorderLayout.SOUTH);
		// when "more filters"is clicked
		clickFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// toggle visibility
				clicked.setVisible(!clicked.isVisible());
				toggle = toggleFilters();
				// re do
				moreFilter.revalidate();
				moreFilter.repaint();
			}
		});

		add(moreFilter, BorderLayout.SOUTH);

	}

	private JPanel timePanel() {
		// container ni extended form
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setAlignmentX(LEFT_ALIGNMENT);

		JPanel labels = new JPanel(new GridLayout(1, 3, 15, 10));
		labels.setMaximumSize(new Dimension(330, 20));

		JLabel hLbl = new JLabel("HOURS");
		JLabel mLbl = new JLabel("MINUTES");
		JLabel ampmLbl = new JLabel("AM/PM");

		hLbl.setFont(new Font("Arial", Font.PLAIN, 12));
		mLbl.setFont(new Font("Arial", Font.PLAIN, 12));
		ampmLbl.setFont(new Font("Arial", Font.PLAIN, 12));

		labels.add(hLbl);
		labels.add(mLbl);
		labels.add(ampmLbl);

		JPanel inputs = new JPanel(new GridLayout(1, 3, 15, 0));
		inputs.setMaximumSize(new Dimension(330, 30));

		hrs = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
		mins = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

		JComboBox<String> meridiem = new JComboBox<>(new String[] { "AM", "PM" });

		inputs.add(hrs);
		inputs.add(mins);
		inputs.add(meridiem);

		container.add(Box.createVerticalStrut(5));
		container.add(labels);
		// adds spacing lang to
		container.add(Box.createVerticalStrut(3));
		container.add(inputs);

		return container;
	}

	private boolean toggleFilters() {
		if (toggle == false) {
			return true;
		} else {
			return false;
		}
	}

	// fetch input data from input components
	private List<Building> fetchChosenBuildings(Container container) throws SQLException {
		List<Building> buildings = new ArrayList<>();
		BuildingDAO buildingDao = new BuildingDAO();
		for (Component comp : container.getComponents()) {
			if (comp instanceof JCheckBox && ((JCheckBox) comp).isSelected()) {
				String buildingCode = ((JCheckBox) comp).getName();
				buildings.add(buildingDao.get(buildingCode));
			} else if (comp instanceof Container) {
				buildings.addAll(fetchChosenBuildings((Container) comp));
			}
		}
		return buildings;
	}

	private void fetchTime(Container container, List<String> time) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof JSpinner) {
				time.add(((JSpinner) comp).getValue().toString());
			} else if (comp instanceof JComboBox) {
				time.add(((JComboBox) comp).getSelectedItem().toString());
			} else if (comp instanceof Container) {
				fetchTime((Container) comp, time);
			}
		}
	}

	// load data from database to components
	public void loadCourse(List<Course> courses) {
		for (Course course : courses) {
			courseCombo.addItem(course);
		}
	}

	public void loadBuilding(List<Building> buildings) {
		for (Building building : buildings) {
			// rounded panel for each choice
			RoundedPanel choice = new RoundedPanel(20, 1, Color.BLACK, new BorderLayout());
			choice.setBackground(new Color(221, 221, 219));
			// choices
			check = new JCheckBox(building.getName());
			check.setName(building.getCode());
			check.setFont(new Font("Arial", Font.PLAIN, 16));
			check.setMargin(new Insets(3, 10, 2, 10));
			check.setOpaque(false);
			// adds the choices to a rounded panel
			choice.add(check, BorderLayout.CENTER);
			// add it on the container
			buildingContainer.add(choice);
		}
	}

	// returns the input data to main controller
	public List<Building> getChosenBuildings() throws SQLException {
		return fetchChosenBuildings(buildingContainer);
	}

	public String getTimeIn() {
		List<String> time = new ArrayList<>();
		fetchTime(timeInPanel, time);
		String mins = time.get(1);
		if (time.get(1).length() == 1) {
			mins = "0" + mins;
		}
		return time.get(0) + ":" + mins + " " + time.get(2);
	}

	public String getTimeOut() {
		List<String> time = new ArrayList<>();
		fetchTime(timeOutPanel, time);
		String mins = time.get(1);
		if (time.get(1).length() == 1) {
			mins = "0" + mins;
		}
		return time.get(0) + ":" + mins + " " + time.get(2);
	}

	public Course getCourse() {
		return (Course) courseCombo.getSelectedItem();
	}

	public int getFloorLevel() {
		if (toggle == false) {
			return 0;
		}

		return Integer.parseInt(input.getSelectedItem().toString().substring(0, 1));
	}

	public int getCapacity() {
		if (toggle == false) {
			return 0;
		}

		return Integer.parseInt(cap.getValue().toString());
	}

	// clear input components
	public void clearAll() {
		clearCheckBoxes(buildingContainer);
		clearPanel(timeInPanel);
		clearPanel(timeOutPanel);
		clearPanel(comboPanel);
		clearPanel(clicked);
		clicked.setVisible(false);
	}

	private void clearPanel(Container container) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof JSpinner) {
				SpinnerNumberModel model = (SpinnerNumberModel) ((JSpinner) comp).getModel();
				((JSpinner) comp).setValue(model.getMinimum());
			} else if (comp instanceof JComboBox) {
				((JComboBox) comp).setSelectedIndex(0);
			} else if (comp instanceof Container) {
				clearPanel((Container) comp);
			}
		}
	}

	private void clearCheckBoxes(Container container) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof JCheckBox) {
				((JCheckBox) comp).setSelected(false);
			} else if (comp instanceof Container) {
				clearCheckBoxes((Container) comp);
			}
		}
	}

	// set actions to button listeners
	public void setOnClearButton(ActionListener action) {
		confirmArea.setBtn1Action(action);
	}

	public void setOnConfirmButton(ActionListener action) {
		confirmArea.setBtn2Action(action);
	}
}