package com.iliadonline.server.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.iliadonline.server.objects.GameObject;
import com.iliadonline.server.objects.IliadMap;

public class HsqlDataProvider implements DataInterface
{
	private static final String tag = "com.iliadonline.server.data.HsqlDataProvider";
	
	protected Connection connection;
		
	public HsqlDataProvider(File location, boolean initialize) throws SQLException
	{
		 try {
		     Class.forName("org.hsqldb.jdbc.JDBCDriver");
		 } catch (Exception e) {
		     System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
		     e.printStackTrace();
		     return;
		 }
		
		if(!location.isDirectory())
		{
			throw new IllegalArgumentException("Location must be a directory.");
		}
		
		try
		{
			String create = "create=" + ((initialize)?"true":"false");
			//Gdx.app.log(tag, "DB Directory: " + location.getAbsolutePath());
			this.connection = DriverManager.getConnection("jdbc:hsqldb:file:" + location.getAbsolutePath() + ";" + create, "SA", "");
		}
		catch (SQLException e)
		{
			throw e;
		}
	}
	
	/**
	 * Creates a persistent database at the folder provided
	 * @param location
	 */
	public HsqlDataProvider(File location) throws SQLException
	{
		this(location, true);
	}

	/**
	 * Creates an in memory database (non-persistent)
	 */
	/*public HsqlDataProvider()
	{
		
	}*/

	@Override
	public List<IliadMap> loadMaps()
	{

		return null;
	}

	@Override
	public void saveMaps(List<IliadMap> maps)
	{


	}

	@Override
	public IliadMap loadMap(int mapId)
	{

		return null;
	}

	@Override
	public void saveMap(IliadMap map)
	{


	}

	@Override
	public List<GameObject> loadObjects()
	{

		return null;
	}

	@Override
	public void saveObjects(List<GameObject> objects)
	{


	}

	@Override
	public GameObject loadObject(int objectId)
	{

		return null;
	}

	@Override
	public void saveObject(GameObject object)
	{


	}

}
