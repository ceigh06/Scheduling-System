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
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import dao.BuildingDAO;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;
import model.Building;
import model.Course;
import model.Section;
import model.user.User;

public class SearchRooms1 extends JPanel {
	JPanel buildingContainer, selectionWrapper, form, comboPanel, moreFilter, clicked, btnPanel, timeInPanel,
			timeOutPanel, mainWrapper, sectionComboPanel;
	JLabel selectSection, select, timeIn, timeOut, timeInLbl, timeOutLbl, selectCourseLbl, clickFilter, floorLbl,
			capLbl;
	JScrollPane selectBuilding, mainScrollPane;
	JCheckBox check;
	JSpinner hrs, mins, cap;

	JSpinner timeInHrs, timeInMins;
	JComboBox<String> timeInMeridiem;

	JSpinner timeOutHrs, timeOutMins;
	JComboBox<String> timeOutMeridiem;

	JComboBox<Section> sectionCombo;
	JComboBox<Course> courseCombo;

	private ConfirmPanel confirmArea;
	JComboBox<String> input;
	boolean toggle = false;

	public SearchRooms1(User user) {

		setLayout(new BorderLayout());

		selectionWrapper = new JPanel(new BorderLayout());
		selectionWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		selectionWrapper.setMaximumSize(new Dimension(450, 50));
		// HEADER
		select = new JLabel("SELECT BUILDING");
		select.setFont(new Font("Arial", Font.BOLD, 20));
		select.setForeground(new Color(91, 112, 121));
		select.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		selectionWrapper.add(select, BorderLayout.NORTH);

		// ROUNDED PANEL CONTAINER
		RoundedPanel buildings = new RoundedPanel(50, 2, new Color(117, 144, 156), new BorderLayout());
		buildings.setPreferredSize(new Dimension(380, 180));
		buildings.setMaximumSize(new Dimension(380, 180));
		buildings.setBackground(new Color(243, 244, 247));

		// container for building choices
		buildingContainer = new JPanel();
		buildingContainer.setLayout(new GridLayout(0, 1, 5, 5));
		buildingContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		buildingContainer.setOpaque(false);

		// scrollpane for buildings
		selectBuilding = new JScrollPane(buildingContainer);
		selectBuilding.setOpaque(false);
		selectBuilding.getViewport().setOpaque(false);
		selectBuilding.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		selectBuilding.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		selectBuilding.getVerticalScrollBar().setUnitIncrement(16); // adjust scroll speed
		ScrollBarHelper.applySlimScrollBar(selectBuilding, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
		selectBuilding.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));

		buildings.add(selectBuilding, BorderLayout.CENTER);

		selectionWrapper.add(buildings, BorderLayout.CENTER);

		// form panel
		form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));
		form.setPreferredSize(new Dimension(400, 400)); // or whatever height you need
		form.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
		// time in
		timeInLbl = new JLabel("TIME IN");
		timeInLbl.setFont(new Font("Arial", Font.BOLD, 20));
		timeInLbl.setForeground(new Color(91, 122, 121));
		timeInLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(Box.createVerticalStrut(10));
		form.add(timeInLbl);
		timeInPanel = timePanel(true);
		form.add(timeInPanel);
		form.add(Box.createVerticalStrut(10));// add space/padding

		// timeout
		timeOutLbl = new JLabel("TIME OUT");
		timeOutLbl.setFont(new Font("Arial", Font.BOLD, 20));
		timeOutLbl.setForeground(new Color(91, 122, 121));
		timeOutLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(Box.createVerticalStrut(10));
		form.add(timeOutLbl);
		timeOutPanel = timePanel(false);
		form.add(timeOutPanel);
		form.add(Box.createVerticalStrut(10));

		// select course
		selectCourseLbl = new JLabel("SELECT COURSE");
		selectCourseLbl.setFont(new Font("Arial", Font.BOLD, 20));
		selectCourseLbl.setForeground(new Color(91, 122, 121));
		selectCourseLbl.setAlignmentX(LEFT_ALIGNMENT);

		form.add(Box.createVerticalStrut(10));
		form.add(selectCourseLbl);
		// wrapper panel for combo box
		comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		comboPanel.setAlignmentX(LEFT_ALIGNMENT);

		courseCombo = new JComboBox<>();
		courseCombo.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 20));
		courseCombo.setPreferredSize(new Dimension(372, 25));
		form.add(Box.createVerticalStrut(5));
		comboPanel.add(courseCombo);

		sectionCombo = new JComboBox<>();

		// more filters clickable label
		clickFilter = new JLabel("More Filters");
		clickFilter.setForeground(new Color(91, 112, 121));
		clickFilter.setFont(new Font("Arial", Font.PLAIN, 13));
		clickFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Font font = clickFilter.getFont();
		Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		clickFilter.setFont(font.deriveFont(attributes));

		form.add(comboPanel);
		// i added the click filter here
		showSectionCombo(user.getUserType().equals("Faculty"));
		form.add(clickFilter);
		// end of default look--------------------------------------------------

		// more filters panel wiwth buttons
		moreFilter = new JPanel(new BorderLayout());
		moreFilter.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		moreFilter.setPreferredSize(new Dimension(450, 130));

		// container for the components
		clicked = new JPanel();
		clicked.setLayout(new BoxLayout(clicked, BoxLayout.Y_AXIS));
		clicked.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		clicked.setAlignmentX(LEFT_ALIGNMENT);
		clicked.setVisible(false);

		JPanel filterWrapper = new JPanel(new GridLayout(2, 2, 40, 5));
		filterWrapper.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

		// MODIFIED --START
		// FLOOR LEVEL
		floorLbl = new JLabel("FLOOR LEVEL");
		floorLbl.setFont(new Font("Arial", Font.BOLD, 16));
		floorLbl.setAlignmentX(LEFT_ALIGNMENT);

		JComboBox<String> input = new JComboBox<>();
		input.addItem("1st Floor");
		input.addItem("2nd Floor");
		input.addItem("3rd Floor");
		input.addItem("4th Floor");
		input.setMaximumSize(new Dimension(200, 20));
		input.setPreferredSize(new Dimension(200, 20));
		input.setAlignmentX(LEFT_ALIGNMENT);

		// CAPACITY
		capLbl = new JLabel("CAPACITY");
		capLbl.setFont(new Font("Arial", Font.BOLD, 16));
		capLbl.setAlignmentX(LEFT_ALIGNMENT);
		cap = new JSpinner(new SpinnerNumberModel(60, 1, 200, 1));
		cap.setMaximumSize(new Dimension(100, 20));
		cap.setAlignmentX(LEFT_ALIGNMENT);

		filterWrapper.add(floorLbl);
		filterWrapper.add(capLbl);
		filterWrapper.add(input);
		filterWrapper.add(cap);
		clicked.add(filterWrapper);
		moreFilter.add(clicked, BorderLayout.NORTH);
		// MODIFIED

		// buttons below
		btnPanel = new JPanel();
		confirmArea = new ConfirmPanel(MainFrame.getFrame(), "Clear All", "Search",
				new Color(91, 112, 121), 2,
				new Color(91, 112, 121), 2);
		confirmArea.setBackground(null);
		btnPanel.add(confirmArea.getConfirmPanel(), BorderLayout.CENTER);

		moreFilter.add(btnPanel, BorderLayout.SOUTH);
		// when "more filters"is clicked
		clickFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked.setVisible(!clicked.isVisible());
				toggle = toggleFilters();

				// Revalidate only the necessary panels
				SwingUtilities.invokeLater(() -> {
					moreFilter.revalidate();
					moreFilter.repaint();
				});
			}
		});
		// NEW PANEL --START
		mainWrapper = new JPanel(new BorderLayout());
		mainWrapper.setPreferredSize(new Dimension(420, 760));
		mainWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainWrapper.add(selectionWrapper, BorderLayout.NORTH);
		mainWrapper.add(form, BorderLayout.CENTER);
		mainWrapper.add(moreFilter, BorderLayout.SOUTH);

		JScrollPane mainScrollPane = new JScrollPane(mainWrapper);
		mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		ScrollBarHelper.applySlimScrollBar(mainScrollPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
		add(mainScrollPane);
		// END

	}

	public void showSectionCombo(boolean isFaculty) {
		selectSection = new JLabel("SELECT SECTION");
		selectSection.setFont(new Font("Arial", Font.BOLD, 20));
		selectSection.setForeground(new Color(91, 112, 121));
		selectSection.setAlignmentX(LEFT_ALIGNMENT);

		if (isFaculty) {
			// Add vertical spacing
			form.add(Box.createVerticalStrut(10));
			// Add the section label
			form.add(selectSection);
			form.add(Box.createVerticalStrut(5));

			// Create wrapper panel for section combo (same structure as course combo)
			JPanel sectionComboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			sectionComboPanel.setAlignmentX(LEFT_ALIGNMENT);
			sectionComboPanel.setOpaque(false); // Match course combo panel styling

			// Configure section combo styling to match course combo
			sectionCombo.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 20));
			sectionCombo.setPreferredSize(new Dimension(372, 25));
			sectionCombo.setMaximumSize(new Dimension(372, 25));

			sectionComboPanel.add(sectionCombo);
			form.add(sectionComboPanel);

		}
	}

	private JPanel timePanel(boolean isTimeIn) {
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

		JSpinner hSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 12, 1));
		JSpinner mSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		JComboBox<String> mer = new JComboBox<>(new String[] { "AM", "PM" });

		// store references
		if (isTimeIn) {
			timeInHrs = hSpinner;
			timeInMins = mSpinner;
			timeInMeridiem = mer;
		} else {
			timeOutHrs = hSpinner;
			timeOutMins = mSpinner;
			timeOutMeridiem = mer;
		}

		inputs.add(hSpinner);
		inputs.add(mSpinner);
		inputs.add(mer);

		container.add(Box.createVerticalStrut(5));
		container.add(labels);
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
		ActionListener[] listeners = courseCombo.getActionListeners();
		for (ActionListener al : listeners)
			courseCombo.removeActionListener(al);

		for (Course course : courses) {
			courseCombo.addItem(course);
		}

		for (ActionListener al : listeners)
			courseCombo.addActionListener(al);
	}

	public void loadSection(List<Section> sections) {
		System.out.println("view.loadSection called, size: " + sections.size());
		sectionCombo.removeAllItems();
		for (Section section : sections) {
			sectionCombo.addItem(section);
		}
	}

	public void loadBuilding(List<Building> buildings) {
		for (Building building : buildings) {
			// rounded panel for each choice
			RoundedPanel choice = new RoundedPanel(20, 1, new Color(91, 112, 121), new BorderLayout());
			choice.setBackground(new Color(117, 144, 156));
			choice.setPreferredSize(new Dimension(200, 55));
			choice.setMaximumSize(new Dimension(200, 55));
			// choices

			ImageIcon uncheckedIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Unchecked.png"));
			ImageIcon checkedIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Checked.png"));
			Image uncheckedImg = uncheckedIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			Image checkedImg = checkedIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

			uncheckedIcon = new ImageIcon(uncheckedImg);
			checkedIcon = new ImageIcon(checkedImg);
			check = new JCheckBox(building.getName());
			check.setIcon(uncheckedIcon);
			check.setSelectedIcon(checkedIcon);
			check.setBorderPainted(false);
			check.setFocusPainted(false);
			check.setContentAreaFilled(false);
			check.setName(building.getCode());
			check.setFont(new Font("Arial", Font.PLAIN, 20));
			check.setForeground(Color.WHITE);
			check.setMargin(new Insets(15, 15, 15, 10));
			check.setOpaque(false);
			// adds the choices to a rounded panel
			choice.add(check, BorderLayout.CENTER);
			// add it on the container
			buildingContainer.add(choice);
		}

	}

	private boolean isUpdating = false;

public void setTimeIn(int hour, int minute, String meridiem) {
    isUpdating = true;
    timeInHrs.setValue(hour);
    timeInMins.setValue(minute);
    timeInMeridiem.setSelectedItem(meridiem);
    isUpdating = false;
}

public void setTimeOut(int hour, int minute, String meridiem) {
    isUpdating = true;
    timeOutHrs.setValue(hour);
    timeOutMins.setValue(minute);
    timeOutMeridiem.setSelectedItem(meridiem);
    isUpdating = false;
}

public void setOnTimeInChanged(ChangeListener action) {
    timeInHrs.addChangeListener(e -> { if (!isUpdating) action.stateChanged(e); });
    timeInMins.addChangeListener(e -> { if (!isUpdating) action.stateChanged(e); });
    timeInMeridiem.addActionListener(e -> {
        if (!isUpdating) action.stateChanged(new javax.swing.event.ChangeEvent(timeInMeridiem));
    });
}

public void setOnTimeOutChanged(ChangeListener action) {
    timeOutHrs.addChangeListener(e -> { if (!isUpdating) action.stateChanged(e); });
    timeOutMins.addChangeListener(e -> { if (!isUpdating) action.stateChanged(e); });
    timeOutMeridiem.addActionListener(e -> {
        if (!isUpdating) action.stateChanged(new javax.swing.event.ChangeEvent(timeOutMeridiem));
    });
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

	public Section getSection() {
		return (Section) sectionCombo.getSelectedItem();
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

	public void setOnCourseChanged(ActionListener action) {
		for (ActionListener al : courseCombo.getActionListeners()) {
			courseCombo.removeActionListener(al);
		}
		courseCombo.addActionListener(action);
	}
}