package com.iliadonline.client.render;

import com.iliadonline.client.state.GameState;

public interface RenderInterface
{
	public void render(GameState state);
	public void resize(int width, int height);
}
