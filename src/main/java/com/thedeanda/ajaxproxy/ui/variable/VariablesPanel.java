package com.thedeanda.ajaxproxy.ui.variable;

import com.thedeanda.ajaxproxy.config.model.Variable;
import com.thedeanda.ajaxproxy.ui.variable.controller.VariableController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VariablesPanel extends JPanel {
	private static final Logger log = LoggerFactory.getLogger(VariablesPanel.class);
	private static final long serialVersionUID = 1L;
	private final VariableController variableController;
	private VariableEditor variableEditor;
	private VariableTableModel variableModel;
	private JTable variableTable;

	public VariablesPanel(VariableController variableController) {
		this.variableController = variableController;
		variableModel = new VariableTableModel(variableController);

		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		variableTable = new JTable(variableModel);
		variableTable.setColumnModel(new VariableColumnModel());
		JScrollPane scroll = new JScrollPane(variableTable);
		add(scroll);

		variableEditor = new VariableEditor(this);
		add(variableEditor);

		final ListSelectionModel cellSelectionModel = variableTable.getSelectionModel();
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					startEdit();
				}
			}

		});

		layout.putConstraint(SpringLayout.NORTH, scroll, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scroll, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, scroll, -10, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, scroll, -10, SpringLayout.NORTH, variableEditor);

		layout.putConstraint(SpringLayout.WEST, variableEditor, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, variableEditor, -10, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, variableEditor, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, variableEditor, -60, SpringLayout.SOUTH, this);
	}

	private void startEdit() {
		int row = variableTable.getSelectedRow();
		log.trace("start edit {}", row);
		Variable value = variableController.get(row).orElse(Variable.builder().build());
		variableEditor.startEdit(value);
	}

	public void changeValue(String oldKey, String newKey, String newValue) {
		variableModel.updateValue(oldKey, newKey, newValue);
		int index = variableController.getKeyIndex(newKey);
		if (index >= 0) {
			variableTable.setRowSelectionInterval(index, index);
		}
	}

}
