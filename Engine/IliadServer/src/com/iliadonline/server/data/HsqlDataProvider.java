package com.iliadonline.server.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

import com.badlogic.gdx.Gdx;
import com.iliadonline.server.objects.GameObject;
import com.iliadonline.server.objects.IliadMap;

public class HsqlDataProvider implements DataInterface
{
	private static final String tag = "com.iliadonline.server.data.HsqlDataProvider";
	
	protected Connection connection;
		
	/**
	 * Creates a HsqlDataProvider with a Connection to the database at the given location.
	 * @param location
	 * @param create Should this Database be Created if it does not already exist
	 * @throws SQLException
	 */
	public HsqlDataProvider(File location, boolean create) throws SQLException, URISyntaxException, Exception
	{
		 try {
		     Class.forName("org.hsqldb.jdbc.JDBCDriver");
		 } catch (Exception e) {
		     System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
		     throw e;
		 }
		
		if(!location.isDirectory())
		{
			throw new IllegalArgumentException("Location must be a directory.");
		}
		
		try
		{
			String createStr = "create=" + ((create)?"true":"false");
			this.connection = DriverManager.getConnection("jdbc:hsqldb:file:" + location.getAbsolutePath() + ";" + createStr, "SA", "");
		}
		catch (SQLException e)
		{
			throw e;
		}
		
		if(create)
		{
			this.initializeDatabase();
		}
	}
	
	/**
	 * Creates a persistent database at the folder provided
	 * @param location
	 */
	public HsqlDataProvider(File location) throws SQLException, Exception
	{
		this(location, true);
	}

	/**
	 * Creates an in memory database (non-persistent)
	 */
	/*public HsqlDataProvider()
	{
		
	}*/

	/**
	 * Responsible for creating the default database.
	 * Opted to include all of the throws separately so we know what potential issues we need to respond to.
	 * At this point, it's expected that any of these problems may be unrecoverable,
	 * so knowing what is wrong and reporting specific issues is important.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws SqlToolError 
	 */
	protected void initializeDatabase() throws URISyntaxException, IOException, SqlToolError, SQLException
	{
		File file = new File(HsqlDataProvider.class.getResource("/com/iliadonline/server/sql/init/create_tables.sql").toURI());
		SqlFile sqlFile = new SqlFile(file);
		
		sqlFile.setConnection(this.connection);
		sqlFile.execute();
	}
	
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

	@Override
	protected void finalize() throws Throwable
	{
		//TODO: Do we need to issue a SHUTDOWN command?
		this.connection.close();
		super.finalize();
	}

}
