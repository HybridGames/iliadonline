package com.iliadonline.client.render;

import java.util.Collection;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.iliadonline.client.GameState;
import com.iliadonline.client.GameState.GameStateEnum;
import com.iliadonline.client.menu.MainMenu;
import com.iliadonline.shared.data.GameObject;
import com.iliadonline.shared.data.IliadMap;
import com.iliadonline.shared.data.Location;

/**
 * Our class that handles on screen rendering
 * Render will also be an access point for all graphics resources
 * 	(fonts, sprites, skins, etc.)
 * TODO: Look into using GL20
 */
public class Render
{
	private static final String tag = "com.iliadonline.client.render.Render";
	
	private FileHandle gfxDir;
	protected AssetManager assetManager;
	private int width, height;
	
	private OrthographicCamera camera;
	private ShapeRenderer shapes;
	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	
	private Sprite char1;

	private float x, y;
	
	private HashMap<String, Sprite> sprites;
	private HashMap<String, BitmapFont> fonts;
	
	private MainMenu mainMenu;
	
	private CharSequence str = "Iliad Online Beta 0.0.1";
	protected CharSequence stateStr = "";
	
	BitmapFont font;
	
	/**
	 * Constructor for Render
	 * @param gfxDir Directory to the Graphics files
	 * @param width
	 * @param height
	 */
	public Render(FileHandle gfxDir, int width, int height)
	{
		if(!gfxDir.isDirectory())
		{
			throw new IllegalArgumentException("gfxDir must be a directory. A valid directory containing the game's graphics must be supplied.");
		}
		
		this.gfxDir = gfxDir;
		this.loadSprites(gfxDir.child("sprites"));
		//TODO: Fix the font issue: http://stackoverflow.com/questions/17513460/libgdx-unable-to-find-files-in-android-application-data-directory
		this.loadFonts(gfxDir.child("fonts"));
		
		this.resize(width, height);
		
		camera = new OrthographicCamera(width, height);
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		shapes = new ShapeRenderer();
		font = this.getFont("arial32");
		
		mainMenu = new MainMenu(this);
		Gdx.input.setInputProcessor(mainMenu);	//TODO: Should this be somewhere else?
	}
	
	/**
	 * Constructor for Render, using AssetManager
	 * @param assetManager
	 * @param width
	 * @param height
	 */
	public Render(AssetManager assetManager, int width, int height)
	{
		this.assetManager = assetManager;
		
		this.resize(width, height);
		
		camera = new OrthographicCamera(width, height);
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();
		hudBatch = new SpriteBatch();
		//font = this.getFont("arial32");
		
		this.assetManager.load("gfx/fonts/arial32.fnt", BitmapFont.class);
		
		/*mainMenu = new MainMenu(this);
		Gdx.input.setInputProcessor(mainMenu);*/
	}

	/**
	 * Main function that performs the rendering
	 */
	public void render(GameState state)
	{
		if(!this.assetManager.update())
		{
			Gdx.app.log(tag, "Asset Manager loading: " + this.assetManager.getProgress());
			return;
		}
		
		
		
		//Get Renderable Elements from GameState
		stateStr = state.getState().name();
		
		//Clear the buffer
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		hudBatch.begin();
		
		//TODO: have Render get assets after they're loaded, not at every call to render
		font = this.assetManager.get("gfx/fonts/arial32.fnt", BitmapFont.class);
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		font.draw(hudBatch, str + " - " + stateStr, 100, 100);
				
		hudBatch.end();
		
		if(state.getState() == GameStateEnum.Authenticated)
		{
			batch.begin();
			
			Location location = state.getPlayer().getLocation();
			camera.position.set(location.x, location.y, 0);
			camera.update();
			camera.apply(Gdx.graphics.getGL11());
		
			int x = Math.round(location.x);
			int y = Math.round(location.y);
			
			shapes.begin(ShapeType.Line);
			shapes.setProjectionMatrix(camera.combined);
			shapes.setColor(1,0,1,1);
			shapes.line(x + 10, y, x, y + 10);
			shapes.line(x, y+10, x-10, y);
			shapes.line(x-10, y, x, y-10);
			shapes.line(x, y-10, x+10, y);
			shapes.end();
			
			batch.end();
		
			hudBatch.begin();
			
			font.draw(hudBatch, "[" + location.x + ", " + location.y + "]", 100, 1000);
						
			hudBatch.end();
		}
		
		
		
		//Iterate over the Renderable Elements
		//TODO: Need a strategy to pass the renderable objects from the GameState so they are layered properly.
		
		//TODO: Should the state be important here? or should we just ask the game state to tell us what to render?		
		/*switch(state.getState())
		{
			case Authenticated:
				if(state.getPlayer() == null)
				{
					break;
				}
				
				camera.position.set(state.getPlayer().getLocation().x, state.getPlayer().getLocation().y, 0);
				camera.update();
				batch.setProjectionMatrix(camera.combined);
				batch.begin();
					shapes.begin(ShapeType.Line);
					shapes.setColor(new Color(1, 0, 0, 1));
					shapes.line(state.getPlayer().getLocation().x, state.getPlayer().getLocation().y, state.getPlayer().getLocation().x + 5, state.getPlayer().getLocation().y + 5);
					shapes.end();
					
					font.draw(batch, state.getPlayer().getLocation().x + " " + state.getPlayer().getLocation().y, 10, 10);
				batch.end();
				break;
			case Connected:
				break;
			case MainMenu:
				break;
			case Paused:
				break;
			case Playing:
				break;
			case Started:
				break;
			default:
				break;
		}*/
		
		
		
		
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		/*mainMenu.act(Gdx.graphics.getDeltaTime());
		mainMenu.draw();
		
		Table.drawDebug(mainMenu);*/
		
		/*Location center = state.getPlayer().getLocation();
		IliadMap map = state.getMap();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		camera.position.set(center.x, center.y, 0f);
		camera.update();
		
		Gdx.app.log(tag, "Camera Center: " + camera.position.x + "," + camera.position.y);
				
		Collection<GameObject> gameObjects = map.getGameObjects().values();
		
		//TODO: Review SpriteBatch, see if we should use it like this or need to associate {Begin/End} pairs with specific texture atlases or game object types
		//batch.begin();
		for(GameObject gameObj : gameObjects)
		{
			//if(gameObj.getRenderer() != null)
			{
				relativeLocation.setRelativeLocation(center, gameObj.getLocation());
				x = relativeLocation.x;
				y = relativeLocation.y;
								
				//Set position and draw.
				//char1.setPosition(x, y);
				//char1.draw(batch);
				
				shapes.begin(ShapeType.Line);
				shapes.setColor(1,0,1,1);
				shapes.line(x + 10, y, x, y + 10);
				shapes.line(x, y+10, x-10, y);
				shapes.line(x-10, y, x, y-10);
				shapes.line(x, y-10, x+10, y);
				shapes.end();
			}
		}
		//batch.end();
*/	}
	
	/**
	 * Resize function to change Height and Width
	 * Resizing will probably require additional processing.
	 * @param height
	 * @param width
	 */
	public void resize(int width, int height)
	{		
		this.width = width;
		this.height = height;

		camera = new OrthographicCamera(width, height);
		
		//TODO any graphical adjustments required on resize should go here

		Gdx.app.log(tag, "Screen Size: " + width + "," + height);
	}

	/**
	 * Looks for .fnt files in the folder and adds the Bitmap fonts to the hashmap
	 * Does not do a recursive search like Sprites
	 * @param fontsDir
	 */
	protected void loadFonts(FileHandle fontsDir)
	{
		this.fonts = new HashMap<String, BitmapFont>();
		
		if(!fontsDir.isDirectory())
		{
			throw new IllegalArgumentException("No 'fonts' directory found. Create a folder named 'fonts' at the root of the graphics folder.");
		}
		
		BitmapFontData fontData;
		
		FileHandle fontTextureFile;
		TextureRegion texture;
		
		for(FileHandle fontFile : fontsDir.list(".fnt"))
		{
			fontData = new BitmapFontData(fontFile, false);
			
			/*fontTextureFile = new FileHandle(fontData.imagePath);
			texture = new TextureRegion(new Texture(fontTextureFile, false));
			
			BitmapFont font = new BitmapFont(fontFile, texture, false);*/
			
			BitmapFont font = new BitmapFont(fontFile, false);
			this.fonts.put(fontFile.nameWithoutExtension(), font);
		}
	}
	
	/**
	 * Loads the Sprites from the file system into the Sprites hashmap.
	 */
	protected void loadSprites(FileHandle spritesDir)
	{
		this.sprites = new HashMap<String, Sprite>();
		
		if(!spritesDir.isDirectory())
		{
			throw new IllegalArgumentException("No 'sprites' directory found. Create a folder named 'sprites' at the root of the graphics folder.");
		}
		
		loadSpritesHelper(spritesDir, "sprites");
	}
	
	/**
	 * Recursive walk through the sprites directory looking for .atlas files to unpack
	 * This will fill the Sprites HashMap
	 * @param currentDir
	 * @param namespace
	 */
	private void loadSpritesHelper(FileHandle currentDir, String namespace)
	{
		TextureAtlas atlas;
		String fullName;
		for(FileHandle atlasFile : currentDir.list(".atlas"))
		{
			atlas = new TextureAtlas(atlasFile);
			
			for(AtlasRegion region : atlas.getRegions())
			{
				fullName = namespace + "." + region.name + (region.index != -1?"."+region.index:"");
				this.sprites.put(fullName, atlas.createSprite(region.name));
			}
		}
		
		for(FileHandle file : currentDir.list())
		{
			if(file.isDirectory())
			{
				this.loadSpritesHelper(file, namespace + "." + file.name());
			}
		}
	}
	
	/**
	 * Gets a font from our font hasmap
	 * @param name
	 * @return
	 */
	public BitmapFont getFont(String name)
	{
		return this.fonts.get(name);
	}
	
	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}
}
