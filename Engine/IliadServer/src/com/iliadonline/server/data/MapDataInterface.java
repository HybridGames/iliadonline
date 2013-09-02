package com.iliadonline.server.data;

import java.util.List;

import com.iliadonline.server.objects.IliadMap;

/**
 * Interface of Data Methods for Maps
 */
public interface MapDataInterface
{
	public List<IliadMap> loadMaps();
	public void saveMaps(List<IliadMap> maps);
	public IliadMap loadMap(int mapId);
	public void saveMap(IliadMap map);
}
