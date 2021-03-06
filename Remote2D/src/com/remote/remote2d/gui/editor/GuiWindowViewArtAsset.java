package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2;

public class GuiWindowViewArtAsset extends GuiWindow {
	
	GuiTextField field;
	GuiButton doneButton;
	Texture tex;

	public GuiWindowViewArtAsset(WindowHolder holder, Vector2 pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2(400,450), allowedBounds, "Art Viewer/Selecter");
		field = new GuiTextField(new Vector2(10,10), new Vector2(380,40), 20);
	}
	
	public void initGui()
	{
		buttonList.clear();
		doneButton = new GuiButton(0, new Vector2(20,400), new Vector2(360,40), "Done");
		buttonList.add(doneButton);
	}

	@Override
	public void renderContents(float interpolation) {
		field.render(interpolation);

		if(tex == null)
			reloadTex();
		else if(!field.text.equals(tex.textureLocation))
			reloadTex();
	}
	
	public void reloadTex()
	{
		if(Remote2D.getInstance().artLoader.textureExists(field.text))
		{
			if(tex != null)
				tex.removeTexture();
			tex = new Texture(field.text);
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		field.tick((int)getMouseInWindow(i, j).x, (int)getMouseInWindow(i, j).y, k);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			holder.closeWindow(this);
		}
	}
	
	
	
}
