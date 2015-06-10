package travel.kiri.backend.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import travel.kiri.backend.puller.DataPuller;

public class DataPullerTest {

	public static String SQL_PROPERTIES_FILE = "etc/mysql.properties";
	public static String TRACKS_CONF_FILE = "etc/tracks.conf";
	
	private static Connection connection;
	private static Statement statement;
	private static DataPuller puller;
	private static Exception pullException;
	
	@BeforeClass
	public static void setUp() throws FileNotFoundException, IOException, SQLException {
		// Setup database
		Properties sqlProperties = new Properties();
		sqlProperties.load(new FileReader(SQL_PROPERTIES_FILE));
		connection = DriverManager.getConnection(String.format(
				"jdbc:mysql://%s/%s?user=%s&password=%s",
				sqlProperties.get("host"), sqlProperties.get("database"),
				sqlProperties.get("user"), sqlProperties.get("password")));
		
		statement = connection.createStatement();
		statement.execute("DROP TABLE IF EXISTS `tracks`;");
		statement.execute("CREATE TABLE IF NOT EXISTS `tracks` (`trackId` varchar(32) NOT NULL, `trackTypeId` varchar(32) NOT NULL DEFAULT 'angkot', `trackName` varchar(64) NOT NULL, `internalInfo` varchar(1024) NOT NULL, `geodata` linestring DEFAULT NULL, `pathloop` tinyint(1) NOT NULL DEFAULT '0', `penalty` decimal(4,2) NOT NULL DEFAULT '1.00', `transferNodes` varchar(1024) DEFAULT NULL, `extraParameters` varchar(256) DEFAULT NULL, `officialTrackNo` varchar(32) DEFAULT NULL, `officialTrackName` varchar(256) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		statement.execute("INSERT INTO `tracks` VALUES ('testnotimestamp', 'bdo_angkot', 'Test for timestamp is not yet specified', 'angkotwebid:642', GeomFromText(NULL), '0', '1.00', NULL, NULL, '1A', 'Abdul Muis - Cicaheum (via Binong)');");
		statement.execute("INSERT INTO `tracks` VALUES ('testlowertimestamp', 'bdo_angkot', 'Test for timestamp is lower than today', 'angkotwebid:641:808628400', GeomFromText('POINT(107.60380 -6.91082)'), '0', '1.00', NULL, NULL, '1B', 'Abdul Muis - Cicaheum (via Aceh)');");
		statement.execute("INSERT INTO `tracks` VALUES ('testhighertimestamp', 'bdo_angkot', 'Test for timestamp is higher than today', 'angkotwebid:109:2386551600', GeomFromText('LINESTRING(107.6038 -6.91082)'), '0', '1.00', NULL, NULL, '1B', 'Abdul Muis - Cicaheum (via Aceh)');");
		statement.execute("INSERT INTO `tracks` VALUES ('testnokeyword', 'bdo_angkot', 'Test for no keyword is used', 'Not an angkotwebid track', GeomFromText('LINESTRING(107.6038 -6.91082)'), '0', '1.00', NULL, NULL, NULL, NULL);");
		
		// Remove tracks.conf file (silent on error)
		new File(TRACKS_CONF_FILE).delete();
		
		// Perform pull, catching any errors thrown
		pullException = null;
		try {
			puller = new DataPuller();
			puller.pull(new File(SQL_PROPERTIES_FILE), new PrintStream(new FileOutputStream(TRACKS_CONF_FILE)));
		} catch (Exception e) {
			pullException = e;
		}
	}

	@AfterClass
	public static void tearDown() throws SQLException {
		statement.close();
		connection.close();
	}
	
	/**
	 * Utility class untuk mencari baris pada berkas tracks.conf dengan trackId tertentu.
	 * @param trackId trackId yang dicari
	 * @return array of string kolom, dari baris terpisah tab atau null jika tidak ditemukan.
	 * @throws IOException 
	 */
	private static String[] getColumnsFromTracksConf(String trackId) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(TRACKS_CONF_FILE));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] columns = line.trim().split("\\t");
			if (columns[0].equals("bdo_angkot." + trackId)) {
				reader.close();
				return columns;
			}
		}
		reader.close();
		return null;
	}

	/**
	 * Memastikan tidak ada eksepsi yang terjadi saat operasi pull dijalankan.
	 */
	@Test
	public void testEnsurePullNoException() {
		if (pullException != null) {
			pullException.printStackTrace();
			Assert.fail("Exception was found during pull: " + pullException);
		}
	}

	/**
	 * Memastikan pull berhasil memperbaharui SQL untuk data dengan internalInfo berisi 'angkotwebid:nnn' (tanpa timestamp)
	 * @throws SQLException
	 */
	@Test
	public void testSQLUpdatedFromNoTimestamp() throws SQLException {
		ResultSet result = statement.executeQuery("SELECT internalInfo, AsText(geodata) FROM tracks WHERE trackId='testnotimestamp';");
		Assert.assertTrue("Data uji tidak ditemukan!", result.next());
		Assert.assertTrue("internalInfo tidak terupdate dengan baik! ", result.getString(1).matches("angkotwebid:642:[0-9]+"));
		Assert.assertTrue("geodata tidak terupdate dengan baik!", result.getString(2).matches("LINESTRING(.+)"));
	}

	/**
	 * Memastikan pull berhasil memperbaharui tracks.conf untuk data dengan internalInfo berisi 'angkotwebid:nnn' (tanpa timestamp)
	 * @throws IOException
	 */
	@Test
	public void testTracksConfUpdatedFromNoTimestamp() throws IOException {
		String[] columns = getColumnsFromTracksConf("testnotimestamp");
		Assert.assertNotNull("Tidak ditemukan data uji di tracks.conf", columns);
		int numOfCoordinates = Integer.parseInt(columns[2]);
		Assert.assertTrue("Jumlah titik harus lebih dari 0!", numOfCoordinates > 0);
		String[] coordinateNumbers = columns[3].split(" ");
		Assert.assertEquals("Jumlah titik harus sesuai jumlah koordinat", numOfCoordinates * 2, coordinateNumbers.length);
		Assert.assertEquals("transferNodes tidak sesuai di tracks.conf!", "0-" + (numOfCoordinates - 1), columns[5]);
	}

	/**
	 * Memastikan pull berhasil memperbaharui SQL untuk data dengan internalInfo berisi 'angkotwebid:nnn:mmm' (dengan timestamp tetapi lebih rendah)
	 * @throws SQLException
	 */
	@Test
	public void testSQLUpdatedFromLowerTimestamp() throws SQLException {
		ResultSet result = statement.executeQuery("SELECT internalInfo, AsText(geodata) FROM tracks WHERE trackId='testlowertimestamp';");
		Assert.assertTrue("Data uji tidak ditemukan!", result.next());
		Assert.assertTrue("internalInfo tidak terupdate dengan baik: " + result.getString(1), result.getString(1).matches("angkotwebid:641:[0-9]+"));
		String[] string1 = result.getString(1).split(":");
		long timestamp = Long.parseLong(string1[2]);
		Assert.assertTrue("timestamp tidak terupdate dengan baik!", timestamp > 808628400);
		Assert.assertTrue("geodata tidak terupdate dengan baik!", result.getString(2).matches("LINESTRING(.+)"));
	}

	/**
	 * Memastikan pull berhasil memperbaharui tracks.conf untuk data dengan internalInfo berisi 'angkotwebid:nnn:mmm' (dengan timestamp tetapi lebih rendah)
	 * @throws IOException
	 */
	@Test
	public void testTracksConfUpdatedFromLowerTimestamp() throws IOException {
		String[] columns = getColumnsFromTracksConf("testlowertimestamp");
		Assert.assertNotNull("Tidak ditemukan data uji di tracks.conf", columns);
		int numOfCoordinates = Integer.parseInt(columns[2]);
		Assert.assertTrue("Jumlah titik harus lebih dari 0!", numOfCoordinates > 0);
		String[] coordinateNumbers = columns[3].split(" ");
		Assert.assertEquals("Jumlah titik harus sesuai jumlah koordinat", numOfCoordinates * 2, coordinateNumbers.length);
		Assert.assertEquals("transferNodes tidak sesuai di tracks.conf!", "0-" + (numOfCoordinates - 1), columns[5]);
	}

	/**
	 * Memastikan pull berhasil memperbaharui SQL untuk data dengan internalInfo berisi 'angkotwebid:nnn:mmm' (dengan timestamp tetapi lebih tinggi)
	 * @throws SQLException
	 */
	@Test
	public void testSQLNotUpdatedFromHigherTimestamp() throws SQLException {
		ResultSet result = statement.executeQuery("SELECT internalInfo, AsText(geodata) FROM tracks WHERE trackId='testhighertimestamp';");
		Assert.assertTrue("Data uji tidak ditemukan!", result.next());
		Assert.assertEquals("internalInfo tidak boleh terupdate!", "angkotwebid:109:2386551600", result.getString(1));
		Assert.assertEquals("geodata tidak boleh terupdate!", "LINESTRING(107.6038 -6.91082)", result.getString(2));
	}

	/**
	 * Memastikan pull berhasil memperbaharui tracks.conf untuk data dengan internalInfo berisi 'angkotwebid:nnn:mmm' (dengan timestamp tetapi lebih tinggi)
	 * @throws IOException
	 */
	@Test
	public void testTracksConfNotUpdatedFromHigherTimestamp() throws IOException {
		String[] columns = getColumnsFromTracksConf("testhighertimestamp");
		Assert.assertNotNull("Tidak ditemukan data uji di tracks.conf", columns);
		int numOfCoordinates = Integer.parseInt(columns[2]);
		Assert.assertEquals("Jumlah titik harus tetap 1!", 1, numOfCoordinates);
		Assert.assertEquals("transferNodes tidak boleh berubah!", "0", columns[5]);
	}	

	/**
	 * Memastikan pull tidak memperbaharui SQL untuk data dengan internalInfo berisi 'Not an angkotwebid track' (bukan track yang terintegrasi)
	 * @throws SQLException
	 */
	@Test
	public void testSQLNotUpdatedFromNotAngkotWebid() throws SQLException {
		ResultSet result = statement.executeQuery("SELECT internalInfo, AsText(geodata) FROM tracks WHERE trackId='testnokeyword';");
		Assert.assertTrue("Data uji tidak ditemukan!", result.next());
		Assert.assertEquals("internalInfo tidak boleh terupdate!", "Not an angkotwebid track", result.getString(1));
		Assert.assertEquals("geodata tidak boleh terupdate!", "LINESTRING(107.6038 -6.91082)", result.getString(2));
	}

	/**
	 * Memastikan pull tidak memperbaharui tracks.conf untuk data dengan internalInfo berisi 'Not an angkotwebid track' (bukan track yang terintegrasi)
	 * @throws IOException
	 */
	@Test
	public void testTracksConfUpdatedFromHigherTimestamp() throws IOException {
		String[] columns = getColumnsFromTracksConf("testnokeyword");
		Assert.assertNotNull("Tidak ditemukan data uji di tracks.conf", columns);
		int numOfCoordinates = Integer.parseInt(columns[2]);
		Assert.assertEquals("Jumlah titik harus tetap 1!", 1, numOfCoordinates);
		Assert.assertEquals("transferNodes tidak boleh berubah!", "0", columns[5]);
	}	

}