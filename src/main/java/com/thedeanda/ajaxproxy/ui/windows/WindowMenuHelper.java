package com.thedeanda.ajaxproxy.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thedeanda.ajaxproxy.ui.json.JsonViewerFrame;
import com.thedeanda.ajaxproxy.ui.rest.RestClientFrame;

public class WindowMenuHelper implements WindowListListener {
	private static final Logger log = LoggerFactory
			.getLogger(WindowMenuHelper.class);
	private String windowId;
	private JMenuBar menuBar;
	private JMenu menu;

	public WindowMenuHelper(String windowId, JMenuBar menuBar) {
		this.windowId = windowId;
		this.menuBar = menuBar;

		menu = new JMenu("Window");
		menu.setMnemonic(KeyEvent.VK_W);
		menuBar.add(menu);

		Windows.get().addListener(this);
	}

	private boolean stillOpen(Collection<WindowContainer> windows) {
		boolean stillOpen = false;
		for (WindowContainer wc : windows) {
			if (windowId.equals(wc.getId())) {
				stillOpen = true;
				break;
			}
		}
		return stillOpen;
	}

	@Override
	public void windowsChanged(Collection<WindowContainer> windows) {
		log.info("windows changed {}", windowId);
		// first check if our window got closed...

		if (!stillOpen(windows)) {
			// TODO: stop listening
			log.info("stop listening from: {}", windowId);
			Windows.get().removeListener(this);
			return;
		}

		JMenuItem mi;
		menu.removeAll();

		addGeneric();

		for (final WindowContainer wc : windows) {
			mi = new JMenuItem(wc.getName());
			menu.add(mi);
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrame frame = wc.getFrame();
					if (frame != null) {
						frame.setVisible(true);
						frame.requestFocus();
					}
				}
			});
		}
	}

	private void addGeneric() {
		JMenuItem mi = null;

		mi = new JMenuItem("New Rest Client");
		mi.setMnemonic(KeyEvent.VK_R);
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				handleRest();
			}
		});
		menu.add(mi);

		mi = new JMenuItem("New Json Viewer");
		mi.setMnemonic(KeyEvent.VK_J);
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
				ActionEvent.CTRL_MASK));
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				handleJson();
			}
		});
		menu.add(mi);

		menu.add(new JSeparator());
	}

	private void handleRest() {
		RestClientFrame frame = new RestClientFrame();
		frame.setVisible(true);
	}

	private void handleJson() {
		JsonViewerFrame frame = new JsonViewerFrame();
		frame.setVisible(true);
	}
}
