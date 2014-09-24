package com.thedeanda.ajaxproxy.ui.rest;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.thedeanda.ajaxproxy.LoadedResource;
import com.thedeanda.ajaxproxy.ui.ResourcePanel;

public class RestClientFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private RestClientPanel panel;

	public RestClientFrame(LoadedResource resource) {
		panel = new RestClientPanel();
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, panel);
		// panel.setResource(resource);
		setTitle("Ajax Proxy - Rest Client");
		setPreferredSize(new Dimension(640, 700));
		pack();
	}

}