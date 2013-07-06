package com.remote.remote2d.gui.editor.browser;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.GuiCreateSpriteSheet;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.particles.ParticleSystem;
import com.remote.remote2d.world.Map;

public class GuiEditorBrowser extends Gui {
	
	private ArrayList<Folder> folderStack;
	private int selectedFile = -1;
	private int doubleClickTime = 500;
	private long lastDoubleClickTime = 0;
	private GuiEditor editor;
	
	public Vector2D pos;
	public Vector2D dim;
	
	public GuiEditorBrowser(GuiEditor editor, Vector2D pos, Vector2D dim)
	{
		folderStack = new ArrayList<Folder>();
		folderStack.add(new Folder(Remote2D.getJarPath().getPath()));
		
		this.pos = pos;
		this.dim = dim;
		this.editor = editor;
	}

	@Override
	public void tick(int i, int j, int k) {
		
		Folder currentFolder = folderStack.get(folderStack.size()-1);
		
		if(Remote2D.getInstance().hasMouseBeenPressed() && pos.getColliderWithDim(dim).isPointInside(new Vector2D(i,j)))
		{
			int yPos = j-pos.y-20;
			if(yPos < (currentFolder.files.size()+currentFolder.folders.size()+1)*20)
			{
				if(selectedFile == yPos/20)
				{
					if(System.currentTimeMillis() - lastDoubleClickTime <= doubleClickTime)
					{
						lastDoubleClickTime = -1;
						if(selectedFile == 0)
						{
							if(folderStack.size() > 1)
								folderStack.remove(folderStack.size()-1);
						}
						else if(selectedFile-1 < currentFolder.files.size())
						{
							String localPath = currentFolder.files.get(selectedFile-1).getPath().substring((int) (Remote2D.getJarPath().getPath().length()));
							Log.info("Browser is opening file: "+localPath);
							if(localPath.endsWith(Animation.getExtension()))
								Remote2D.getInstance().guiList.push(new GuiCreateSpriteSheet(new Animation(localPath)));
							else if(localPath.endsWith(Entity.getExtension()))
							{
								Entity e = new Entity();
								R2DFileManager manager = new R2DFileManager(localPath,e);
								manager.read();
								editor.setActiveEntity(e);
							} else if(localPath.endsWith(ParticleSystem.getExtension()))
							{
								ParticleSystem system = new ParticleSystem(editor.getMap());
								R2DFileManager manager = new R2DFileManager(localPath,system);
								manager.read();
								editor.getInspector().setCurrentEntity(system);
							} else if(localPath.endsWith(Map.getExtension()))
							{
								Map map = new Map();
								R2DFileManager manager = new R2DFileManager(localPath,map);
								manager.read();
								editor.setMap(map);
							}
						}
						else if(selectedFile-1-currentFolder.files.size() < currentFolder.folders.size())
							folderStack.add(currentFolder.folders.get(selectedFile-1-currentFolder.files.size()));
						selectedFile = -1;
					} else
					{
						selectedFile = yPos/20;
						lastDoubleClickTime = System.currentTimeMillis();
					}
				} else
				{
					selectedFile = yPos/20;
					lastDoubleClickTime = System.currentTimeMillis();
				}
			} else
			{
				selectedFile = -1;
				lastDoubleClickTime = System.currentTimeMillis();
			}
		} else if(Remote2D.getInstance().hasMouseBeenPressed())
		{
			selectedFile = -1;
			lastDoubleClickTime = System.currentTimeMillis();
		}
		
	}

	@Override
	public void render(float interpolation) {
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0,0,0,0.5f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y+dim.y);
			GL11.glVertex2f(pos.x,pos.y+dim.y);
		GL11.glEnd();
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(pos.x, pos.y+20);
		GL11.glVertex2f(pos.x+dim.x, pos.y+20);
		GL11.glVertex2f(pos.x, pos.y+40);
		GL11.glVertex2f(pos.x+dim.x, pos.y+40);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Fonts.get("Arial").drawString("Browser", pos.x+10, pos.y, 20, 0xffffff);
		
		Folder currentFolder = folderStack.get(folderStack.size()-1);
		
		Fonts.get("Arial").drawString("...", pos.x+10, pos.y+20, 20, folderStack.size() == 1 ? 0x999999 : 0xffffff);
		int yOffset = 20;
		if(selectedFile == 0)
		{
			GL11.glColor4f(1,1,1,0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x,  pos.y+yOffset);
			GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
			GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
			GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
			GL11.glEnd();
			GL11.glColor4f(1,1,1,1);
		}
		yOffset = 40;
		
		for(int x=0;x<currentFolder.folders.size();x++)
		{
			if(selectedFile-1 == x)
			{
				GL11.glColor4f(1,1,1,0.5f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(pos.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
				GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
				GL11.glEnd();
				GL11.glColor4f(1,1,1,1);
			}
			
			Fonts.get("Arial").drawString(new File(currentFolder.folders.get(x).getPath()).getName(), pos.x+10, pos.y+yOffset, 20, 0xffffff);
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, pos.y+yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
		
		for(int x=0;x<currentFolder.files.size();x++)
		{
			Fonts.get("Arial").drawString(currentFolder.files.get(x).getName(), pos.x+10, pos.y+yOffset, 20, 0xffffff);
			
			if(selectedFile-1-currentFolder.folders.size() == x)
			{
				GL11.glColor4f(1,1,1,0.5f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(pos.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset);
				GL11.glVertex2f(pos.x+dim.x,  pos.y+yOffset+20);
				GL11.glVertex2f(pos.x,  pos.y+yOffset+20);
				GL11.glEnd();
				GL11.glColor4f(1,1,1,1);
			}
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(pos.x, pos.y+yOffset+20);
			GL11.glVertex2f(pos.x+dim.x, pos.y+yOffset+20);
			GL11.glEnd();
			
			bindRGB(0xffffff);
			
			yOffset += 20;
		}
	}
	
	
	
}
