package com.iliadonline.server.data;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.hsqldb.HsqlException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HsqlDataProviderTest
{
	@Rule
	public ExpectedException sqlExRule = ExpectedException.none();
	
	private static final String testPath = "C:\\Work\\test\\iliad\\server\\data";

	@Test
	public void testCreateFalseException() throws SQLException, Exception
	{
		File location = new File(testPath + "\\invalid");
		location.mkdirs();
		
		sqlExRule.expect(SQLException.class);
		sqlExRule.expectMessage("Database does not exists: " + testPath + "\\invalid");
		
		HsqlDataProvider data = new HsqlDataProvider(location, false);
	}
}
