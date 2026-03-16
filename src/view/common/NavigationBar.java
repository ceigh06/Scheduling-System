package view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.components.RoundedPanel;

public class NavigationBar {

    private JPanel navPanel;
    private RoundedPanel selectedPanel, browsePanel, homePanel, reqPanel, pfPanel; 

    public RoundedPanel getSelectedPanel() { // still not sure what is the purpose of this
		return selectedPanel;
	}

	public void setOnBrowsePanel(MouseAdapter action) {
        browsePanel.addMouseListener(action);
		
	}

	public void setOnHomePanel(MouseAdapter action) {
        homePanel.addMouseListener(action);
	}

	public void setOnRequestPanel(MouseAdapter action) {
		reqPanel.addMouseListener(action);
	}

	public void setOnProfilePanel(MouseAdapter action) {
		pfPanel.addMouseListener(action);
	}

	public NavigationBar(JFrame frame){
        navPanel = new JPanel(new GridBagLayout());
        navPanel.setPreferredSize(new Dimension(frame.getWidth(),50));
        navPanel.setBorder(BorderFactory.createEmptyBorder(-10,1,-10,1));
        navPanel.setBackground(Color.WHITE);
        
        homePanel = createOption("H","Home");
        browsePanel = createOption("B","Browse");
        reqPanel = createOption("R","Requests");
        pfPanel = createOption("P","Profile");
        
        addPanel(homePanel,0);
        addPanel(browsePanel,1);
        addPanel(reqPanel,2);
        addPanel(pfPanel,3);
    }

    private void addPanel(RoundedPanel panel,int x){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,2,0,2);
        navPanel.add(panel,gbc);
    }

    private RoundedPanel createOption(String letter,String text){
        RoundedPanel panel = new RoundedPanel(20,0);
        panel.setBackground(new Color(139,0,0));

        JLabel label = new JLabel(letter, JLabel.CENTER);
        label.setForeground(Color.WHITE);
        panel.add(label);

        //BACKEND TO DO: Make it so that when I click the button item, it will go to the frames
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GridBagLayout layout = (GridBagLayout) navPanel.getLayout();
                if(selectedPanel != null){
                    JLabel inactiveLbl = (JLabel) selectedPanel.getComponent(0);
                    inactiveLbl.setText(inactiveLbl.getText().substring(0,1));
                    inactiveLbl.setForeground(Color.WHITE);
                    selectedPanel.setBackground(new Color(139,0,0));

                    GridBagConstraints old = layout.getConstraints(selectedPanel);
                    old.weightx = 1;
                    layout.setConstraints(selectedPanel, old); //Goes back to the old positions 
                }

                selectedPanel = panel;
                label.setText(text);
                GridBagConstraints gbc = layout.getConstraints(panel);
                gbc.weightx = 2; //magiging two grids yung makukuha nya 
                layout.setConstraints(panel, gbc);

                //so it will go back to the previous looks
                navPanel.revalidate();
                navPanel.repaint();
            }
        });

        return panel;
    }

    public JPanel getNavBar(){
        return navPanel;
    }
}