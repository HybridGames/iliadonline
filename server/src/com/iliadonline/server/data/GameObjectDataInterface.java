package com.iliadonline.server.data;

import java.util.List;

import com.iliadonline.server.objects.GameObject;

/**
 * Interface of Data Methods for GameObjects
 */
public interface GameObjectDataInterface
{
	public List<GameObject> loadObjects();
	public void saveObjects(List<GameObject> objects);
	public GameObject loadObject(int objectId);
	public void saveObject(GameObject object);
}