package com.iliadonline.server.data;

import java.util.List;

import com.iliadonline.server.objects.GameObject;
import com.iliadonline.server.objects.IliadMap;

public class HsqlDataProvider implements GameObjectDataInterface, MapDataInterface
{
	

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
