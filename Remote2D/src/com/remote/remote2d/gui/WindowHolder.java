package com.remote.remote2d.gui;

public interface WindowHolder {
	
	public GuiWindow getTopWindow();
	public void attemptToPutWindowOnTop(GuiWindow window);
	public void closeWindow(GuiWindow window);
	
}
