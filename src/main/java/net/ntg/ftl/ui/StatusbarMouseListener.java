package net.ntg.ftl.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import net.ntg.ftl.ui.FTLFrame;


public class StatusbarMouseListener extends MouseAdapter {
	private FTLFrame frame = null;
	private String text = null;


	public StatusbarMouseListener( FTLFrame frame, String text ) {
		this.frame = frame;
		this.text = text;
	}

	@Override
	public void mouseEntered( MouseEvent e ) {
		frame.setStatusText( text );
	}

	@Override
	public void mouseExited( MouseEvent e ) {
		frame.setStatusText("");
	}
}
