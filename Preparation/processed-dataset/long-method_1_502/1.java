public void addToPanel(Container cp, GridBagConstraints gbc, PropertiePanel pp, QSAdminMain qsadminMain) {
    gbc.weighty = 0.0;
    gbc.weightx = 0.0;
    gbc.gridy++;
    gbc.gridheight = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    String temp = getType().toLowerCase();
    if (temp == null)
        temp = "edit";
    //space  
    gbc.gridx = 0;
    gbc.weightx = 0.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    cp.add(Box.createRigidArea(new Dimension(10, 10)), gbc);
    //label  
    gbc.gridx++;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    namelabel = new JLabel(getName());
    namelabel.setToolTipText(getDesc());
    cp.add(namelabel, gbc);
    //space  
    gbc.gridx++;
    gbc.weightx = 0.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    cp.add(Box.createRigidArea(new Dimension(10, 10)), gbc);
    //value  
    gbc.gridx++;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    if (temp.equals("edit")) {
        editField = new JTextField();
        editField.setEnabled(false);
        editField.setToolTipText(getDesc());
        cp.add(editField, gbc);
    } else if (temp.equals("select")) {
        temp = getSelect();
        StringTokenizer st = new StringTokenizer(temp, "|");
        String[] valStrings = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            valStrings[i] = st.nextToken();
        }
        selectList = new JComboBox(valStrings);
        selectList.setMaximumRowCount(3);
        selectList.setEditable(false);
        selectList.setEnabled(false);
        cp.add(selectList, gbc);
    }
    //space  
    gbc.weightx = 0.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx++;
    cp.add(Box.createRigidArea(new Dimension(10, 10)), gbc);
    //control  
    gbc.gridx++;
    gbc.weightx = 0.5;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    if (isSet() == true) {
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(getSaveAction(qsadminMain, Propertie.this));
        cp.add(saveButton, gbc);
    } else {
        cp.add(new JLabel(), gbc);
    }
    //extra space  
    gbc.gridx++;
    gbc.weightx = 0.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    cp.add(Box.createRigidArea(new Dimension(10, 10)), gbc);
    if (temp.equals("edit")) {
        editField.getDocument().addDocumentListener(new EditFieldDocumentListener(saveButton));
    } else {
        selectList.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                saveButton.setEnabled(true);
            }
        });
    }
}
