package view.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import view.components.RoundedLabel;
import view.components.RoundedPanel;
import view.components.ScrollBarHelper;

@SuppressWarnings("serial")
public class CheckRequests extends JPanel {
    JScrollPane mainScrollPane;
    JPanel reqNumPanel, requestsWrapper, requestPanel;
    JLabel reqNum;

    private Consumer<String> onAccept;
    private Consumer<String> onDecline;

    public void setOnAccept(Consumer<String> onAccept) {
        this.onAccept = onAccept;
    }

    public void setOnDecline(Consumer<String> onDecline) {
        this.onDecline = onDecline;
    }

    public CheckRequests() {
        setLayout(new BorderLayout());
		setBackground(Color.WHITE);

        requestsWrapper = new JPanel();
        requestsWrapper.setLayout(new BoxLayout(requestsWrapper, BoxLayout.Y_AXIS));
		// requestsWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        requestsWrapper.setOpaque(false);

        mainScrollPane = new JScrollPane(requestsWrapper);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ScrollBarHelper.applySlimScrollBar(mainScrollPane, 10, 30, Color.GRAY, Color.LIGHT_GRAY);
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    public void loadRequestCount(int requestCount) {
        reqNumPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reqNum = new JLabel(requestCount + " Classroom Schedule Requests");
		reqNum.setForeground(new Color(91, 112, 121	));
        reqNum.setFont(new Font("Arial", Font.PLAIN, 20));
        reqNumPanel.add(reqNum);
        requestsWrapper.add(reqNumPanel);
    }

    public void loadRequests(List<String> data, String requestKey) {
    // Expected data structure:
    // 0: Name (Jessie Claire C. Santos)
    // 1: Program/Section (BSIT - 2A-G2)
    // 2: Subject (IT 203)
    // 3: Request time (10:00 AM)
    // 4: Room Code (MH - APP 101)
    // 5: Schedule Time (3:00PM - 6:00PM)
    
    requestPanel = new JPanel(new GridBagLayout());
    requestPanel.setBackground(new Color(243, 244, 247)); // Light gray background
    requestPanel.setOpaque(true);

    GridBagConstraints gbcPfp = new GridBagConstraints();
		gbcPfp.gridx = 0;
		gbcPfp.gridy = 0;
		gbcPfp.gridheight = 4;   
		gbcPfp.weightx = 0.1;
		gbcPfp.weighty = 0;             
		gbcPfp.insets = new Insets(5, 5, 0, 0);
		gbcPfp.anchor = GridBagConstraints.NORTH;

		ImageIcon rawIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Profile.png"));
		Image scaled = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaled), 2, new Color(91, 112, 121), 70);
		requestPanel.add(pfp, gbcPfp);
		requestPanel.revalidate();	
		requestPanel.repaint();

    // COLUMN 1: Name, time (can expand)
    GridBagConstraints gbcCol1 = new GridBagConstraints();
    gbcCol1.gridx = 1;
    gbcCol1.anchor = GridBagConstraints.WEST;
    gbcCol1.fill = GridBagConstraints.HORIZONTAL;
    gbcCol1.weightx = 0.9;  // Take remaining space
    gbcCol1.gridheight = 1;

    // Name (row 0, column 1)
    gbcCol1.gridy = 0;
    gbcCol1.insets = new Insets(2, 10, 2, 10);
    JLabel nameLabel = new JLabel(data.get(0));
    nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
    nameLabel.setForeground(new Color(80, 80, 80));
    requestPanel.add(nameLabel, gbcCol1);

    // Program/Section (row 1, column 1)
    gbcCol1.gridy = 1;
    JLabel programLabel = new JLabel(data.get(1));
    programLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    programLabel.setForeground(new Color(100, 100, 100));
    requestPanel.add(programLabel, gbcCol1);

    // Subject (row 2, column 1)
    gbcCol1.gridy = 2;
    JLabel subjectLabel = new JLabel(data.get(2));
    subjectLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    subjectLabel.setForeground(new Color(100, 100, 100));
    requestPanel.add(subjectLabel, gbcCol1);

    // Requested at (row 3, column 1)
    gbcCol1.gridy = 3;
    gbcCol1.insets = new Insets(8, 10, 15, 10);
    JLabel requestedLabel = new JLabel("REQUESTED AT: " + data.get(3));
    requestedLabel.setFont(new Font("Arial", Font.BOLD, 11));
    requestedLabel.setForeground(new Color(120, 120, 120));
    requestPanel.add(requestedLabel, gbcCol1);

   GridBagConstraints gbcRoomCode = new GridBagConstraints();
		gbcRoomCode.gridx = 0;
		gbcRoomCode.gridy = 4;
		gbcRoomCode.anchor = GridBagConstraints.WEST;
		gbcRoomCode.insets = new Insets(10, 10, 0, 5);
		requestPanel.add(new JLabel("ROOM CODE") {
			{
				setFont(new Font("Arial", Font.BOLD, 11));
                setForeground(new Color(150, 150, 150));
			}
		}, gbcRoomCode);

		GridBagConstraints gbcRoomData = new GridBagConstraints();
		gbcRoomData.gridx = 0;
		gbcRoomData.gridy = 5;
		gbcRoomData.anchor = GridBagConstraints.WEST;
		gbcRoomData.insets = new Insets(8,10, 5, 5);
		requestPanel.add(new JLabel(data.get(4)) {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
                setForeground(new Color(91, 112, 121));
			}
		}, gbcRoomData);

    // TIME label (row 4, column 1)
    gbcCol1.gridy = 4;
    gbcCol1.insets = new Insets(10, 10, 0, 10);
    JLabel timeTitleLabel = new JLabel("TIME");
    timeTitleLabel.setFont(new Font("Arial", Font.BOLD, 11));
    timeTitleLabel.setForeground(new Color(150, 150, 150));
    requestPanel.add(timeTitleLabel, gbcCol1);

    // Time value (row 5, column 1)
    gbcCol1.gridy = 5;
    gbcCol1.insets = new Insets(8, 10, 5, 10);
    JLabel timeValueLabel = new JLabel(data.get(5));
    timeValueLabel.setFont(new Font("Arial", Font.BOLD, 12));
    timeValueLabel.setForeground(new Color(91, 112, 121));
    requestPanel.add(timeValueLabel, gbcCol1);

    RoundedPanel mainPanel = new RoundedPanel(50, 2, new Color(91, 112, 121), new BorderLayout());
    mainPanel.setBackground(new Color(243, 244, 247));
		mainPanel.setPreferredSize(new Dimension(400, 240));
		mainPanel.setMaximumSize(new Dimension(400, 250));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

    mainPanel.add(requestPanel, BorderLayout.CENTER);

    ConfirmPanel confirmBtns = new ConfirmPanel(
            requestPanel,
            "DECLINE", "ACCEPT",
            new Color(227, 75, 75), 2,
            new Color(77, 139, 78), 2);
    confirmBtns.setBtn1Color(new Color(255, 100, 100));
    confirmBtns.setBtn2Color(new Color(63, 193, 127));
    confirmBtns.getConfirmPanel().setOpaque(false);

    confirmBtns.setBtn1Action(e -> {
        if (onDecline != null)
            onDecline.accept(requestKey);
    });

    confirmBtns.setBtn2Action(e -> {
        if (onAccept != null)
            onAccept.accept(requestKey);
    });

    mainPanel.add(confirmBtns.getConfirmPanel(), BorderLayout.SOUTH);

    // Wrapper
    JPanel wrapper = new JPanel(new FlowLayout());
    wrapper.setOpaque(false);
	wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    wrapper.add(mainPanel);

    requestsWrapper.add(wrapper);
    requestsWrapper.revalidate();
    requestsWrapper.repaint();
}
}