import java.math.BigDecimal;
import java.util.ArrayList;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

public class RtoA {
	private String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees";
	private String UTMK_PARAM = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs";
	private CoordinateTransformFactory ctFactory;
	private CRSFactory csFactory;
	private CoordinateReferenceSystem WGS84;
	private CoordinateReferenceSystem UTMK;
	
	RtoA(){
		ctFactory = new CoordinateTransformFactory();
		csFactory = new CRSFactory();
		WGS84 = csFactory.createFromParameters("WGS84", WGS84_PARAM);
		UTMK = csFactory.createFromParameters("UTMK", UTMK_PARAM);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RtoA ra = new RtoA();
		
		double lon = 126.9775;
		double lat = 37.5680555555556;
		double centerAngle = 0;
		double[][] aa = {
				{945.392737,1170.153860},				{864.958069,1102.661162},				{841.941835,1130.090842},
				{922.376503,1197.583540},				{2490.000000,-42.505431},				{2510.000000,-42.505431},
				{2510.000000,-550.006179},				{2490.000000,-550.006179},				{2504.000000,-694.000000},
				{2496.000000,-694.000000},				{2496.000000,-550.006179},				{2504.000000,-550.006179},
				{3094.000000,-305.000000},				{2510.000000,-305.000000},				{2510.000000,-285.000000},
				{3094.000000,-285.000000},				{3094.000000,-686.000000},				{3094.000000,-694.000000},
				{2504.000000,-694.000000},				{2504.000000,-686.000000},				{3094.000000,-686.000000},
				{3086.000000,-686.000000},				{3086.000000,-305.000000},				{3094.000000,-305.000000},
				{3500.000000,-849.500000},				{3500.000000,-857.500000},				{3086.000000,-857.500000},
				{3086.000000,-849.500000},				{3510.000000,-305.000000},				{3094.000000,-305.000000},
				{3094.000000,-285.000000},				{3510.000000,-285.000000},				{3094.000000,-849.500000},
				{3086.000000,-849.500000},				{3086.000000,-694.000000},				{3094.000000,-694.000000},
				{8010.000000,-837.500000},				{8010.000000,-857.500000},				{3490.000000,-857.500000},
				{3490.000000,-837.500000},				{969.957257,1214.138063},				{969.967405,-42.505741},
				{864.967405,-42.506589},				{864.957108,1232.652423},
		};
		
		for (int i=0; i<aa.length; i++){
				ArrayList<BigDecimal> result = ra.convertLonLat(aa[i][0], aa[i][1], lon, lat, centerAngle);
				System.out.println(result.toString());
		}
	}

	
	
	public ArrayList<BigDecimal> convertLonLat(double x, double y, double centerLon, double centerLat, double centerAngle) {
		CoordinateTransform trans = ctFactory.createTransform(WGS84, UTMK);
		ProjCoordinate p = new ProjCoordinate(centerLon, centerLat);
		ProjCoordinate p1 = new ProjCoordinate();
		ArrayList<BigDecimal> resultLonLat = new ArrayList<BigDecimal>();
		trans.transform(p, p1);
			

		BigDecimal x_ = new BigDecimal(x);
		BigDecimal y_ = new BigDecimal(y);
		double scaledX = (x_.multiply(new BigDecimal(0.01))).doubleValue();
		double scaledY = (y_.multiply(new BigDecimal(0.01))).doubleValue();
		double rotation = Math.PI * ( - centerAngle) / 180.0; // 현재  -180 기본 값 하지만 나중에 0이 기본값이 될 예쩡  intruderEdit
		//double rotation = Math.PI * ((centerAngle) / 180.0); // 현재  -180 기본 값 하지만 나중에 0이 기본값이 될 예쩡  intruderEdit
		//double rotation = Math.toRadians(centerAngle); // 현재  -180 기본 값 하지만 나중에 0이 기본값이 될 예쩡  intruderEdit
		double rotX = (scaledX * Math.cos(rotation)) - (scaledY * Math.sin(rotation));
		double rotY = (scaledX * Math.sin(rotation)) + (scaledY * Math.cos(rotation));
		double latCal = rotX + p1.x;
		double lonCal = rotY + p1.y;
		ProjCoordinate p2 = new ProjCoordinate(latCal, lonCal);
		ProjCoordinate p3 = new ProjCoordinate();
		CoordinateTransform trans1 = ctFactory.createTransform(UTMK, WGS84);
		trans1.transform(p2, p3);
		resultLonLat.add(new BigDecimal(p3.x));
		resultLonLat.add(new BigDecimal(p3.y));
		return resultLonLat;
	}
}
