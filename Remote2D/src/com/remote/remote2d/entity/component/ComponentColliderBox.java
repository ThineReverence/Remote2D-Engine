package com.remote.remote2d.entity.component;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.logic.Collider;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2;

public class ComponentColliderBox extends Component implements ComponentCollider {
	
	public Vector2 pos = new Vector2(0,0);
	public Vector2 dim = new Vector2(10,10);

	@Override
	public void tick(int i, int j, int k) {
		
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void onEntitySpawn() {
		
 	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		pos.getColliderWithDim(dim).getTransformedCollider(entity.pos).drawCollider(0x00ff00);
	}

	@Override
	public void apply() {
		
	}

	@Override
	public Collider getCollider() {
		return pos.getColliderWithDim(dim);
	}
}