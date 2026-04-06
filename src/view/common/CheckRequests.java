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

@SuppressWarnings("serial")
public class CheckRequests extends JPanel {
    JScrollPane mainScrollPane;
    JPanel reqNumPanel, requestsWrapper;
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

        requestsWrapper = new JPanel();
        requestsWrapper.setLayout(new BoxLayout(requestsWrapper, BoxLayout.Y_AXIS));
        requestsWrapper.setOpaque(false);

        mainScrollPane = new JScrollPane(requestsWrapper);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBorder(null);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    public void loadRequestCount(int requestCount) {
        reqNumPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reqNum = new JLabel(requestCount + " Classroom Schedule Requests");
        reqNum.setFont(new Font("Arial", Font.PLAIN, 20));
        reqNumPanel.add(reqNum);
        requestsWrapper.add(reqNumPanel);
    }

    public void loadRequests(List<String> data, String requestKey) {
        JPanel requestPanel = new JPanel(new GridBagLayout());
        requestPanel.setFont(new Font("Arial", Font.BOLD, 20));
        requestPanel.setBackground(new Color(221, 221, 219));

        GridBagConstraints gbcPfp = new GridBagConstraints();
		gbcPfp.gridx = 0;
		gbcPfp.gridy = 0;
		gbcPfp.gridheight = 4;   
		gbcPfp.weightx = 0.1;
		gbcPfp.weighty = 0;             
		gbcPfp.insets = new Insets(5, 5, 0, 0);
		gbcPfp.anchor = GridBagConstraints.NORTH;

        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/resources/images/icons/Profile.png"));
		Image scaled = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		RoundedLabel pfp = new RoundedLabel(new ImageIcon(scaled), 2, new Color(91, 112, 121), 80);
		requestPanel.add(pfp, gbcPfp);
		requestPanel.revalidate();	
		requestPanel.repaint();

        for (int i = 0; i < 4; i++) {
            GridBagConstraints gbcInfo = new GridBagConstraints();
			gbcInfo.gridx = 1;
    		gbcInfo.gridy = i - 1;      
    		gbcInfo.weightx = 0.9;
    		gbcInfo.weighty = 0;         
    		gbcInfo.anchor = GridBagConstraints.WEST;
    		gbcInfo.insets = new Insets(i == 1 ? 4 : 2, 20, 2, 5);  
            if (i == 3) {
                requestPanel.add(new JLabel("Requested at: " + data.get(3)) {
					{
						setFont(new Font("Arial", Font.BOLD, 14));
					}
				}, gbcInfo);
            } else {
                if (i == 1) {
					requestPanel.add(new JLabel(data.get(i)) {
						{
							setFont(new Font("Arial", Font.BOLD, 20));
						}
					}, gbcInfo);
				} else {
					requestPanel.add(new JLabel(data.get(i)) {
						{
							setFont(new Font("Arial", Font.BOLD, 15));
						}
					}, gbcInfo);
				}
            }
        }

        GridBagConstraints gbcRoomCode = new GridBagConstraints();
		gbcRoomCode.gridx = 0;
		gbcRoomCode.gridy = 5;
		gbcRoomCode.anchor = GridBagConstraints.WEST;
		gbcRoomCode.insets = new Insets(15, 15, 0, 5);
		requestPanel.add(new JLabel("ROOM CODE") {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcRoomCode);


        GridBagConstraints gbcRoomData = new GridBagConstraints();
		gbcRoomData.gridx = 0;
		gbcRoomData.gridy = 6;
		gbcRoomData.anchor = GridBagConstraints.WEST;
		gbcRoomData.insets = new Insets(5,15, 15, 5);
		requestPanel.add(new JLabel(data.get(4)) {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcRoomData);

        GridBagConstraints gbcTimeLabel = new GridBagConstraints();
		gbcTimeLabel.gridx = 1;
		gbcTimeLabel.gridy = 5;
		gbcTimeLabel.anchor = GridBagConstraints.WEST;
		gbcTimeLabel.insets = new Insets(15, 20, 0, 5);
		requestPanel.add(new JLabel("TIME") {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcTimeLabel);

        GridBagConstraints gbcTimeData = new GridBagConstraints();
		gbcTimeData.gridx = 1;
		gbcTimeData.gridy = 6;
		gbcTimeData.anchor = GridBagConstraints.WEST;
		gbcTimeData.insets = new Insets(5, 20, 15, 5);
		requestPanel.add(new JLabel(data.get(5)) {
			{
				setFont(new Font("Arial", Font.BOLD, 12));
			}
		}, gbcTimeData);

        ConfirmPanel confirmBtns = new ConfirmPanel(
                requestPanel,
                "DECLINE", "ACCEPT",
                new Color(227, 75, 75), 2,
                new Color(77, 139, 78), 2);
        confirmBtns.setBtn1Color(new Color(255, 100, 100));
        confirmBtns.setBtn2Color(new Color(63, 193, 127));

        confirmBtns.setBtn1Action(e -> {
            if (onDecline != null)
                onDecline.accept(requestKey);
        });

        confirmBtns.setBtn2Action(e -> {
            if (onAccept != null)
                onAccept.accept(requestKey);
        });

        RoundedPanel mainPanel = new RoundedPanel(60, 3, new Color(91, 112, 121), new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBackground(new Color(243, 244, 247));
		mainPanel.setPreferredSize(new Dimension(400, 250));
		mainPanel.setMaximumSize(new Dimension(400, 250));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        requestPanel.setOpaque(false);
        confirmBtns.getConfirmPanel().setOpaque(false);

        mainPanel.add(requestPanel, BorderLayout.CENTER);
        mainPanel.add(confirmBtns.getConfirmPanel(), BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout());
        wrapper.setOpaque(false);
        wrapper.add(mainPanel);

        requestsWrapper.add(wrapper);
        requestsWrapper.revalidate();
        requestsWrapper.repaint();
    }
}